-- 清空旧数据
TRUNCATE TABLE t_base_user;

-- 插入示例数据
INSERT INTO `t_base_user`
(`id`, `username`, `username_reversal`, `age`, `email`, `email_reversal`, `create_time`, `update_time`, `deleted`)
VALUES
(1, 'Jone',  'enoJ',  18, 'test1@baomidou.com', 'moc.uodimoab@1tset',  NOW(), NOW(), 0),
(2, 'Jack',  'kcaJ',  20, 'test2@baomidou.com', 'moc.uodimoab@2tset',  NOW(), NOW(), 0),
(3, 'Tom',   'moT',   28, 'test3@baomidou.com', 'moc.uodimoab@3tset',  NOW(), NOW(), 0),
(4, 'Sandy', 'ydnaS', 21, 'test4@baomidou.com', 'moc.uodimoab@4tset',  NOW(), NOW(), 0),
(5, 'Billie','eilliB',24, 'test5@baomidou.com', 'moc.uodimoab@5tset',  NOW(), NOW(), 0);
