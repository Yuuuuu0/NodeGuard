package com.yu.nodeguard.controller.admin;

import com.yu.nodeguard.common.R;
import com.yu.nodeguard.entity.SysUser;
import com.yu.nodeguard.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    public R<List<SysUser>> list() {
        return R.ok(userService.findAll());
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody SysUser user) {
        return userService.save(user) ? R.ok() : R.error("保存失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        return userService.deleteById(id) ? R.ok() : R.error("删除失败");
    }

    @PutMapping("/password")
    public R<?> updatePassword(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");

        // 获取当前登录用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // 验证旧密码
        SysUser user = userService.getByUsername(username);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return R.error("原密码不正确");
        }

        // 更新密码和token版本
        user.setPassword(newPassword);
        userService.updateById(user);
        return R.ok();
    }
}
