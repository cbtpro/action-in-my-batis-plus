package com.chenbitao.action_in_my_batis_plus.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@TableName("t_base_user")  // 指定对应的数据库表名
public class User {
    
    /**
     * 用户ID - 主键
     * &#064;TableId  注解表示这是主键
     * value = "id" 表示对应数据库字段名
     * type = IdType.AUTO 表示主键自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户名
     * 如果字段名与数据库列名一致（驼峰转下划线），可以省略 @TableField
     */
    private String username;

    /**
     * 用户名反转字符串，用于模糊查询
     */
    private String usernameReversal;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱反转字符串，用于模糊查询
     */
    private String emailReversal;
    
    /**
     * 创建时间
     * &#064;TableField  配置填充策略
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     * 插入和更新时都自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 逻辑删除字段
     * 0-未删除 1-已删除
     */
    @TableLogic
    private Integer deleted;
}