package com.xdclass.userapp.mapper;

import com.xdclass.userapp.domain.TCoupon;
import com.xdclass.userapp.domain.TCouponExample;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;


public interface TCouponMapper {
    @SelectProvider(type=TCouponSqlProvider.class, method="countByExample")
    int countByExample(TCouponExample example);

    @DeleteProvider(type=TCouponSqlProvider.class, method="deleteByExample")
    int deleteByExample(TCouponExample example);

    @Delete({
        "delete from t_coupon",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into t_coupon (code, pic_url, ",
        "reduce_amount, achieve_amount, ",
        "stock, title, createTime, ",
        "status)",
        "values (#{code,jdbcType=VARCHAR}, #{picUrl,jdbcType=VARCHAR}, ",
        "#{reduceAmount,jdbcType=INTEGER}, #{achieveAmount,jdbcType=INTEGER}, ",
        "#{stock,jdbcType=BIGINT}, #{title,jdbcType=VARCHAR}, #{createtime,jdbcType=TIMESTAMP}, ",
        "#{status,jdbcType=INTEGER})"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insert(TCoupon record);

    @InsertProvider(type=TCouponSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insertSelective(TCoupon record);

    @SelectProvider(type=TCouponSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="pic_url", property="picUrl", jdbcType=JdbcType.VARCHAR),
        @Result(column="reduce_amount", property="reduceAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="achieve_amount", property="achieveAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="stock", property="stock", jdbcType=JdbcType.BIGINT),
        @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
        @Result(column="createTime", property="createtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    List<TCoupon> selectByExample(TCouponExample example);

    @Select({
        "select",
        "id, code, pic_url, reduce_amount, achieve_amount, stock, title, createTime, ",
        "status",
        "from t_coupon",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="pic_url", property="picUrl", jdbcType=JdbcType.VARCHAR),
        @Result(column="reduce_amount", property="reduceAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="achieve_amount", property="achieveAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="stock", property="stock", jdbcType=JdbcType.BIGINT),
        @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
        @Result(column="createTime", property="createtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    TCoupon selectByPrimaryKey(Integer id);

    @UpdateProvider(type=TCouponSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") TCoupon record, @Param("example") TCouponExample example);

    @UpdateProvider(type=TCouponSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") TCoupon record, @Param("example") TCouponExample example);

    @UpdateProvider(type=TCouponSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(TCoupon record);

    @Update({
        "update t_coupon",
        "set code = #{code,jdbcType=VARCHAR},",
          "pic_url = #{picUrl,jdbcType=VARCHAR},",
          "reduce_amount = #{reduceAmount,jdbcType=INTEGER},",
          "achieve_amount = #{achieveAmount,jdbcType=INTEGER},",
          "stock = #{stock,jdbcType=BIGINT},",
          "title = #{title,jdbcType=VARCHAR},",
          "createTime = #{createtime,jdbcType=TIMESTAMP},",
          "status = #{status,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(TCoupon record);
}