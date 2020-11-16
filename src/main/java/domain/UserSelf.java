package domain;

import util.annontation.DBField;
import util.annontation.DBTable;

/**
 * @Auther: yxl15
 * @Date: 2020/10/20 08:41
 * @Description:
 */

@DBTable(tableName = "user_self")

public class UserSelf {

    // 最少要体现数量为100的金子，按10：1进行提现
    public static final int MINI_WITHDRAW_NUM = 100;

    // 高级用户享受九折优惠
    public static final double HIGHER_PRIORITY = 0.9;

    // 小哥赚取订单金子20个中的八成
    public static final double WORKER_EARN = 0.8;

    // 根据经验划分三个等级
    public static final int EXPERIENCE_FIRST = 30;
    public static final int EXPERIENCE_SECOND = 100;

    // 用户等级
    @DBField("level_id")
    private int level;

    // 用户经验
    @DBField("user_experience")
    private int experience;

    // 金子余额
    @DBField("user_balance")
    private int balance;

    // 订单数
    @DBField("user_order_count")
    private int orderCount;

    // token
    @DBField("user_token")
    private String token;

    public UserSelf() {
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserSelf{" +
                "level=" + level +
                ", experience=" + experience +
                ", balance=" + balance +
                ", orderCount=" + orderCount +
                ", token='" + token + '\'' +
                '}';
    }
}
