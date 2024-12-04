package com.yu.nodeguard.enums;

/**
 * 系统内代表是与否的枚举
 *
 * @author valarchie
 */
public enum YesOrNoEnum implements BasicEnum<Integer> {
    /**
     * 是与否
     */
    YES(1, "是"),
    NO(0, "否");

    private final int value;
    private final String description;

    YesOrNoEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String description() {
        return description;
    }

}
