-- 描述用户画像项目相关数据库建设

1. 用户画像标签表
-- 存储在HBase表中
-- 表名 tbl_profile
-- 列簇：
--      用户标签：user
--      商品标签：item
-- RowKey：用户ID userId
-- 标签值：（列值）
--      实际存储标签值：tagValue
--      标签字段：tagName
-- 标签信息表：
--  tagId,      tagName,    tagDesc
--  384         男           用户的性别为男性
