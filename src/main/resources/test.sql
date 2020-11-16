use jdbc;
CREATE TABLE `book`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `bookname`    varchar(50)    DEFAULT NULL,
    `price`       decimal(50, 0) DEFAULT NULL,
    `author`      varchar(50)    DEFAULT NULL,
    `typeid`      int(25)        DEFAULT NULL,
    `picturePath` varchar(50)    DEFAULT NULL,
    `content`     varchar(255)   DEFAULT NULL,
    `standby1`    varchar(255)   DEFAULT NULL,
    `standby2`    varchar(255)   DEFAULT NULL,
    `inventory`   int(25)        DEFAULT NULL,
    `saleCount`   int(25)        DEFAULT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO `book` VALUES ('8', '巴黎圣母院', '450', '维克多雨果', '1', 'test.png', '这就是一个测试', '2019-05-24', '1-1', '500', '201');
INSERT INTO `book` VALUES ('9', '测试', '12', '测试内容1', '1', 'rec.jpg', '这里是显示测试', '2019-05-20', '1-2', '520', '23');
INSERT INTO `book` VALUES ('10', '测试1', '13', '测试内容1', '3', 'rec.jpg', '看看决胜巅峰来表示', '2019-05-20', '1-2', '45', '12');
INSERT INTO `book` VALUES ('11', '测试2', '45', '测试内容2', '4', 'rec.jpg', '阿斯顿发世纪东方', '2019-05-20', '1-3', '78', '23');
INSERT INTO `book` VALUES ('12', '测试3', '78', '测试内容3', '5', 'rec.jpg', '发大家好轧空淡饭黄齑', '2019-05-21', '1-4', '457', '23');
INSERT INTO `book` VALUES ('14', 'mysql数据库', '99', '张三', '14', 'rec.jpg', 'mysql数据库作为一个关系型数据库', '2019-05-21', '1-c-2019', '300', '120');
INSERT INTO `book` VALUES ('16', 'C语言从入门到精通', '788', '测试', '1', 'rec.jpg', '456465465', '2019-05-24', '1-1', '500', '100');