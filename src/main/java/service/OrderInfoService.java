package service;

import domain.Order;
import domain.PageBean;

/**
 * @Auther: yxl15
 * @Date: 2020/10/21 10:32
 * @Description:
 */
public interface OrderInfoService {

    // 显示每页所有的订单
    PageBean<Order> showOnePageOrder(int currentPage, int pageSize, String serviceType, String status);

}
