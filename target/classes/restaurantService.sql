
-- 一、建表

-- 1. 建立 餐厅、楼层 表
use `threemen`;

create table `restaurant_location`
(
    `location_id` int not null,
    `restaurant` varchar(5) comment '南饭/北饭',
    `floor` int(5) comment '楼层',

    primary key (location_id)
);


-- 2. 建立 店铺 表
create table `restaurant_shop`
(
    `id`          int not null auto_increment,
    `shop_name`   varchar(20) comment '店铺名称',
    `dish_name`   varchar(20) comment '菜名',
    `dish_price`  double comment '菜品价格',
    `phone`       char(13) comment '店铺联系方式',
    `school`      char(18) comment '学校',
    `time`        varchar(25) comment '营业时间',
    `location`    int comment '地点',
    `shop_img_id` int comment '店铺id',
    `dish_id`     int comment '菜品id',
    `img`         longtext comment '菜品照片',

    primary key (id, location),
    constraint fk_location foreign key (location) references restaurant_location (location_id),
    constraint fk_shop_img foreign key (shop_img_id) references restaurant_shop_img (id)
);


-- 3. 建立 店铺图片表
create table `restaurant_shop_img`
(
    `id`  int not null auto_increment,
    `img` longtext comment '店铺照片',

    primary key (id)
);

-- 4. 建立 餐厅代买订单表
create table `order_restaurant`
(
    `order_id`          int auto_increment,

    `user_address`      varchar(10)  not null comment '收货地址',
    `comm_address`      varchar(10)  not null comment '购买地址',
    `comm_cost_real`    double  default 0 comment '预估费用',
    `comm_cost_coin`    tinyint default 20 comment '代买服务所需金币数',
    `comm_reward`       tinyint default 0 comment '打赏',
    `comm_left_message` varchar(100) comment '用户留言',
    `comm_date`         datetime comment '下单时间',
    `status_id`         int default 0 comment '订单状态',
    `comm_num`          varchar(50)  not null comment '订单号',
    `user_token`        varchar(50)  not null comment '用户的token',
    `worker_token`      varchar(50) comment '小哥的token',

    primary key (order_id),
    unique (comm_num),
    constraint fk_status_restaurant foreign key (status_id) references order_status (id)

)






-- 二、插入数据

-- 1. 插入 餐厅、楼层 的数据
insert into restaurant_location values (1, '北饭', 1);
insert into restaurant_location values (2, '北饭', 2);
insert into restaurant_location values (3, '北饭', 3);
insert into restaurant_location values (4, '南饭', 4);
insert into restaurant_location values (5, '南饭', 5);


