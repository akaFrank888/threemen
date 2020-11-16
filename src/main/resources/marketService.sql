-- 一、建库

create database threeMen;

-- 二、建表


-- 模块一、勤工俭学信息平台：

-- 注意：不是引号‘’，而是``（否则编译出错）
use threeMen;
create table `workstudy_platform`
(
    `workstudy_id`       int auto_increment,
    `workstudy_position` varchar(20) not null,
    `workstudy_fix_seat` varchar(150),
    `workstudy_tem_seat` varchar(100),

    primary key (`workstudy_id`),
    unique key (`workstudy_id`)

);

-- 模块二、代拿与兼职的三种业务：

-- 不管什么业务，先建立关于 用户与小哥 的表

-- 1. 建立 用户注册 的表
use threeMen;
create table `user_register`
(
    `user_id`       int auto_increment,

    `user_account`  varchar(20) not null comment '账号',
    `user_password` varchar(50) not null comment '密码',

    `user_nickname` varchar(20) not null comment '昵称/称呼',
    `user_email`    varchar(20) not null comment '邮箱',
    `user_status`   char(1) comment '激活状态',

    `user_key`      varchar(50) comment 'token',


    primary key (user_id),
    unique key (user_account),
    unique key (user_email),
    unique key (user_key)

);




-- 2. 建立 用户信息 的表
create table `user_info`
(
    `user_id`         int auto_increment,

    `user_img`        longtext comment '头像',

    `user_department` varchar(20) comment '院',
    `user_master`     varchar(10) comment '系',
    `user_address`    varchar(15) comment '住址（格式为：苑#栋）',
    `user_phone`      char(15) comment '手机号',

    `user_token`      varchar(50) comment 'token',

    primary key (user_id),
    unique key (user_token),
    unique key (user_phone)

);



-- 3. 建立 用户记录 的表
create table `user_self`
(
    `user_id`          int auto_increment,
    `level_id`         int      default 1 comment '等级索引',
    `user_experience`  smallint default 0 comment '经验',
    `user_balance`     smallint default 30 comment '余额',
    `user_order_count` smallint default 0 comment '下单数',
    `user_token`       varchar(50) not null comment 'token',

    primary key (user_id),
    unique key (user_token),
    constraint fk_level foreign key (level_id) references user_level (id)

);





-- 4. 建立 用户等级 的表
create table `user_level`
(
    `id`         int auto_increment,
    `level`      char(8) comment '等级',
    `level_name` char(8) comment '等级名字',

    primary key (id)

);

-- 5. 建立 用户实名验证 的表
create table `user_realize`
(
    `user_id`       int auto_increment,
    `user_account` varchar(10) not null comment '教务系统账号',
    `user_password`    varchar(50) not null comment '教务系统密码',
    `worker_token`    varchar(50) not null comment 'token',


    primary key (user_id),
    unique key (user_account),
    unique key (worker_token)


);



-- 1. 建立 小哥打工记录 的表
create table `worker_info`
(

    `worker_id`          int auto_increment,
    `worker_mark`        tinyint  default 0 comment '评分',
    `worker_total_count` smallint default 0 comment '接单数',
    `worker_good_count`  smallint default 0 comment '好评数',
    `worker_bad_count`   smallint default 0 comment '污点数',

    `worker_token`       varchar(50) not null,


    primary key (worker_id),
    unique key (worker_token)

);




-- 业务一：超市代购

-- 1. 建立 订单 的表
create table `order_commodity`
(

    `id`          int auto_increment,

    `user_address`      varchar(10) not null comment '收货地址',
    `comm_info`         varchar(200) comment '商品信息（超市代购）或取件码（快递代拿）或空（餐厅代购）',
    `comm_address`      varchar(10) comment '购买地址',
    `comm_cost_real`    double  default 0 comment '预估费用',
    `comm_cost_coin`    tinyint default 20 comment '代买服务所需金币数',
    `comm_reward`       tinyint default 0 comment '打赏',
    `comm_left_message` varchar(100) comment '用户留言',
    `comm_date`         datetime comment '下单时间',
    `status_id`         int     default 0 comment '订单状态',
    `comm_num`          varchar(50) not null comment '订单号',
    `user_token`        varchar(50) not null comment '用户的token',
    `service_id`        int comment '服务类型的索引',

    primary key (id),
    unique (comm_num),
    constraint fk_status foreign key (status_id) references order_status (id),
    constraint fk_service foreign key (service_id) references service_type (id)

);

-- 2. 建立 小哥接单的表
create table `order_worker`
(
    `id`            int auto_increment,
    `comm_num`      varchar(50) not null comment '订单号',
    `worker_token`  varchar(50) comment '小哥的token',
    'pick_time'     datetime comment '接单时间',
    `order_comment` varchar(100) comment '用户对小哥的评价',
    `reach_time`    datetime comment '到达时间',


    primary key (id)
);








