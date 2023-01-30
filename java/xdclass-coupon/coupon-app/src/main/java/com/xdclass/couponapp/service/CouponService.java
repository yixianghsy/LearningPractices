package com.xdclass.couponapp.service;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.xdclass.couponapp.constant.Constant;
import com.xdclass.couponapp.domain.TCoupon;
import com.xdclass.couponapp.domain.TCouponExample;
import com.xdclass.couponapp.domain.TUserCoupon;
import com.xdclass.couponapp.domain.TUserCouponExample;
import com.xdclass.couponapp.mapper.TCouponMapper;
import com.xdclass.couponapp.mapper.TUserCouponMapper;
import com.xdclass.couponapp.util.SnowflakeIdWorker;
import com.xdclass.couponserviceapi.dto.CouponDto;
import com.xdclass.couponserviceapi.dto.CouponNoticeDto;
import com.xdclass.couponserviceapi.dto.UserCouponDto;
import com.xdclass.couponserviceapi.dto.UserCouponInfoDto;
import com.xdclass.couponserviceapi.service.ICouponService;
import com.xdclass.userapi.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CouponService implements ICouponService {

    @Resource
    private TCouponMapper tCouponMapper;
    @Resource
    private TUserCouponMapper tUserCouponMapper;

    @Reference
    private IUserService iUserService;

    @Resource
    private RedisTemplate redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

    private static final String COUPON = "couponSet";

    private static final int COUPON_NUM = 5;


    LoadingCache<Integer,List<TCoupon>> couponCache = CacheBuilder.newBuilder()
            .expireAfterWrite(10,TimeUnit.MINUTES).refreshAfterWrite(5,TimeUnit.MINUTES)
            .build(new CacheLoader<Integer, List<TCoupon>>() {
                @Override
                public List<TCoupon> load(Integer o) throws Exception {
                    return loadCoupon(o);
                }
            });


    com.github.benmanes.caffeine.cache.LoadingCache<Integer,List<TCoupon>> couponCaffeine = Caffeine.newBuilder()
            .expireAfterWrite(10,TimeUnit.MINUTES).refreshAfterWrite(5,TimeUnit.MINUTES)
            .build(new com.github.benmanes.caffeine.cache.CacheLoader<Integer, List<TCoupon>>() {
                @Override
                public List<TCoupon> load(Integer o) throws Exception {
                    return loadCoupon(o);
                }
            });



    LoadingCache<Integer,TCoupon> couponIdsCache = CacheBuilder.newBuilder()
            .expireAfterWrite(10,TimeUnit.MINUTES).refreshAfterWrite(5,TimeUnit.MINUTES)
            .build(new CacheLoader<Integer,TCoupon>() {
                @Override
                public TCoupon load(Integer o) throws Exception {
                    return loadIdCoupon(o);
                }
            });


    private Map couponMap = new ConcurrentHashMap();


    public void updateCouponMap(){
        Map couponMap1 = new ConcurrentHashMap();
        List<TCoupon> tCoupons = Lists.newArrayList();
        try{
            tCoupons= this.loadCoupon(1);
            couponMap1.put(1,tCoupons);
            couponMap = couponMap1;
            //logger.info("update coupon list:{},coupon list size:{}",JSON.toJSONString(tCoupons),tCoupons.size());
        }catch (Exception e){
            logger.error("update coupon list:{},coupon list size:{}",JSON.toJSONString(tCoupons),tCoupons.size(),e);
        }
    }

    /***
     * 获取有效时间的可用优惠券列表
     * @return
     */
    public List<TCoupon> getCouponList4Map(){
        List<TCoupon> tCoupons =  (List<TCoupon>)couponMap.get(1);
        return tCoupons;
    }


    private TCoupon loadIdCoupon(Integer id) {
        return tCouponMapper.selectByPrimaryKey(id);
    }

    public List<TCoupon> getCouponListByIds(String ids){
        String[] idStr = ids.split(",");
        List<Integer> loadFromDB = Lists.newArrayList();
        List<TCoupon> tCoupons = Lists.newArrayList();
        List<String> idList = Lists.newArrayList(idStr);
        for (String id:idList) {
            TCoupon tCoupon =  couponIdsCache.getIfPresent(id);
            if (tCoupon==null){
                loadFromDB.add(Integer.parseInt(id));
            }else {
                tCoupons.add(tCoupon);
            }
        }
        List<TCoupon> tCoupons1 = couponByIds(loadFromDB);
        Map<Integer,TCoupon> tCouponMap = tCoupons1.stream().collect(Collectors.toMap(TCoupon::getId, TCoupon->TCoupon));
        tCoupons.addAll(tCoupons1);
        //将返回结果会写到缓存里面
        couponIdsCache.putAll(tCouponMap);
        return tCoupons;
    }


    private List<TCoupon> couponByIds(List<Integer> ids) {
        TCouponExample example = new TCouponExample();
        example.createCriteria().andIdIn(ids);
        return tCouponMapper.selectByExample(example);
    }


    public List<TCoupon> loadCoupon(Integer o) {
        TCouponExample example = new TCouponExample();
        example.createCriteria().andStatusEqualTo(Constant.USERFUL);
        return tCouponMapper.selectByExample(example);
    }

    /***
     * 获取有效时间的可用优惠券列表
     * // 1、是否存在远程调用 HTTP、RPC Metrics
     * // 2、大量内存处理  list.contain() ==>set.contain
     * @return
     */
    @Override
    public List<CouponDto> getCouponList(){
        List<TCoupon> tCoupons = Lists.newArrayList();
        List<CouponDto> dtos = Lists.newArrayList();
        try {
            tCoupons =  couponCache.get(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        tCoupons.forEach(tCoupon -> {
            CouponDto dto = new CouponDto();
            BeanUtils.copyProperties(tCoupon,dto);
            dtos.add(dto);
        });
        return dtos;
    }


    /***
     * 获取有效时间的可用优惠券列表
     * // 1、是否存在远程调用 HTTP、RPC Metrics
     * // 2、大量内存处理  list.contain() ==>set.contain
     * @return
     */
    public List<TCoupon> getCouponListCaffeine(){
        List<TCoupon> tCoupons = Lists.newArrayList();
        try {
            tCoupons =  couponCaffeine.get(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tCoupons;
    }


    public String query(){
        TCouponExample example = new TCouponExample();
        example.createCriteria().andCodeEqualTo("0057da3c-f2ad-42bd-b6d2-8bb58b6dbc90");
        List<TCoupon> tCoupon =  tCouponMapper.selectByExample(example);
        return tCoupon.get(0).toString();
    }


    public String getUserById(int id){
        return iUserService.getUserById(id).toString();
    }


    //userCoupon id,user_coupon_code,coupon_id,user_id,status,order_id,pic_url,create_time,update_time
    //互联网适用做冗余，优点：性能高  缺点：有维护风险、占一定空间
    @Override
    public String saveUserCoupon(UserCouponDto dto){
        String result = check(dto);
        if(result!=null){
            return result;
        }
        TCoupon coupon = tCouponMapper.selectByPrimaryKey(dto.getCouponId());
        if(coupon==null){
            return "coupon无效";
        }
        return save2DB(dto,coupon);
    }


    private String check(UserCouponDto dto){
        Integer couponId =  dto.getCouponId();
        Integer userId = dto.getUserId();
        if(couponId== null||userId == null){
            return "couponId或者userId为空";
        }
        return null;
    }


    private String save2DB(UserCouponDto dto,TCoupon coupon){
        TUserCoupon userCoupon = new TUserCoupon();
        BeanUtils.copyProperties(dto,userCoupon);
        userCoupon.setPicUrl(coupon.getPicUrl());
        userCoupon.setCreateTime(new Date());
        SnowflakeIdWorker worker = new SnowflakeIdWorker(0,0);
        userCoupon.setUserCouponCode(worker.nextId()+"");
        tUserCouponMapper.insertSelective(userCoupon);
        logger.info("save coupon success:{}",JSON.toJSONString(dto));
        return "领取成功";
    }



    @Override
    public List<UserCouponInfoDto> userCouponList(Integer userId) {
        List<UserCouponInfoDto> dtos = Lists.newArrayList();
        if(userId==null){
            return dtos;
        }
        List<TUserCoupon> userCoupon = getUserCoupon(userId);
        if(CollectionUtils.isEmpty(userCoupon)){
            return dtos;
        }
        Map<Integer,TCoupon> idCouponMap = getCouponMap(userCoupon);
        //封装coupon
        return wrapCoupon(userCoupon,idCouponMap);
    }


    private Map<Integer,TCoupon> getCouponMap(List<TUserCoupon> userCoupon){
        Set<Integer> couponIds = getCouponIds(userCoupon);
        List<TCoupon> coupons = getCouponListByIds(StringUtils.join(couponIds,","));
        Map<Integer,TCoupon> idCouponMap = couponList2Map(coupons);
        return idCouponMap;
    }

    private List<TUserCoupon> getUserCoupon(Integer userId){
        //查出用户未使用的券
        TUserCouponExample example = new TUserCouponExample();
        example.createCriteria().andUserIdEqualTo(userId).andStatusEqualTo(0);
        List<TUserCoupon> userCoupon = tUserCouponMapper.selectByExample(example);
        return userCoupon;
    }

    /**
     *获取couponIds
     */
    private Set<Integer> getCouponIds(List<TUserCoupon> userCoupons){
        Set<Integer> couponIds= userCoupons.stream().map(userCoupon -> userCoupon.getCouponId()).collect(Collectors.toSet());
        return couponIds;
    }

    private Map<Integer,TCoupon> couponList2Map(List<TCoupon> coupons){
        return coupons.stream().collect(Collectors.toMap(o -> o.getId(),o -> o));
    }


    //程序员就要拥抱变化
    private List<UserCouponInfoDto> wrapCoupon(List<TUserCoupon> userCoupons,Map<Integer,TCoupon> idCouponMap){
        List<UserCouponInfoDto> dtos = userCoupons.stream().map(userCoupon -> {
            UserCouponInfoDto dto = new UserCouponInfoDto();
            int couponId = userCoupon.getCouponId();
            TCoupon coupon = idCouponMap.get(couponId);
            BeanUtils.copyProperties(userCoupon,dto);
            dto.setAchieveAmount(coupon.getAchieveAmount());
            dto.setReduceAmount(coupon.getReduceAmount());
            return dto;
        }).collect(Collectors.toList());
        logger.info("invoke get user coupon list,result:{}",JSON.toJSONString(dtos));
        return dtos;
    }

    /**
     * 查询coupon公告栏couponId,前10条数据
     */
    public List<String> queryCouponList(){
        Set<String> couponSet = redisTemplate.opsForZSet().reverseRange(COUPON,0,-1);
        //获取set里面前N条数据
        return couponSet.stream().limit(COUPON_NUM).collect(Collectors.toList());
    }


    /**
     * 支付成功更新coupon核销为已经核销状态
     * @param orderId
     * @param userId
     */
    public void payReuslt(int orderId,int userId){
        TUserCouponExample example = new TUserCouponExample();
        example.createCriteria().andUserIdEqualTo(userId).andOrderIdEqualTo(orderId);
        List<TUserCoupon> tUserCoupons = tUserCouponMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(tUserCoupons)){
            logger.warn("can't no find userId:{},orderId:{}",userId,orderId);
            return;
        }
        TUserCoupon userCoupon = tUserCoupons.get(0);
        userCoupon.setStatus(1);
        tUserCouponMapper.updateByPrimaryKeySelective(userCoupon);
    }


    /**
     * 用户下单维护coupon和order之间的关系
     * @param orderId
     * @param couponCode
     * @param userId
     */
    public void saveOrder(int orderId,String couponCode,int userId){
        TUserCouponExample example = new TUserCouponExample();
        example.createCriteria().andUserCouponCodeEqualTo(couponCode).andOrderIdEqualTo(orderId);
        List<TUserCoupon> tUserCoupons = tUserCouponMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(tUserCoupons)){
            logger.warn("can't no find couponCode:{}",couponCode);
            return;
        }
        TUserCoupon userCoupon = tUserCoupons.get(0);
        userCoupon.setOrderId(orderId);
        userCoupon.setUserId(userId);
        //未核销状态
        userCoupon.setStatus(0);
        tUserCouponMapper.updateByPrimaryKeySelective(userCoupon);
    }

    /**
     * 接收coupon优惠券核销mq的时候被调用,以时间窗口展示前N条数据,userCouponStr代表userId_couponId
     */
    public void updateCoupon(String userCouponStr){
        redisTemplate.opsForZSet().add(COUPON,userCouponStr,System.currentTimeMillis());
        Set<String> couponSet = redisTemplate.opsForZSet().range(COUPON,0,-1);
        if(couponSet.size()>COUPON_NUM){
            String remUserCouponStr = couponSet.stream().findFirst().get();
            redisTemplate.opsForZSet().remove(COUPON,remUserCouponStr);
        }
    }



    /**
     * 查询coupon公告栏,前10条数据  userId_couponId  ==>  1_1==> string[]  string[0] =1,string[1] =1
     */
    @Override
    public List<CouponNoticeDto> queryCouponNotice(){
        Set<String> couponSet = redisTemplate.opsForZSet().reverseRange(COUPON,0,-1);
        //获取set里面前N条数据
        List<String> userCouponStrs = couponSet.stream().limit(COUPON_NUM).collect(Collectors.toList());
        Map<String,String> couponUserMap =userCouponStrs.stream().collect(Collectors.toMap(o -> o.split("_")[1],o -> o.split("_")[0]));
        List<String> couponIds = userCouponStrs.stream().map(s -> s.split("_")[1]).collect(Collectors.toList());
        //[1,2] ==>1,2   StringUtils.join
        String couponIdStrs = StringUtils.join(couponIds,",");
        //通过couponIdStrs批量获取coupon缓存数据
        List<TCoupon> tCoupons = getCouponListByIds(couponIdStrs);
        List<CouponNoticeDto> dtos = tCoupons.stream().map(tCoupon -> {
            CouponNoticeDto dto = new CouponNoticeDto();
            BeanUtils.copyProperties(tCoupon,dto);
            dto.setUserId(Integer.parseInt(couponUserMap.get(dto.getId()+"")));
            return dto;
        }).collect(Collectors.toList());
        return dtos;
    }


}
