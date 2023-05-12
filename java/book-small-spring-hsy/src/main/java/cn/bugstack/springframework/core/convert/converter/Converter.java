package cn.bugstack.springframework.core.convert.converter;

/**
 * 类型转换处理接口
 * @param <S>
 * @param <T>
 */
public interface Converter<S, T>  {
    /** Convert the source object of type {@code S} to target type {@code T}. */
    T convert(S source);
}
