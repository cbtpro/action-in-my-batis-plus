package com.chenbitao.action_in_my_batis_plus.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenbitao.action_in_my_batis_plus.domain.User;
import com.chenbitao.action_in_my_batis_plus.mapper.UserMapper;
import com.chenbitao.action_in_my_batis_plus.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * UserService 实现类
 * 
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
        return lambdaQuery()
                .eq(User::getUsername, username)  // username = ?
                .one();  // 查询一条记录
    }
    
    /**
     * 自定义方法：根据邮箱后缀查询用户
     */
    public List<User> getUsersByEmailSuffix(String emailSuffix) {
        return lambdaQuery()
                .likeRight(User::getEmail, emailSuffix)  // email like 'emailSuffix%'
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
}