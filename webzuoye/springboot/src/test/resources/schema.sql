CREATE TABLE user (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      date VARCHAR(255),
                      name VARCHAR(255),
                      province VARCHAR(255),
                      city VARCHAR(255),
                      address VARCHAR(255),
                      zip VARCHAR(255)
);

-- 添加 UserT 表
CREATE TABLE userT (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       password VARCHAR(255),
                       name VARCHAR(255),
                       email VARCHAR(255),
                       avatar VARCHAR(255),
                       birthday VARCHAR(255),
                       money FLOAT
);