package com.yu.nodeguard.controller.admin;

import com.yu.nodeguard.common.R;
import com.yu.nodeguard.entity.SysDict;
import com.yu.nodeguard.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dict")
public class SysDictController {

    @Autowired
    private SysDictService dictService;

    @GetMapping("/list")
    public R<List<SysDict>> list() {
        return R.ok(dictService.findAll());
    }

    @GetMapping("/type/{dictType}")
    public R<List<SysDict>> getByType(@PathVariable String dictType) {
        return R.ok(dictService.findByType(dictType));
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody SysDict dict) {
        return dictService.save(dict) ? R.ok() : R.error("保存失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        return dictService.deleteById(id) ? R.ok() : R.error("删除失败");
    }
}
