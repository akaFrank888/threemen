package dao;

import domain.RestaurantInfo;

import java.util.List;

/**
 * @Auther: yxl15
 * @Date: 2020/10/25 21:20
 * @Description:
 */
public interface RestaurantInfoDao {

    // 按location分类展示所有商铺
    List<Object[]> getShopsByLocation(int locationId);
    // 按 苑 展示所有店铺
    List<Object[]> getShopsByYuan(int yuan);

    // 根据店铺名称 查询 该店铺下的 所有 菜品
    List<RestaurantInfo> getAllDishByShop(String shopName);

    // 根据location和dishId 获取 某菜品
    RestaurantInfo getOneDish(int location, int dishId);

    // 获得图片路径
    String getImgPath(int location, int dishId);


}
