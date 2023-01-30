package com.xdclass.shop.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 转换对象为字符串.
 * <p/>
 * 不输出的请在getter或者field上标注{@link Invisible}
 * <p/>
 * 需要掩码的请在getter或者field上标注{@link Maskable}
 * <p/>
 * java.util.Date会按照"yyyy-MM-dd HH:mm:ss"格式输出
 * <p/>
 * 数组(50)/集合(50)/Map(50)/String(100)超过一定的限制(括号内数字),多余的元素会输出为...
 * <p/>
 * 集合/Map已检查循环引用问题.能确保原来toString不出问题,此方法也不会出问题.
 * <p/>
 * mask长度超过100时,会先截断在mask.
 * <p/>
 * StringBuilder会初始化大小
 * <p/>
 *
 * <h3>Usage Examples</h3>
 * 修改javabean的toString方法为:
 *
 * <pre class="code">
 * {@code
 * @Override
 * public String toString() {
 *  return ToString.toString(this);
 * }
 * }
 * </pre>
 * <p>
 * 使用时请确保classpath中有javassist:
 *
 * <pre class="code">
 * {@code
 * <dependency>
 * <groupId>org.javassist</groupId>
 * <artifactId>javassist</artifactId>
 * </dependency>
 * }
 * </pre>
 *
 * @author daniel
 * @version 2020/12/15
 */
public class ToString {

    private static final String packageName = getPackageName(ToString.class);
    private static final String NULL = "null";
    private static final Map<String, I> cache = Maps.newConcurrentMap();
    private static final Map<ClassLoader, ClassPool> CLASS_POOL_CACHE = Maps.newConcurrentMap();

    private static final String DATE_FORMART = "yyyy-MM-dd HH:mm:ss";

    /**
     * 打印生成源代码
     */
    public static boolean logSource = false;
    public static String dumpClass = null;
    private static final Logger logger = LoggerFactory.getLogger(ToString.class);

    public static interface I {
        String toString(Object source);
    }

    /**
     * 标注在getter上,标识此属性不会输出
     */
    @Target({ElementType.METHOD, ElementType.FIELD})
    @Retention(RUNTIME)
    @Documented
    public static @interface Invisible {

    }

    /**
     * 标注在getter上,标识此属性会被mask,只支持String、Map类型
     */
    @Target({ElementType.METHOD, ElementType.FIELD})
    @Retention(RUNTIME)
    @Documented
    public static @interface Maskable {
        /**
         * 是否全部mask,只支持String
         */
        boolean maskAll() default false;

        /**
         * 标注在返回值为Map的getter或field上，会对指定的key对应的值mask
         */
        String[] maskKeys() default {};
    }

    public interface Masked extends Serializable {
        /**
         * 获取原始值
         */
        Object getOri();

        /**
         * 是否mask
         */
        boolean masked();
    }

    public static abstract class AbstractMasked implements Masked {
        private boolean mask = false;

        public AbstractMasked(boolean mask) {
            this.mask = mask;
        }

        public boolean isMask() {
            return mask;
        }

        public void setMask(boolean mask) {
            this.mask = mask;
        }

        @Override
        public String toString() {
            if (getOri() == null) {
                return NULL;
            } else {
                return masked() ? mask(getOri().toString()) : getOri().toString();
            }
        }

        @Override
        public boolean masked() {
            return mask;
        }
    }

    /**
     * 包裹Object
     */
    public static class MaskedObject extends AbstractMasked {
        private Object ori;

        public MaskedObject() {
            super(false);
        }

        public MaskedObject(Object ori, boolean mask) {
            super(mask);
            this.ori = ori;
        }

        public void setOri(Object ori) {
            this.ori = ori;
        }

        public Object getOri() {
            return ori;
        }

    }

    /**
     * 包裹String
     */
    public static class MaskedString extends AbstractMasked {

        private String ori;

        public MaskedString() {
            super(false);
        }

        public MaskedString(String ori, boolean mask) {
            super(mask);
            this.ori = ori;
        }

        public void setOri(String ori) {
            this.ori = ori;
        }

        public String getOri() {
            return ori;
        }

    }

