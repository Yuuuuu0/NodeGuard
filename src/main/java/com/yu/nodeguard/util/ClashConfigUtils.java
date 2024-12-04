package com.yu.nodeguard.util;

import cn.hutool.core.collection.CollUtil;
import com.yu.nodeguard.entity.VlessNode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Clash配置生成工具类
 */
public class ClashConfigUtils {

    /**
     * 生成Clash Meta节点配置
     *
     * @param nodes VLESS节点列表
     * @return 配置字符串
     */
    public static String generateMetaConfig(List<VlessNode> nodes) {
        if (CollUtil.isEmpty(nodes)) {
            return "";
        }

        StringBuilder config = new StringBuilder();
        config.append("proxies:\n");
        
        for (VlessNode node : nodes) {
            appendNodeConfig(config, node);
        }

        return config.toString();
    }

    /**
     * 添加单个节点配置
     */
    private static void appendNodeConfig(StringBuilder config, VlessNode node) {
        config.append("  - name: \"").append(node.getName()).append("\"\n")
              .append("    server: ").append(node.getServer()).append("\n")
              .append("    type: vless\n")
              .append("    port: ").append(node.getPort()).append("\n")
              .append("    uuid: ").append(node.getUuid()).append("\n");

        if (StringUtils.isNotBlank(node.getServerName())) {
            config.append("    servername: ").append(node.getServerName()).append("\n");
        }
        
        if (StringUtils.isNotBlank(node.getNetwork())) {
            config.append("    network: ").append(node.getNetwork()).append("\n");
        }

        config.append("    udp: ").append(node.getUdp() == 1).append("\n")
              .append("    tls: ").append(node.getTls() == 1).append("\n");

        appendRealityConfig(config, node);
        appendClientFingerprint(config, node);
        appendGrpcConfig(config, node);
    }

    /**
     * 添加Reality配置
     */
    private static void appendRealityConfig(StringBuilder config, VlessNode node) {
        if ("reality".equalsIgnoreCase(node.getSecurity())) {
            config.append("    reality-opts:\n")
                  .append("      public-key: ").append(node.getPublicKey()).append("\n")
                  .append("      short-id: ").append(node.getShortId()).append("\n");
        }
    }

    /**
     * 添加客户端指纹配置
     */
    private static void appendClientFingerprint(StringBuilder config, VlessNode node) {
        if (StringUtils.isNotBlank(node.getClientFingerprint())) {
            config.append("    client-fingerprint: ").append(node.getClientFingerprint()).append("\n");
        }
    }

    /**
     * 添加gRPC配置
     */
    private static void appendGrpcConfig(StringBuilder config, VlessNode node) {
        if ("grpc".equalsIgnoreCase(node.getNetwork()) && StringUtils.isNotBlank(node.getGrpcServiceName())) {
            config.append("    grpc-opts:\n")
                  .append("      grpc-service-name: ").append(node.getGrpcServiceName()).append("\n");
        }
    }
} 