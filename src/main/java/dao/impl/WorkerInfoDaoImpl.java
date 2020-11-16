package dao.impl;

import dao.BaseDao;
import dao.WorkerInfoDao;
import domain.WorkerInfo;
import domain.UserRealize;
import org.junit.Test;
import util.utils.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class WorkerInfoDaoImpl extends BaseDao implements WorkerInfoDao {

    // 若想打开资料卡，则需先打开 小哥实名认证 页面
    // 实名认证的sql进行批处理，将部分信息插入到worker_info表中

    @Override
    public boolean insertRealizeAccountAndPassword(UserRealize userRealize) {


        // 用Statement进行批处理，而不是PrepareStatement（扬长避短）

        boolean flag = true;
        Connection connection = null;
        Statement statement = null;

        try {
            // 1. 获得连接
            connection = JDBCUtil.getConnection();

            // 设置手动提交事务
            connection.setAutoCommit(false);

            // 2. 创建Statement
            statement = connection.createStatement();
            // 3. statement加入批处理
            // （注意：Statement传参的sql书写格式）
            statement.addBatch("insert into threemen.user_realize values(null," + "'" + userRealize.getRealizeAccount() + "'" + ",'" + userRealize.getRealizePassword() + "'" + ",'" + userRealize.getToken() + "')");
            statement.addBatch("insert into threemen.worker_info(worker_id,worker_token) values(null,"+"'"+ userRealize.getToken()+"')");
            // 4. statement执行批处理
            int[] arr = statement.executeBatch();
            for (int value : arr) {
                if (value < 0) {
                    flag = false;
                    break;
                }
            }
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
        }finally {
            JDBCUtil.close(connection, statement);
        }
        return flag;
    }



    @Override
    public WorkerInfo showByToken(String token) {


        return this.queryForOne(WorkerInfo.class, "select " +
                "i.worker_mark as mark, " +
                "i.worker_total_count as totalCount, " +
                "i.worker_good_count as goodCount, " +
                "i.worker_bad_count as badCount, " +
                "r.worker_realName as realName, " +
                "r.worker_stuId as stuId " +
                "from worker_info i " +
                "inner join user_realize r " +
                "on i.worker_token=r.worker_token " +
                "where r.worker_token=?", token);
    }


    // 接单数+1，好评数/污点数+1
    @Override
    public boolean successForWorker(String worker_token, int good, int bad) {

        boolean b = false;
        int i = this.update("update worker_info set worker_total_count=worker_total_count+1,worker_good_count=worker_good_count+?,worker_bad_count=worker_bad_count+? where worker_token=?", good, bad, worker_token);
        if (i > 0) {
            b = true;
        }
        return b;
    }

    @Override
    public boolean isRecorded(String token) {
        boolean b = false;
        Object o = this.queryForSingleValue("select worker_realName from user_realize where worker_token=?", token);
        if (o != null) {
            b = true;
        }
        return b;
    }

    /*// 用于实名认证，且需要批处理

    @Override
    public boolean saveInfo(WorkerInfo info) {

        // 用Statement进行批处理，而不是PrepareStatement（扬长避短）

        boolean flag = false;
        Connection connection = null;
        Statement statement = null;

        try {
            // 1. 获得连接
            connection = JDBCUtil.getConnection();
            // 2. 创建Statement
            statement = connection.createStatement();
            // 3. statement加入批处理
            // （注意：Statement传参的sql书写格式）
            statement.addBatch("insert platform.user_realize values(null,'" + "'"+",'"+info.getRealName()+"'"+",'"+info.getStuId()+"'"+",'"+info.getToken()+"')");
            statement.addBatch("insert platform.worker_info values(null,'"+"'"+",'"+info.getRealName()+"'"+",90,0,0,0"+",'"+info.getToken()+"'");

            // 4. statement执行批处理
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.close(connection, statement);
        }
        return flag;




*//*        boolean flag = false;
        try {
            // 1. 创建QueryRunner对象
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            Connection connection = JDBCUtil.getConnection();
            // 3. 定义sql
            String sql = "insert worker_info(worker_account,worker_real_name,worker_stu_id) values(?,?,?)";
            // 4. 执行
            int i = query.update(connection, sql, null, info.getRealName(), info.getStuId());
            // 5. 判断
            if (i > 0) {
                flag = true;
                System.out.println("学号为：" + info.getStuId() + "的一条记录插入成功！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;*//*
    }*/

    @Test
    public void test() {

        WorkerInfo workerInfo = this.showByToken("438");
        System.out.println(workerInfo);

    }

}
