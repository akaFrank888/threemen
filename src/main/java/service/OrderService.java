package service;

import domain.ServiceType;
import domain.UserContact;
import domain.Order;

import java.util.List;

/**
 * @Auther: yxl15
 * @Date: 2020/10/19 07:37
 * @Description:
 */
public interface OrderService {

    // 保存订单信息，返回订单号
    boolean saveOrder(Order order, String serviceType, String preCommNum);

    // 通过订单号，显示 订单信息
    Order showOrder(String orderNumber);

    // 通过订单号，返回 下单时间
    String getDateTime(String commNum);

    // 连接 yuan和dong
    String join(String yuan, String dong);

    // 修改订单状态
    boolean alterOrderStatus(String commNum,int statusIndex);

    // 小哥接单（插入小哥的token）
    boolean workerPickOrder(String commNum,String token);
    // 用户评论
    boolean userComment(String commNum, String comment);

    // 通过订单号取得小哥的邮箱和手机号
    UserContact getContactWithWorker(String commNum);

    // 通过订单号取得用户的邮箱和手机号
    UserContact getContactWithUser(String commNum);

    // 为用户返回特定的订单
    List<Order> findSpecificOrder(int isUser, String status, String token, String serviceType);

    // 插入订单到达时间
    boolean insertReachTime(String commNum);
}