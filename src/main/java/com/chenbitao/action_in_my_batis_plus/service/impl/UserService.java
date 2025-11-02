package com.chenbitao.action_in_my_batis_plus.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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

    @DS("cluster1")
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
    public String saveOrUpdateUser(User user) {
        boolean bool = this.saveOrUpdate(user);
        return bool ? "操作成功" : "操作失败";
    }

    @DS("cluster1")
    @Override
    public Page<UserVO> queryUserPage(Page<User> page) {
        // 转换 User -> UserVO
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
}