package com.yu.nodeguard.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_rule_sets")
public class RuleSet extends BaseEntity {
    private Integer type;
    private String domain;
} 