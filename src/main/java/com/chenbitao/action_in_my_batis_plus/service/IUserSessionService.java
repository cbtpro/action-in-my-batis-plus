package com.chenbitao.action_in_my_batis_plus.service;

public interface IUserSessionService {
    String register(String userId);

    String getToken(String userId);

    void unregister(String userId);
}
