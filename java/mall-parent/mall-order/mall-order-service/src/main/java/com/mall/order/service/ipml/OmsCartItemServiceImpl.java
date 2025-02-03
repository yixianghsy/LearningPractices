package com.mall.order.service.ipml;

import com.mall.api.ResultCode;
import com.mall.exception.Asserts;
import com.mall.order.dto.AddCarDTO;
import com.mall.order.dto.CartItemStockDTO;
import com.mall.order.mapper.OmsCartItemMapper;
import com.mall.order.model.OmsCartItem;
import com.mall.order.model.OmsCartItemExample;
import com.mall.order.service.OmsCartItemService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OmsCartItemServiceImpl  implements OmsCartItemService {

    @Autowired
    private OmsCartItemMapper cartItemMapper;

    @Autowired
    OmsCartItemMapper omsCartItemMapper;
    @Override
    public int add(OmsCartItem cartItem) {
        return 0;
    }

    @Override
    public Boolean add(AddCarDTO addCarDTO) {
        OmsCartItem omsCartItem = new OmsCartItem();
        BeanUtils.copyProperties(addCarDTO,omsCartItem);
        // 判断同一个商品、sku、用户 下是否添加的重复的购物车
        OmsCartItem cartItem = getCartItem(omsCartItem);
        // 新增
        if(cartItem==null) {
            omsCartItem.setMemberNickname(addCarDTO.getMemberNickname());
            omsCartItem.setPrice(addCarDTO.getPrice());
            omsCartItem.setSp1(addCarDTO.getSp1());
            omsCartItem.setSp2(addCarDTO.getSp2());
            omsCartItem.setSp3(addCarDTO.getSp3());
            omsCartItem.setProductPic(addCarDTO.getPic());
            omsCartItem.setProductSkuCode(addCarDTO.getSkuCode());
            omsCartItem.setCreateDate(new Date());
            omsCartItem.setModifyDate(new Date());
            // TODO  插入语句有问题
            return cartItemMapper.insert(omsCartItem)>0;
            // 修改  给商品数量+1
        } else{
            cartItem.setQuantity(cartItem.getQuantity()+1);
            cartItem.setModifyDate(new Date());
            OmsCartItemExample example = new OmsCartItemExample();
            example.createCriteria().andDeleteStatusEqualTo(0).andIdEqualTo(cartItem.getId());
//            example.createCriteria().andIdEqualTo(cartItem.getId()).andDeleteStatusEqualTo(0);
            return cartItemMapper.updateByExampleSelective(cartItem,example) > 0;

        }
    }

    @Override
    public Long cartItemCount() {
        return null;
    }

    @Override
    public List<OmsCartItem> list(Long memberId) {
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andMemberIdEqualTo(memberId);
        return cartItemMapper.selectByExample(example);
    }

    @Override
    public int updateQuantity(Long id,  Integer quantity) {
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setQuantity(quantity);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andDeleteStatusEqualTo(0)
                .andIdEqualTo(id);
        return cartItemMapper.updateByExampleSelective(cartItem, example);
    }

    @Override
    public int delete( List<Long> ids) {
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andIdIn(ids);
        return cartItemMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateAttr(OmsCartItem cartItem) {
        return 0;
    }

    @Override
    public int clear(Long memberId) {
        return 0;
    }

    @Override
    public Integer getCarProdutSum(Long memberId) {
        List<Map<String, Object>> list = omsCartItemMapper.selectMaps(memberId);
        // SELECT sum(quantity) as total FROM oms_cart_item WHERE delete_status=0 AND (member_id = ?)
        if(list!=null && list.size()==1){
            Map<String, Object> map = list.get(0);
            if(map.get("total")!=null){
                return Integer.parseInt(map.get("total").toString());
            }
        }
        return 0;
    }

    @Override
    public List<OmsCartItem> listByIds(List<Long> ids) {

        /**
         * SELECT id,product_id,product_sku_id,member_id,quantity,
         * price,sp1,sp2,sp3,product_pic,product_name,product_sub_title,
         * product_sku_code,member_nickname,create_date,modify_date,
         * delete_status,product_category_id,product_brand,product_sn,
         * product_attr FROM oms_cart_item WHERE id IN ( ? ) AND delete_status=0
         */
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andIdIn(ids).andDeleteStatusEqualTo(0);
        return  cartItemMapper.selectByExample(example);
    }

    @Override
    public List<CartItemStockDTO> getList(Long memberId) {
        return null;
    }

    @Override
    public List<CartItemStockDTO> getCartItemStockByIds(Long id, List<Long> itemIds) {
        return cartItemMapper.getCartItemStockByIds(id, itemIds);
    }

    @Override
    public void removeByIds(List<Long> ids) {
        cartItemMapper.removeByIds(ids);
    }

    /**
     * 判断同一个商品、sku、用户 下是否添加的重复的购物车
     * @param productId
     * @param skuId
     * @param memberId
     * @return
     */
    private OmsCartItem getCartItem(OmsCartItem cartItem) {
        OmsCartItemExample example = new OmsCartItemExample();
        //5.添加xxx字段等于value条件
        //criteria.andXxxEqualTo(value)
        OmsCartItemExample.Criteria criteria = example.createCriteria().andMemberIdEqualTo(cartItem.getMemberId())
                .andProductIdEqualTo(cartItem.getProductId()).andDeleteStatusEqualTo(0);
        if (!StringUtils.isEmpty(cartItem.getSp1())) {
            criteria.andSp1EqualTo(cartItem.getSp1());
        }
        if (!StringUtils.isEmpty(cartItem.getSp2())) {
            criteria.andSp2EqualTo(cartItem.getSp2());
        }
        if (!StringUtils.isEmpty(cartItem.getSp3())) {
            criteria.andSp3EqualTo(cartItem.getSp3());
        }
        if (!StringUtils.isEmpty(cartItem.getProductSkuId())) {
            criteria.andProductSkuIdEqualTo(cartItem.getProductSkuId());
        }
        List<OmsCartItem> cartItemList = cartItemMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(cartItemList)) {
            return cartItemList.get(0);
        }
        return null;
    }
}
