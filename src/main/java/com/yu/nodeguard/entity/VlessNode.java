package com.yu.nodeguard.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * VLESS节点实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_nodes_vless")
public class VlessNode extends BaseEntity {
    /**
     * VLESS链接的UUID
     */
    private String uuid;
    
    /**
     * 服务器地址
     */
    private String server;
    
    /**
     * 端口号
     */
    private Integer port;
    
    /**
     * 传输类型
     */
    private String type;
    
    /**
     * 网络类型（如TCP, HTTP, gRPC等）
     */
    private String network;
    
    /**
     * 是否支持UDP（0：不支持 1：支持）
     */
    private Integer udp;
    
    /**
     * 是否使用TLS（0：不使用 1：使用）
     */
    private Integer tls;
    
    /**
     * SNI
     */
    private String serverName;
    
    /**
     * gRPC服务名称
     */
    private String grpcServiceName;
    
    /**
     * 加密方式，如reality
     */
    private String security;
    
    /**
     * 公钥，用于REALITY加密
     */
    private String publicKey;
    
    /**
     * 短ID，用于REALITY加密
     */
    private String shortId;
    
    /**
     * 客户端指纹策略
     */
    private String clientFingerprint;
    
    /**
     * 节点名称
     */
    private String name;
} 