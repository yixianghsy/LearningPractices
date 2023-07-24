package com.mall.item.service.ipml;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.mall.item.mapper.TbItemDescMapper;
import com.mall.item.mapper.TbItemMapper;
import com.mall.item.service.ItemService;
import com.mall.modules.Item.TbItem;
import com.mall.modules.Item.TbItemDesc;
import com.mall.modules.Item.TbItemExample;
import com.mall.pojo.EasyUIDataGridResult;
import com.mall.utils.E3Result;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.util.List;
@Service
public class ItemServiceImpl implements ItemService {
    @Resource
    private TbItemDescMapper tbItemDescMapper;
    @Resource
    private TbItemMapper tbItemMapper;

    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);


    @Override
    public TbItem getItemById(long itemId) {
        return null;
    }

    @Override
    public EasyUIDataGridResult getItemListgetItemList(int page, int rows) {
        System.out.println("ItemServiceImpl.getItemListgetItemList");
        System.out.println("page = " + page + ", rows = " + rows);
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample example = new TbItemExample();

        List<TbItem> list = tbItemMapper.selectByExample(example);
        //取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);

        //创建返回结果对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);
        return result;
    }

    @Override
    public TbItemDesc getItemDescById(long itemId) {
        return null;
    }

    @Override
    public E3Result addItem(TbItem item, String desc) {
        return null;
    }

    @Override
    public void deleteItemList(Long[] ids) {
        TbItem tbItem= new TbItem();
        for (Long id:ids){
            tbItem.setId(id);
            //1-正常，2-下架，3-删除
            tbItem.setStatus((byte) 3);
            //执行查询
            tbItemMapper.updateByPrimaryKeySelective(tbItem);
        }
    }
    @Override
    public void instockItemList(Long[] ids) {
        TbItem tbItem= new TbItem();
        for (Long id:ids){
            tbItem.setId(id);
            //1-正常，2-下架，3-删除
            tbItem.setStatus((byte) 2);
            //执行查询
            tbItemMapper.updateByPrimaryKeySelective(tbItem);
        }

    }

    @Override
    public void reshelfItemList(Long[] ids) {
        TbItem tbItem= new TbItem();
        for (Long id:ids){
            tbItem.setId(id);
            //1-正常，2-下架，3-删除
            tbItem.setStatus((byte) 1);
            //执行查询
            tbItemMapper.updateByPrimaryKeySelective(tbItem);
        }

    }

    private static ByteBuffer buffer = ByteBuffer.allocate(8);
    //byte 数组与 long 的相互转换
    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}


