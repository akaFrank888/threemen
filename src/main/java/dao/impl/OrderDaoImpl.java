package dao.impl;

import dao.BaseDao;
import dao.OrderDao;
import domain.ServiceType;
import domain.UserContact;
import domain.Order;
import domain.Status;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import static java.time.LocalDateTime.now;

/**
 * @Auther: yxl15
 * @Date: 2020/10/19 08:04
 * @Description:
 *
 */
public class OrderDaoImpl extends BaseDao implements OrderDao {


    // 利用 now() 函数，插入当前时间
    @Override
    public boolean saveOrder(Order order) {
        boolean b = false;
        int i = this.update("insert into order_commodity values(null,?,?,?,?,?,?,?,?,?,?,?,?)",
                order.getAddress(), order.getCommInfo(), order.getCommAddress(), order.getCommCostReal(), order.getCommCostCoin(), order.getCommReward(),
                order.getCommLeftMessage(), now(), Status.unpaid.getIndex(), order.getCommNum(), order.getUserToken(), ServiceType.valueOf(order.getServiceType()).getIndex());

        if (i > 0) {
            b = true;
        }
        return b;
    }

    @Override
    public Order findOrderByOrderNumber(String commNum) {
        Order order = null;
        order = this.queryForOne(Order.class, "select " +
                "user_address as address, " +
                "comm_info as commInfo, " +
                "comm_address as commAddress, " +
                "comm_cost_real as commCostReal, " +
                "comm_cost_coin as commCostCoin, " +
                "comm_reward as commReward, " +
                "comm_left_message as commLeftMessage, " +
                "comm_date as date " +
                "from order_commodity where comm_num=?", commNum);

        // 再用内连接（连表查询）得到Status的value
        String status = (this.queryForSingleValue("select " +
                "s.status " +
                "from order_status s inner join order_commodity c " +
                "on c.status_id=s.id " +
                "where c.comm_num=?", commNum)).toString();
        System.out.println("这是该订单的状态：   "+status);
        // 同样用内连接（连表查询）得到ServiceType的value
        String serviceType = (this.queryForSingleValue("select " +
                "t.type " +
                "from service_type t inner join order_commodity c " +
                "on t.id=c.service_id " +
                "where c.comm_num=?", commNum)).toString();
        System.out.println("这是该订单的服务类型：   " + serviceType);

        order.setStatus(status);
        order.setServiceType(serviceType);

        return order;
    }

    @Override
    public boolean alterOrderStatus(String commNum,int statusIndex) {
        boolean b = false;
        int i = this.update("update order_commodity set status_id=? where comm_num=?", statusIndex, commNum);
        if (i > 0) {
            b = true;
        }
        return b;
    }

    @Override
    public boolean insertWorkerToken(String commNum,String workerToken) {
        boolean b = false;
        String sql = "insert into order_worker values(null,?,?,?,null,null)";
        int i = this.update(sql, commNum, workerToken, now());
        if (i > 0) {
            b = true;
        }
        return b;
    }

    @Override
    public boolean insertComment(String commNum, String comment) {
        boolean b = false;
        String sql = "update order_worker set order_comment=? where comm_num=?";
        int i = this.update(sql, comment, commNum);
        if (i > 0) {
            b = true;
        }
        return b;
    }

