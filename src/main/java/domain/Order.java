package domain;

import util.annontation.DBField;
import util.annontation.DBTable;

import java.io.Serializable;

/**
 * @Auther: yxl15
 * @Date: 2020/10/19 07:39
 * @Description:
 */


@DBTable(tableName = "order_commodity,order_worker")



public class Order implements Serializable {


    private static final long serialVersionUID = 4761911546687663818L;

    // 收货地址
    @DBField("user_address")
    private String address;

    // 超市代购：商品信息  餐厅代购：null    快递代拿：取件码
    @DBField("comm_info")
    private String commInfo;

    // 代买地址
    @DBField("comm_address")
    private String commAddress;
    // 预估价格
    @DBField("comm_cost_real")
    private double commCostReal;
    // 所需金子
    @DBField("comm_cost_coin")
    private int commCostCoin;
    // 打赏
    @DBField("comm_reward")
    private int commReward;
    // 留言
    @DBField("comm_left_message")
    private String commLeftMessage;
    // 下单时间
    @DBField("comm_date")
    private String date;
    // 订单号
    @DBField("comm_num")
    private String commNum;
    // 用户的token
    @DBField("user_token")
    private String userToken;
    // 小哥的token
    @DBField("worker_token")
    private String workerToken;
    // 小哥的接单时间
    @DBField("pick_time")
    private String pickTime;
    // 用户对小哥的评价
    @DBField("order_comment")
    private String comment;

    // 订单状态     但是JavaBean中是String类型
    @DBField("status_id")
    private String status;
    // 服务类型的索引      但是JavaBean中是String类型
    @DBField("service_id")
    private String serviceType;

    // 小哥的接单时间
    @DBField("reach_time")
    private String reachTime;

    public Order() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommInfo() {
        return commInfo;
    }

    public void setCommInfo(String commInfo) {
        this.commInfo = commInfo;
    }

    public String getCommAddress() {
        return commAddress;
    }

    public void setCommAddress(String commAddress) {
        this.commAddress = commAddress;
    }

    public double getCommCostReal() {
        return commCostReal;
    }

    public void setCommCostReal(double commCostReal) {
        this.commCostReal = commCostReal;
    }

    public int getCommCostCoin() {
        return commCostCoin;
    }

    public void setCommCostCoin(int commCostCoin) {
        this.commCostCoin = commCostCoin;
    }

    public int getCommReward() {
        return commReward;
    }

    public void setCommReward(int commReward) {
        this.commReward = commReward;
    }

    public String getCommLeftMessage() {
        return commLeftMessage;
    }

    public void setCommLeftMessage(String commLeftMessage) {
        this.commLeftMessage = commLeftMessage;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCommNum() {
        return commNum;
    }

    public void setCommNum(String commNum) {
        this.commNum = commNum;
    }

    public String getWorkerToken() {
        return workerToken;
    }

    public void setWorkerToken(String workerToken) {
        this.workerToken = workerToken;
    }

    public String getReachTime() {
        return reachTime;
    }

    public void setReachTime(String reachTime) {
        this.reachTime = reachTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPickTime() {
        return pickTime;
    }

    public void setPickTime(String pickTime) {
        this.pickTime = pickTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }


    @Override
    public String toString() {
        return "Order{" +
                "address='" + address + '\'' +
                ", commInfo='" + commInfo + '\'' +
                ", commAddress='" + commAddress + '\'' +
                ", commCostReal=" + commCostReal +
                ", commCostCoin=" + commCostCoin +
                ", commReward=" + commReward +
                ", commLeftMessage='" + commLeftMessage + '\'' +
                ", date='" + date + '\'' +
                ", commNum='" + commNum + '\'' +
                ", userToken='" + userToken + '\'' +
                ", workerToken='" + workerToken + '\'' +
                ", pickTime='" + pickTime + '\'' +
                ", comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", reachTime='" + reachTime + '\'' +
                '}';
    }
}
