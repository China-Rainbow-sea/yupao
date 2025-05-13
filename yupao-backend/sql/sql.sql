create table tag
(
    id           bigint auto_increment comment 'id'
        primary key,
    tagName      varchar(256)                       null comment '标签名称',
    userId       bigint                             null comment '用户id',
    parenId      bigint                             null comment '父标签 id',
    isParent     tinyint                            null comment '0 - 不是, 1- 父标签',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除 0 1（逻辑删除）'
)
    comment '标签';


--   为 user 表，添加上一个新的 tags 字段
alter table user add column tags varchar(1024) null comment '标签列表';