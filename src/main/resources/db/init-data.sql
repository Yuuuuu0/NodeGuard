-- 清空表并重置自增ID
TRUNCATE TABLE sys_user;
ALTER TABLE sys_user AUTO_INCREMENT = 1;

-- 插入测试用户，密码为 admin123
INSERT INTO sys_user (
    username, 
    password,  -- 这是使用BCryptPasswordEncoder加密后的 "admin123"
    email, 
    mobile, 
    status, 
    create_time, 
    update_time
) VALUES (
    'admin',
    '$2a$10$X/uME6GXqbUaD2bwUZqxUOVFUYi9h2d9fwOoIyGhODhH/odHekepu',
    'admin@example.com',
    '13800138000',
    1,
    NOW(),
    NOW()
); 