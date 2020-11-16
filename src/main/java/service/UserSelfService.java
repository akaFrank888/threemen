package service;

import domain.UserSelf;

/**
 * @Auther: yxl15
 * @Date: 2020/10/20 23:50
 * @Description:
 */
public interface UserSelfService {


    // 通过token，获得余额
    int getBalanceByToken(String token);

    // 用户支付订单
    boolean payOrder(String token, int commCostCoin,int commCostReward);

    // 小哥赚金子和经验
    boolean earnAndAccumulate(String token, double earnCoin);

    // 用户增加单数和经验
    boolean increaseCountAndAccumulateExp(String token);

    // 获取用户等级（索引）
    int getLevelByToken(String token);

    // 获取用户等级（非索引）
    String getLevelStrByToken(String token);

    // 特级用户的提现
    boolean withdraw(int coinNum, String token);

    // 用户/小哥的充值
    boolean recharge(int rechargeNum, String token);

    // 查询用户的个人纪录
    UserSelf showUserSelf(String token);
}