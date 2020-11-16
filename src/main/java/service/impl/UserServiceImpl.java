package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import domain.User;
import service.UserService;
import util.utils.MailUtil;
import util.utils.UuidUtil;

public class UserServiceImpl implements UserService {


    UserDao dao = new UserDaoImpl();

    @Override
    public User login(User user) {
        return dao.find(user.getAccount(), user.getPassword());
    }

    @Override
    public String getKeyCodeByAccount(String account) {
        return dao.getKeyCodeByAccount(account);
    }

    @Override
    public String register(User user) {
        String result = "注册成功";
        System.out.println("这是email："+user.getEmail());

        // 校验敏感字符
        if (this.findByAccount(user.getAccount())) {
            // 如果存在该用户，则返回false
            return "该账户已存在";
        }
        if (this.findByEmail(user.getEmail())) {
            // 如果该邮箱已被用于注册，则返回false
            return "该邮箱已被用于注册";
        }

        // 如果是新用户，则可以注册，但先要用邮箱激活（确保邮箱可用）
        // 1. 设置激活码和激活状态
        user.setKey(UuidUtil.getUuid());
        user.setStatus("N");

        // 2. 发送激活邮件
        // ActiveUserServlet中会get此key来比较已经存入的key来寻找该用户，并将其status改为"Y"
        String content = "<a href='http://localhost:8080/activeUserServlet?key=" + user.getKeyCode()+"'>点击激活 金子平台 </a>";


        if (!MailUtil.sendMail(user.getEmail(), content, "激活邮件")) {

            result = "授权码错误";
        }

        // 3. 最后没问题，保存进数据库
        dao.save(user);
        return result;
    }

    @Override
    public boolean findByAccount(String account) {
        return dao.findByAccount(account);
    }

    @Override
    public boolean findByEmail(String email) {
        return dao.findByEmail(email);
    }

    @Override
    public boolean active(String key) {
        // 1. 根据key找到用户
        boolean flag = dao.findByKey(key);
        if (flag) {
            // 2. 调用dao的修改status的方法
            dao.updateStatus(key);
            return true;
        }
        return false;
    }

    @Override
    public User getUserByToken(String token) {
        return dao.getUserByToken(token);
    }

}
