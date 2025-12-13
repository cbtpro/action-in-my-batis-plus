package com.chenbitao.action_in_my_batis_plus.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenbitao.action_in_my_batis_plus.domain.User;
import com.chenbitao.action_in_my_batis_plus.mapper.UserMapper;
import com.chenbitao.action_in_my_batis_plus.service.IUserService;
import com.chenbitao.action_in_my_batis_plus.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * UserService 实现类
 * <p>
 * 继承 ServiceImpl 就自动拥有了更多的 CRUD 方法
 * 参数说明：
 * - UserMapper：你的 Mapper 接口
 * - User：你的实体类
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {
    // 可以在这里添加自定义的业务方法

    /**
     * 自定义方法：根据用户名查询用户
     */
    public User getUserByUsername(String username) {
        // 使用 MP 的查询构造器
        return lambdaQuery().eq(User::getUsername, username)  // username = ?
                .one();  // 查询一条记录
    }

    /**
     * 自定义方法：根据邮箱后缀查询用户
     */
    public List<User> getUsersByEmailSuffix(String emailSuffix) {
        return lambdaQuery().likeRight(User::getEmail, emailSuffix)  // email like 'emailSuffix%'
                .list();
    }

    @Override
    @Transactional
    public boolean updateById(Long id, Map<String, Object> updates) {
        if (id == null || updates == null || updates.isEmpty()) {
            return false;
        }

        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id); // 按 ID 条件

        // 设置更新字段
        updates.forEach(wrapper::set);

        // 执行更新
        return this.update(wrapper);
    }

    @Override
    public UserVO getUserById(Long id) {
        User user = this.getById(id);
        return Optional.ofNullable(user)
                .map(userObj -> UserVO.builder()
                        .id(userObj.getId())
                        .username(userObj.getUsername())
                        .age(userObj.getAge())
                        .createTime(userObj.getCreateTime())
                        .updateTime(userObj.getUpdateTime())
                        .deleted(userObj.getDeleted())
                        .build())
                .orElse(null);
    }

    @Transactional
    @Override
    public String addUser(User user) {
        user.setUsernameReversal(new StringBuilder(user.getUsername()).reverse().toString());
        user.setEmailReversal(new StringBuilder(user.getEmail()).reverse().toString());
        boolean bool = this.save(user);
        return bool ? "新增用户成功" : "新增用户失败";
    }

    @Transactional
    @Override
    public String saveOrUpdateUser(User user) {
        user.setUsernameReversal(new StringBuilder(user.getUsername()).reverse().toString());
        user.setEmailReversal(new StringBuilder(user.getEmail()).reverse().toString());
        boolean bool = this.saveOrUpdate(user);
        return bool ? "操作成功" : "操作失败";
    }

    @Override
    public Page<UserVO> queryUserPage(Page<User> page) {
        Page<User> records = this.page(page);

        // 将转换后的数据设置回分页对象
        Page<UserVO> voPage = new Page<>();
        voPage.setCurrent(records.getCurrent());
        voPage.setSize(records.getSize());
        voPage.setTotal(records.getTotal());
        voPage.setRecords(records.getRecords().stream()
                .map(user -> UserVO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .age(user.getAge())
                        .createTime(user.getCreateTime())
                        .updateTime(user.getUpdateTime())
                        .deleted(user.getDeleted())
                        .build())
                .toList());

        return voPage;
    }

    @Transactional
    public String deleteOldUser() {
        boolean success = this.lambdaUpdate()
                .gt(User::getAge, 100)
                .remove();
        return success ? "删除老年用户成功" : "删除失败";
    }

    @Transactional
    @Override
    public String batchDeleteUsersByIds(List<Long> ids) {
        boolean success = this.removeByIds(ids);
        return success ? "批量删除成功" : "批量删除失败";
    }

    @Transactional
    @Override
    public String deleteUserById(Long id) {
        // 逻辑删除，实际上执行的是 update 操作，将 deleted 字段设置为 1
        boolean success = this.removeById(id);
        return success ? "删除成功" : "删除失败";
    }

    @Transactional
    @Override
    public String addUsers(List<User> users) {
        boolean success = this.saveBatch(users, 100);
        return success ? "批量新增成功" : "批量新增失败";
    }

    @Override
    public List<User> getAdultUsers() {
        return this.lambdaQuery()
                .gt(User::getAge, 18)
                .list();
    }

    @Override
    public List<User> getComplexUsers() {
        return this.lambdaQuery()
                .between(User::getAge, 18, 60)           // age between 18 and 60
                .like(User::getUsername, "张")           // username like '%张%'
                .orderByDesc(User::getAge)               // 按年龄降序
                .list();
    }

    @Override
    public Page<UserVO> getUserPageWithCondition(Integer current, Integer size, String keyword) {
        Page<User> page = new Page<>(current, size);
        Page<User> records = this.lambdaQuery()
                .like(StringUtils.isNotBlank(keyword), User::getUsername, keyword)
                .page(page);

        // 将转换后的数据设置回分页对象
        Page<UserVO> voPage = new Page<>();
        voPage.setCurrent(records.getCurrent());
        voPage.setSize(records.getSize());
        voPage.setTotal(records.getTotal());
        voPage.setRecords(records.getRecords().stream()
                .map(user -> UserVO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .age(user.getAge())
                        .createTime(user.getCreateTime())
                        .updateTime(user.getUpdateTime())
                        .deleted(user.getDeleted())
                        .build())
                .toList());
        return voPage;
    }

    @Transactional
    @Override
    public String partialUpdateUser(Long id, Map<String, Object> updates) {
        // 移除不能更新的字段
        updates.remove("id");
        updates.remove("createTime");

        boolean success = this.updateById(id, updates);
        return success ? "部分更新成功" : "部分更新失败";
    }

    @Transactional
    @Override
    public String updateUser(User user) {
        if (user.getId() == null) {
            return "用户ID不能为空";
        }

        // 方法1：根据ID更新所有字段
        boolean success = this.updateById(user);

        return success ? "更新成功" : "更新失败";
    }

    @Transactional
    @Override
    public String fixAge() {
        boolean success = this.lambdaUpdate()
                .lt(User::getAge, 18)        // age < 18
                .set(User::getAge, 18)       // 设置 age = 18
                .update();

        return success ? "年龄修复成功" : "年龄修复失败";
    }
}