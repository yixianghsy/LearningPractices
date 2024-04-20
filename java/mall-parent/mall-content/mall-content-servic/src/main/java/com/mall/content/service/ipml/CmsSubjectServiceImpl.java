package com.mall.content.service.ipml;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;

import com.mall.content.mapper.CmsSubjectMapper;
import com.mall.content.model.CmsSubject;
import com.mall.content.model.CmsSubjectExample;
import com.mall.content.service.CmsSubjectService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

/**
 * 商品专题管理Service实现类
 * Created by macro on 2018/6/1.
 */
@Service
public class CmsSubjectServiceImpl implements CmsSubjectService {
    @Autowired
    private CmsSubjectMapper subjectMapper;

    @Override
    public List<CmsSubject> listAll() {
        return subjectMapper.selectByExample(new CmsSubjectExample());
    }

    @Override
    public List<CmsSubject> list(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        CmsSubjectExample example = new CmsSubjectExample();
        CmsSubjectExample.Criteria criteria = example.createCriteria();
        if (!StrUtil.isEmpty(keyword)) {
            criteria.andTitleLike("%" + keyword + "%");
        }
        return subjectMapper.selectByExample(example);
    }
}
