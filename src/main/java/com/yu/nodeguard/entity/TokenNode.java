package com.yu.nodeguard.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Token与节点关联实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_token_nodes")
public class TokenNode extends BaseEntity {
    /**
     * Token ID
     */
    private Long tokenId;
    
    /**
     * 节点 ID
     */
    private Long nodeId;
} 