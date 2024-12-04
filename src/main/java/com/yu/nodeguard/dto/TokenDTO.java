package com.yu.nodeguard.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * Token数据传输对象
 */
@Data
public class TokenDTO {
    /**
     * Token ID，新增时为null
     */
    private Long id;
    
    /**
     * Token值
     */
    private String token;
    
    /**
     * Token过期时间，NULL表示永不过期
     */
    private Date expirationDate;
    
    /**
     * 关联的节点ID列表
     */
    private List<Long> nodeIds;
} 