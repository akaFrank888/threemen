package service;

import domain.User;

/**

        用户的登录、注册、退出

 */

public interface UserService {

    // 登录
    User login(User user);

    // 登录时需要用通过account寻找keyCode
    String getKeyCodeByAccount(String account);

    // 注册
    String register(User user);

    // 用account查找
    boolean findByAccount(String account);

    // 用email查找
    boolean findByEmail(String email);

    // 激活用户邮箱
    boolean active(String key);

    // 通过token获得user对象
    User getUserByToken(String token);


}
