package com.mall.content.service.ipml;

import com.mall.content.service.ContentService;
import com.mall.pojo.EasyUIDataGridResult;
import com.mall.utils.E3Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 内容管理Service
 * <p>Title: ContentServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Service
public class ContentServiceImpl  implements ContentService {

    @Override
    public EasyUIDataGridResult getContentListByCategoryId(Long categoryId, int page, int rows) {
        return null;
    }

}
