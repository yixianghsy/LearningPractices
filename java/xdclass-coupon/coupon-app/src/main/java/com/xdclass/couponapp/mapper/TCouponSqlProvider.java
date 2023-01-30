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

import com.xdclass.couponapp.domain.TCoupon;
import com.xdclass.couponapp.domain.TCouponExample.Criteria;
import com.xdclass.couponapp.domain.TCouponExample.Criterion;
import com.xdclass.couponapp.domain.TCouponExample;
import java.util.List;
import java.util.Map;

public class TCouponSqlProvider {

    public String countByExample(TCouponExample example) {
        BEGIN();
        SELECT("count(*)");
        FROM("t_coupon");
        applyWhere(example, false);
        return SQL();
    }

    public String deleteByExample(TCouponExample example) {
        BEGIN();
        DELETE_FROM("t_coupon");
        applyWhere(example, false);
        return SQL();
    }

    public String insertSelective(TCoupon record) {
        BEGIN();
        INSERT_INTO("t_coupon");
        
        if (record.getCode() != null) {
            VALUES("code", "#{code,jdbcType=VARCHAR}");
        }
        
        if (record.getPicUrl() != null) {
            VALUES("pic_url", "#{picUrl,jdbcType=VARCHAR}");
        }
        
        if (record.getAchieveAmount() != null) {
            VALUES("achieve_amount", "#{achieveAmount,jdbcType=INTEGER}");
        }
        
        if (record.getReduceAmount() != null) {
            VALUES("reduce_amount", "#{reduceAmount,jdbcType=INTEGER}");
        }
        
        if (record.getStock() != null) {
            VALUES("stock", "#{stock,jdbcType=INTEGER}");
        }
        
        if (record.getTitle() != null) {
            VALUES("title", "#{title,jdbcType=VARCHAR}");
        }
        
        if (record.getStatus() != null) {
            VALUES("status", "#{status,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getStartTime() != null) {
            VALUES("start_time", "#{startTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getEndTime() != null) {
            VALUES("end_time", "#{endTime,jdbcType=TIMESTAMP}");
        }
        
        return SQL();
    }

    public String selectByExample(TCouponExample example) {
        BEGIN();
        if (example != null && example.isDistinct()) {
            SELECT_DISTINCT("id");
        } else {
            SELECT("id");
        }
        SELECT("code");
        SELECT("pic_url");
        SELECT("achieve_amount");
        SELECT("reduce_amount");
        SELECT("stock");
        SELECT("title");
        SELECT("status");
        SELECT("create_time");
        SELECT("start_time");
        SELECT("end_time");
        FROM("t_coupon");
        applyWhere(example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            ORDER_BY(example.getOrderByClause());
        }
        
        return SQL();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        TCoupon record = (TCoupon) parameter.get("record");
        TCouponExample example = (TCouponExample) parameter.get("example");
        
        BEGIN();
        UPDATE("t_coupon");
        
        if (record.getId() != null) {
            SET("id = #{record.id,jdbcType=INTEGER}");
        }
        
        if (record.getCode() != null) {
            SET("code = #{record.code,jdbcType=VARCHAR}");
        }
        
        if (record.getPicUrl() != null) {
            SET("pic_url = #{record.picUrl,jdbcType=VARCHAR}");
        }
        
        if (record.getAchieveAmount() != null) {
            SET("achieve_amount = #{record.achieveAmount,jdbcType=INTEGER}");
        }
        
        if (record.getReduceAmount() != null) {
            SET("reduce_amount = #{record.reduceAmount,jdbcType=INTEGER}");
        }
        
        if (record.getStock() != null) {
            SET("stock = #{record.stock,jdbcType=INTEGER}");
        }
        
        if (record.getTitle() != null) {
            SET("title = #{record.title,jdbcType=VARCHAR}");
        }
        
        if (record.getStatus() != null) {
            SET("status = #{record.status,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            SET("create_time = #{record.createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getStartTime() != null) {
            SET("start_time = #{record.startTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getEndTime() != null) {
            SET("end_time = #{record.endTime,jdbcType=TIMESTAMP}");
        }
        
        applyWhere(example, true);
        return SQL();
    }

    public String updateByExample(Map<String, Object> parameter) {
        BEGIN();
        UPDATE("t_coupon");
        
        SET("id = #{record.id,jdbcType=INTEGER}");
        SET("code = #{record.code,jdbcType=VARCHAR}");
        SET("pic_url = #{record.picUrl,jdbcType=VARCHAR}");
        SET("achieve_amount = #{record.achieveAmount,jdbcType=INTEGER}");
        SET("reduce_amount = #{record.reduceAmount,jdbcType=INTEGER}");
        SET("stock = #{record.stock,jdbcType=INTEGER}");
        SET("title = #{record.title,jdbcType=VARCHAR}");
        SET("status = #{record.status,jdbcType=INTEGER}");
        SET("create_time = #{record.createTime,jdbcType=TIMESTAMP}");
        SET("start_time = #{record.startTime,jdbcType=TIMESTAMP}");
        SET("end_time = #{record.endTime,jdbcType=TIMESTAMP}");
        
        TCouponExample example = (TCouponExample) parameter.get("example");
        applyWhere(example, true);
        return SQL();
    }

    public String updateByPrimaryKeySelective(TCoupon record) {
        BEGIN();
        UPDATE("t_coupon");
        
        if (record.getCode() != null) {
            SET("code = #{code,jdbcType=VARCHAR}");
        }
        
        if (record.getPicUrl() != null) {
            SET("pic_url = #{picUrl,jdbcType=VARCHAR}");
        }
        
        if (record.getAchieveAmount() != null) {
            SET("achieve_amount = #{achieveAmount,jdbcType=INTEGER}");
        }
        
        if (record.getReduceAmount() != null) {
            SET("reduce_amount = #{reduceAmount,jdbcType=INTEGER}");
        }
        
        if (record.getStock() != null) {
            SET("stock = #{stock,jdbcType=INTEGER}");
        }
        
        if (record.getTitle() != null) {
            SET("title = #{title,jdbcType=VARCHAR}");
        }
        
        if (record.getStatus() != null) {
            SET("status = #{status,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getStartTime() != null) {
            SET("start_time = #{startTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getEndTime() != null) {
            SET("end_time = #{endTime,jdbcType=TIMESTAMP}");
        }
        
        WHERE("id = #{id,jdbcType=INTEGER}");
        
        return SQL();
    }

    protected void applyWhere(TCouponExample example, boolean includeExamplePhrase) {
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