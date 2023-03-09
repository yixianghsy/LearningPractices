package cn.bugstack.springframework.core.annotation;

import cn.bugstack.springframework.core.ResolvableType;
import cn.bugstack.springframework.core.util.ConcurrentReferenceHashMap;
import cn.bugstack.springframework.core.util.ObjectUtils;
import cn.bugstack.springframework.core.util.StringUtils;
import cn.bugstack.springframework.util.ClassUtils;
import cn.bugstack.springframework.util.ReflectionUtils;
import cn.hutool.core.lang.Assert;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.*;
import java.util.*;

/**
 *
 * @description General utility methods for working with annotations, handling meta-annotations,
 * bridge methods (which the compiler generates for generic declarations) as well
 * as super methods (for optional <em>annotation inheritance</em>).
 * @date 2022/3/16
 *  /CodeDesignTutorials
 *
 */
public class AnnotationUtils {

    /**
     * The attribute name for annotations with a single element.
     */
    public static final String VALUE = "value";

    private static final Map<AnnotationCacheKey, Annotation> findAnnotationCache =
            new ConcurrentReferenceHashMap<>(256);

    private static final Map<AnnotationCacheKey, Boolean> metaPresentCache =
            new ConcurrentReferenceHashMap<>(256);

    private static final Map<AnnotatedElement, Annotation[]> declaredAnnotationsCache =
            new ConcurrentReferenceHashMap<>(256);

    private static final Map<Class<?>, Set<Method>> annotatedBaseTypeCache =
            new ConcurrentReferenceHashMap<>(256);

    @SuppressWarnings("unused")
    @Deprecated  // just here for older tool versions trying to reflectively clear the cache
    private static final Map<Class<?>, ?> annotatedInterfaceCache = annotatedBaseTypeCache;

    private static final Map<Class<? extends Annotation>, Boolean> synthesizableCache =
            new ConcurrentReferenceHashMap<>(256);

    private static final Map<Class<? extends Annotation>, Map<String, List<String>>> attributeAliasesCache =
            new ConcurrentReferenceHashMap<>(256);

    private static final Map<Class<? extends Annotation>, List<Method>> attributeMethodsCache =
            new ConcurrentReferenceHashMap<>(256);

