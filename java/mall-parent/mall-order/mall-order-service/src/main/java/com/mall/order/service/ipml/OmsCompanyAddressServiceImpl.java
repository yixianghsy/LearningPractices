package com.mall.order.service.ipml;

import com.mall.order.mapper.OmsCompanyAddressMapper;
import com.mall.order.model.OmsCompanyAddress;
import com.mall.order.model.OmsCompanyAddressExample;
import com.mall.order.service.OmsCompanyAddressService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

/**
 * 收货地址管理Service实现类
 * Created by macro on 2018/10/18.
 */
@Service
public class OmsCompanyAddressServiceImpl implements OmsCompanyAddressService {
    @Autowired
    private OmsCompanyAddressMapper companyAddressMapper;
    @Override
    public List<OmsCompanyAddress> list() {
        return companyAddressMapper.selectByExample(new OmsCompanyAddressExample());
    }
}
