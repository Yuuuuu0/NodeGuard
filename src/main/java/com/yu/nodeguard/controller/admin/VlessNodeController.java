package com.yu.nodeguard.controller.admin;

import com.yu.nodeguard.common.R;
import com.yu.nodeguard.entity.VlessNode;
import com.yu.nodeguard.service.VlessNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/admin/vless")
public class VlessNodeController {

    @Autowired
    private VlessNodeService vlessNodeService;

    @GetMapping("/list")
    public R<List<VlessNode>> list() {
        log.info("接收到查询VLESS节点列表请求");
        return R.ok(vlessNodeService.list());
    }

    @GetMapping("/{id}")
    public R<VlessNode> getById(@PathVariable Long id) {
        log.info("接收到查询VLESS节点详情请求, id: {}", id);
        return R.ok(vlessNodeService.getById(id));
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody VlessNode node) {
        log.info("接收到保存VLESS节点请求: {}", node);
        return vlessNodeService.save(node) ? R.ok() : R.error("保存失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        log.info("接收到删除VLESS节点请求, id: {}", id);
        return vlessNodeService.deleteById(id) ? R.ok() : R.error("删除失败");
    }

    @PostMapping("/import")
    public R<Void> importNode(@RequestBody String vlessUrl) {
        try {
            String decodedUrl = URLDecoder.decode(vlessUrl, StandardCharsets.UTF_8.name());
            boolean success = vlessNodeService.importAppNodesVlessEntity(decodedUrl);
            return success ? R.ok() : R.error("导入失败");
        } catch (UnsupportedEncodingException e) {
            log.error("URL解码失败: {}", e.getMessage(), e);
            return R.error("导入失败：URL格式错误");
        }
    }

    @DeleteMapping("/batch")
    public R<Void> batchDelete(@RequestBody List<Long> ids) {
        log.info("接收到批量删除VLESS节点请求, ids: {}", ids);
        return vlessNodeService.deleteByIds(ids) ? R.ok() : R.error("删除失败");
    }

}
