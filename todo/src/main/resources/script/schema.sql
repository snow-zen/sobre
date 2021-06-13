# 标签表
CREATE TABLE TODO_TAG
(
    id   INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '标签名',
    CONSTRAINT uk_name UNIQUE (name)
);

# 类型表
CREATE TABLE TODO_CATEGORY
(
    id   INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '类型名',
    CONSTRAINT uk_name UNIQUE (name)
);

# 任务表
CREATE TABLE TODO_TASK
(
    id           INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    title        VARCHAR(100) NOT NULL COMMENT '任务标题',
    content      TEXT COMMENT '任务内容',
    finish_time  DATETIME COMMENT '任务预计完成时间',
    is_active    TINYINT UNSIGNED COMMENT '任务是否启动',
    gmt_create   DATETIME     NOT NULL DEFAULT NOW() COMMENT '任务创建时间',
    gmt_modified DATETIME     NOT NULL DEFAULT NOW() COMMENT '任务更新时间',
    CONSTRAINT uk_title UNIQUE (title)
);

# 任务-标签中间表
CREATE TABLE TODO_TASK_TAG
(
    id      INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    task_id INT UNSIGNED,
    tag_id  INT UNSIGNED
) COMMENT '任务-标签中间表';

# 任务-类型中间表
CREATE TABLE TODO_TASK_CATEGORY
(
    id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    task_id     INT UNSIGNED,
    category_id INT UNSIGNED
) COMMENT '任务-类型中间表';