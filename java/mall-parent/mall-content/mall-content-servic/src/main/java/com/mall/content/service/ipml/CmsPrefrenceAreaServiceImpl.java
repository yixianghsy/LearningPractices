package com.mall.content.service.ipml;

import com.mall.content.mapper.CmsPrefrenceAreaMapper;
import com.mall.content.model.CmsPrefrenceArea;
import com.mall.content.model.CmsPrefrenceAreaExample;
import com.mall.content.service.CmsPrefrenceAreaService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 商品优选管理Service实现类
 * Created by macro on 2018/6/1.
 */
@Service
public class CmsPrefrenceAreaServiceImpl implements CmsPrefrenceAreaService {
    @Autowired
    private CmsPrefrenceAreaMapper prefrenceAreaMapper;

    @Override
    public List<CmsPrefrenceArea> listAll() {
        return prefrenceAreaMapper.selectByExample(new CmsPrefrenceAreaExample());
    }
}
