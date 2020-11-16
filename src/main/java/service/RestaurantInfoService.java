package service;

import domain.RestaurantInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: yxl15
 * @Date: 2020/10/25 21:17
 * @Description:
 */
public interface RestaurantInfoService {

    // 按location分类展示所有商铺    若locationId==0，则默认查询所有店铺
    List<Object[]> showAllShops(int locationId);
    // 按 苑 分类展示所有店铺
    List<Object[]> showAllShopsByYuan(int yuan);

    // 根据店铺名称 查询 该店铺下的 所有 菜品
    List<RestaurantInfo> showAllDishByShop(String shopName);

    // 通过 location 和 dishId 获取某菜品
    RestaurantInfo getOneDish(int location, int dishId);

    // 获得图片路径
    String getImgPath(int location, int dishId);
    // 通过图片路径，将图片转成Base64编码方式的String类型
    String getImgByString(String imgPath);

    // 计算购物车中菜品价格之和
    double countAllCostReal(ArrayList<RestaurantInfo> list);
}
