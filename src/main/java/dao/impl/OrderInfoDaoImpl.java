package dao.impl;

import dao.BaseDao;
import dao.OrderInfoDao;
import domain.ServiceType;
import domain.Status;
import domain.Order;

import java.util.List;

/**
 * @Auther: yxl15
 * @Date: 2020/10/21 10:36
 * @Description:
 */
public class OrderInfoDaoImpl extends BaseDao implements OrderInfoDao {

    @Override
    public List<Order> showOnePageOrder(int beginIndex, int pageSize, String serviceType, String status) {

        List<Order> list;

        list = this.queryForList(Order.class, "select " +
                        "comm_cost_real as commCostReal, " +
                        "comm_address as commAddress, " +
                        "user_address as address, " +
                        "comm_num as commNum " +
                        "from order_commodity " +
                        "where service_id=? and status_id=? " +
                        "limit ?,? "
                , ServiceType.valueOf(serviceType).getIndex(),
                Status.valueOf(status).getIndex(), beginIndex, pageSize);

        // 输出
        System.out.println("该页的订单的条数为：" + list.size());

        return list;
    }

    @Override
    public int getTotalCount(String serviceType,String status) {

        return ((Long) this.queryForSingleValue("select count(*) from order_commodity where service_id=? and status_id=?",
                ServiceType.valueOf(serviceType).getIndex(),
                Status.valueOf(status).getIndex())).intValue();

    }
}