    private static final Map<Method, AliasDescriptor> aliasDescriptorCache =
            new ConcurrentReferenceHashMap<>(256);
    
    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
        try {
            A annotation = annotatedElement.getAnnotation(annotationType);
            if (annotation == null) {
                for (Annotation metaAnn : annotatedElement.getAnnotations()) {
                    annotation = metaAnn.annotationType().getAnnotation(annotationType);
                    if (annotation != null) {
                        break;
                    }
                }
            }
            return (annotation != null ? synthesizeAnnotation(annotation, annotatedElement) : null);
        }
        catch (Throwable ex) {
            handleIntrospectionFailure(annotatedElement, ex);
            return null;
        }
    }

    static boolean isOverride(Method method, Method candidate) {
        if (!candidate.getName().equals(method.getName()) ||
                candidate.getParameterCount() != method.getParameterCount()) {
            return false;
        }
        Class<?>[] paramTypes = method.getParameterTypes();
        if (Arrays.equals(candidate.getParameterTypes(), paramTypes)) {
            return true;
        }
        for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i] != ResolvableType.forMethodParameter(candidate, i, method.getDeclaringClass()).resolve()) {
                return false;
            }
        }
        return true;
    }

    static Set<Method> getAnnotatedMethodsInBaseType(Class<?> baseType) {
        boolean ifcCheck = baseType.isInterface();
        if (ifcCheck && ClassUtils.isJavaLanguageInterface(baseType)) {
            return Collections.emptySet();
        }

        Set<Method> annotatedMethods = annotatedBaseTypeCache.get(baseType);
        if (annotatedMethods != null) {
            return annotatedMethods;
        }
        Method[] methods = (ifcCheck ? baseType.getMethods() : baseType.getDeclaredMethods());
        for (Method baseMethod : methods) {
            try {
                // Public methods on interfaces (including interface hierarchy),
                // non-private (and therefore overridable) methods on base classes
                if ((ifcCheck || !Modifier.isPrivate(baseMethod.getModifiers())) &&
                        hasSearchableAnnotations(baseMethod)) {
                    if (annotatedMethods == null) {
                        annotatedMethods = new HashSet<>();
                    }
                    annotatedMethods.add(baseMethod);
                }
            }
            catch (Throwable ex) {
                handleIntrospectionFailure(baseMethod, ex);
            }
        }
        if (annotatedMethods == null) {
            annotatedMethods = Collections.emptySet();
        }
        annotatedBaseTypeCache.put(baseType, annotatedMethods);
        return annotatedMethods;
    }


    private static boolean hasSearchableAnnotations(Method ifcMethod) {
        Annotation[] anns = getDeclaredAnnotations(ifcMethod);
        if (anns.length == 0) {
            return false;
        }
        for (Annotation ann : anns) {
            String name = ann.annotationType().getName();
            if (!name.startsWith("java.lang.") && !name.startsWith("org.springframework.lang.")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieve a potentially cached array of declared annotations for the
     * given element.
     * @param element the annotated element to introspect
     * @return a potentially cached array of declared annotations
     * (only for internal iteration purposes, not for external exposure)
     * @since 5.1
     */
    static Annotation[] getDeclaredAnnotations(AnnotatedElement element) {
        if (element instanceof Class || element instanceof Member) {
            // Class/Field/Method/Constructor returns a defensively cloned array from getDeclaredAnnotations.
            // Since we use our result for internal iteration purposes only, it's safe to use a shared copy.
            return declaredAnnotationsCache.computeIfAbsent(element, AnnotatedElement::getDeclaredAnnotations);
        }
        return element.getDeclaredAnnotations();
    }

    @SuppressWarnings("unchecked")
    private static <A extends Annotation> A findAnnotation(
            Class<?> clazz,  Class<A> annotationType, boolean synthesize) {

        Assert.notNull(clazz, "Class must not be null");
        if (annotationType == null) {
            return null;
        }

        AnnotationCacheKey cacheKey = new AnnotationCacheKey(clazz, annotationType);
        A result = (A) findAnnotationCache.get(cacheKey);
        if (result == null) {
            result = findAnnotation(clazz, annotationType, new HashSet<>());
            if (result != null && synthesize) {
                result = synthesizeAnnotation(result, clazz);
                findAnnotationCache.put(cacheKey, result);
            }
        }
        return result;
    }

    private static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType, Set<Annotation> visited) {
        try {
            A annotation = clazz.getDeclaredAnnotation(annotationType);
            if (annotation != null) {
                return annotation;
            }
            for (Annotation declaredAnn : getDeclaredAnnotations(clazz)) {
                Class<? extends Annotation> declaredType = declaredAnn.annotationType();
                if (!isInJavaLangAnnotationPackage(declaredType) && visited.add(declaredAnn)) {
                    annotation = findAnnotation(declaredType, annotationType, visited);
                    if (annotation != null) {
                        return annotation;
                    }
                }
            }
        }
        catch (Throwable ex) {
            handleIntrospectionFailure(clazz, ex);
            return null;
        }

        for (Class<?> ifc : clazz.getInterfaces()) {
            A annotation = findAnnotation(ifc, annotationType, visited);
            if (annotation != null) {
                return annotation;
            }
        }

        Class<?> superclass = clazz.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        return findAnnotation(superclass, annotationType, visited);
    }

    public static boolean isAnnotationMetaPresent(Class<? extends Annotation> annotationType,
                                                   Class<? extends Annotation> metaAnnotationType) {

        Assert.notNull(annotationType, "Annotation type must not be null");
        if (metaAnnotationType == null) {
            return false;
        }

        AnnotationCacheKey cacheKey = new AnnotationCacheKey(annotationType, metaAnnotationType);
        Boolean metaPresent = metaPresentCache.get(cacheKey);
        if (metaPresent != null) {
            return metaPresent;
        }
        metaPresent = Boolean.FALSE;
        if (findAnnotation(annotationType, metaAnnotationType, false) != null) {
            metaPresent = Boolean.TRUE;
        }
        metaPresentCache.put(cacheKey, metaPresent);
        return metaPresent;
    }

    static boolean hasPlainJavaAnnotationsOnly( Object annotatedElement) {
        Class<?> clazz;
        if (annotatedElement instanceof Class) {
            clazz = (Class<?>) annotatedElement;
        }
        else if (annotatedElement instanceof Member) {
            clazz = ((Member) annotatedElement).getDeclaringClass();
        }
        else {
            return false;
        }
        String name = clazz.getName();
        return (name.startsWith("java") || name.startsWith("org.springframework.lang."));
    }

    static boolean isInJavaLangAnnotationPackage( Class<? extends Annotation> annotationType) {
        return (annotationType != null && isInJavaLangAnnotationPackage(annotationType.getName()));
    }


    public static boolean isInJavaLangAnnotationPackage( String annotationType) {
        return (annotationType != null && annotationType.startsWith("java.lang.annotation"));
    }

    private static AnnotationAttributes getAnnotationAttributes(Object annotatedElement,
                                                                Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {

        AnnotationAttributes attributes =
                retrieveAnnotationAttributes(annotatedElement, annotation, classValuesAsString, nestedAnnotationsAsMap);
        postProcessAnnotationAttributes(annotatedElement, attributes, classValuesAsString, nestedAnnotationsAsMap);
        return attributes;
    }


    static AnnotationAttributes retrieveAnnotationAttributes(Object annotatedElement,
                                                             Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {

        Class<? extends Annotation> annotationType = annotation.annotationType();
        AnnotationAttributes attributes = new AnnotationAttributes(annotationType);

        for (Method method : getAttributeMethods(annotationType)) {
            try {
                Object attributeValue = method.invoke(annotation);
                Object defaultValue = method.getDefaultValue();
                if (defaultValue != null && ObjectUtils.nullSafeEquals(attributeValue, defaultValue)) {
                    attributeValue = new DefaultValueHolder(defaultValue);
                }
                attributes.put(method.getName(),
                        adaptValue(annotatedElement, attributeValue, classValuesAsString, nestedAnnotationsAsMap));
            }
            catch (Throwable ex) {
                if (ex instanceof InvocationTargetException) {
                    Throwable targetException = ((InvocationTargetException) ex).getTargetException();
                    rethrowAnnotationConfigurationException(targetException);
                }
                throw new IllegalStateException("Could not obtain annotation attribute value for " + method, ex);
            }
        }

        return attributes;
    }

    static Object adaptValue( Object annotatedElement,  Object value,
                             boolean classValuesAsString, boolean nestedAnnotationsAsMap) {

        if (classValuesAsString) {
            if (value instanceof Class) {
                return ((Class<?>) value).getName();
            }
            else if (value instanceof Class[]) {
                Class<?>[] clazzArray = (Class<?>[]) value;
                String[] classNames = new String[clazzArray.length];
                for (int i = 0; i < clazzArray.length; i++) {
                    classNames[i] = clazzArray[i].getName();
                }
                return classNames;
            }
        }

        if (value instanceof Annotation) {
            Annotation annotation = (Annotation) value;
            if (nestedAnnotationsAsMap) {
                return getAnnotationAttributes(annotatedElement, annotation, classValuesAsString, true);
            }
            else {
                return synthesizeAnnotation(annotation, annotatedElement);
            }
        }

        if (value instanceof Annotation[]) {
            Annotation[] annotations = (Annotation[]) value;
            if (nestedAnnotationsAsMap) {
                AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[annotations.length];
                for (int i = 0; i < annotations.length; i++) {
                    mappedAnnotations[i] =
                            getAnnotationAttributes(annotatedElement, annotations[i], classValuesAsString, true);
                }
                return mappedAnnotations;
            }
            else {
                return synthesizeAnnotationArray(annotations, annotatedElement);
            }
        }

        // Fallback
        return value;
    }

    static void postProcessAnnotationAttributes(Object annotatedElement,
                                                AnnotationAttributes attributes, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {

        if (attributes == null) {
            return;
        }

        Class<? extends Annotation> annotationType = attributes.annotationType();

        // Track which attribute values have already been replaced so that we can short
        // circuit the search algorithms.
        Set<String> valuesAlreadyReplaced = new HashSet<>();

        if (!attributes.validated) {
            // Validate @AliasFor configuration
            Map<String, List<String>> aliasMap = getAttributeAliasMap(annotationType);
            aliasMap.forEach((attributeName, aliasedAttributeNames) -> {
                if (valuesAlreadyReplaced.contains(attributeName)) {
                    return;
                }
                Object value = attributes.get(attributeName);
                boolean valuePresent = (value != null && !(value instanceof DefaultValueHolder));
                for (String aliasedAttributeName : aliasedAttributeNames) {
                    if (valuesAlreadyReplaced.contains(aliasedAttributeName)) {
                        continue;
                    }
                    Object aliasedValue = attributes.get(aliasedAttributeName);
                    boolean aliasPresent = (aliasedValue != null && !(aliasedValue instanceof DefaultValueHolder));
                    // Something to validate or replace with an alias?
                    if (valuePresent || aliasPresent) {
                        if (valuePresent && aliasPresent) {
                            // Since annotation attributes can be arrays, we must use ObjectUtils.nullSafeEquals().
                            if (!ObjectUtils.nullSafeEquals(value, aliasedValue)) {
                                String elementAsString =
                                        (annotatedElement != null ? annotatedElement.toString() : "unknown element");
                                throw new AnnotationConfigurationException(String.format(
                                        "In AnnotationAttributes for annotation [%s] declared on %s, " +
                                                "attribute '%s' and its alias '%s' are declared with values of [%s] and [%s], " +
                                                "but only one is permitted.", attributes.displayName, elementAsString,
                                        attributeName, aliasedAttributeName, ObjectUtils.nullSafeToString(value),
                                        ObjectUtils.nullSafeToString(aliasedValue)));
                            }
                        }
                        else if (aliasPresent) {
                            // Replace value with aliasedValue
                            attributes.put(attributeName,
                                    adaptValue(annotatedElement, aliasedValue, classValuesAsString, nestedAnnotationsAsMap));
                            valuesAlreadyReplaced.add(attributeName);
                        }
                        else {
                            // Replace aliasedValue with value
                            attributes.put(aliasedAttributeName,
                                    adaptValue(annotatedElement, value, classValuesAsString, nestedAnnotationsAsMap));
                            valuesAlreadyReplaced.add(aliasedAttributeName);
                        }
                    }
                }
            });
            attributes.validated = true;
        }

        // Replace any remaining placeholders with actual default values
        for (Map.Entry<String, Object> attributeEntry : attributes.entrySet()) {
            String attributeName = attributeEntry.getKey();
            if (valuesAlreadyReplaced.contains(attributeName)) {
                continue;
            }
            Object value = attributeEntry.getValue();
            if (value instanceof DefaultValueHolder) {
                value = ((DefaultValueHolder) value).defaultValue;
                attributes.put(attributeName,
                        adaptValue(annotatedElement, value, classValuesAsString, nestedAnnotationsAsMap));
            }
        }
    }

    public static Object getValue(Annotation annotation) {
        return getValue(annotation, VALUE);
    }

    public static Object getValue( Annotation annotation,  String attributeName) {
        if (annotation == null || !StringUtils.hasText(attributeName)) {
            return null;
        }
        try {
            Method method = annotation.annotationType().getDeclaredMethod(attributeName);
            ReflectionUtils.makeAccessible(method);
            return method.invoke(annotation);
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
        catch (InvocationTargetException ex) {
            rethrowAnnotationConfigurationException(ex.getTargetException());
            throw new IllegalStateException("Could not obtain value for annotation attribute '" +
                    attributeName + "' in " + annotation, ex);
        }
        catch (Throwable ex) {
            handleIntrospectionFailure(annotation.getClass(), ex);
            return null;
        }
    }

    public static Object getDefaultValue( Annotation annotation,  String attributeName) {
        return (annotation != null ? getDefaultValue(annotation.annotationType(), attributeName) : null);
    }

    public static Object getDefaultValue(
             Class<? extends Annotation> annotationType,  String attributeName) {

        if (annotationType == null || !StringUtils.hasText(attributeName)) {
            return null;
        }
        try {
            return annotationType.getDeclaredMethod(attributeName).getDefaultValue();
        }
        catch (Throwable ex) {
            handleIntrospectionFailure(annotationType, ex);
            return null;
        }
    }

    public static <A extends Annotation> A synthesizeAnnotation(
            A annotation,  AnnotatedElement annotatedElement) {

        return synthesizeAnnotation(annotation, (Object) annotatedElement);
    }

    @SuppressWarnings("unchecked")
    static <A extends Annotation> A synthesizeAnnotation(A annotation,  Object annotatedElement) {
        if (annotation instanceof SynthesizedAnnotation || hasPlainJavaAnnotationsOnly(annotatedElement)) {
            return annotation;
        }

        Class<? extends Annotation> annotationType = annotation.annotationType();
        if (!isSynthesizable(annotationType)) {
            return annotation;
        }

        DefaultAnnotationAttributeExtractor attributeExtractor =
                new DefaultAnnotationAttributeExtractor(annotation, annotatedElement);
        InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(attributeExtractor);

        // Can always expose Spring's SynthesizedAnnotation marker since we explicitly check for a
        // synthesizable annotation before (which needs to declare @AliasFor from the same package)
        Class<?>[] exposedInterfaces = new Class<?>[] {annotationType, SynthesizedAnnotation.class};
        return (A) Proxy.newProxyInstance(annotation.getClass().getClassLoader(), exposedInterfaces, handler);
    }

    static Annotation[] synthesizeAnnotationArray(Annotation[] annotations,  Object annotatedElement) {
        if (hasPlainJavaAnnotationsOnly(annotatedElement)) {
            return annotations;
        }

        Annotation[] synthesized = (Annotation[]) Array.newInstance(
                annotations.getClass().getComponentType(), annotations.length);
        for (int i = 0; i < annotations.length; i++) {
            synthesized[i] = synthesizeAnnotation(annotations[i], annotatedElement);
        }
        return synthesized;
    }

    static Map<String, List<String>> getAttributeAliasMap( Class<? extends Annotation> annotationType) {
        if (annotationType == null) {
            return Collections.emptyMap();
        }

        Map<String, List<String>> map = attributeAliasesCache.get(annotationType);
        if (map != null) {
            return map;
        }

        map = new LinkedHashMap<>();
        for (Method attribute : getAttributeMethods(annotationType)) {
            List<String> aliasNames = getAttributeAliasNames(attribute);
            if (!aliasNames.isEmpty()) {
                map.put(attribute.getName(), aliasNames);
            }
        }

        attributeAliasesCache.put(annotationType, map);
        return map;
    }

    @SuppressWarnings("unchecked")
    private static boolean isSynthesizable(Class<? extends Annotation> annotationType) {
        if (hasPlainJavaAnnotationsOnly(annotationType)) {
            return false;
        }

        Boolean synthesizable = synthesizableCache.get(annotationType);
        if (synthesizable != null) {
            return synthesizable;
        }

        synthesizable = Boolean.FALSE;
        for (Method attribute : getAttributeMethods(annotationType)) {
            if (!getAttributeAliasNames(attribute).isEmpty()) {
                synthesizable = Boolean.TRUE;
                break;
            }
            Class<?> returnType = attribute.getReturnType();
            if (Annotation[].class.isAssignableFrom(returnType)) {
                Class<? extends Annotation> nestedAnnotationType =
                        (Class<? extends Annotation>) returnType.getComponentType();
                if (isSynthesizable(nestedAnnotationType)) {
                    synthesizable = Boolean.TRUE;
                    break;
                }
            }
            else if (Annotation.class.isAssignableFrom(returnType)) {
                Class<? extends Annotation> nestedAnnotationType = (Class<? extends Annotation>) returnType;
                if (isSynthesizable(nestedAnnotationType)) {
                    synthesizable = Boolean.TRUE;
                    break;
                }
            }
        }

        synthesizableCache.put(annotationType, synthesizable);
        return synthesizable;
    }


    static List<String> getAttributeAliasNames(Method attribute) {
        AliasDescriptor descriptor = AliasDescriptor.from(attribute);
        return (descriptor != null ? descriptor.getAttributeAliasNames() : Collections.emptyList());
    }


    static String getAttributeOverrideName(Method attribute,  Class<? extends Annotation> metaAnnotationType) {
        AliasDescriptor descriptor = AliasDescriptor.from(attribute);
        return (descriptor != null && metaAnnotationType != null ?
                descriptor.getAttributeOverrideName(metaAnnotationType) : null);
    }


    static List<Method> getAttributeMethods(Class<? extends Annotation> annotationType) {
        List<Method> methods = attributeMethodsCache.get(annotationType);
        if (methods != null) {
            return methods;
        }

        methods = new ArrayList<>();
        for (Method method : annotationType.getDeclaredMethods()) {
            if (isAttributeMethod(method)) {
                ReflectionUtils.makeAccessible(method);
                methods.add(method);
            }
        }

        attributeMethodsCache.put(annotationType, methods);
        return methods;
    }

    static boolean isAttributeMethod( Method method) {
        return (method != null && method.getParameterCount() == 0 && method.getReturnType() != void.class);
    }

    static boolean isAnnotationTypeMethod( Method method) {
        return (method != null && method.getName().equals("annotationType") && method.getParameterCount() == 0);
    }

    static Class<? extends Annotation> resolveContainerAnnotationType(Class<? extends Annotation> annotationType) {
        Repeatable repeatable = getAnnotation(annotationType, Repeatable.class);
        return (repeatable != null ? repeatable.value() : null);
    }

    static void rethrowAnnotationConfigurationException(Throwable ex) {
        if (ex instanceof AnnotationConfigurationException) {
            throw (AnnotationConfigurationException) ex;
        }
    }

    static void handleIntrospectionFailure( AnnotatedElement element, Throwable ex) {
        rethrowAnnotationConfigurationException(ex);

    }

    /**
     * Cache key for the AnnotatedElement cache.
     */
    private static final class AnnotationCacheKey implements Comparable<AnnotationCacheKey> {

        private final AnnotatedElement element;

        private final Class<? extends Annotation> annotationType;

        public AnnotationCacheKey(AnnotatedElement element, Class<? extends Annotation> annotationType) {
            this.element = element;
            this.annotationType = annotationType;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AnnotationCacheKey)) {
                return false;
            }
            AnnotationCacheKey otherKey = (AnnotationCacheKey) other;
            return (this.element.equals(otherKey.element) && this.annotationType.equals(otherKey.annotationType));
        }

        @Override
        public int hashCode() {
            return (this.element.hashCode() * 29 + this.annotationType.hashCode());
        }

        @Override
        public String toString() {
            return "@" + this.annotationType + " on " + this.element;
        }

        @Override
        public int compareTo(AnnotationCacheKey other) {
            int result = this.element.toString().compareTo(other.element.toString());
            if (result == 0) {
                result = this.annotationType.getName().compareTo(other.annotationType.getName());
            }
            return result;
        }
    }

    /**
     * {@code AliasDescriptor} encapsulates the declaration of {@code @AliasFor}
     * on a given annotation attribute and includes support for validating
     * the configuration of aliases (both explicit and implicit).
     * @since 4.2.1
     * @see #from
     * @see #getAttributeAliasNames
     * @see #getAttributeOverrideName
     */
    private static final class AliasDescriptor {

        private final Method sourceAttribute;

        private final Class<? extends Annotation> sourceAnnotationType;

        private final String sourceAttributeName;

        private final Method aliasedAttribute;

        private final Class<? extends Annotation> aliasedAnnotationType;

        private final String aliasedAttributeName;

        private final boolean isAliasPair;

        /**
         * Create an {@code AliasDescriptor} <em>from</em> the declaration
         * of {@code @AliasFor} on the supplied annotation attribute and
         * validate the configuration of {@code @AliasFor}.
         * @param attribute the annotation attribute that is annotated with
         * {@code @AliasFor}
         * @return an alias descriptor, or {@code null} if the attribute
         * is not annotated with {@code @AliasFor}
         * @see #validateAgainst
         */
        
        public static AliasDescriptor from(Method attribute) {
            AliasDescriptor descriptor = aliasDescriptorCache.get(attribute);
            if (descriptor != null) {
                return descriptor;
            }

            AliasFor aliasFor = attribute.getAnnotation(AliasFor.class);
            if (aliasFor == null) {
                return null;
            }

            descriptor = new AliasDescriptor(attribute, aliasFor);
            descriptor.validate();
            aliasDescriptorCache.put(attribute, descriptor);
            return descriptor;
        }

        @SuppressWarnings("unchecked")
        private AliasDescriptor(Method sourceAttribute, AliasFor aliasFor) {
            Class<?> declaringClass = sourceAttribute.getDeclaringClass();

            this.sourceAttribute = sourceAttribute;
            this.sourceAnnotationType = (Class<? extends Annotation>) declaringClass;
            this.sourceAttributeName = sourceAttribute.getName();

            this.aliasedAnnotationType = (Annotation.class == aliasFor.annotation() ?
                    this.sourceAnnotationType : aliasFor.annotation());
            this.aliasedAttributeName = getAliasedAttributeName(aliasFor, sourceAttribute);
            if (this.aliasedAnnotationType == this.sourceAnnotationType &&
                    this.aliasedAttributeName.equals(this.sourceAttributeName)) {
                String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] points to " +
                                "itself. Specify 'annotation' to point to a same-named attribute on a meta-annotation.",
                        sourceAttribute.getName(), declaringClass.getName());
                throw new AnnotationConfigurationException(msg);
            }
            try {
                this.aliasedAttribute = this.aliasedAnnotationType.getDeclaredMethod(this.aliasedAttributeName);
            }
            catch (NoSuchMethodException ex) {
                String msg = String.format(
                        "Attribute '%s' in annotation [%s] is declared as an @AliasFor nonexistent attribute '%s' in annotation [%s].",
                        this.sourceAttributeName, this.sourceAnnotationType.getName(), this.aliasedAttributeName,
                        this.aliasedAnnotationType.getName());
                throw new AnnotationConfigurationException(msg, ex);
            }

            this.isAliasPair = (this.sourceAnnotationType == this.aliasedAnnotationType);
        }

        private void validate() {
            // Target annotation is not meta-present?
            if (!this.isAliasPair && !isAnnotationMetaPresent(this.sourceAnnotationType, this.aliasedAnnotationType)) {
                String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] declares " +
                                "an alias for attribute '%s' in meta-annotation [%s] which is not meta-present.",
                        this.sourceAttributeName, this.sourceAnnotationType.getName(), this.aliasedAttributeName,
                        this.aliasedAnnotationType.getName());
                throw new AnnotationConfigurationException(msg);
            }

            if (this.isAliasPair) {
                AliasFor mirrorAliasFor = this.aliasedAttribute.getAnnotation(AliasFor.class);
                if (mirrorAliasFor == null) {
                    String msg = String.format("Attribute '%s' in annotation [%s] must be declared as an @AliasFor [%s].",
                            this.aliasedAttributeName, this.sourceAnnotationType.getName(), this.sourceAttributeName);
                    throw new AnnotationConfigurationException(msg);
                }

                String mirrorAliasedAttributeName = getAliasedAttributeName(mirrorAliasFor, this.aliasedAttribute);
                if (!this.sourceAttributeName.equals(mirrorAliasedAttributeName)) {
                    String msg = String.format("Attribute '%s' in annotation [%s] must be declared as an @AliasFor [%s], not [%s].",
                            this.aliasedAttributeName, this.sourceAnnotationType.getName(), this.sourceAttributeName,
                            mirrorAliasedAttributeName);
                    throw new AnnotationConfigurationException(msg);
                }
            }

            Class<?> returnType = this.sourceAttribute.getReturnType();
            Class<?> aliasedReturnType = this.aliasedAttribute.getReturnType();
            if (returnType != aliasedReturnType &&
                    (!aliasedReturnType.isArray() || returnType != aliasedReturnType.getComponentType())) {
                String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] " +
                                "and attribute '%s' in annotation [%s] must declare the same return type.",
                        this.sourceAttributeName, this.sourceAnnotationType.getName(), this.aliasedAttributeName,
                        this.aliasedAnnotationType.getName());
                throw new AnnotationConfigurationException(msg);
            }

            if (this.isAliasPair) {
                validateDefaultValueConfiguration(this.aliasedAttribute);
            }
        }

        private void validateDefaultValueConfiguration(Method aliasedAttribute) {
            Object defaultValue = this.sourceAttribute.getDefaultValue();
            Object aliasedDefaultValue = aliasedAttribute.getDefaultValue();

            if (defaultValue == null || aliasedDefaultValue == null) {
                String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] " +
                                "and attribute '%s' in annotation [%s] must declare default values.",
                        this.sourceAttributeName, this.sourceAnnotationType.getName(), aliasedAttribute.getName(),
                        aliasedAttribute.getDeclaringClass().getName());
                throw new AnnotationConfigurationException(msg);
            }

            if (!ObjectUtils.nullSafeEquals(defaultValue, aliasedDefaultValue)) {
                String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] " +
                                "and attribute '%s' in annotation [%s] must declare the same default value.",
                        this.sourceAttributeName, this.sourceAnnotationType.getName(), aliasedAttribute.getName(),
                        aliasedAttribute.getDeclaringClass().getName());
                throw new AnnotationConfigurationException(msg);
            }
        }

        /**
         * Validate this descriptor against the supplied descriptor.
         * <p>This method only validates the configuration of default values
         * for the two descriptors, since other aspects of the descriptors
         * are validated when they are created.
         */
        private void validateAgainst(AliasDescriptor otherDescriptor) {
            validateDefaultValueConfiguration(otherDescriptor.sourceAttribute);
        }

        /**
         * Determine if this descriptor represents an explicit override for
         * an attribute in the supplied {@code metaAnnotationType}.
         * @see #isAliasFor
         */
        private boolean isOverrideFor(Class<? extends Annotation> metaAnnotationType) {
            return (this.aliasedAnnotationType == metaAnnotationType);
        }

        /**
         * Determine if this descriptor and the supplied descriptor both
         * effectively represent aliases for the same attribute in the same
         * target annotation, either explicitly or implicitly.
         * <p>This method searches the attribute override hierarchy, beginning
         * with this descriptor, in order to detect implicit and transitively
         * implicit aliases.
         * @return {@code true} if this descriptor and the supplied descriptor
         * effectively alias the same annotation attribute
         * @see #isOverrideFor
         */
        private boolean isAliasFor(AliasDescriptor otherDescriptor) {
            for (AliasDescriptor lhs = this; lhs != null; lhs = lhs.getAttributeOverrideDescriptor()) {
                for (AliasDescriptor rhs = otherDescriptor; rhs != null; rhs = rhs.getAttributeOverrideDescriptor()) {
                    if (lhs.aliasedAttribute.equals(rhs.aliasedAttribute)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public List<String> getAttributeAliasNames() {
            // Explicit alias pair?
            if (this.isAliasPair) {
                return Collections.singletonList(this.aliasedAttributeName);
            }

            // Else: search for implicit aliases
            List<String> aliases = new ArrayList<>();
            for (AliasDescriptor otherDescriptor : getOtherDescriptors()) {
                if (this.isAliasFor(otherDescriptor)) {
                    this.validateAgainst(otherDescriptor);
                    aliases.add(otherDescriptor.sourceAttributeName);
                }
            }
            return aliases;
        }

        private List<AliasDescriptor> getOtherDescriptors() {
            List<AliasDescriptor> otherDescriptors = new ArrayList<>();
            for (Method currentAttribute : getAttributeMethods(this.sourceAnnotationType)) {
                if (!this.sourceAttribute.equals(currentAttribute)) {
                    AliasDescriptor otherDescriptor = AliasDescriptor.from(currentAttribute);
                    if (otherDescriptor != null) {
                        otherDescriptors.add(otherDescriptor);
                    }
                }
            }
            return otherDescriptors;
        }

        
        public String getAttributeOverrideName(Class<? extends Annotation> metaAnnotationType) {
            // Search the attribute override hierarchy, starting with the current attribute
            for (AliasDescriptor desc = this; desc != null; desc = desc.getAttributeOverrideDescriptor()) {
                if (desc.isOverrideFor(metaAnnotationType)) {
                    return desc.aliasedAttributeName;
                }
            }

            // Else: explicit attribute override for a different meta-annotation
            return null;
        }

        
        private AliasDescriptor getAttributeOverrideDescriptor() {
            if (this.isAliasPair) {
                return null;
            }
            return AliasDescriptor.from(this.aliasedAttribute);
        }


        private String getAliasedAttributeName(AliasFor aliasFor, Method attribute) {
            String attributeName = aliasFor.attribute();
            String value = aliasFor.value();
            boolean attributeDeclared = StringUtils.hasText(attributeName);
            boolean valueDeclared = StringUtils.hasText(value);

            // Ensure user did not declare both 'value' and 'attribute' in @AliasFor
            if (attributeDeclared && valueDeclared) {
                String msg = String.format("In @AliasFor declared on attribute '%s' in annotation [%s], attribute 'attribute' " +
                                "and its alias 'value' are present with values of [%s] and [%s], but only one is permitted.",
                        attribute.getName(), attribute.getDeclaringClass().getName(), attributeName, value);
                throw new AnnotationConfigurationException(msg);
            }

            // Either explicit attribute name or pointing to same-named attribute by default
            attributeName = (attributeDeclared ? attributeName : value);
            return (StringUtils.hasText(attributeName) ? attributeName.trim() : attribute.getName());
        }

        @Override
        public String toString() {
            return String.format("%s: @%s(%s) is an alias for @%s(%s)", getClass().getSimpleName(),
                    this.sourceAnnotationType.getSimpleName(), this.sourceAttributeName,
                    this.aliasedAnnotationType.getSimpleName(), this.aliasedAttributeName);
        }
    }


    private static class DefaultValueHolder {

        final Object defaultValue;

        public DefaultValueHolder(Object defaultValue) {
            this.defaultValue = defaultValue;
        }
    }

}
