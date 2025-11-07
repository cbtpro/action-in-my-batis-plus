package com.chenbitao.action_in_my_batis_plus.controller;

import com.chenbitao.action_in_my_batis_plus.service.IUserService;
import com.chenbitao.action_in_my_batis_plus.service.IUserSessionService;
import com.chenbitao.action_in_my_batis_plus.service.impl.UserService;
import com.chenbitao.action_in_my_batis_plus.service.impl.UserSessionService;

public class UserSigninController {

    private final IUserService userService;
    private final IUserSessionService userSessionService;

    private UserSigninController(UserService userService, UserSessionService userSessionService) {
        this.userService = userService;
        this.userSessionService = userSessionService;
    }
}
