package com.yu.nodeguard.service;

import com.yu.nodeguard.entity.VlessNode;
import com.yu.nodeguard.mapper.VlessNodeMapper;
import com.yu.nodeguard.util.VlessConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class VlessNodeService {

    @Autowired
    private VlessNodeMapper vlessNodeMapper;

    public List<VlessNode> list() {
        log.info("开始查询VLESS节点列表");
        List<VlessNode> list = vlessNodeMapper.selectList(null);
        log.info("查询VLESS节点列表成功, size: {}", list.size());
        return list;
    }

    public List<VlessNode> listByIds(List<Long> ids) {
        log.info("开始查询VLESS节点列表, ids: {}", ids);
        List<VlessNode> list = vlessNodeMapper.selectBatchIds(ids);
        log.info("查询VLESS节点列表成功, size: {}", list.size());
        return list;
    }

    public VlessNode getById(Long id) {
        log.info("开始查询VLESS节点, id: {}", id);
        VlessNode node = vlessNodeMapper.selectById(id);
        if (node == null) {
            log.warn("VLESS节点不存在, id: {}", id);
            return null;
        }
        log.info("查询VLESS节点成功, id: {}", id);
        return node;
    }

    public boolean save(VlessNode node) {
        log.info("开始保存VLESS节点: {}", node);
        try {
            boolean success;
            if (node.getId() == null) {
                node.setStatus(1);
                success = vlessNodeMapper.insert(node) > 0;
                log.info("新增VLESS节点成功, id: {}", node.getId());
            } else {
                success = vlessNodeMapper.updateById(node) > 0;
                log.info("更新VLESS节点成功, id: {}", node.getId());
            }

            if (!success) {
                log.error("保存VLESS节点失败");
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("保存VLESS节点异常", e);
            throw e;
        }
    }

    public boolean deleteById(Long id) {
        log.info("开始删除VLESS节点, id: {}", id);
        try {
            if (vlessNodeMapper.deleteById(id) <= 0) {
                log.error("删除VLESS节点失败, id: {}", id);
                return false;
            }
            log.info("删除VLESS节点成功, id: {}", id);
            return true;
        } catch (Exception e) {
            log.error("删除VLESS节点异常", e);
            throw e;
        }
    }

    public boolean deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return vlessNodeMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 添加节点
     *
     * @param vless
     * @return
     */
    public boolean importAppNodesVlessEntity(String vless) {
        VlessNode vlessEntity = VlessConverter.convert(vless);
        if (Objects.isNull(vlessEntity)) {
            return false;
        }
        return this.save(vlessEntity);
    }
}
