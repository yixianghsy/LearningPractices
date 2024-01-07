package com.mall.portal.service.impl;

import com.mall.api.ResultCode;
import com.mall.exception.Asserts;
import com.mall.exception.BusinessException;
import com.mall.mansger.model.PmsProduct;
import com.mall.mansger.model.PmsSkuStock;
import com.mall.mansger.service.PmsSkuStockService;
import com.mall.order.dto.CartItemStockDTO;
import com.mall.order.mapper.OmsCartItemMapper;
import com.mall.order.model.OmsCartItem;
import com.mall.order.model.OmsCartItemExample;
import com.mall.portal.dao.PortalProductDao;
import com.mall.portal.domain.CartProduct;
import com.mall.portal.domain.CartPromotionItem;
import com.mall.portal.dto.AddCarDTO;
import com.mall.portal.service.OmsCartItemService;
import com.mall.portal.service.OmsPromotionService;
import com.mall.portal.service.PmsProductService;
import com.mall.portal.service.UmsMemberService;
import com.mall.sso.model.UmsMember;
import io.swagger.annotations.Example;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 购物车管理Service实现类
 * Created on 2018/8/2.
 */
@Service
public class OmsCartItemServiceImpl implements OmsCartItemService {
    @Autowired
    private OmsCartItemMapper cartItemMapper;
    @Autowired
    private PortalProductDao productDao;
    @Autowired
    private OmsPromotionService promotionService;
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    PmsProductService productService;
    @Autowired
    OmsCartItemMapper omsCartItemMapper;
    @Reference
    PmsSkuStockService skuStockService;
    @Override
    public int add(OmsCartItem cartItem) {
        int count;
        UmsMember currentMember = memberService.getCurrentMember();
        cartItem.setMemberId(currentMember.getId());
        cartItem.setMemberNickname(currentMember.getNickname());
        cartItem.setDeleteStatus(0);
        OmsCartItem existCartItem = getCartItem(cartItem);
        if (existCartItem == null) {//创建购物车
            cartItem.setCreateDate(new Date());
            //查询产品信息
            CartProduct cartProduct = productDao.getCartProduct(cartItem.getProductId());
            cartItem.setProductName(cartProduct.getName());
            cartItem.setPrice(cartProduct.getPrice());
            cartItem.setProductPic(cartProduct.getPic());
            cartItem.setProductBrand(cartProduct.getBrandName());
            cartItem.setProductCategoryId(cartProduct.getProductCategoryId());
            cartItem.setProductSn(cartProduct.getProductSn());
            cartItem.setProductSubTitle(cartProduct.getSubTitle());
            cartItem.setPrice(cartProduct.getPrice());
            //遍历产品sku，设置购买规格
            cartProduct.getSkuStockList().stream().forEach((skuItem)->{
                if(cartItem.getProductSkuId() == skuItem.getId()){
                    cartItem.setSp1(skuItem.getSp1());
                    cartItem.setSp2(skuItem.getSp2());
                    cartItem.setSp3(skuItem.getSp3());
                    cartItem.setProductPic(skuItem.getPic());
                    cartItem.setPrice(skuItem.getPrice());
                    cartItem.setProductSkuCode(skuItem.getSkuCode());
                    //product_attr,此处有设计缺陷，不太好拿，暂时留空
                }
            });
            count = cartItemMapper.insert(cartItem);
            // 修改  给商品数量+1
        } else {
            cartItem.setModifyDate(new Date());
            existCartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
            count = cartItemMapper.updateByPrimaryKey(existCartItem);
        }
        return count;
    }

