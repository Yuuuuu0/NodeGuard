package com.yu.nodeguard.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yu.nodeguard.entity.SysDict;
import com.yu.nodeguard.mapper.SysDictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictService {
    
    @Autowired
    private SysDictMapper dictMapper;
    
    public List<SysDict> findByType(String dictType) {
        return dictMapper.selectList(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getDictType, dictType));
    }
    
    public List<SysDict> findAll() {
        return dictMapper.selectList(null);
    }
    
    public boolean save(SysDict dict) {
        if (dict.getId() == null) {
            dict.setStatus(1);
            return dictMapper.insert(dict) > 0;
        } else {
            return dictMapper.updateById(dict) > 0;
        }
    }
    
    public boolean deleteById(Long id) {
        return dictMapper.deleteById(id) > 0;
    }
} 