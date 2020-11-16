package dao;

import domain.Order;

import java.util.List;

/**
 * @Auther: yxl15
 * @Date: 2020/10/21 10:34
 * @Description:
 */
public interface OrderInfoDao {

    // 显示一页的所有订单
    List<Order> showOnePageOrder(int beginIndex, int pageSize, String serviceType, String status);

    // 设计一个方法   得到总记录数
    int getTotalCount(String serviceType, String status);


}