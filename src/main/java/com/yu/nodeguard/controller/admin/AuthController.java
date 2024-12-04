package com.yu.nodeguard.controller.admin;

import com.yu.nodeguard.common.R;
import com.yu.nodeguard.entity.SysUser;
import com.yu.nodeguard.service.SysUserService;
import com.yu.nodeguard.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SysUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody SysUser user) {
        log.info("用户登录请求: username={}, password={}", user.getUsername(), user.getPassword().replaceAll(".", "*"));
        try {
            // 先查询用户信息
            SysUser dbUser = userService.getByUsername(user.getUsername());
            if (dbUser == null) {
                log.error("用户不存在: {}", user.getUsername());
                return R.error("用户不存在");
            }

            // 检查用户状态
            if (dbUser.getStatus() != 1) {
                log.error("用户已被禁用: username={}", user.getUsername());
                return R.error("账号已被禁用");
            }

            log.info("数据库中的用户信息: id={}, username={}, password={}, tokenVersion={}, status={}", 
                dbUser.getId(), dbUser.getUsername(), dbUser.getPassword(), dbUser.getTokenVersion(), dbUser.getStatus());
            log.info("前端传入的密码: {}", user.getPassword());
            
            // 测试密码匹配
            boolean matches = passwordEncoder.matches(user.getPassword(), dbUser.getPassword());
            log.info("密码匹配结果: {}", matches);
            
            if (!matches) {
                log.error("密码错误: username={}", user.getUsername());
                return R.error("密码错误");
            }

            // 进行身份验证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            log.info("用户认证成功: username={}", user.getUsername());
            
            // 生成token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = JwtUtils.generateToken(userDetails, dbUser.getTokenVersion());
            log.info("生成的token: {}", token);

            // 返回token和用户信息
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("username", userDetails.getUsername());
            result.put("id", dbUser.getId());
            result.put("email", dbUser.getEmail());
            result.put("mobile", dbUser.getMobile());

            return R.ok(result);
        } catch (BadCredentialsException e) {
            log.error("认证失败: username={}, error={}", user.getUsername(), e.getMessage(), e);
            return R.error("用户名或密码错误");
        } catch (Exception e) {
            log.error("登录失败: username={}, error={}", user.getUsername(), e.getMessage(), e);
            return R.error("登录失败: " + e.getMessage());
        }
    }
}
