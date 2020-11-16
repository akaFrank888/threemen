package dao.impl;

import dao.BaseDao;
import dao.RestaurantInfoDao;
import domain.RestaurantInfo;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.junit.Test;
import util.utils.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Auther: yxl15
 * @Date: 2020/10/25 21:21
 * @Description:
 */

public class RestaurantInfoDaoImpl extends BaseDao implements RestaurantInfoDao {


    // 按location分类展示所有商铺
    @Override
    public List<Object[]> getShopsByLocation(int locationId) {

        List<Object[]> list = null;

        Connection conn = null;
        String sql = null;
        try {
            // 1. 获得连接
            conn = JDBCUtil.getConnection();
            // 2. 创建QueryRunner
            QueryRunner queryRunner = new QueryRunner();
            // 3. 写sql
            if (locationId == 0) {
                // 默认查询所有店铺
                sql = "select distinct s.shop_name as shopName, i.img as img " +
                        "from restaurant_shop s " +
                        "inner join restaurant_shop_img i " +
                        "on s.shop_img_id=i.id";
                // 4. 执行
                list = queryRunner.query(conn, sql, new ArrayListHandler());
            } else {
                // 根据 餐厅及楼层 查询店铺
                sql = "select distinct s.shop_name as shopName, i.img as img " +
                        "from restaurant_shop s " +
                        "inner join restaurant_shop_img i " +
                        "on s.shop_img_id=i.id " +
                        "where location=?";
                // 4. 执行
                list = queryRunner.query(conn, sql, new ArrayListHandler(), locationId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn);
        }
        return list;
    }

    @Override
    public List<Object[]> getShopsByYuan(int yuan) {

        Connection conn = null;
        List<String> result = null;
        String sql = null;
        List<Object[]> list = null;
        try {
            // 1. 获得连接
            conn = JDBCUtil.getConnection();
            // 2. 创建QueryRunner
            QueryRunner queryRunner = new QueryRunner();
            // 3. 写sql
            if (yuan == 0) {
                // 默认查询所有店铺
                sql = "select distinct s.shop_name as shopName, i.img as img " +
                        "from restaurant_shop s " +
                        "inner join restaurant_shop_img i " +
                        "on s.shop_img_id=i.id";
                // 4. 执行
                list = queryRunner.query(conn, sql, new ArrayListHandler());
            } else if (yuan == 1) {

                // 南苑
                sql = "select distinct s.shop_name as shopName, i.img as img " +
                        "from restaurant_shop s " +
                        "inner join restaurant_shop_img i " +
                        "on s.shop_img_id=i.id " +
                        "where location=? or location=?";
                // 4. 执行
                list = queryRunner.query(conn, sql, new ArrayListHandler(), 4, 5);
            } else {
                // 北苑
                sql = "select distinct s.shop_name as shopName, i.img as img " +
                        "from restaurant_shop s " +
                        "inner join restaurant_shop_img i " +
                        "on s.shop_img_id=i.id " +
                        "where location=? or location=? or location=?";
                // 4. 执行
                list = queryRunner.query(conn, sql, new ArrayListHandler(), 1, 2, 3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn);
        }
        return list;

    }

    @Override
    public List<RestaurantInfo> getAllDishByShop(String shopName) {

        String sql = "select dish_id as dishId, dish_name as dishName, dish_price as dishPrice, img from restaurant_shop where shop_name=?";

        return this.queryForList(RestaurantInfo.class, sql, shopName);
    }

    @Override
    public RestaurantInfo getOneDish(int location, int dishId) {

        String sql = "select shop_name as shopName, dish_name as dishName, dish_price as dishPrice, " +
                "phone, time, img from restaurant_shop where location=? and dish_id=?";

        return this.queryForOne(RestaurantInfo.class, sql, location, dishId);
    }

    @Override
    public String getImgPath(int location, int dishId) {

        String sql = "select img from restaurant_shop where location=? and dish_id=?";

        return this.queryForSingleValue(sql, location, dishId).toString();
    }

    @Test
    public void test() {
        List<RestaurantInfo> list = this.getAllDishByShop("南四扒饭系列");
        for (RestaurantInfo info : list) {
            System.out.println(info.getDishName() + info.getDishPrice());
        }
    }
}
