package dao;

import domain.UserContact;
import domain.Order;

import java.util.List;

/**
 * @Auther: yxl15
 * @Date: 2020/10/19 08:01
 * @Description:
 */
public interface OrderDao {


    // 保存订单信息，返回订单号
    boolean saveOrder(Order order);

    // 通过 订单号 查找 order
    Order findOrderByOrderNumber(String orderNumber);

    boolean alterOrderStatus(String commNum,int statusIndex);

    boolean insertWorkerToken(String commNum,String token);
    boolean insertComment(String commNum, String comment);

    String getDateTime(String commNum);

    UserContact getContactWithWorker(String commNum);

    UserContact getContactWithUser(String commNum);

    List<Order> findOrderByStatus(int isUser, String status, String token, String serviceType);

    boolean insertReachTime(String commNum);

}
