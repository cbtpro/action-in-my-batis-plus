package com.chenbitao.action_in_my_batis_plus.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenbitao.action_in_my_batis_plus.domain.User;
import com.chenbitao.action_in_my_batis_plus.service.IUserService;
import com.chenbitao.action_in_my_batis_plus.service.impl.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String addUser(@RequestBody User user) {
        // 方法1：使用 save 方法
        boolean success = userService.save(user);
        
        if (success) {
            return "新增用户成功，用户ID：" + user.getId();
        } else {
            return "新增用户失败";
        }
    }

    @PostMapping("/batch")
    public String addUsers(@RequestBody List<User> users) {
        boolean success = userService.saveBatch(users, 100);
        return success ? "批量新增成功" : "批量新增失败";
    }

    @PostMapping("/save-or-update")
    public String saveOrUpdateUser(@RequestBody User user) {
        boolean success = userService.saveOrUpdate(user);
        return success ? "操作成功" : "操作失败";
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.list();
    }

    @GetMapping("/adults")
    public List<User> getAdultUsers() {
        return userService.lambdaQuery()
                .gt(User::getAge, 18)        // age > 18
                .list();
    }

    @GetMapping("/complex")
    public List<User> getComplexUsers() {
        return userService.lambdaQuery()
                .between(User::getAge, 18, 60)           // age between 18 and 60
                .like(User::getUsername, "张")           // username like '%张%'
                .orderByDesc(User::getAge)               // 按年龄降序
                .list();
    }

    @GetMapping("/page")
    public Page<User> getUserPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<User> page = new Page<>(current, size);

        return userService.page(page);
    }

    @GetMapping("/page-with-condition")
    public Page<User> getUserPageWithCondition(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {

        Page<User> page = new Page<>(current, size);

        return userService.lambdaQuery()
                .like(StringUtils.isNotBlank(keyword), User::getUsername, keyword)
                .page(page);
    }

    @PutMapping
    public String updateUser(@RequestBody User user) {
        if (user.getId() == null) {
            return "用户ID不能为空";
        }

        // 方法1：根据ID更新所有字段
        boolean success = userService.updateById(user);

        return success ? "更新成功" : "更新失败";
    }

    @PutMapping("/fix-age")
    public String fixAge() {
        boolean success = userService.lambdaUpdate()
                .lt(User::getAge, 18)        // age < 18
                .set(User::getAge, 18)       // 设置 age = 18
                .update();

        return success ? "年龄修复成功" : "年龄修复失败";
    }

    @PatchMapping("/{id}")
    public String partialUpdateUser(@PathVariable Long id,
                                    @RequestBody Map<String, Object> updates) {
        // 移除不能更新的字段
        updates.remove("id");
        updates.remove("createTime");

        boolean success = userService.updateById(id, updates);
        return success ? "部分更新成功" : "部分更新失败";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        // 逻辑删除，实际上执行的是 update 操作，将 deleted 字段设置为 1
        boolean success = userService.removeById(id);
        return success ? "删除成功" : "删除失败";
    }

    @DeleteMapping("/batch")
    public String batchDeleteUsers(@RequestBody List<Long> ids) {
        boolean success = userService.removeByIds(ids);
        return success ? "批量删除成功" : "批量删除失败";
    }

    @DeleteMapping("/old")
    public String deleteOldUsers() {
        boolean success = userService.lambdaUpdate()
                .gt(User::getAge, 100)
                .remove();
        return success ? "删除老年用户成功" : "删除失败";
    }
}