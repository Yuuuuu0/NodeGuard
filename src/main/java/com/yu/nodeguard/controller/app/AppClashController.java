package com.yu.nodeguard.controller.app;

import cn.hutool.core.collection.CollUtil;
import com.yu.nodeguard.entity.SysDict;
import com.yu.nodeguard.entity.Token;
import com.yu.nodeguard.entity.TokenNode;
import com.yu.nodeguard.entity.VlessNode;
import com.yu.nodeguard.service.SysDictService;
import com.yu.nodeguard.service.TokenService;
import com.yu.nodeguard.service.VlessNodeService;
import com.yu.nodeguard.util.ConfigGenerator;
import com.yu.nodeguard.util.ClashConfigUtils;
import com.yu.nodeguard.vo.TokenVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Clash生成接口
 */
@Slf4j
@RestController
@RequestMapping("/app/clash")
public class AppClashController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysDictService dictService;

    @Autowired
    private VlessNodeService vlessNodeService;

    /**
     * 获取Clash配置
     *
     * @param token 访问令牌
     * @return Clash配置内容
     */
    @GetMapping("/clashMetaProfiles/{token}")
    public String getClashConfig(@PathVariable String token) {
        log.info("接收到获取Clash配置请求, token: {}", token);

        // 验证token
        if (StringUtils.isBlank(token)) {
            log.error("token不能为空");
            return "token is required";
        }

        Token tokenEntity = tokenService.getByToken(token);
        if (tokenEntity == null) {
            log.error("token无效: {}", token);
            return "invalid token";
        }

        // 从数据字典获取配置参数
        String domain = getDictValue("domain");
        String providerUrl = getDictValue("provider_url");
        String directUrl = getDictValue("direct_url");
        String rejectUrl = getDictValue("reject_url");
        String customUrl = getDictValue("custom_url");

        // 生成配置
        try {
            String config = ConfigGenerator.generateConfig(domain, token, providerUrl, directUrl, rejectUrl, customUrl);
            log.info("生成Clash配置成功");
            return config;
        } catch (Exception e) {
            log.error("生成Clash配置失败", e);
            return "generate config failed: " + e.getMessage();
        }
    }

    /**
     * 从数据字典获取配置值
     */
    private String getDictValue(String label) {
        return dictService.findByType("clash_config").stream().filter(dict -> label.equals(dict.getDictLabel()))
            .map(SysDict::getDictValue).findFirst().orElse("");
    }

    @GetMapping("/clashMeta/{token}")
    public String getClashMetaConfig(@PathVariable String token) {
        log.info("接收到获取ClashMeta配置请求, token: {}", token);

        // 验证token
        Token tokenEntity = tokenService.getByToken(token);
        if (tokenEntity == null) {
            log.error("token无效: {}", token);
            return "invalid token";
        }

        // 获取token关联的节点
        List<TokenNode> tokenNodes = tokenService.getTokenNodes(tokenEntity.getId());
        if (CollUtil.isEmpty(tokenNodes)) {
            log.warn("token未关联任何节点: {}", token);
            return "no nodes available";
        }

        // 获取节点信息
        List<Long> nodeIds = tokenNodes.stream().map(TokenNode::getNodeId).collect(Collectors.toList());
        List<VlessNode> nodes = vlessNodeService.listByIds(nodeIds);
        if (CollUtil.isEmpty(nodes)) {
            log.warn("未找到有效节点: {}", token);
            return "no valid nodes";
        }

        // 生成配置
        String config = ClashConfigUtils.generateMetaConfig(nodes);
        log.info("生成ClashMeta配置成功, 节点数量: {}", nodes.size());
        return config;
    }
}
