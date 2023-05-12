package cn.bugstack.springframework.core.convert.support;

/**
 * @description A specialization of {@link GenericConversionService} configured by default
 *
 */
public class DefaultConversionService extends GenericConversionService{

    public DefaultConversionService() {
        addDefaultConverters(this);

    }

    private void addDefaultConverters(DefaultConversionService converterRegistry) {
        // 添加各类类型转换工厂
        converterRegistry.addConverterFactory(new StringToNumberConverterFactory());

    }
}
