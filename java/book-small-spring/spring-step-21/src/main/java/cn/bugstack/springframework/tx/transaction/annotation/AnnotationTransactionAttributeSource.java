package cn.bugstack.springframework.tx.transaction.annotation;

import cn.bugstack.springframework.tx.transaction.interceptor.AbstractFallbackTransactionAttributeSource;
import cn.bugstack.springframework.tx.transaction.interceptor.TransactionAttribute;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

/**
 *
 * @description Implementation of the TransactionAttributeSource}
 * interface for working with transaction metadata in JDK 1.5+ annotation format.
 * @date 2022/3/16
 *  /CodeDesignTutorials
 *
 */
public class AnnotationTransactionAttributeSource extends AbstractFallbackTransactionAttributeSource implements Serializable {

    private final boolean publicMethodsOnly;

    private final Set<TransactionAnnotationParser> annotationParsers;

    public AnnotationTransactionAttributeSource() {
        this(true);
    }

    public AnnotationTransactionAttributeSource(boolean publicMethodsOnly) {
        this.publicMethodsOnly = publicMethodsOnly;
        this.annotationParsers = Collections.singleton(new SpringTransactionAnnotationParser());
    }

    @Override
    protected TransactionAttribute findTransactionAttribute(Method method) {
        return determineTransactionAttribute(method);
    }

    @Override
    public TransactionAttribute findTransactionAttribute(Class<?> clazz) {
        return determineTransactionAttribute(clazz);
    }

    protected TransactionAttribute determineTransactionAttribute(AnnotatedElement element) {
        for (TransactionAnnotationParser annotationParser : this.annotationParsers) {
            TransactionAttribute attr = annotationParser.parseTransactionAnnotation(element);
            if (attr != null) {
                return attr;
            }
        }
        return null;
    }

}
