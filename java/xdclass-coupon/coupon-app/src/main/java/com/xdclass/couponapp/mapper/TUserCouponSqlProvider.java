package com.xdclass.couponapp.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.DELETE_FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.ORDER_BY;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT_DISTINCT;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.xdclass.couponapp.domain.TUserCoupon;
import com.xdclass.couponapp.domain.TUserCouponExample.Criteria;
import com.xdclass.couponapp.domain.TUserCouponExample.Criterion;
import com.xdclass.couponapp.domain.TUserCouponExample;
import java.util.List;
import java.util.Map;

public class TUserCouponSqlProvider {

    public String countByExample(TUserCouponExample example) {
        BEGIN();
        SELECT("count(*)");
        FROM("t_user_coupon");
        applyWhere(example, false);
        return SQL();
    }

    public String deleteByExample(TUserCouponExample example) {
        BEGIN();
        DELETE_FROM("t_user_coupon");
        applyWhere(example, false);
        return SQL();
    }

    public String insertSelective(TUserCoupon record) {
        BEGIN();
        INSERT_INTO("t_user_coupon");
        
        if (record.getUserCouponCode() != null) {
            VALUES("user_coupon_code", "#{userCouponCode,jdbcType=VARCHAR}");
        }
        
        if (record.getPicUrl() != null) {
            VALUES("pic_url", "#{picUrl,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatetime() != null) {
            VALUES("createTime", "#{createtime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCouponId() != null) {
            VALUES("coupon_id", "#{couponId,jdbcType=INTEGER}");
        }
        
        if (record.getUserId() != null) {
            VALUES("user_id", "#{userId,jdbcType=INTEGER}");
        }
        
        if (record.getStatus() != null) {
            VALUES("status", "#{status,jdbcType=INTEGER}");
        }
        
        if (record.getOrderId() != null) {
            VALUES("order_id", "#{orderId,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        return SQL();
    }

    public String selectByExample(TUserCouponExample example) {
        BEGIN();
        if (example != null && example.isDistinct()) {
            SELECT_DISTINCT("id");
        } else {
            SELECT("id");
        }
        SELECT("user_coupon_code");
        SELECT("pic_url");
        SELECT("createTime");
        SELECT("coupon_id");
        SELECT("user_id");
        SELECT("status");
        SELECT("order_id");
        SELECT("create_time");
        FROM("t_user_coupon");
        applyWhere(example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            ORDER_BY(example.getOrderByClause());
        }
        
        return SQL();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        TUserCoupon record = (TUserCoupon) parameter.get("record");
        TUserCouponExample example = (TUserCouponExample) parameter.get("example");
        
        BEGIN();
        UPDATE("t_user_coupon");
        
        if (record.getId() != null) {
            SET("id = #{record.id,jdbcType=INTEGER}");
        }
        
        if (record.getUserCouponCode() != null) {
            SET("user_coupon_code = #{record.userCouponCode,jdbcType=VARCHAR}");
        }
        
        if (record.getPicUrl() != null) {
            SET("pic_url = #{record.picUrl,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatetime() != null) {
            SET("createTime = #{record.createtime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCouponId() != null) {
            SET("coupon_id = #{record.couponId,jdbcType=INTEGER}");
        }
        
        if (record.getUserId() != null) {
            SET("user_id = #{record.userId,jdbcType=INTEGER}");
        }
        
        if (record.getStatus() != null) {
            SET("status = #{record.status,jdbcType=INTEGER}");
        }
        
        if (record.getOrderId() != null) {
            SET("order_id = #{record.orderId,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            SET("create_time = #{record.createTime,jdbcType=TIMESTAMP}");
        }
        
        applyWhere(example, true);
        return SQL();
    }

    public String updateByExample(Map<String, Object> parameter) {
        BEGIN();
        UPDATE("t_user_coupon");
        
        SET("id = #{record.id,jdbcType=INTEGER}");
        SET("user_coupon_code = #{record.userCouponCode,jdbcType=VARCHAR}");
        SET("pic_url = #{record.picUrl,jdbcType=VARCHAR}");
        SET("createTime = #{record.createtime,jdbcType=TIMESTAMP}");
        SET("coupon_id = #{record.couponId,jdbcType=INTEGER}");
        SET("user_id = #{record.userId,jdbcType=INTEGER}");
        SET("status = #{record.status,jdbcType=INTEGER}");
        SET("order_id = #{record.orderId,jdbcType=INTEGER}");
        SET("create_time = #{record.createTime,jdbcType=TIMESTAMP}");
        
        TUserCouponExample example = (TUserCouponExample) parameter.get("example");
        applyWhere(example, true);
        return SQL();
    }

    public String updateByPrimaryKeySelective(TUserCoupon record) {
        BEGIN();
        UPDATE("t_user_coupon");
        
        if (record.getUserCouponCode() != null) {
            SET("user_coupon_code = #{userCouponCode,jdbcType=VARCHAR}");
        }
        
        if (record.getPicUrl() != null) {
            SET("pic_url = #{picUrl,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatetime() != null) {
            SET("createTime = #{createtime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCouponId() != null) {
            SET("coupon_id = #{couponId,jdbcType=INTEGER}");
        }
        
        if (record.getUserId() != null) {
            SET("user_id = #{userId,jdbcType=INTEGER}");
        }
        
        if (record.getStatus() != null) {
            SET("status = #{status,jdbcType=INTEGER}");
        }
        
        if (record.getOrderId() != null) {
            SET("order_id = #{orderId,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        WHERE("id = #{id,jdbcType=INTEGER}");
        
        return SQL();
    }

    protected void applyWhere(TUserCouponExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            WHERE(sb.toString());
        }
    }
}