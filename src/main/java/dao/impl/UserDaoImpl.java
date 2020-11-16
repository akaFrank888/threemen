package dao.impl;

import dao.BaseDao;
import dao.UserDao;
import domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;
import util.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class UserDaoImpl extends BaseDao implements UserDao {


    // 用于登录

    @Override
    public User find(String account, String password) {


        return queryForOne(User.class, "select " +
                "user_account as account, user_password as password, user_nickname as nickname, user_email as email, user_key as keyCode" +
                " from user_register where user_account=? and user_password=?", account, password);


/*        User user = null;
        try {
            // 1. 创建QueryRunner对象
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            Connection connection = JDBCUtil.getConnection();
            // 3， 编写sql
            // 把字段名改成属性名，以实现WorkStudyInfo对象的封装
            String sql = "select " +
                    "user_account as account, user_password as password, user_nickname as nickname, user_email as email" +
                    " from user_register where user_account=? and user_password=?";
            // 4. 执行
            user = query.query(connection, sql, new BeanHandler<>(User.class), account, password);
            // 输出
            if (user != null) {
                System.out.println("该用户登录成功：" + account);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;*/
    }


    // 用于注册，且需要批处理
    // 所以用prepareStatement而不用QueryRunner

    @Override
    public boolean save(User user) {

        // 用Statement进行批处理，而不是PrepareStatement（扬长避短）

        boolean flag = false;
        Connection connection = null;
        Statement statement = null;

        try {
            // 1. 获得连接
            connection = JDBCUtil.getConnection();

            // JDBC事务：设置手动提交
            connection.setAutoCommit(false);

            // 2. 创建Statement
            statement = connection.createStatement();
            // 3. statement加入批处理
            // （注意：Statement传参的sql书写格式）
            statement.addBatch("insert into user_register values(null ,'" + user.getAccount() + "'" + ",'" + user.getPassword() + "'" + ",'" + user.getNickname() + "'" + ",'" + user.getEmail() + "'" + ",'" + user.getStatus() + "'" + ",'" + user.getKeyCode() + "'" + ")");
            statement.addBatch("insert into user_info(user_id,user_token) values(null,'" + user.getKeyCode() + "')");
            statement.addBatch("insert into user_self(user_id,user_token) values(null,'" + user.getKeyCode() + "')");

            // 4. statement执行批处理
            statement.executeBatch();

            // 提交事务
            connection.commit();

        } catch (SQLException e) {
            // 一旦其中一个操作出错，就回滚
            try {
                assert connection != null;
                flag = false;
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            JDBCUtil.close(connection, statement);
        }
        return flag;
       /* boolean flag = false;
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;

        try {
            // 1. 获得连接
            connection = JDBCUtil.getConnection();
            // 2. 定义sql
            String sql1 = "insert user_register values(?,?,?,?,?,?)";
            String sql2 = "insert user_Info values(null,?,?,null,?,null,null,null,null,null)";
            // 3. 获得prepareStatement
            preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement2 = connection.prepareStatement(sql2);
            // 4. 给第一个pre赋值
            preparedStatement1.setString(1, user.getAccount());
            preparedStatement1.setString(2, user.getPassword());
            preparedStatement1.setString(3, user.getNickname());
            preparedStatement1.setString(4, user.getEmail());
            preparedStatement1.setString(5, user.getStatus());
            preparedStatement1.setString(6, user.getKey());
            // 5. 加入批处理
            preparedStatement1.addBatch();
            // 4. 给第二个赋值
            preparedStatement2.setString(1, user.getAccount());
            preparedStatement2.setString(2, user.getEmail());
            preparedStatement2.setString(3, user.getNickname());
            // 5. 加入批处理
            preparedStatement2.addBatch();
            // 6. 最后：执行

        } catch (SQLException e) {
            e.printStackTrace();
        }*/


/*        boolean flag = false;
        try {
            // 1. 创建QueryRunner对象
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            Connection connection = JDBCUtil.getConnection();
            // 3. 定义sql
            String sql = "insert user_register values(?,?,?,?,?,?)";
            // 4. 执行
            int i = query.update(connection, sql, user.getAccount(), user.getPassword(),
                    user.getNickname(), user.getEmail(), user.getStatus(), user.getKey());
            // 5. 判断
            if (i > 0) {
                flag = true;
                System.out.println("账户为：" + user.getAccount() + "的一条记录插入成功！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;*/
    }

    @Override
    public boolean findByAccount(String account) {

        boolean flag = false;
        User user = this.queryForOne(User.class, "select user_account as account from user_register where user_account = ?", account);
        if (user != null) {
            flag = true;
        }
        return flag;

/*        boolean flag = false;
        try {
            // 1. 创建QueryRunner对象
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            Connection connection = JDBCUtil.getConnection();
            // 3. 定义sql
            String sql = "select user_account as account from user_register where user_account = ?";
            // 4. 执行
            User user = query.query(connection, sql, new BeanHandler<>(User.class), account);
            // 5. 判断
            if (user != null) {
                flag = true;
                System.out.println("存在账号为：" + account + "的用户");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;*/
    }

    // 根据key，寻找用户
    @Override
    public boolean findByKey(String key) {

        boolean flag = false;
        User user = this.queryForOne(User.class, "select user_account as account from user_register where user_key = ?", key);
        if (user != null) {
            flag = true;
        }
        return flag;

/*        boolean flag = false;

        try {
            // 1. 创建QueryRunner
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            Connection connection = JDBCUtil.getConnection();
            // 3. 定义sql
            String sql = "select user_account as account from user_register where user_keycode = ?";
            // 4. 执行
            User user = query.query(connection, sql, new BeanHandler<>(User.class), key);
            // 5. 判断
            if (user != null) {
                flag = true;
                System.out.println("存在激活码为为：" + key + "的用户");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;*/
    }

    // 修改status
    @Override
    public boolean updateStatus(String key) {

        boolean flag = false;
        int i = this.update("update user_register set user_status = ? where user_key = ?", "Y", key);
        if (i > 0) {
            flag = true;
        }
        return flag;

/*        try {
            // 1. 创建QueryRunner
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            Connection connection = JDBCUtil.getConnection();
            // 3. 定义sql
            String sql = "update user_register set user_status = ? where user_keycode = ?";
            // 4. 执行
            query.update(connection, sql, "Y", key);
            // 输出
            System.out.println("激活码为为：" + key + "的用户的status已经被修改为：Y");
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public String getKeyCodeByAccount(String account) {
        /*return this.queryForOne(String.class, "select user_key from user_register where user_account = ?", account);*/

        String token = null;
        Connection connection = null;
        try {
            // 1. 创建QueryRunner
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            connection = JDBCUtil.getConnection();

            // 设置手动提交事务
            connection.setAutoCommit(false);

            // 3. 定义sql
            String sql = "select user_key from user_register where user_account = ?";
            // 4. 执行
            // 注意：如何通过QueryRunner的ScalarHandler获取String类型的数据！！！
            token = (query.query(connection, sql, new ScalarHandler<>(), account)).toString();
            // 5. 判断
            if (token != null) {
                System.out.println("token为：" + token);
            }

            // 提交事务
            connection.commit();

        } catch (SQLException e) {
            // 一旦其中一个操作出错，就回滚
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return token;
    }

    @Override
    public User getUserByToken(String token) {
        return this.queryForOne(User.class, "select user_account as account, " +
                "user_nickname as nickname, " +
                "user_email as email, " +
                "user_key as keycode " +
                "from user_register " +
                "where user_key=?", token);
    }


    @Override
    public boolean findByEmail(String email) {

        boolean flag = false;

        User user = queryForOne(User.class, "select user_account as account from user_register where user_email = ?", email);
        if (user != null) {
            flag = true;
        }
        return flag;





/*        boolean flag = false;

        try {
            // 1. 创建QueryRunner
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            Connection connection = JDBCUtil.getConnection();
            // 3. 定义sql
            String sql = "select user_account as account from user_register where user_email = ?";
            // 4. 执行
            User user = query.query(connection, sql, new BeanHandler<>(User.class), email);
            // 5. 判断
            if (user != null) {
                flag = true;
                System.out.println("存在邮箱为：" + email + "的用户");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;*/
    }

    @Test
    public void test() {
        User user = this.getUserByToken("438");
        System.out.println(user);
    }
}
