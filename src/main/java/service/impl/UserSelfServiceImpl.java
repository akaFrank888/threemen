package service.impl;

import dao.UserSelfDao;
import dao.impl.UserSelfDaoImpl;
import domain.Level;
import domain.UserSelf;
import service.UserSelfService;

/**
 * @Auther: yxl15
 * @Date: 2020/10/20 23:51
 * @Description:
 */
public class UserSelfServiceImpl implements UserSelfService {

    UserSelfDao dao = new UserSelfDaoImpl();

    @Override
    public int getBalanceByToken(String token) {
        return dao.getBalanceByToken(token);
    }

    // 需要判断 用户的等级，然后进行分类
    @Override
    public boolean payOrder(String token, int commCostCoin,int commCostReward) {
        double totalCost = 0;
        // 先通过连表查询，获得该用户的 等级（用单词表示）
        String levelStr = getLevelStrByToken(token);
        Level level = Level.valueOf(levelStr);
        switch (level) {
            case lower:
                // 普通用户，原价支付
                totalCost = commCostCoin + commCostReward;
                break;
            case higher:
                // 高级用户，享受九折
            case special:
                // 特级用户：享受九折，并可提现
                totalCost = commCostCoin * UserSelf.HIGHER_PRIORITY;
                break;
        }
        return dao.reduceCoin(token, totalCost);
    }

    @Override
    public boolean earnAndAccumulate(String token, double earnCoin) {
        boolean b = dao.increaseCoinAndExp(token, earnCoin);
        boolean b1 = dao.updateGrade(token);
        return b && b1;
    }

    // 用户增加单数和经验
    @Override
    public boolean increaseCountAndAccumulateExp(String token) {
        boolean b = dao.increaseCountAndExp(token);
        boolean b1 = dao.updateGrade(token);
        return b && b1;
    }

    // 获得用户等级（索引）
    @Override
    public int getLevelByToken(String token) {
        return dao.getLevelByToken(token);
    }

    @Override
    public String getLevelStrByToken(String token) {
        return dao.getLevelStrByToken(token);
    }

    @Override
    public boolean withdraw(int coinNum, String token) {
        return dao.reduceCoin(token, coinNum);
    }

    @Override
    public boolean recharge(int rechargeNum, String token) {
        return dao.increaseCoin(rechargeNum, token);
    }

    @Override
    public UserSelf showUserSelf(String token) {
        return dao.showUserSelf(token);
    }
}
