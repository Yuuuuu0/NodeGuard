package com.yu.nodeguard.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yu.nodeguard.entity.SysUser;
import com.yu.nodeguard.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SysUserService implements UserDetailsService {

    @Autowired
    private SysUserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public SysUserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<SysUser> findAll() {
        return userMapper.selectList(null);
    }

    public SysUser getById(Long id) {
        return userMapper.selectById(id);
    }

    public boolean save(SysUser user) {
        if (user.getId() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setStatus(1);
            return userMapper.insert(user) > 0;
        } else {
            if (StringUtils.isNotBlank(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                user.setPassword(null);
            }
            return userMapper.updateById(user) > 0;
        }
    }

    public boolean deleteById(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    public SysUser getByUsername(String username) {
        return userMapper.selectOne(
            new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("加载用户信息: username={}", username);

        // 查询用户
        SysUser user = getByUsername(username);
        log.info("查询用户结果: user={}", user);

        if (user == null) {
            log.error("用户不存在: username={}", username);
            throw new UsernameNotFoundException("用户不存在");
        }

        if (user.getStatus() != 1) {
            log.error("用户已禁用: username={}", username);
            throw new UsernameNotFoundException("用户已禁用");
        }

        // 打印密码信息用于调试
        log.debug("数据库中的密码: {}", user.getPassword());

        return User.builder()
            .username(user.getUsername())
            // 这里直接使用数据库中的加密密码
            .password(user.getPassword())
            .authorities("ROLE_USER")
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }

    public boolean updateById(SysUser user) {
        // 如果密码不为空，说明要修改密码，需要加密
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // 增加版本号使当前token失效
            user.setTokenVersion(user.getTokenVersion() + 1);
        }
        return userMapper.updateById(user) > 0;
    }
}
