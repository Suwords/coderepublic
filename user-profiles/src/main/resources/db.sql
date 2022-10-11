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

2. MySQL标签数据
-- 标签表：tbl_basic_tag，存储标签的基本信息，属于基础标签
CREATE TABLE `tbl_basic_tag`(
  id bigint(20) not null auto_increment comment'标签ID',
  name varchar(50) default null comment '标签名称',
  industry varchar(30) default null comment '行业、子行业、业务类型、标签、属性',
  rule varchar(300) default null comment '标签规则',
  business varchar(100) default null comment '业务描述',
  level int(11) default null comment '标签等级',
  pid bigint(20) default null comment '父标签ID',
  ctime datetime default null comment '状态：1申请中、2开发中、3开发完成、4已上线、5已下线、6已禁用',
  remark varchar(100) default null comment '备注',
  primary key (id)
) engine=InnoDB AUTO_INCREMENT=233 DEFAULT CHARSET=utf8 COMMENT ='基础标签表';

-- 模型表：tbl_model，存储每个4级标签具体Spark应用程序相关信息，对应于基础标签
CREATE TABLE tbl_model(
  id bigint(20) default null ,
  tag_id bigint(20) default null comment '标签ID',
  `type` int(11) default null comment '算法类型：统计-Statistics、规则匹配-Match、挖掘-具体算法-DecisionTree’，
  model_name varchar(200) default null comment '模型名称',
  model_main varchar(200) default null comment '模型运行主类名称',
  model_path varchar(200) default null comment '模型JAR包HDFS路径',
  sche_time varchar(200) default null comment '模型调度时间',
  ctime datetime default null comment '创建模型时间戳',
  utime datetime default null comment '更新模型时间戳',
  state int(11) default null comment '模型状态：1运行、2停止',
  remark varchar(100) default null ,
  operator varchar(100) default null,
  operation varchar (100) default null ,
  args varchar(100) default null comment '模型运行应用配置参数，如资源配置参数'
) engine=InnoDB default charset=utf8;

SELECT tb.id AS tagId,
       tb.`name` AS tagName,
       tb.business,
       tb.industry,
       tb.`level`,
       tb.rule,
       tb.model_main AS modelMain,
       tb.model_name AS modelName,
       tb.model_path AS modelPath,
       tb.sche_time AS schetime,
       tb.args
FROM (
  SELECT tb1.id,
         tb1.`name`,
         tb1.business,
         tb1.industry,
         tb1.`level`,
         tb1.pid,
         tb1.state,
         tb2.model_main,
         tb2.model_name,
         tb2.model_path,
         tb2.sche_time,
         tb3.rule,
         tb2.args
   FROM
   tbl_basic_tag tb1
   INNER JOIN tbl_model tb2
   ON tb1.id = tb2.tag_id
   INNER JOIN tbl_rule tb3
   ON tb1.id = tb3.tag_id
) tb WHERE 1 = 1 AND tb.state != -1 AND tb.id = 8 ;

