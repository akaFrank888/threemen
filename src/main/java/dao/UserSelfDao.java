package dao;

import domain.UserSelf;

/**
 * @Auther: yxl15
 * @Date: 2020/10/20 23:52
 * @Description:
 */
public interface UserSelfDao {

    // 通过token，获得余额
    int getBalanceByToken(String token);

    boolean reduceCoin(String token, double commCostCoin);

    // 经验对等级的更新
    boolean updateGrade(String token);
    // 小哥赚取金子和经验
    boolean increaseCoinAndExp(String token, double earnCoin);
    // 用户增加单数和经验
    boolean increaseCountAndExp(String token);

    // 获取用户等级（索引）
    int getLevelByToken(String token);
    // 获取用户等级（非索引）
    String getLevelStrByToken(String token);

    // 用户/小哥充值金子
    boolean increaseCoin(int rechargeNum, String token);
    // 查询用户的个人纪录
    UserSelf showUserSelf(String token);
}
