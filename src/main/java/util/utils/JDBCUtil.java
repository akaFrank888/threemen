package util.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 	  Druid连接池的工具类
 */

/*            //QueryRunner的DQL（数据查询语言）
            // （一）有参数
            // MapHandler用于 返回单个map     （handler 处理者，管理者）
            Map<String, Object> map = query.query(sql, new MapHandler(), account, password);
            // ScalarHandler用于 将单个值封装   （scalar 标量、数量）
            Object object = query.query(sql, new ScalarHandler<>(), account, password);
            // BeanHandler用于 将结果集中的第第第一行数据都封装到一个对应的JavaBean中
            User user = query.query(sql, new BeanHandler<User>(User.class));
            // BeanListHandler用于 将结果集中的每每每一行数据都封装到一个对应的JavaBean中，再放到list里
            List<User> list = query.query(sql, new BeanListHandler<User>(User.class));
            // ArrayHandler用于 将结果集中的第一行数据转成对象数组
            Object[] objects = query.query(sql, new ArrayHandler());
            // ArrayListHandler
            List<Object[]> list1 = query.query(sql, new ArrayListHandler());

            //（二）没参数就不写参数
            // QueryRunner的DML（数据操作语言）
            // 只有一句话
            query.update(connection, sql, 参数);*/


public class JDBCUtil {

	// 声明静态 数据源 成员变量

	private static DataSource ds;

	// 创建一个ThreadLoacl对象，用当前线程作为key

	private static ThreadLocal<Connection> tc = new ThreadLocal<>();

	// 创建连接池对象
	static {
		// 加载配置文件中的数据   ---  先得到类加载器，再得到流
		InputStream is = JDBCUtil.class.getClassLoader().getResourceAsStream("druid.properties");
		// Properties类是用来操作properties文件的
		Properties props = new Properties();
		try {
			// 加载进来，一行一行的读取
			props.load(is);
			// 创建连接池，使用配置文件中的参数
			ds = DruidDataSourceFactory.createDataSource(props);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设计一个方法    得到数据源

	public static DataSource getDataSource() {
		return ds;
	}

	// 设计一个方法    获得连接

	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}


	// 开启事务

	public static void startTransaction() {

		try {
			// 获取连接
			Connection conn = getConnection();
			// 开启事务
			/*
			 * setAutoCommit总的来说就是保持数据的完整性，一个系统的更新操作可能要涉及多张表，需多个SQL语句进行操作

			 * 循环里连续的进行插入操作，如果你在开始时设置了：conn.setAutoCommit(false);

			 * 最后才进行conn.commit(),这样你即使插入的时候报错，修改的内容也不会提交到数据库，

			 */
			conn.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void commit() {

		try {
			Connection conn = tc.get();
			if (conn != null) {
				conn.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void rollback() {
		try {
			// 从集合tc中得到一个连接
			Connection conn = tc.get();
			if (conn != null) {
				// 该方法用于取消在当前事务中进行的更改，并释放当前Connection对象持有的所有数据库锁。此方法只有在手动事务模式下才可用
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



















	// 设计一个方法    关闭连接

	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 设计一个方法    重载关闭方法

	public static void close(Connection conn, Statement stmt) {
		close(conn, stmt, null);
	}

	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
