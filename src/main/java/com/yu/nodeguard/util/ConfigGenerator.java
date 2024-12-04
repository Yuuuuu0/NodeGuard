package com.yu.nodeguard.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author geyu
 * @ClassName ConfigGenerator
 * @Description clash verge模版处理
 * @date 2024/12/3 19:27
 * @Version 1.0
 */
public class ConfigGenerator {
    public static String generateConfig(String domain, String token, String providerUrl, String directUrl, String rejectUrl, String customUrl) throws IOException, TemplateException {
        // 设置Freemarker配置
        Configuration cfg = new Configuration(new Version(2, 3, 30));
        cfg.setClassForTemplateLoading(ConfigGenerator.class, "/templates");
        cfg.setDefaultEncoding("UTF-8");

        // 加载模板
        Template template = cfg.getTemplate("clash_verge_template.ftl");

        // 准备数据模型
        Map<String, Object> root = new HashMap<>(16);
        root.put("domain", domain);
        root.put("provider_url", providerUrl);
        root.put("TOKEN", token);
        root.put("direct_url", directUrl);
        root.put("reject_url", rejectUrl);
        root.put("custom_url", customUrl);

        // 处理模板并输出结果
        StringWriter stringWriter = new StringWriter();
        template.process(root, stringWriter);

        return stringWriter.toString();
    }
}
