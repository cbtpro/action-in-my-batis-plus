package com.chenbitao.action_in_my_batis_plus.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenbitao.action_in_my_batis_plus.domain.User;
import com.chenbitao.action_in_my_batis_plus.vo.UserVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    String addUser(User user);
    String addUsers(List<User> users);
    String saveOrUpdateUser(User user);

    Page<UserVO> queryUserPage(Page<User> page);

    String deleteOldUser();

    String deleteUserById(Long id);
    String batchDeleteUsersByIds(List<Long> ids);
    List<User> getAdultUsers();
    List<User> getComplexUsers();
    Page<UserVO> getUserPageWithCondition(Page<User> page, String keyword);
    String partialUpdateUser(Long id, Map<String, Object> updates);
    String updateUser(User user);
    String fixAge();
}
