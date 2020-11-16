package dao.impl;

import dao.BaseDao;
import dao.UserSelfDao;
import domain.Level;
import domain.UserSelf;
import org.junit.Test;

/**
 * @Auther: yxl15
 * @Date: 2020/10/20 23:53
 * @Description:
 */
public class UserSelfDaoImpl extends BaseDao implements UserSelfDao {

    @Override
    public int getBalanceByToken(String token) {

        return (int) this.queryForSingleValue("select user_balance from user_self where user_token=?", token);
    }

    // 小哥赚钱，并增加经验！！！！！！！！！
    @Override
    public boolean increaseCoinAndExp(String token, double earnCoin) {

        boolean flag = false;
        int i = this.update("update user_self set user_balance=user_balance+?,user_experience=user_experience+10 where user_token=?", earnCoin, token);
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    // 用户增加单数和经验
    @Override
    public boolean increaseCountAndExp(String token) {
        boolean flag = false;
        int i = this.update("update user_self set user_order_count=user_order_count+1,user_experience=user_experience+5 where user_token=?", token);
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public int getLevelByToken(String token) {
        return (int) this.queryForSingleValue("select level_id from user_self where user_token=?", token);
    }

    @Override
    public String getLevelStrByToken(String token) {
        String sql = "select l.level from user_level l inner join user_self s on l.id=s.level_id where s.user_token=?";

        System.out.println("这是我要测试的sql："+sql);
        return this.queryForSingleValue(sql, token).toString();
    }

    @Override
    public boolean increaseCoin(int rechargeNum, String token) {
        boolean b = false;
        int i = this.update("update user_self set user_balance=user_balance+? where user_token=?", rechargeNum, token);
        if (i > 0) {
            b = true;
        }
        return b;
    }

    @Override
    public UserSelf showUserSelf(String token) {
        String sql = "select level_id as level, " +
                "user_experience as experience, " +
                "user_balance as balance, " +
                "user_order_count as orderCount " +
                "from user_self " +
                "where user_token=?";
        return this.queryForOne(UserSelf.class, sql, token);
    }

    // 用户消费/提现都可以用这个方法
    @Override
    public boolean reduceCoin(String token, double totalCost) {

        boolean flag = false;
        int i = this.update("update user_self set user_balance=user_balance-? where user_token=?", totalCost, token);
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean updateGrade(String token) {

        boolean flag = false;

        // 先查询该用户的积分
        int experience = (int)this.queryForSingleValue("select user_experience from user_self where user_token=?", token);
        // 再根据积分划分等级
        Level level;
        int finalLevel;
        if (experience < UserSelf.EXPERIENCE_FIRST) {
            // 低级用户
            level = Level.valueOf("lower");
            finalLevel = level.getIndex();
        } else if (experience <= UserSelf.EXPERIENCE_SECOND) {
            // 高级用户
            level = Level.valueOf("higher");
            finalLevel = level.getIndex();
        } else {
            // 特级用户
            level = Level.valueOf("special");
            finalLevel = level.getIndex();
        }
        // 更新等级
        int i = this.update("update user_self set level_id=? where user_token=?", finalLevel, token);
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    @Test
    public void test() {
        boolean b = this.updateGrade("438");
        if (b) {
            System.out.println("成功");
        }
    }
}
