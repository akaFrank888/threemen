package service.impl;

import dao.OrderDao;
import dao.impl.OrderDaoImpl;
import domain.RestaurantInfo;
import domain.ServiceType;
import domain.UserContact;
import domain.Order;
import service.OrderService;
import service.RestaurantInfoService;
import util.utils.OrderRedisUtil;
import util.utils.UuidUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: yxl15
 * @Date: 2020/10/19 08:00
 * @Description:
 */
public class OrderServiceImpl implements OrderService {

    private OrderDao dao = new OrderDaoImpl();

    // 保存一个订单，并返回订单编号
    @Override
    public boolean saveOrder(Order order, String serviceType, String preCommNum) {

        boolean b = false;

        order.setCommNum(preCommNum);
        if (dao.saveOrder(order)) {
            // 保存成功
            b = true;
        }
        return b;
    }

    @Override
    public Order showOrder(String commNum) {
        return dao.findOrderByOrderNumber(commNum);
    }

    @Override
    public String getDateTime(String commNum) {
        return dao.getDateTime(commNum);
    }

    @Override
    public String join(String yuan, String dong) {
        StringBuilder sb = new StringBuilder(yuan);
        sb.append("#");
        sb.append(dong);
        return sb.toString();
    }

    @Override
    public boolean alterOrderStatus(String commNum,int statusIndex) {
        return dao.alterOrderStatus(commNum,statusIndex);
    }

    @Override
    public boolean workerPickOrder(String commNum,String token) {
        return dao.insertWorkerToken(commNum, token);
    }

    @Override
    public boolean userComment(String commNum, String comment) {
        return dao.insertComment(commNum, comment);
    }


    @Override
    public UserContact getContactWithWorker(String commNum) {
        return dao.getContactWithWorker(commNum);
    }

    @Override
    public UserContact getContactWithUser(String commNum) {
        return dao.getContactWithUser(commNum);
    }

    @Override
    public List<Order> findSpecificOrder(int isUser, String status, String token, String serviceType) {
        return dao.findOrderByStatus(isUser, status, token, serviceType);
    }

    @Override
    public boolean insertReachTime(String commNum) {
        return dao.insertReachTime(commNum);
    }
}
