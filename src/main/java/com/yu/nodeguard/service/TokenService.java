package com.yu.nodeguard.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yu.nodeguard.dto.TokenDTO;
import com.yu.nodeguard.entity.Token;
import com.yu.nodeguard.entity.TokenNode;
import com.yu.nodeguard.mapper.TokenMapper;
import com.yu.nodeguard.mapper.TokenNodeMapper;
import com.yu.nodeguard.vo.TokenVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenService {

    @Autowired
    private TokenMapper tokenMapper;

    @Autowired
    private TokenNodeMapper tokenNodeMapper;

    public List<Token> list() {
        return tokenMapper.selectList(null);
    }

    public Token getById(Long id) {
        return tokenMapper.selectById(id);
    }

    public Token getByToken(String token) {
        return tokenMapper.selectOne(new LambdaQueryWrapper<Token>()
                .eq(Token::getToken, token));
    }

    public List<TokenNode> getTokenNodes(Long tokenId) {
        return tokenNodeMapper.selectList(new LambdaQueryWrapper<TokenNode>()
                .eq(TokenNode::getTokenId, tokenId));
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveWithNodes(TokenDTO tokenDTO) {
        log.info("开始保存Token信息: {}", tokenDTO);
        try {
            // 保存或更新Token
            Token token = new Token();
            token.setId(tokenDTO.getId());
            token.setToken(tokenDTO.getToken());
            token.setExpirationDate(tokenDTO.getExpirationDate());
            
            boolean success;
            if (token.getId() == null) {
                token.setStatus(1);
                success = tokenMapper.insert(token) > 0;
                log.info("新增Token成功, id: {}", token.getId());
            } else {
                success = tokenMapper.updateById(token) > 0;
                log.info("更新Token成功, id: {}", token.getId());
                // 删除原有的节点关联
                tokenNodeMapper.delete(new LambdaQueryWrapper<TokenNode>().eq(TokenNode::getTokenId, token.getId()));
                log.info("删除Token原有节点关联成功, tokenId: {}", token.getId());
            }
            
            if (!success) {
                log.error("保存Token失败");
                return false;
            }
            
            // 保存节点关联
            if (tokenDTO.getNodeIds() != null && !tokenDTO.getNodeIds().isEmpty()) {
                for (Long nodeId : tokenDTO.getNodeIds()) {
                    TokenNode tokenNode = new TokenNode();
                    tokenNode.setTokenId(token.getId());
                    tokenNode.setNodeId(nodeId);
                    tokenNode.setStatus(1);
                    if (tokenNodeMapper.insert(tokenNode) <= 0) {
                        log.error("保存Token节点关联失败, tokenId: {}, nodeId: {}", token.getId(), nodeId);
                        throw new RuntimeException("保存节点关联失败");
                    }
                }
                log.info("保存Token节点关联成功, tokenId: {}, nodeIds: {}", token.getId(), tokenDTO.getNodeIds());
            }
            
            return true;
        } catch (Exception e) {
            log.error("保存Token信息异常", e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteWithNodes(Long id) {
        log.info("开始删除Token, id: {}", id);
        try {
            // 删除Token
            if (tokenMapper.deleteById(id) <= 0) {
                log.error("删除Token失败, id: {}", id);
                return false;
            }
            log.info("删除Token成功, id: {}", id);
            
            // 删除关联的节点记录
            tokenNodeMapper.delete(new LambdaQueryWrapper<TokenNode>()
                    .eq(TokenNode::getTokenId, id));
            log.info("删除Token关联节点成功, tokenId: {}", id);
            
            return true;
        } catch (Exception e) {
            log.error("删除Token异常", e);
            throw e;
        }
    }

    public List<TokenVO> listWithNodes() {
        log.info("开始查询Token列表");
        List<Token> tokens = tokenMapper.selectList(null);
        List<TokenVO> result = tokens.stream().map(this::convertToTokenVO).collect(Collectors.toList());
        log.info("查询Token列表成功, size: {}", result.size());
        return result;
    }
    
    public TokenVO getByIdWithNodes(Long id) {
        log.info("开始查询Token详情, id: {}", id);
        Token token = tokenMapper.selectById(id);
        if (token == null) {
            log.warn("Token不存在, id: {}", id);
            return null;
        }
        TokenVO result = convertToTokenVO(token);
        log.info("查询Token详情成功, id: {}", id);
        return result;
    }
    
    private TokenVO convertToTokenVO(Token token) {
        TokenVO tokenVO = new TokenVO();
        BeanUtils.copyProperties(token, tokenVO);
        
        // 获取关联的节点ID列表
        List<Long> nodeIds = tokenNodeMapper.selectList(
                new LambdaQueryWrapper<TokenNode>()
                        .eq(TokenNode::getTokenId, token.getId())
        ).stream().map(TokenNode::getNodeId).collect(Collectors.toList());
        
        tokenVO.setNodeIds(nodeIds);
        return tokenVO;
    }
}
