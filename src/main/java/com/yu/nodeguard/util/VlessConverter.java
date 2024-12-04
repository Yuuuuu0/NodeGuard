package com.yu.nodeguard.util;

import cn.hutool.core.util.StrUtil;
import com.yu.nodeguard.entity.VlessNode;
import com.yu.nodeguard.enums.YesOrNoEnum;
import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author geyu
 * @ClassName VlessConverter
 * @Description vless节点信息转换器
 * @date 2024/12/3 20:32
 * @Version 1.0
 */
@Slf4j
public class VlessConverter {
    /**
     * 节点转换
     * 入参格式参考
     * vless://802b755d-14df-44a0-9ec2-db6a68a4814e@8.219.109.153:40181?type=grpc&serviceName=phpa5.com&authority=&security=reality&pbk=XOX96Gq3e2LHs5FsXoNRHUdAXrwRQPsKGhxCwk9_RTA&fp=random&sni=phpa5.com&sid=e7326ea3&spx=%2F#linuxdo共享-新加坡1TB
     *
     * @param vlessUrl
     * @return
     */
    public static VlessNode convert(String vlessUrl) {
        try {
            if(StrUtil.isBlank(vlessUrl)){
                return null;
            }
            // 解析VLESS链接
            String[] parts = vlessUrl.split("vless://");
            if (parts.length < 2) {
                log.error("导入vless节点参数错误 {}", vlessUrl);
                return null;
            }
            // 解析明文部分
            String serverInfo = parts[1].split("\\?")[0];
            String[] userAndServer = serverInfo.split("@");
            String[] userAndPort = userAndServer[0].split(":");
            String[] hostAndPort = userAndServer[1].split(":");
            String query = parts[1].split("\\?")[1].split("#")[0];
            Map<String, String> params = parseQuery(query);

            VlessNode node = new VlessNode();
            node.setUuid(userAndPort[0]);
            node.setServer(hostAndPort[0]);
            node.setPort(Integer.parseInt(hostAndPort[1]));
            node.setType(params.getOrDefault("type", ""));
            node.setNetwork(node.getType());
            // 这里udp和tls默认为true 如果不支持
            node.setUdp(1);
            node.setTls(1);
            node.setServerName(params.get("sni"));
            node.setGrpcServiceName(params.get("serviceName"));
            node.setSecurity(params.get("security"));
            node.setPublicKey(params.get("pbk"));
            node.setShortId(params.get("sid"));
            node.setClientFingerprint(params.get("fp"));
            String name = URLDecoder.decode(parts[1].split("#")[1], StandardCharsets.UTF_8.name());
            // 如果name以"结尾 移除他
            if (name.endsWith("\"")) {
                name = name.substring(0, name.length() - 1);
            }
            node.setName(name);
            // 这里可以设置默认状态为启用
            node.setStatus(YesOrNoEnum.YES.getValue());
            return node;
        } catch (Exception e) {
            log.error("vless节点信息转换失败", e);
            return null;
        }
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>(16);
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length > 1) {
                params.put(pair[0], pair[1]);
            }
        }
        return params;
    }
}
