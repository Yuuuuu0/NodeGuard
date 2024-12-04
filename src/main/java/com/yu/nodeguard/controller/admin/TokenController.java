package com.yu.nodeguard.controller.admin;

import com.yu.nodeguard.common.R;
import com.yu.nodeguard.dto.TokenDTO;
import com.yu.nodeguard.service.TokenService;
import com.yu.nodeguard.vo.TokenVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/token")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/list")
    public R<List<TokenVO>> list() {
        log.info("接收到查询Token列表请求");
        return R.ok(tokenService.listWithNodes());
    }

    @GetMapping("/{id}")
    public R<TokenVO> getById(@PathVariable Long id) {
        log.info("接收到查询Token详情请求, id: {}", id);
        return R.ok(tokenService.getByIdWithNodes(id));
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody TokenDTO tokenDTO) {
        log.info("接收到保存Token请求: {}", tokenDTO);
        return tokenService.saveWithNodes(tokenDTO) ? R.ok() : R.error("保存失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        log.info("接收到删除Token请求, id: {}", id);
        return tokenService.deleteWithNodes(id) ? R.ok() : R.error("删除失败");
    }
}
