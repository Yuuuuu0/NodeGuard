package com.yu.nodeguard.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则类型枚举
 */
@Getter
@AllArgsConstructor
public enum RuleTypeEnum {
    CUSTOM(1, "自定义"),
    REJECT(2, "拒绝"),
    DIRECT(3, "直连");

    private final Integer value;
    private final String desc;

    public static RuleTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (RuleTypeEnum type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
} 