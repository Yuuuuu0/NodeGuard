package com.yu.nodeguard.vo;

import com.yu.nodeguard.entity.Token;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Token视图对象，用于前端展示
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TokenVO extends Token {
    /**
     * 关联的节点ID列表
     */
    private List<Long> nodeIds;
} 