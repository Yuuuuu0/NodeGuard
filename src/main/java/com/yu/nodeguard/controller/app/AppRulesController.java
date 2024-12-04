package com.yu.nodeguard.controller.app;

import cn.hutool.core.collection.CollUtil;
import com.yu.nodeguard.entity.RuleSet;
import com.yu.nodeguard.enums.RuleTypeEnum;
import com.yu.nodeguard.service.RuleSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 前台配置接口
 */
@Slf4j
@RestController
@RequestMapping("/app/rules")
public class AppRulesController {

    @Autowired
    private RuleSetService ruleSetService;

    /**
     * 获取规则配置
     * @param type 规则类型
     * @return 格式化后的规则内容
     */
    @GetMapping("/{type}")
    public String getRules(@PathVariable Integer type) {
        log.info("接收到获取规则配置请求, type: {}", type);

        // 获取规则类型枚举
        RuleTypeEnum typeEnum = RuleTypeEnum.getByValue(type);
        if (typeEnum == null) {
            log.error("无效的规则类型: {}", type);
            return "";
        }

        // 查询规则列表
        List<RuleSet> ruleSets = ruleSetService.listByType(type);

        // 转换规则格式
        String result = convertRules(typeEnum, ruleSets);
        log.info("规则转换完成, type: {}, size: {}", type, ruleSets.size());
        return result;
    }

    /**
     * 转换规则格式
     */
    private String convertRules(RuleTypeEnum typeEnum, List<RuleSet> ruleSets) {
        if(CollUtil.isEmpty(ruleSets) || Objects.isNull(typeEnum)){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("payload:").append("\n");
        switch (typeEnum){
            case CUSTOM:
            case REJECT:
                ruleSets.forEach(rule -> sb.append("  DOMAIN-SUFFIX,").append(rule.getDomain()).append("\n"));
                break;
            case DIRECT:
                ruleSets.forEach(rule -> sb.append("  - '").append(rule.getDomain()).append("'\n"));
                break;
            default:
                break;
        }
        return sb.toString();
    }
}
