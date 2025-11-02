package com.chenbitao.action_in_my_batis_plus.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenbitao.action_in_my_batis_plus.domain.User;
import com.chenbitao.action_in_my_batis_plus.vo.UserVO;

import java.util.List;
import java.util.Map;

public interface IUserService extends IService<User> {
    /**
     * 按 ID 更新部分字段（动态更新）
     *
     * @param id      用户 ID
     * @param updates 要更新的字段 Map，例如 {"age": 20, "username": "张三"}
     * @return 更新是否成功
     */
    boolean updateById(Long id, Map<String, Object> updates);

    UserVO getUserById(Long id);

    String saveOrUpdateUser(User user);

    Page<UserVO> queryUserPage(Page<User> page);
}
