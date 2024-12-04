package com.yu.nodeguard.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * Token实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_tokens")
public class Token extends BaseEntity {
    /**
     * Token值
     */
    private String token;
    
    /**
     * Token过期时间，NULL表示永不过期
     */
    private Date expirationDate;
} 