package com.yu.nodeguard.controller.admin;

import com.yu.nodeguard.common.R;
import com.yu.nodeguard.entity.RuleSet;
import com.yu.nodeguard.service.RuleSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin/rule")
public class RuleSetController {

    @Autowired
    private RuleSetService ruleSetService;

    @GetMapping("/list")
    public R<List<RuleSet>> list() {
        log.info("接收到查询规则列表请求");
        return R.ok(ruleSetService.list());
    }

    @GetMapping("/type/{type}")
    public R<List<RuleSet>> listByType(@PathVariable Integer type) {
        log.info("接收到查询指定类型规则列表请求, type: {}", type);
        return R.ok(ruleSetService.listByType(type));
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody RuleSet ruleSet) {
        log.info("接收到保存规则请求: {}", ruleSet);
        return ruleSetService.save(ruleSet) ? R.ok() : R.error("保存失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        log.info("接收到删除规则请求, id: {}", id);
        return ruleSetService.deleteById(id) ? R.ok() : R.error("删除失败");
    }
}
