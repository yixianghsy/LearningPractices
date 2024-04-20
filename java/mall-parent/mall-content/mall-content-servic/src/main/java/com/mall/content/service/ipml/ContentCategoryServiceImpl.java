package com.mall.content.service.ipml;
import com.mall.content.service.ContentCategoryService;
import com.mall.pojo.EasyUITreeNode;
import com.mall.utils.E3Result;
import org.apache.dubbo.config.annotation.Service;
import java.util.List;

/**
 * 内容分类管理Service
 * <p>Title: ContentCategoryServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Service
public class ContentCategoryServiceImpl  implements ContentCategoryService {


    @Override
    public List<EasyUITreeNode> getContentCategoryList(Long parentId) {

        return null;

    }

    @Override
    public E3Result addContentCategory(long parentId, String name) {
        return null;
    }
}
