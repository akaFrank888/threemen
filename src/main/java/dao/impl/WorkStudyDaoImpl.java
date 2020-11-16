package dao.impl;

import dao.BaseDao;
import dao.WorkStudyDao;
import domain.WorkStudyInfo;
import java.util.List;

public class WorkStudyDaoImpl extends BaseDao implements WorkStudyDao {

    // 从数据库中查询

    @Override
    public List<WorkStudyInfo> showOnePageInfo(int beginIndex, int pageSize, String content) {

        List<WorkStudyInfo> list ;

        // 共分为两种情况

        if (content != null && content.length() > 0) {
            // 情况一、搜索来的
            list = this.queryForList(WorkStudyInfo.class, "select " +
                    "workstudy_position as position, " +
                    "workstudy_fix_seat as fixSeat, " +
                    "workstudy_tem_seat as temSeat " +
                    "from threemen.workstudy_platform " +
                    "where workstudy_fix_seat like ? or workstudy_tem_seat like ? or workstudy_position like ?" +
                    "limit ?,?", "%" + content + "%", "%" + content + "%", "%" + content + "%", beginIndex, pageSize);
        } else {
            // 情况二、普通展示
            list = this.queryForList(WorkStudyInfo.class, "select " +
                    "workstudy_position as position, " +
                    "workstudy_fix_seat as fixSeat, " +
                    "workstudy_tem_seat as temSeat " +
                    "from threemen.workstudy_platform limit ?,?", beginIndex, pageSize);
        }

        // 输出
        System.out.println("该页的勤工俭学信息的条数为：" + list.size());

        return list;


/*        List<WorkStudyInfo> list = null;
        try {
            // 1. 创建QueryRunner
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            Connection connection = JDBCUtil.getConnection();
            // 3. 定义sql
            // 把字段名改成属性名，以实现WorkStudyInfo对象的封装
            String sql = "select workstudy_position as position,workstudy_fix_seat as fixSeat, workstudy_tem_seat as temSeat" +
                    " from platform.workstudy_platform limit ?,?";
            // 4. 执行
            // （注意：如果要使用BeanListHandler，则对应的JavaBean不能只写带参的构造方法，否则报错！！！）
            list =  query.query(connection, sql, new BeanListHandler<>(WorkStudyInfo.class), beginIndex, pageSize);
            // 输出
            System.out.println("该页的勤工俭学信息的条数为：" + list.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;*/
    }

    @Override
    public int getTotalCount(String content) {

        int count = 0;
        // 共分两种情况：
        if (content != null && content.length() > 0) {
            // 情况一：搜索来的：
            count = ((Long) this.queryForSingleValue("select count(*) from workstudy_platform " +
                    "where workstudy_fix_seat like ? or " +
                    "workstudy_tem_seat like ? or workstudy_position like ?", "%" + content + "%", "%" + content + "%", "%" + content + "%")).intValue();
            System.out.println("这是通过搜索查询寻的总数" + count);

        } else {
            // 情况二：普通展示
            count = ((Long) this.queryForSingleValue("select count(*) from workstudy_platform")).intValue();
            System.out.println("这是count记录总数：" + count);
        }


        return count;

        /*int count = 0;
        try {
            // 1. 创建QueryRunner
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            Connection connection = JDBCUtil.getConnection();
            // 3. 定义sql
            String sql = "select count(*) from workstudy_platform ";
            // 4. 执行
            // ScalarHandler返回的结果是object，而转成int的方法：先强制转成Long，再intValue()！！
            // 如果直接强转int会报错。因为如果数据量较大，会造成数据丢失。
            count = ((Long) query.query(connection, sql, new ScalarHandler<>())).intValue();
            System.out.println("这是count记录总数：" + count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;*/

    }

/*    @Test
    public void test() {
        List<WorkStudyInfo> list = this.showOnePageInfo(3, 5);

        System.out.println(list.get(0));

    }*/

}
