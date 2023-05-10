package cn.bugstack.springframework.util;

/**
 * 将读取到得属性填充到容器
 */
public interface StringValueResolver {
    String resolveStringValue(String strVal);

}