    @Override
    public String getDateTime(String commNum) {

        // mysql中的dateTime 对应 Java中的TimeStamp

        Timestamp timeStamp = (Timestamp) this.queryForSingleValue("select comm_date from order_commodity where comm_num=?", commNum);

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeStamp);
    }

    // 查询 邮箱、手机号
    // 思路：用内连接，条件是 order_commodity和user_info两个表中的小哥的token相等，连接起前者的任意一个后者没有的字段（既实现了连接，又为后面封装做铺垫，因为故意封装不上）与后者的真实姓名、邮箱、手机号
    @Override
    public UserContact getContactWithWorker(String commNum) {


        String sql = "select r.user_email as email,u.user_phone as phone,r.user_nickname as nickname  " +
                "from order_commodity o inner " +

                "join user_info u " +
                "on o.worker_token=u.user_token " +
                "join user_register r " +
                "on o.worker_token=r.user_key " +
                "where o.comm_num=?";

        // 内连接查询（连表查询）
        return this.queryForOne(UserContact.class, sql, commNum);
    }

    @Override
    public UserContact getContactWithUser(String commNum) {
        String sql = "select r.user_email as email,u.user_phone as phone,r.user_nickname as nickname " +
                "from order_commodity o inner " +

                "join user_info u " +
                "on o.user_token=u.user_token " +
                "join user_register r " +
                "on o.user_token=r.user_key " +
                "where o.comm_num=?";
        // 内连接查询（连表查询）
        return this.queryForOne(UserContact.class, sql, commNum);
    }

    @Override
    public List<Order> findOrderByStatus(int isUser, String status, String token, String serviceType) {

        String sql;

        if (isUser == 1) {
            // 用户
            if (status.equals("0")) {
                // 查询所有状态的订单
                sql = "select " +
                        "comm_cost_real as commCostReal, " +
                        "comm_info as commInfo, " +
                        "comm_cost_coin as commCostCoin, " +
                        "comm_date as date, " +
                        "status_id as status, " +
                        "user_address as address, " +
                        "comm_address as commAddress, " +
                        "comm_num as commNum " +
                        "from order_commodity " +
                        "where user_token=? and service_id=?";
                return this.queryForList(Order.class, sql, token, ServiceType.valueOf(serviceType).getIndex());
            } else {
                // 查询特定状态的订单
                sql = "select " +
                        "comm_cost_real as commCostReal, " +
                        "comm_info as commInfo, " +
                        "comm_cost_coin as commCostCoin, " +
                        "comm_date as date, " +
                        "status_id as status, " +
                        "user_address as address, " +
                        "comm_address as commAddress, " +
                        "comm_num as commNum " +
                        "from order_commodity " +
                        "where status_id=? and user_token=? and service_id=?";
                return this.queryForList(Order.class, sql, Status.valueOf(status).getIndex(), token, ServiceType.valueOf(serviceType).getIndex());
            }

        } else {
            // 小哥
            if (status.equals("0")) {
                // 查询所有状态的订单
                sql = "select " +
                        "o.comm_cost_real as commCostReal, " +
                        "o.comm_info as commInfo, " +
                        "o.comm_cost_coin as commCostCoin, " +
                        "o.comm_date as date, " +
                        "o.comm_left_message as commLeftMessage, " +
                        "o.status_id as status, " +
                        "o.user_address as address, " +
                        "o.comm_address as commAddress, " +
                        "o.comm_num as commNum, " +
                        "p.pick_time as pickTime, " +
                        "p.order_comment as comment "+
                        "from order_commodity o " +
                        "inner join order_worker p " +
                        "where p.worker_token=? and o.service_id=?";
                return this.queryForList(Order.class, sql, token,ServiceType.valueOf(serviceType).getIndex());
            } else {
                // 查询特定状态的订单
                sql = "select " +
                        "o.comm_cost_real as commCostReal, " +
                        "o.comm_info as commInfo, " +
                        "o.comm_cost_coin as commCostCoin, " +
                        "o.comm_date as date, " +
                        "o.comm_left_message as commLeftMessage, " +
                        "o.status_id as status, " +
                        "o.user_address as address, " +
                        "o.comm_address as commAddress, " +
                        "o.comm_num as commNum, " +
                        "p.pick_time as pickTime, " +
                        "p.order_comment as comment "+
                        "from order_commodity o " +
                        "inner join order_worker p " +
                        "where o.status_id=? and p.worker_token=? and o.service_id=?";
                return this.queryForList(Order.class, sql, Status.valueOf(status), token, ServiceType.valueOf(serviceType).getIndex());
            }

        }

    }

    @Override
    public boolean insertReachTime(String commNum) {
        boolean b = false;
        String sql = "update order_worker set reach_time=? where comm_num=?";
        int i = this.update(sql, now(), commNum);
        if (i > 0) {
            b = true;
        }

        return b;
    }


    @Test
    public void test() {

    }
}
