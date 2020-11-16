package service.impl;

import dao.OrderInfoDao;
import dao.impl.OrderInfoDaoImpl;
import domain.Order;
import domain.PageBean;
import service.OrderInfoService;

/**
 * @Auther: yxl15
 * @Date: 2020/10/21 10:33
 * @Description:
 */
public class OrderInfoServiceImpl implements OrderInfoService {

    private OrderInfoDao dao = new OrderInfoDaoImpl();

    @Override
    public PageBean<Order> showOnePageOrder(int currentPage, int pageSize, String serviceType, String status) {

        // 封装一个PageBean，然后返回
        PageBean<Order> pb = new PageBean<>();

        // 设置pb的5个属性
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        // 得到 该服务类型该订单状态下的订单总数
        int totalCount = dao.getTotalCount(serviceType, status);
        System.out.println("这是totalCount = " + totalCount);
        pb.setTotalCount(totalCount);

        // 公式： beginIndex = （当前页-1）*每页条数
        int beginIndex = (currentPage - 1) * pageSize;
        pb.setList(dao.showOnePageOrder(beginIndex, pageSize, serviceType, status));

        // 巧用条件运算符   --->    ? :
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);
        System.out.println("这是totalPage = " + totalPage);

        return pb;
    }
}
