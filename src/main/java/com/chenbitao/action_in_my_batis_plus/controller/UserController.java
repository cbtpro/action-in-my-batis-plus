package com.chenbitao.action_in_my_batis_plus.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenbitao.action_in_my_batis_plus.domain.User;
import com.chenbitao.action_in_my_batis_plus.service.IUserService;
import com.chenbitao.action_in_my_batis_plus.service.IUserSessionService;
import com.chenbitao.action_in_my_batis_plus.service.impl.UserService;
import com.chenbitao.action_in_my_batis_plus.service.impl.UserSessionService;
import com.chenbitao.action_in_my_batis_plus.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;
    private final IUserSessionService userSessionService;

    private UserController(UserService userService, UserSessionService userSessionService) {
        this.userService = userService;
        this.userSessionService = userSessionService;
    }

    @PostMapping
    public String addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping("/batch")
    public String addUsers(@RequestBody List<User> users) {
        return userService.addUsers(users.stream().map(user-> {
            user.setUsernameReversal(new StringBuilder(user.getUsername()).reverse().toString());
            user.setEmailReversal(new StringBuilder(user.getEmail()).reverse().toString());
            return user;
        }).toList());
    }

    @PostMapping("/save-or-update")
    public String saveOrUpdateUser(@RequestBody User user) {
        return userService.saveOrUpdateUser(user);
    }

    @GetMapping("/{id}")
    public UserVO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.list();
    }

    @GetMapping("/adults")
    public List<User> getAdultUsers() {
        return userService.getAdultUsers();
    }

    @GetMapping("/complex")
    public List<User> getComplexUsers() {
        return userService.getComplexUsers();
    }

    @GetMapping("/page")
    public Page<UserVO> getUserPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<User> page = new Page<>(current, size);

        return userService.queryUserPage(page);
    }

    @GetMapping("/page-with-condition")
    public Page<UserVO> getUserPageWithCondition(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {

        return userService.getUserPageWithCondition(current, size, keyword);
    }

    @PutMapping
    public String updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/fix-age")
    public String fixAge() {
        return userService.fixAge();
    }

    @PatchMapping("/{id}")
    public String partialUpdateUser(@PathVariable Long id,
                                    @RequestBody Map<String, Object> updates) {
        return userService.partialUpdateUser(id, updates);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @DeleteMapping("/batch")
    public String batchDeleteUsers(@RequestBody List<Long> ids) {
        return userService.batchDeleteUsersByIds(ids);
    }

    @DeleteMapping("/old")
    public String deleteOldUsers() {
        return userService.deleteOldUser();
    }
}