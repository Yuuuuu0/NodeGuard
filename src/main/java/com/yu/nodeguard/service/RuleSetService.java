package com.yu.nodeguard.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yu.nodeguard.entity.RuleSet;
import com.yu.nodeguard.mapper.RuleSetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class RuleSetService {
    
    @Autowired
    private RuleSetMapper ruleSetMapper;
    
    public List<RuleSet> list() {
        log.info("开始查询规则列表");
        List<RuleSet> list = ruleSetMapper.selectList(null);
        log.info("查询规则列表成功, size: {}", list.size());
        return list;
    }
    
    public List<RuleSet> listByType(Integer type) {
        log.info("开始查询指定类型的规则列表, type: {}", type);
        List<RuleSet> list = ruleSetMapper.selectList(new LambdaQueryWrapper<RuleSet>()
                .eq(RuleSet::getType, type));
        log.info("查询指定类型的规则列表成功, type: {}, size: {}", type, list.size());
        return list;
    }
    
    public boolean save(RuleSet ruleSet) {
        log.info("开始保存规则: {}", ruleSet);
        try {
            boolean success;
            if (ruleSet.getId() == null) {
                ruleSet.setStatus(1);
                success = ruleSetMapper.insert(ruleSet) > 0;
                log.info("新增规则成功, id: {}", ruleSet.getId());
            } else {
                success = ruleSetMapper.updateById(ruleSet) > 0;
                log.info("更新规则成功, id: {}", ruleSet.getId());
            }
            
            if (!success) {
                log.error("保存规则失败");
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("保存规则异常", e);
            throw e;
        }
    }
    
    public boolean deleteById(Long id) {
        log.info("开始删除规则, id: {}", id);
        try {
            if (ruleSetMapper.deleteById(id) <= 0) {
                log.error("删除规则失败, id: {}", id);
                return false;
            }
            log.info("删除规则成功, id: {}", id);
            return true;
        } catch (Exception e) {
            log.error("删除规则异常", e);
            throw e;
        }
    }
} 