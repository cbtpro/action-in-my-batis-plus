package com.chenbitao.action_in_my_batis_plus.controller;

import com.chenbitao.action_in_my_batis_plus.service.IUserService;
import com.chenbitao.action_in_my_batis_plus.service.IUserSessionService;
import com.chenbitao.action_in_my_batis_plus.service.impl.UserService;
import com.chenbitao.action_in_my_batis_plus.service.impl.UserSessionService;
import com.chenbitao.action_in_my_batis_plus.utils.SseEmitterUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/login")
public class UserLoginController {
    private final IUserService userService;
    private final IUserSessionService userSessionService;

    private UserLoginController(UserService userService, UserSessionService userSessionService) {
        this.userService = userService;
        this.userSessionService = userSessionService;
    }

    /**
     * 登录（注册 token）
     */
    @PostMapping("/login")
    public String login(@RequestParam String userId, HttpServletResponse response) {
        String token = userSessionService.register(userId);
        // 写入 Cookie 或返回给前端
        response.addCookie(new Cookie("SSE_TOKEN", token));
        return "登录成功，token=" + token;
    }

    /**
     * 登出：清除 token 与 SSE 连接
     */
    @PostMapping("/logout")
    public String logout(@RequestParam String userId) {
        userSessionService.unregister(userId);
        return "✅ 用户 " + userId + " 已退出并清理注册信息";
    }

    /**
     * 建立 SSE 连接
     */
    @GetMapping("/sse")
    public SseEmitter connect(HttpServletRequest request) {
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("SSE_TOKEN".equals(c.getName())) {
                    token = c.getValue();
                }
            }
        }
        if (token == null) {
            throw new RuntimeException("未找到 token");
        }

        return SseEmitterUtil.connect(token, 0L);
    }
}