    /**
     * 标注在getter上,调用此getter对象xx方法 <br/>
     * xx方法必须满足下面两个条件: 1.没有入参 2.返回String <br/>
     * 比如:
     *
     * <pre>
     * {@code
     *   @ToString.ToStringMethod("toSX")
     *   public NestBean getNestBean() {
     *      return nestBean;
     *   }
     * }
     * 生成toString会调用NestBean对象的toSX()方法.
     * </pre>
     */
    @Target({ElementType.METHOD, ElementType.FIELD})
    @Retention(RUNTIME)
    @Documented
    public static @interface ToStringMethod {
        String value();
    }

    /**
     * 从缓存中移除ClassPool
     *
     * @param classLoader
     */
    public static void removeClassPool(ClassLoader classLoader) {
        if (classLoader == null) {
            return;
        }
        CLASS_POOL_CACHE.remove(classLoader);
    }

    /**
     * 清楚缓存中所有的ClassPool
     */
    public static void clearClassPool() {
        CLASS_POOL_CACHE.clear();
    }

    /**
     * 把对象转换为字符串.
     *
     * @param o
     * @return
     */
    public static String toString(Object o) {
        if (o == null) {
            return NULL;
        }

        I i = cache.get(o.getClass().getName());
        if (i == null) {
            synchronized (o.getClass()) {
                i = cache.get(o.getClass().getName());
                if (i == null) {
                    Generator generator = new Generator(o.getClass());
                    try {
                        i = generator.generate().newInstance();
                        cache.put(o.getClass().getName(), i);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
        }

        return i.toString(o);
    }

    public static class Generator {

        /**
         * 超过此长度的数组元素用...表示
         */
        public static final int TO_STRING_ARRAY_SIZE_THRESHOLD = 50;
        public static final int TO_STRING_COLLECTION_SIZE_THRESHOLD = 50;
        public static final int TO_STRING_MAP_SIZE_THRESHOLD = 50;
        public static final int TO_STRING_STRING_SIZE_THRESHOLD = 100;
        public static final int TO_STRING_MASK_SIZE_THRESHOLD = 50;
        private static AtomicInteger index = new AtomicInteger(
                100000);

        private Class source;
        private static final String SOURCE = "s";
        private String beginSource;
        private String endSources;
        private List<String> bodySources = Lists.newArrayList();
        private int fieldIndex = 0;
        private int fieldCount = 0;
        private Map<String, Field> fieldMap = Maps.newHashMap();
        private List<String> maskKeyFields = Lists.newArrayList();

        public Generator(Class source) {
            this.source = source;
            parseFields();
        }

        private void parseFields() {
            for (Class acls = this.source; acls != Object.class; acls = acls.getSuperclass()) {
                Field[] fields = acls.getDeclaredFields();
                for (Field field : fields) {
                    if (!fieldMap.containsKey(field.getName())) {
                        fieldMap.put(field.getName(), field);
                    }
                }
            }
        }

        private void generateBegin() {
            beginSource = "public String toString(Object xxoo){\n";
            // 强制转换源对象
            String convertSource = String.format("%s %s=(%s)xxoo;", source.getName(), SOURCE,
                    source.getName(), SOURCE);
            beginSource += convertSource;
        }

        private void generateEnd() {
            endSources = "\nsb.append(\"}\");\n return sb.toString();}";
        }

        private void generateBody() {
            List<PropertyDescriptor> pds = getPropertyDescriptors();
            bodySources.add(String.format("if(%s==null){return null;}\n", SOURCE));
            bodySources
                    .add(String.format("StringBuilder sb=new StringBuilder(%d);\n", pds.size() * 20));
            bodySources.add(String.format("sb.append(\"%s{\");", source.getSimpleName()));
            fieldCount = pds.size();
            String toStringClassName = ToString.class.getName();
            for (PropertyDescriptor pd : pds) {
                Method read = pd.getReadMethod();
                if (read == null || getAnnotation(pd, Invisible.class) != null) {
                    fieldCount--;
                    continue;
                }
                fieldIndex++;
                //参数为泛型时，type不等于proptertyType
                Class<?> returnType = pd.getReadMethod().getReturnType();
                Class<?> proptertyType = pd.getPropertyType();
                String propName = pd.getName();
                //boolean generic = !(type == proptertyType);
                String readMethod = buildReadMethod(pd);
                if (returnType.isArray()) {
                    buildArray(toStringClassName, pd, readMethod, propName);
                } else if (proptertyType == String.class || returnType == String.class) {
                    buildString(toStringClassName, pd, readMethod, propName);
                } else if (isCollection(proptertyType) || isCollection(returnType)) {
                    buildCollection(toStringClassName, pd, readMethod, propName);
                } else if (isMap(proptertyType) || isMap(returnType)) {
                    buildMap(toStringClassName, pd, readMethod, propName);
                } else if (proptertyType == Date.class || returnType == Date.class) {
                    buildDate(toStringClassName, pd, readMethod, propName);
                } else {
                    doProp(propName, readMethod);
                }
            }
        }

        private String buildReadMethod(PropertyDescriptor pd) {
            String readMethod = String.format("%s.%s()", SOURCE, pd.getReadMethod().getName());
            ToStringMethod toStringMethod = getAnnotation(pd, ToStringMethod.class);
            if (toStringMethod != null) {
                readMethod = String.format("%s==null?null:%s.%s()", readMethod, readMethod,
                        toStringMethod.value());
            }
            return readMethod;
        }

        private void buildArray(String toStringClassName, PropertyDescriptor pd, String readMethod,
                                String propName) {
            Maskable maskable = getAnnotation(pd, Maskable.class);
            if (maskable == null) {
                //不做mask处理
                //fixme 如果是泛型参数，需要做类型转换
                readMethod = String.format("%s.toString(%s,%d)", toStringClassName, readMethod,
                        TO_STRING_ARRAY_SIZE_THRESHOLD);
            } else if (maskable.maskAll()) {
                //全部掩码，不输出array的内容 array=[******]
                readMethod = String.format("%s.mask(\"[%s]\")", toStringClassName,
                        MaskUtils.ALL_ASTERISK);
            } else {
                //对内容进行掩码
                readMethod = String.format("%s.toString(%s,%d,%s)", toStringClassName, readMethod,
                        TO_STRING_COLLECTION_SIZE_THRESHOLD, Boolean.toString(true));
            }

            doProp(propName, readMethod);
        }

        private void buildDate(String toStringClassName, PropertyDescriptor pd, String readMethod,
                               String propName) {
            Maskable maskable = getAnnotation(pd, Maskable.class);
            if (maskable == null) {
                //不做mask处理
                readMethod = String.format("%s.parseDate((java.util.Date)%s)", toStringClassName,
                        readMethod);
            } else {
                //要么不掩码，要么全部掩码，不输出的内容 date=******
                readMethod = String.format("%s.mask(\"%s\")", toStringClassName,
                        MaskUtils.ALL_ASTERISK);
            }

            doProp(propName, readMethod);
        }

        private void buildCollection(String toStringClassName, PropertyDescriptor pd,
                                     String readMethod, String propName) {
            Maskable maskable = getAnnotation(pd, Maskable.class);
            if (maskable == null) {
                //不做mask处理
                readMethod = String.format("%s.toString((java.util.Collection)%s,%d)",
                        toStringClassName, readMethod, TO_STRING_COLLECTION_SIZE_THRESHOLD);
            } else if (maskable.maskAll()) {
                //全部掩码，不输出list的内容 list=[******]
                readMethod = String.format("%s.mask(\"[%s]\")", toStringClassName,
                        MaskUtils.ALL_ASTERISK);
            } else {
                //对内容进行掩码
                readMethod = String.format("%s.toString((java.util.Collection)%s,%d,%s)",
                        toStringClassName, readMethod, TO_STRING_COLLECTION_SIZE_THRESHOLD,
                        Boolean.toString(true));
            }
            doProp(propName, readMethod);
        }

        private void buildMap(String toStringClassName, PropertyDescriptor pd, String readMethod,
                              String propName) {
            Maskable maskable = getAnnotation(pd, Maskable.class);
            if (maskable == null) {
                readMethod = String.format("%s.toString((java.util.Map)%s,%d,null)",
                        toStringClassName, readMethod, TO_STRING_MAP_SIZE_THRESHOLD);
            } else if (maskable.maskAll()) {
                //全部掩码，不输出list的内容 list=[******]
                readMethod = String.format("%s.mask(\"{%s}\")", toStringClassName,
                        MaskUtils.ALL_ASTERISK);
            } else {
                List<String> maskKeyList = (maskable.maskKeys() == null
                        || maskable.maskKeys().length == 0) ? Lists.newArrayList("*")
                        : Arrays.asList(maskable.maskKeys());
                readMethod = String.format("%s.toString((java.util.Map)%s,%d,%s)",
                        toStringClassName, readMethod, TO_STRING_MAP_SIZE_THRESHOLD,
                        maskKeyField(maskKeyList));
            }
            doProp(propName, readMethod);
        }

        private void buildString(String toStringClassName, PropertyDescriptor pd, String readMethod,
                                 String propName) {
            Maskable maskable = getAnnotation(pd, Maskable.class);
            if (maskable == null) {
                readMethod = String.format("%s.toString((String)%s,%d)", toStringClassName,
                        readMethod, TO_STRING_ARRAY_SIZE_THRESHOLD);
            } else if (maskable.maskAll()) {
                readMethod = String.format("%s.maskAll((String)%s)", toStringClassName, readMethod);
            } else {
                readMethod = String.format("%s.mask((String)%s)", toStringClassName, readMethod);
            }
            doProp(propName, readMethod);
        }

        private <T extends Annotation> T getAnnotation(PropertyDescriptor pd,
                                                       Class<T> annotationType) {
            T t = pd.getReadMethod().getAnnotation(annotationType);
            if (t != null) {
                return t;
            }
            Field field = fieldMap.get(pd.getName());
            if (field != null) {
                t = field.getAnnotation(annotationType);
            }
            return t;
        }

        private void doProp(String name, String readMethod) {
            bodySources.add("\nsb.append(\"" + name + "=\");");
            bodySources.add("\nsb.append(" + readMethod + ");");
            if (fieldIndex < fieldCount) {
                bodySources.add("\nsb.append(\",\");");
            }

        }

        public Class<I> generate() {
            generateBegin();
            generateBody();
            generateEnd();
            StringBuilder sb = new StringBuilder();
            sb.append(beginSource);
            for (String propSource : bodySources) {
                sb.append(propSource);
            }
            sb.append(endSources);
            String source = sb.toString();
            if (logSource) {
                logger.info("\n{}", source);
            }

            ClassLoader classLoader = getDefaultClassLoader();
            ClassPool pool = getClassPool(classLoader);

            ClassClassPath classPath = new ClassClassPath(this.getClass());
            pool.insertClassPath(classPath);
            CtClass cc = pool.makeClass(packageName + ".ToStringImpl" + index.incrementAndGet());

            Class<I> copyClass = null;
            try {
                cc.addInterface(pool.get(I.class.getName()));
                //gen field
                for (String maskKeyField : maskKeyFields) {
                    cc.addField(CtField.make(maskKeyField, cc));
                }
                //gen method
                cc.addMethod(CtNewMethod.make(source, cc));
                if (dumpClass != null) {
                    CtClass.debugDump = dumpClass;
                }
                logger.debug("classloader:{}", classLoader);
                copyClass = cc.toClass(classLoader, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return copyClass;
        }

        /**
         * 根据ClassLoader创建 ClassPool
         *
         * @param loader
         * @return
         */
        public static ClassPool getClassPool(ClassLoader loader) {
            if (loader == null) {
                return ClassPool.getDefault();
            }
            ClassPool pool = CLASS_POOL_CACHE.get(loader);
            if (pool == null) {
                pool = new ClassPool(true);
                pool.appendClassPath(new LoaderClassPath(loader));
                //如果缓存存在就不放入缓存
                ((ConcurrentHashMap) CLASS_POOL_CACHE).putIfAbsent(loader, pool);
                //从缓存中获取pool，以非同步的方式实现同步
                pool = CLASS_POOL_CACHE.get(loader);
            }
            return pool;
        }

        /**
         * 通过需要mask的参数列表，生成field源代码并返回参数名
         */
        private String maskKeyField(List<String> params) {
            StringBuilder sb = new StringBuilder();
            String fieldName = "maskKeys" + index.incrementAndGet();
            sb.append(
                    "private java.util.List " + fieldName + "=com.yjf.common.util.ToString.newList(");
            if (params != null && params.size() != 0) {
                sb.append("new String[]{");
                for (String s : params) {
                    sb.append("\"" + s + "\",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append("}");
            } else {
                sb.append(NULL);
            }
            sb.append(");");
            //添加需要生成的field源代码
            maskKeyFields.add(sb.toString());
            return fieldName;
        }

        private ClassLoader getDefaultClassLoader() {
            ClassLoader cl = null;
            try {
                cl = Thread.currentThread().getContextClassLoader();
            } catch (Exception ex) {
                // ignore
            }
            if (cl == null) {
                cl = this.getClass().getClassLoader();
            }
            return cl;
        }

        public List<PropertyDescriptor> getPropertyDescriptors() {
            Class clazz = this.source;
            BeanInfo beanInfo;
            try {
                beanInfo = Introspector.getBeanInfo(clazz);
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                List<PropertyDescriptor> propertyDescriptorList = new ArrayList<>();
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    String name = propertyDescriptor.getName();
                    if (fieldMap.containsKey(name)) {
                        propertyDescriptorList.add(propertyDescriptor);
                    }
                }
                return propertyDescriptorList;
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }
        }

        private boolean isMap(Class<?> type) {
            return Map.class.isAssignableFrom(type);
        }

        private boolean isCollection(Class<?> type) {
            return Collection.class.isAssignableFrom(type);
        }
    }

    private static String getPackageName(Class<?> clazz) {
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(".");
        return (lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "");
    }

    /**
     * 集合toString 注意避免循环引用
     *
     * @param collection
     * @param size
     * @return
     */
    public static String toString(Collection collection, int size) {
        return toString(collection, size, false);
    }

    public static String toString(Collection collection, int size, boolean masked) {
        if (collection == null) {
            return NULL;
        }
        Iterator it = collection.iterator();
        if (!it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder(10 * Math.min(size, collection.size()));
        sb.append('[');
        for (int i = 0; i < size; i++) {
            Object e = it.next();
            sb.append(e == collection ? "(this Collection)"
                    : (masked ? (e == null ? e : mask(e.toString())) : e));
            if (!it.hasNext())
                return sb.append(']').toString();
            sb.append(',');
        }
        sb.append("...]");
        return sb.toString();
    }

    /**
     * map tostring 注意避免循环引用
     *
     * @param map      map
     * @param size     打印entry个数
     * @param maskKeys 需要mask的key列表
     * @return
     */
    public static String toString(Map map, int size, List<String> maskKeys) {
        if (map == null) {
            return NULL;
        }
        Iterator<Map.Entry> it = map.entrySet().iterator();
        if (!it.hasNext())
            return "{}";

        //如果maskKeys="*"的话，对所有条目进行掩码
        boolean maskAllKeys = false;
        if (maskKeys != null && map != null && maskKeys.size() == 1 && "*".endsWith(maskKeys.get(0))
                && !map.containsKey(maskKeys.get(0))) {
            maskAllKeys = true;
        }

        StringBuilder sb = new StringBuilder(10 * Math.min(size, map.size()));
        sb.append('{');
        for (int i = 0; i < size; i++) {
            Map.Entry e = it.next();
            Object key = e.getKey();
            Object value = e.getValue();
            sb.append(key == map ? "(this Map)" : key);
            sb.append('=');
            if (maskAllKeys || (maskKeys != null && maskKeys.contains(key))) {
                String maskValue;
                if (value == null) {
                    maskValue = NULL;
                } else if (value instanceof Masked) {
                    maskValue = value.toString();
                } else {
                    maskValue = mask(value.toString());
                }
                sb.append(value == map ? "(this Map)" : maskValue);
            } else {
                sb.append(value == map ? "(this Map)" : value);
            }
            if (!it.hasNext()) {
                return sb.append('}').toString();
            }
            sb.append(',');
        }
        sb.append("...}");
        return sb.toString();
    }

    /**
     * string tostring
     *
     * @param str
     * @param size
     * @return
     */
    public static String toString(String str, int size) {
        if (str == null) {
            return NULL;
        }
        if (str.length() < size) {
            return str;
        } else {
            return str.substring(0, size) + "...";
        }

    }

    public static String toString(long[] a, int size) {
        return toString(a, size, false);
    }

    public static String toString(long[] a, int size, boolean masked) {
        if (a == null)
            return NULL;
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder(5 * Math.min(size, a.length));
        b.append('[');
        for (int i = 0; i < size; i++) {
            b.append(masked ? mask(Long.toString(a[i])) : a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(",");
        }
        b.append("...]");
        return b.toString();
    }

    public static String toString(int[] a, int size) {
        return toString(a, size, false);
    }

    public static String toString(int[] a, int size, boolean masked) {
        if (a == null)
            return NULL;
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder(5 * Math.min(size, a.length));
        b.append('[');
        for (int i = 0; i < size; i++) {
            b.append(masked ? mask(Integer.toString(a[i])) : a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(",");
        }
        b.append("...]");
        return b.toString();
    }

    //public static String toString(Object o, int size) {
    //	if (o == null)
    //		return NULL;
    //	if (o instanceof String) {
    //		return toString((String) o, size);
    //	} else if (o.getClass().isArray()) {
    //		return toString((Object[]) o, size);
    //	} else if (o instanceof Map) {
    //		return toString((Map) o, size);
    //	} else if (o instanceof Collection) {
    //		return toString((Collection) o, size);
    //	} else {
    //		return toString(o);
    //	}
    //}

    public static String toString(short[] a, int size) {
        return toString(a, size, false);
    }

    public static String toString(short[] a, int size, boolean masked) {

        if (a == null)
            return NULL;
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder(3 * Math.min(size, a.length));
        b.append('[');
        for (int i = 0; i < size; i++) {
            b.append(masked ? mask(Short.toString(a[i])) : a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(",");
        }
        b.append("...]");
        return b.toString();
    }

    public static String toString(char[] a, int size) {
        return toString(a, size, false);
    }

    public static String toString(char[] a, int size, boolean masked) {

        if (a == null)
            return NULL;
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder(1 * Math.min(size, a.length));
        b.append('[');
        for (int i = 0; i < size; i++) {
            b.append(masked ? MaskUtils.SEPARATOR_CHAR_ASTERISK : a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(",");
        }
        b.append("...]");
        return b.toString();
    }

    public static String toString(byte[] a, int size) {
        return toString(a, size, false);
    }

    public static String toString(byte[] a, int size, boolean masked) {
        if (a == null)
            return NULL;
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder(1 * Math.min(size, a.length));
        b.append('[');
        for (int i = 0; i < size; i++) {
            b.append(masked ? mask(Byte.toString(a[i])) : a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(",");
        }
        b.append("...]");
        return b.toString();
    }

    public static String toString(boolean[] a, int size) {
        return toString(a, size, false);
    }

    public static String toString(boolean[] a, int size, boolean masked) {

        if (a == null)
            return NULL;
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder(5 * Math.min(size, a.length));
        b.append('[');
        for (int i = 0; i < size; i++) {
            b.append(masked ? MaskUtils.SEPARATOR_CHAR_ASTERISK : a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(",");
        }
        b.append("...]");
        return b.toString();
    }

    public static String toString(float[] a, int size) {
        return toString(a, size, false);
    }

    public static String toString(float[] a, int size, boolean masked) {
        if (a == null)
            return NULL;

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder(5 * Math.min(size, a.length));
        b.append('[');
        for (int i = 0; i < size; i++) {
            b.append(masked ? mask(Float.toString(a[i])) : a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(",");
        }
        b.append("...]");
        return b.toString();
    }

    public static String toString(double[] a, int size) {
        return toString(a, size, false);
    }

    public static String toString(double[] a, int size, boolean masked) {
        if (a == null)
            return NULL;
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder(5 * Math.min(size, a.length));
        b.append('[');
        for (int i = 0; i < size; i++) {
            b.append(masked ? mask(Double.toString(a[i])) : a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(",");
        }
        b.append("...]");
        return b.toString();
    }

    public static String toString(Object[] a, int size) {
        return toString(a, size, false);
    }

    public static String toString(Object[] a, int size, boolean masked) {
        if (a == null)
            return NULL;

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder(10 * Math.min(size, a.length));
        b.append('[');
        for (int i = 0; i < size; i++) {
            b.append(masked ? mask(String.valueOf(a[i])) : String.valueOf(a[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(",");
        }
        b.append("...]");
        return b.toString();
    }

    public static String toString(byte i) {
        return Byte.toString(i);
    }

    public static String toString(byte[] i) {
        return toString(i, Generator.TO_STRING_ARRAY_SIZE_THRESHOLD);
    }

    public static String toString(short i) {
        return Short.toString(i);
    }

    public static String toString(short[] i) {
        return toString(i, Generator.TO_STRING_ARRAY_SIZE_THRESHOLD);
    }

    public static String toString(int i) {
        return Integer.toString(i);
    }

    public static String toString(int[] i) {
        return toString(i, Generator.TO_STRING_ARRAY_SIZE_THRESHOLD);
    }

    public static String toString(long i) {
        return Long.toString(i);
    }

    public static String toString(long[] i) {
        return toString(i, Generator.TO_STRING_ARRAY_SIZE_THRESHOLD);
    }

    public static String toString(char i) {
        return Character.toString(i);
    }

    public static String toString(char[] i) {
        return toString(i, Generator.TO_STRING_ARRAY_SIZE_THRESHOLD);
    }

    public static String toString(float i) {
        return Float.toString(i);
    }

    public static String toString(float[] i) {
        return toString(i, Generator.TO_STRING_ARRAY_SIZE_THRESHOLD);
    }

    public static String toString(double i) {
        return Double.toString(i);
    }

    public static String toString(double[] i) {
        return toString(i, Generator.TO_STRING_ARRAY_SIZE_THRESHOLD);
    }

    public static String toString(boolean i) {
        return Boolean.toString(i);
    }

    public static String toString(boolean[] i) {
        return toString(i, Generator.TO_STRING_ARRAY_SIZE_THRESHOLD);
    }

    public static String toString(String i) {
        return toString(i, Generator.TO_STRING_STRING_SIZE_THRESHOLD);
    }

    public static String toString(String[] i) {
        return toString(i, Generator.TO_STRING_ARRAY_SIZE_THRESHOLD);
    }

    public static String toString(Collection i) {
        return toString(i, Generator.TO_STRING_ARRAY_SIZE_THRESHOLD);
    }

    public static String toString(Collection i, boolean masked) {
        return toString(i, Generator.TO_STRING_ARRAY_SIZE_THRESHOLD, masked);
    }

    public static String toString(Map i) {
        return toString(i, Generator.TO_STRING_MAP_SIZE_THRESHOLD, null);
    }

    public static String toString(Object[] i) {
        return toString(i, Generator.TO_STRING_ARRAY_SIZE_THRESHOLD);
    }

    /**
     * 把字符串mask
     *
     * @param str 字符串
     * @return mask后的字符串
     */
    public static String mask(String str) {
        if (str == null) {
            return NULL;
        } else if (str.length() > Generator.TO_STRING_MASK_SIZE_THRESHOLD) {
            str = str.substring(0, Generator.TO_STRING_MASK_SIZE_THRESHOLD) + "...";
        }
        return MaskUtils.mask(str);
    }

    public static String maskAll(String str) {
        return MaskUtils.maskAll(str);
    }

    public static <E> List<E> newList(E... e) {
        List<E> list = new ArrayList<>();
        if (e != null) {
            for (E e1 : e) {
                list.add(e1);
            }
        }
        return list;
    }

    public static String parseDate(Date date) {
        if (date == null) {
            return NULL;
        }
        return new SimpleDateFormat(DATE_FORMART).format(date);
    }

}
