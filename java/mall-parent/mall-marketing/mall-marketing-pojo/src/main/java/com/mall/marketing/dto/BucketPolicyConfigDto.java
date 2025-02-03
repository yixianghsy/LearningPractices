package com.mall.marketing.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Minio Bucket访问策略配置
 * Created by macro on 2020/8/11.
 */
@Data
@EqualsAndHashCode
@Builder
public class BucketPolicyConfigDto implements Serializable {

    private String Version;
    private List<Statement> Statement;

    @Data
    @EqualsAndHashCode
    @Builder
    public static class Statement {
        private String Effect;
        private String Principal;
        private String Action;
        private String Resource;

    }
}
