package com.xdclass.couponapp.mapper;

import com.xdclass.couponapp.domain.TCoupon;
import com.xdclass.couponapp.domain.TCouponExample;
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
        "achieve_amount, reduce_amount, ",
        "stock, title, status, ",
        "create_time, start_time, ",
        "end_time)",
        "values (#{code,jdbcType=VARCHAR}, #{picUrl,jdbcType=VARCHAR}, ",
        "#{achieveAmount,jdbcType=INTEGER}, #{reduceAmount,jdbcType=INTEGER}, ",
        "#{stock,jdbcType=INTEGER}, #{title,jdbcType=VARCHAR}, #{status,jdbcType=INTEGER}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{startTime,jdbcType=TIMESTAMP}, ",
        "#{endTime,jdbcType=TIMESTAMP})"
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
        @Result(column="achieve_amount", property="achieveAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="reduce_amount", property="reduceAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="stock", property="stock", jdbcType=JdbcType.INTEGER),
        @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="start_time", property="startTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="end_time", property="endTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<TCoupon> selectByExample(TCouponExample example);

    @Select({
        "select",
        "id, code, pic_url, achieve_amount, reduce_amount, stock, title, status, create_time, ",
        "start_time, end_time",
        "from t_coupon",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="pic_url", property="picUrl", jdbcType=JdbcType.VARCHAR),
        @Result(column="achieve_amount", property="achieveAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="reduce_amount", property="reduceAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="stock", property="stock", jdbcType=JdbcType.INTEGER),
        @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="start_time", property="startTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="end_time", property="endTime", jdbcType=JdbcType.TIMESTAMP)
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
          "achieve_amount = #{achieveAmount,jdbcType=INTEGER},",
          "reduce_amount = #{reduceAmount,jdbcType=INTEGER},",
          "stock = #{stock,jdbcType=INTEGER},",
          "title = #{title,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "start_time = #{startTime,jdbcType=TIMESTAMP},",
          "end_time = #{endTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(TCoupon record);
}