package com.yu.nodeguard.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统字典实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict")
public class SysDict extends BaseEntity {
    /**
     * 字典类型
     */
    private String dictType;
    
    /**
     * 字典标签
     */
    private String dictLabel;
    
    /**
     * 字典值
     */
    private String dictValue;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 备注
     */
    private String remark;
} 