-- 2. 插入 店铺 的数据
insert into restaurant_shop values(null,'南四铁板滑蛋饭系列','铁板茄子',85,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',4,1,1,null);
insert into restaurant_shop values(null,'南四铁板滑蛋饭系列','铁板鸡块',85,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',4,1,2,null);

insert into restaurant_shop values(null,'南四皇叶子纯手工奶茶果汁','柠檬蜜',83,'18802099564','广东金融学院','10:30-13:30；16:30-19:00',4,2,3,null);
insert into restaurant_shop values(null,'南四皇叶子纯手工奶茶果汁','茉莉红茶',42,'18802099564','广东金融学院','10:30-13:30；16:30-19:00',4,2,4,null);
insert into restaurant_shop values(null,'南四皇叶子纯手工奶茶果汁','阿萨姆红茶',42,'18802099564','广东金融学院','10:30-13:30；16:30-19:00',4,2,5,null);
insert into restaurant_shop values(null,'南四皇叶子纯手工奶茶果汁','冻顶乌龙茶',42,'18802099564','广东金融学院','10:30-13:30；16:30-19:00',4,2,6,null);
insert into restaurant_shop values(null,'南四皇叶子纯手工奶茶果汁','四季春茶',52,'18802099564','广东金融学院','10:30-13:30；16:30-19:00',4,2,7,null);
insert into restaurant_shop values(null,'南四皇叶子纯手工奶茶果汁','波霸红/绿茶',52,'18802099564','广东金融学院','10:30-13:30；16:30-19:00',4,2,8,null);

insert into restaurant_shop values(null,'南四扒饭系列','黑椒鸡扒',105,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',4,3,9,null);
insert into restaurant_shop values(null,'南四扒饭系列','脆皮鸡扒',105,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',4,3,10,null);
insert into restaurant_shop values(null,'南四扒饭系列','奥尔良套餐',115,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',4,3,11,null);
insert into restaurant_shop values(null,'南四扒饭系列','黑椒意粉',105,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',4,3,12,null);

insert into restaurant_shop values(null,'南四小炒系列','美极茶树菇肉片',120,'13168998223','广东金融学院','10:30-13:30；16:30-19:00',4,4,13,null);
insert into restaurant_shop values(null,'南四小炒系列','豆角茄子',85,'13168998223','广东金融学院','10:30-13:30；16:30-19:00',4,4,14,null);
insert into restaurant_shop values(null,'南四小炒系列','宫保鸡丁',85,'13168998223','广东金融学院','10:30-13:30；16:30-19:00',4,4,15,null);
insert into restaurant_shop values(null,'南四小炒系列','尖椒回锅肉',85,'13168998223','广东金融学院','10:30-13:30；16:30-19:00',4,4,16,null);

insert into restaurant_shop values(null,'南五汤粉云吞','玉米云吞',85,'18650411488','广东金融学院','10:30-13:30；16:30-19:00',5,5,1,null);
insert into restaurant_shop values(null,'南五汤粉云吞','酸辣粉',85,'18650411488','广东金融学院','10:30-13:30；16:30-19:00',5,5,2,null);
insert into restaurant_shop values(null,'南五汤粉云吞','番茄炒蛋面',85,'18650411488','广东金融学院','10:30-13:30；16:30-19:00',5,5,3,null);
insert into restaurant_shop values(null,'南五汤粉云吞','瘦肉面',85,'18650411488','广东金融学院','10:30-13:30；16:30-19:00',5,5,4,null);

insert into restaurant_shop values(null,'北苑一楼广金美食','辣味炒饭',70,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',1,6,1,null);
insert into restaurant_shop values(null,'北苑一楼广金美食','扬州炒饭',65,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',1,6,2,null);
insert into restaurant_shop values(null,'北苑一楼广金美食','奥尔良炒饭',105,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',1,6,3,null);
insert into restaurant_shop values(null,'北苑一楼广金美食','咖喱炒饭',65,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',1,6,4,null);
insert into restaurant_shop values(null,'北苑一楼广金美食','番茄蛋炒饭',65,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',1,6,5,null);
insert into restaurant_shop values(null,'北苑一楼广金美食','紫菜蛋花汤',25,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',1,6,6,null);

insert into restaurant_shop values(null,'北苑一楼客家档','酸菜鱼+墨鱼花炒西兰花+青菜+米饭',100,'13822184773','广东金融学院','10:30-13:30；16:30-19:00',1,7,7,null);
insert into restaurant_shop values(null,'北苑一楼客家档','紫苏鸭+番茄炒蛋+青菜+米饭',105,'13822184773','广东金融学院','10:30-13:30；16:30-19:00',1,7,8,null);
insert into restaurant_shop values(null,'北苑一楼客家档','黑椒鸡排+肉饼+青菜+米饭',105,'13822184773','广东金融学院','10:30-13:30；16:30-19:00',1,7,9,null);
insert into restaurant_shop values(null,'北苑一楼客家档','辣子鸡+蒸粉丝+青菜+米饭',125,'13822184773','广东金融学院','10:30-13:30；16:30-19:00',1,7,10,null);
insert into restaurant_shop values(null,'北苑一楼客家档','盐焗琵琶腿+咖喱土豆鸡+青菜+米饭',105,'13822184773','广东金融学院','10:30-13:30；16:30-19:00',1,7,11,null);
insert into restaurant_shop values(null,'北苑一楼客家档','酸菜鱼+西兰花+大鸡腿+米饭',165,'13822184773','广东金融学院','10:30-13:30；16:30-19:00',1,7,12,null);

insert into restaurant_shop values(null,'北苑一楼烧腊','烧鸡腿+米饭',95,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',1,8,13,null);
insert into restaurant_shop values(null,'北苑一楼烧腊','烧鸭饭',100,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',1,8,14,null);
insert into restaurant_shop values(null,'北苑一楼烧腊','卤蛋',20,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',1,8,15,null);
insert into restaurant_shop values(null,'北苑一楼烧腊','切鸡+青菜+米饭',85,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',1,8,16,null);
insert into restaurant_shop values(null,'北苑一楼烧腊','油鸡+青菜+米饭',85,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',1,8,17,null);
insert into restaurant_shop values(null,'北苑一楼烧腊','叉烧+青菜+米饭',105,'17512803615','广东金融学院','10:30-13:30；16:30-19:00',1,8,18,null);

insert into restaurant_shop values(null,'北苑一楼肠粉','红米肠粉',54,'13816084241','广东金融学院','10:30-13:30；16:30-19:00',1,9,19,null);
insert into restaurant_shop values(null,'北苑一楼肠粉','叉烧肠粉',63,'13816084241','广东金融学院','10:30-13:30；16:30-19:00',1,9,20,null);
insert into restaurant_shop values(null,'北苑一楼肠粉','腊味肠粉',53,'13816084241','广东金融学院','10:30-13:30；16:30-19:00',1,9,21,null);
insert into restaurant_shop values(null,'北苑一楼肠粉','瘦肉鸡蛋肠粉',42,'13816084241','广东金融学院','10:30-13:30；16:30-19:00',1,9,22,null);
insert into restaurant_shop values(null,'北苑一楼肠粉','香菇瘦肉肠粉',42,'13816084241','广东金融学院','10:30-13:30；16:30-19:00',1,9,23,null);

insert into restaurant_shop values(null,'北苑一楼拉面','牛腩面',73,'13544392062','广东金融学院','10:30-13:30；16:30-19:00',1,10,24,null);
insert into restaurant_shop values(null,'北苑一楼拉面','肥肠面',73,'13544392062','广东金融学院','10:30-13:30；16:30-19:00',1,10,25,null);
insert into restaurant_shop values(null,'北苑一楼拉面','炸酱粉',73,'13544392062','广东金融学院','10:30-13:30；16:30-19:00',1,10,26,null);
insert into restaurant_shop values(null,'北苑一楼拉面','打卤面',73,'13544392062','广东金融学院','10:30-13:30；16:30-19:00',1,10,27,null);
insert into restaurant_shop values(null,'北苑一楼拉面','番茄拌面',73,'13544392062','广东金融学院','10:30-13:30；16:30-19:00',1,10,28,null);

insert into restaurant_shop values(null,'北苑二楼煲仔饭','双腊饭',125,'13544392062','广东金融学院','10:30-13:30；16:30-19:00',2,11,1,null);
insert into restaurant_shop values(null,'北苑二楼煲仔饭','瘦肉腊肠饭',125,'13544392062','广东金融学院','10:30-13:30；16:30-19:00',2,11,2,null);
insert into restaurant_shop values(null,'北苑二楼煲仔饭','鲜虾瘦肉饭',120,'13544392062','广东金融学院','10:30-13:30；16:30-19:00',2,11,3,null);
insert into restaurant_shop values(null,'北苑二楼煲仔饭','栗子鸡猪肉饭',105,'13544392062','广东金融学院','10:30-13:30；16:30-19:00',2,11,4,null);
insert into restaurant_shop values(null,'北苑二楼煲仔饭','香芋鱼腩饭',105,'13544392062','广东金融学院','10:30-13:30；16:30-19:00',2,11,5,null);

insert into restaurant_shop values(null,'北苑二楼西餐','黑椒鸡排',100,'13631432722','广东金融学院','10:30-13:30；16:30-19:00',2,12,6,null);
insert into restaurant_shop values(null,'北苑二楼西餐','卤水鸭',80,'13631432722','广东金融学院','10:30-13:30；16:30-19:00',2,12,7,null);
insert into restaurant_shop values(null,'北苑二楼西餐','吉列扒',90,'13631432722','广东金融学院','10:30-13:30；16:30-19:00',2,12,8,null);
insert into restaurant_shop values(null,'北苑二楼西餐','咖喱鸡饭',85,'13631432722','广东金融学院','10:30-13:30；16:30-19:00',2,12,9,null);
insert into restaurant_shop values(null,'北苑二楼西餐','可乐鸡饭',85,'13631432722','广东金融学院','10:30-13:30；16:30-19:00',2,12,10,null);

insert into restaurant_shop values(null,'北苑三楼大众自选','广式卤水鸭套餐+茄子豆角+蔬菜',120,'13298573440','广东金融学院','10:30-13:30；16:30-19:00',3,13,1,null);
insert into restaurant_shop values(null,'北苑三楼大众自选','排骨+茄子+蔬菜',120,'13298573440','广东金融学院','10:30-13:30；16:30-19:00',3,13,2,null);
insert into restaurant_shop values(null,'北苑三楼大众自选','葱油鸡+花菜+蔬菜',120,'13298573440','广东金融学院','10:30-13:30；16:30-19:00',3,13,3,null);
insert into restaurant_shop values(null,'北苑三楼大众自选','辣子鸡+苦瓜+蔬菜',120,'13298573440','广东金融学院','10:30-13:30；16:30-19:00',3,13,4,null);
