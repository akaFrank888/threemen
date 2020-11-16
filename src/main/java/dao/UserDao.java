package dao;

import domain.User;

/**

        用户的登录、注册、退出

 */
public interface UserDao {

    // 登录
    User find(String account, String password);

    // 注册
    boolean save(User user);

    // 用account查找
    boolean findByAccount(String account);

    // 用email查找
    boolean findByEmail(String email);

    boolean findByKey(String key);

    boolean updateStatus(String key);

    String getKeyCodeByAccount(String account);

    User getUserByToken(String token);
}
