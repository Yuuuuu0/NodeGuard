package com.yu.nodeguard.config;

import com.yu.nodeguard.entity.SysUser;
import com.yu.nodeguard.service.SysUserService;
import com.yu.nodeguard.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.debug("处理请求: {}", requestURI);

        String authHeader = request.getHeader("Authorization");
        log.debug("Authorization请求头: {}", authHeader);

        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.debug("提取到的token: {}", token);

            try {
                String username = JwtUtils.getUsername(token);
                log.debug("从token中提取的用户名: {}", username);

                if (StringUtils.isNotBlank(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    log.debug("加载的用户信息: {}", userDetails);

                    // 获取当前用户的token版本
                    SysUser user = ((SysUserService) userDetailsService).getByUsername(username);

                    if (JwtUtils.validateToken(token, userDetails, user.getTokenVersion())) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.debug("已将认证信息设置到SecurityContext中");
                    } else {
                        log.warn("token验证失败");
                    }
                }
            } catch (Exception e) {
                log.error("token处理异常", e);
            }
        } else {
            log.debug("请求中未找到Bearer token");
        }

        chain.doFilter(request, response);
    }
}