-- 3. 建立 订单状态 的表
create table `order_status`
(
    `id`          int auto_increment,
    `status`      char(15) comment '订单状态',
    `status_name` char(10) comment '订单状态的名字',

    primary key (id),
    unique key (status)

);


-- 4. 建立  服务类型  的表
create table `service_type`
(
    `id`        int not null auto_increment,
    `type`      char(20) comment '服务类型',
    `type_name` char(20) comment '服务类型的名字',

    primary key (id),
    unique key (type)
);




-- 三、插入数据

-- （1）用户等级

insert into user_level values(null,'lower','初级');
insert into user_level values(null,'higher','高级');
insert into user_level values(null,'special','特级');

-- （2）订单状态
insert into order_status values(null,'unpaid','待支付');
insert into order_status values(null,'unpicked','待接单');
insert into order_status values(null,'unreceived','待收货');
insert into order_status values(null,'finished','订单完成');
insert into order_status values(null,'failed','订单关闭');

-- （3）服务类型
insert into service_type values (null, '超市代购','marketService');
insert into service_type values (null, '餐厅代购','restaurantService');
insert into service_type values (null, '快递代拿','deliveryService');


-- （4）勤工俭学信息

insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '教务处',
                                                                                                                  '整理档案，统计数据，公众平台运行、信息编辑，统筹各科室勤工',
                                                                                                                  '考场布置，课表张贴，毕业生采集工作，教材发放，教务系统维护，试卷档案整理，毕业论文整理'
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '党委组织部',
                                                                                                                  '整理资料，录入数据，编辑文稿',
                                                                                                                  null
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '人事处',
                                                                                                                  null,
                                                                                                                  '临时岗位'
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '党委宣传部',
                                                                                                                  null,
                                                                                                                  '档案整理，校报校稿与分发，官微素材搜集，会务工作'
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '校长办公室',
                                                                                                                  '协助完成行政工作，五楼会议室会务服务，收发室报刊分类整理',
                                                                                                                  null
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '图书馆',
                                                                                                                  '流通部书库整理，采编部新书分编，信息部馆刊编辑，公众号管理，期刊部报刊整理，值班，技术部系统维护',
                                                                                                                  '同上'
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '货币馆',
                                                                                                                  null,
                                                                                                                  '讲解接待组，文创产品开发组，运营微信公众号，整理藏品信息，库房藏品维护'
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '保卫处',
                                                                                                                  '巡逻检查，户政室值班，资料收集，校园安全宣传，征兵宣传，维护共享单车秩序',
                                                                                                                  null
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '财务处',
                                                                                                                  '档案管理，预算数据录入',
                                                                                                                  '原始票据整理，学费单整理'
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '科研处',
                                                                                                                  null,
                                                                                                                  '整理各类资料，处理数据，协助开展项目答辩会议事务'
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '党委组织部',
                                                                                                                  '整理资料，录入数据，编辑文稿',
                                                                                                                  null
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '工会',
                                                                                                                  '办公室值班，协助各项教职工活动开展，网站维护与数据录入',
                                                                                                                  '教职工乒乓球赛，八段锦培训班，太极拳培训班，羽毛球比赛，教职工运动会活动'
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '团委',
                                                                                                                  '办公室值班，学生档案管理，收发信件，会务工作，校级组织课室的申请，管理会议室的使用',                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       null
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '财经与新媒体学院',
                                                                                                                  '协助辅导员、教务秘书开展各项工作，周末值班，毕业班就业等工作',
                                                                                                                  null
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '创业教育学院',
                                                                                                                  '办公室往来文书，收发信件文件，部门文件整理，融创空间值班人员',
                                                                                                                  null
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '法学院',
                                                                                                                  '学生思想动态定期汇报，奖勤助贷，毕业生、办公室文件收发，法律援助中心资料整理，周末值班工作',
                                                                                                                  null
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '继续教育学院',
                                                                                                                  '协助教学管理部，招生办公室，学籍管理，周末值班及辅导员开展各项工作',
                                                                                                                  null
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '马克思主义学院',
                                                                                                                  '协助办公室、实验室、党总支开展各项工作',
                                                                                                                  null
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '国际教育学院',
                                                                                                                  '协助处理各类学生事务，文件翻译工作',
                                                                                                                  '招生宣传，整理学生国外院校申请和签证材料，协助文件翻译等'
                                                                                                                 );
insert into workstudy_platform (workstudy_id, workstudy_position, workstudy_fix_seat, workstudy_tem_seat) values (null,
                                                                                                                  '肇庆校区',
                                                                                                                  '协助继教科、教务科、图书馆、财务科、办公室、学生科、卫生所、公共教研室体育器材发放、保卫科和实验技术室等各项工作开展',
                                                                                                                  '同上'
                                                                                                                 );

