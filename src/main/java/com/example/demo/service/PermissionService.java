package com.example.demo.service;

import com.example.demo.dao.RuleMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户权限服务
 * 查询并缓存用户可访问的路径列表
 */
@Service
public class PermissionService {

    private final RuleMapper ruleMapper;
    private final Map<Integer, CacheEntry> cache = new ConcurrentHashMap<>();

    /** 缓存过期时间：5 分钟 */
    private static final long TTL_MS = 5 * 60 * 1000;

    public PermissionService(RuleMapper ruleMapper) {
        this.ruleMapper = ruleMapper;
    }

    /**
     * 获取用户可访问的路径集合（带缓存）
     */
    public Set<String> getUserPaths(Integer userId) {
        CacheEntry entry = cache.get(userId);
        if (entry != null && (System.currentTimeMillis() - entry.timestamp) < TTL_MS) {
            return entry.paths;
        }

        List<String> paths = ruleMapper.findPathsByUserId(userId);
        Set<String> pathSet = new HashSet<>(paths);
        cache.put(userId, new CacheEntry(pathSet));
        return pathSet;
    }

    /**
     * 清除用户权限缓存（角色变更后调用）
     */
    public void evictCache(Integer userId) {
        cache.remove(userId);
    }

    private static class CacheEntry {
        final Set<String> paths;
        final long timestamp;

        CacheEntry(Set<String> paths) {
            this.paths = paths;
            this.timestamp = System.currentTimeMillis();
        }
    }
}