    @Override
    public Boolean add(AddCarDTO addCarDTO) {
        OmsCartItem omsCartItem = new OmsCartItem();
        BeanUtils.copyProperties(addCarDTO,omsCartItem);
        UmsMember currentMember = memberService.getCurrentMember();
        omsCartItem.setMemberId(currentMember.getId());
        // 判断同一个商品、sku、用户 下是否添加的重复的购物车
        OmsCartItem cartItem = getCartItem(omsCartItem.getProductId(), omsCartItem.getProductSkuId(), omsCartItem.getMemberId());
        // 新增
        if(cartItem==null) {
            omsCartItem.setMemberNickname(currentMember.getNickname());
            // 查询sku
            PmsSkuStock sku = skuStockService.getById(omsCartItem.getProductSkuId());
            if (sku == null) Asserts.fail(ResultCode.VALIDATE_FAILED);
            omsCartItem.setPrice(sku.getPrice());
            omsCartItem.setSp1(sku.getSp1());
            omsCartItem.setSp2(sku.getSp2());
            omsCartItem.setSp3(sku.getSp3());
            omsCartItem.setProductPic(sku.getPic());
            omsCartItem.setProductSkuCode(sku.getSkuCode());
            PmsProduct product = productService.getById(omsCartItem.getProductId());
            if (product == null) Asserts.fail(ResultCode.VALIDATE_FAILED);
            omsCartItem.setProductName(product.getName());
            omsCartItem.setProductBrand(product.getBrandName());
            omsCartItem.setProductSn(product.getProductSn());
            omsCartItem.setProductSubTitle(product.getSubTitle());
            omsCartItem.setProductCategoryId(product.getProductCategoryId());

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
    /**
     * getCarProdutSum
     * @return
     */
    @Override
    public Integer getCarProdutSum() {
        Long memberId = memberService.getCurrentMember().getId();
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



    /**
     * 购物车产品数量
     * @return
     */
    public Long cartItemCount(){
        return cartItemMapper.countByExample(new OmsCartItemExample());
    }
    /**
     * 根据会员id,商品id和规格获取购物车中商品
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
    /**
     * 判断同一个商品、sku、用户 下是否添加的重复的购物车
     * @param productId
     * @param skuId
     * @param memberId
     * @return
     */
    public OmsCartItem getCartItem(Long productId,Long skuId, Long memberId){
        OmsCartItemExample example = new OmsCartItemExample();
//        OmsCartItemExample.Criteria criteria = example.createCriteria().andMemberIdEqualTo(memberId)
        OmsCartItemExample.Criteria criteria = example.createCriteria().andDeleteStatusEqualTo(0)
                // product_id = 27 , AND product_sku_id = 99 ,AND member_id = 1
                .andProductIdEqualTo(productId).andMemberIdEqualTo(memberId);
        if (!StringUtils.isEmpty(skuId)) {
            criteria.andProductSkuIdEqualTo(skuId);
        }
        List<OmsCartItem> cartItemList = cartItemMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(cartItemList)) {
            return cartItemList.get(0);
        }
        return null;
    }

    @Override
    public List<OmsCartItem> list(Long memberId) {
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andMemberIdEqualTo(memberId);
        return cartItemMapper.selectByExample(example);
    }
    @Override
    public List<CartItemStockDTO> getList(Long memberId) {
        List<CartItemStockDTO> list=cartItemMapper.getCartItemStock(memberId);
        return list;
    }
    /**
     * 获取被选择的包含促销活动信息的购物车列表
     * add by yangguo
     * @param memberId
     * @param itemIds
     * @return
     */
    public List<CartPromotionItem> listSelectedPromotion(Long memberId,List<Long> itemIds) throws BusinessException {
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria()
                .andDeleteStatusEqualTo(0)
                .andMemberIdEqualTo(memberId)
                .andIdIn(itemIds);
        List<OmsCartItem> cartItemList = cartItemMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(cartItemList)){
            throw new BusinessException("没有选择购物车购买的商品");
        }
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(cartItemList)){
            cartPromotionItemList = promotionService.calcCartPromotion(cartItemList);
        }
        return cartPromotionItemList;
    }

    @Override
    public List<CartPromotionItem> listPromotion(Long memberId) {
        List<OmsCartItem> cartItemList = list(memberId);
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(cartItemList)){
            cartPromotionItemList = promotionService.calcCartPromotion(cartItemList);
        }
        return cartPromotionItemList;
    }

    @Override
    public int updateQuantity(Long id, Long memberId, Integer quantity) {
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setQuantity(quantity);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andDeleteStatusEqualTo(0)
                .andIdEqualTo(id).andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(cartItem, example);
    }

    @Override
    public int delete(Long memberId, List<Long> ids) {
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andIdIn(ids).andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(record, example);
    }

    @Override
    public CartProduct getCartProduct(Long productId) {
        return productDao.getCartProduct(productId);
    }

    @Override
    public int updateAttr(OmsCartItem cartItem) {
        //删除原购物车信息
        OmsCartItem updateCart = new OmsCartItem();
        updateCart.setId(cartItem.getId());
        updateCart.setModifyDate(new Date());
        updateCart.setDeleteStatus(1);
        cartItemMapper.updateByPrimaryKeySelective(updateCart);
        cartItem.setId(null);
        add(cartItem);
        return 1;
    }

    @Override
    public int clear(Long memberId) {
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(record,example);
    }

}
