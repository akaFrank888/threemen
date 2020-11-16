package dao;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import util.utils.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class BaseDao {

    //使用DbUtils操作数据库
    private QueryRunner queryRunner = new QueryRunner();

    // 返回影响的行数
    public int update(String sql, Object... args) {
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();
            // （1）因为JDBC事务默认是开启的，所以要设置手动开启事务
            connection.setAutoCommit(false);
            int i = queryRunner.update(connection, sql, args);

            // （2）手动提交事务
            connection.commit();

            return i;

        } catch (SQLException e) {
            // （3）一旦其中一个操作出错，就回滚
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            JDBCUtil.close(connection);
        }
        return -1;
    }


    public <T> T queryForOne(Class<T> type, String sql, Object... args) {
        Connection con = null;
        try {
            con = JDBCUtil.getConnection();

            // （1）因为JDBC事务默认是开启的，所以要设置手动开启事务
            con.setAutoCommit(false);

            T t = queryRunner.query(con, sql, new BeanHandler<T>(type), args);

            // （2）手动提交事务
            con.commit();

            return t;
        } catch (SQLException e) {
            // （3）一旦其中一个操作出错，就回滚
            try {
                assert con != null;
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            JDBCUtil.close(con);
        }
        return null;
    }


    public <T> List<T> queryForList(Class<T> type, String sql, Object... args) {
        Connection con = null;
        try {
            con = JDBCUtil.getConnection();

            // （1）因为JDBC事务默认是开启的，所以要设置手动开启事务
            con.setAutoCommit(false);

            List<T> query = queryRunner.query(con, sql, new BeanListHandler<T>(type), args);

            // （2）手动提交事务
            con.commit();

            return query;
        } catch (SQLException e) {
            // （3）一旦其中一个操作出错，就回滚
            try {
                assert con != null;
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            JDBCUtil.close(con);
        }
        return null;
    }


    public Object queryForSingleValue(String sql, Object... args){

        Connection con = null;
        try {
            con = JDBCUtil.getConnection();

            // （1）因为JDBC事务默认是开启的，所以要设置手动开启事务
            con.setAutoCommit(false);

            Object query = queryRunner.query(con, sql, new ScalarHandler<>(), args);

            // （2）手动提交事务
            con.commit();

            return query;
        } catch (Exception e) {
            // （3）一旦其中一个操作出错，就回滚
            try {
                assert con != null;
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            JDBCUtil.close(con);
        }
        return null;
    }
}
