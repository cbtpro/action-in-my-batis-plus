-- 清空旧数据
TRUNCATE TABLE t_base_user;

-- 插入示例数据
INSERT INTO `t_base_user`
(`id`, `username`, `username_reversal`, `age`, `email`, `email_reversal`, `create_time`, `update_time`, `deleted`)
VALUES
(1, 'Jone',  REVERSE('Jone'),  18, 'test1@baomidou.com', REVERSE('test1@baomidou.com'),  NOW(), NOW(), 0),
(2, 'Jack',  REVERSE('Jack'),  20, 'test2@baomidou.com', REVERSE('test2@baomidou.com'),  NOW(), NOW(), 0),
(3, 'Tom',   REVERSE('Tom'),   28, 'test3@baomidou.com', REVERSE('test3@baomidou.com'),  NOW(), NOW(), 0),
(4, 'Sandy', REVERSE('Sandy'), 21, 'test4@baomidou.com', REVERSE('test4@baomidou.com'),  NOW(), NOW(), 0),
(5, 'Billie',REVERSE('Billie'),24, 'test5@baomidou.com', REVERSE('test5@baomidou.com'),  NOW(), NOW(), 0);
