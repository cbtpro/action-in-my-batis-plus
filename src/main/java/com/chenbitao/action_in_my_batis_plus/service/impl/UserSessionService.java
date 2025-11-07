package com.chenbitao.action_in_my_batis_plus.service.impl;

import com.chenbitao.action_in_my_batis_plus.service.IUserSessionService;
import com.chenbitao.action_in_my_batis_plus.utils.SseEmitterUtil;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSessionService implements IUserSessionService {

    // 维护 userId <-> token 映射
    private final Map<String, String> userToToken = new ConcurrentHashMap<>();

    @Override
    public String register(String userId) {
        String token = UUID.randomUUID().toString();
        userToToken.put(userId, token);
        return token;
    }

    @Override
    public String getToken(String userId) {
        return userToToken.get(userId);
    }

    @Override
    public void unregister(String userId) {
        String token = userToToken.remove(userId);
        if (token != null) {
            // 同步清除 SseEmitter
            SseEmitterUtil.remove(token);
        }
    }
}
