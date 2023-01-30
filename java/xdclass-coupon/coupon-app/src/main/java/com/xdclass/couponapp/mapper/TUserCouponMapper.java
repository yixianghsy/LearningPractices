package com.xdclass.couponapp.mapper;

import com.xdclass.couponapp.domain.TUserCoupon;
import com.xdclass.couponapp.domain.TUserCouponExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface TUserCouponMapper {
    @SelectProvider(type=TUserCouponSqlProvider.class, method="countByExample")
    int countByExample(TUserCouponExample example);

    @DeleteProvider(type=TUserCouponSqlProvider.class, method="deleteByExample")
    int deleteByExample(TUserCouponExample example);

    @Delete({
        "delete from t_user_coupon",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into t_user_coupon (user_coupon_code, pic_url, ",
        "createTime, coupon_id, ",
        "user_id, status, ",
        "order_id, create_time)",
        "values (#{userCouponCode,jdbcType=VARCHAR}, #{picUrl,jdbcType=VARCHAR}, ",
        "#{createtime,jdbcType=TIMESTAMP}, #{couponId,jdbcType=INTEGER}, ",
        "#{userId,jdbcType=INTEGER}, #{status,jdbcType=INTEGER}, ",
        "#{orderId,jdbcType=INTEGER}, #{createTime,jdbcType=TIMESTAMP})"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insert(TUserCoupon record);

    @InsertProvider(type=TUserCouponSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insertSelective(TUserCoupon record);

    @SelectProvider(type=TUserCouponSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="user_coupon_code", property="userCouponCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="pic_url", property="picUrl", jdbcType=JdbcType.VARCHAR),
        @Result(column="createTime", property="createtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="coupon_id", property="couponId", jdbcType=JdbcType.INTEGER),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="order_id", property="orderId", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<TUserCoupon> selectByExample(TUserCouponExample example);

    @Select({
        "select",
        "id, user_coupon_code, pic_url, createTime, coupon_id, user_id, status, order_id, ",
        "create_time",
        "from t_user_coupon",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="user_coupon_code", property="userCouponCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="pic_url", property="picUrl", jdbcType=JdbcType.VARCHAR),
        @Result(column="createTime", property="createtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="coupon_id", property="couponId", jdbcType=JdbcType.INTEGER),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="order_id", property="orderId", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP)
    })
    TUserCoupon selectByPrimaryKey(Integer id);

    @UpdateProvider(type=TUserCouponSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") TUserCoupon record, @Param("example") TUserCouponExample example);

    @UpdateProvider(type=TUserCouponSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") TUserCoupon record, @Param("example") TUserCouponExample example);

    @UpdateProvider(type=TUserCouponSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(TUserCoupon record);

    @Update({
        "update t_user_coupon",
        "set user_coupon_code = #{userCouponCode,jdbcType=VARCHAR},",
          "pic_url = #{picUrl,jdbcType=VARCHAR},",
          "createTime = #{createtime,jdbcType=TIMESTAMP},",
          "coupon_id = #{couponId,jdbcType=INTEGER},",
          "user_id = #{userId,jdbcType=INTEGER},",
          "status = #{status,jdbcType=INTEGER},",
          "order_id = #{orderId,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(TUserCoupon record);
}