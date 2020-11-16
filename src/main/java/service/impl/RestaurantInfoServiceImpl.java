package service.impl;

import dao.RestaurantInfoDao;
import dao.impl.RestaurantInfoDaoImpl;
import domain.RestaurantInfo;
import service.RestaurantInfoService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @Auther: yxl15
 * @Date: 2020/10/25 21:19
 * @Description:
 */
public class RestaurantInfoServiceImpl implements RestaurantInfoService {


    private RestaurantInfoDao dao = new RestaurantInfoDaoImpl();

    @Override
    public List<Object[]> showAllShops(int locationId) {

        List<Object[]> list = dao.getShopsByLocation(locationId);

        // 遍历 list中的图片路径，并将其换成String类型的图片
        for (Object[] os : list) {
            String path = (String) os[1];
            String img = getImgByString(path);
            // 替换
            os[1] = img;
        }
        return list;
    }

    @Override
    public List<Object[]> showAllShopsByYuan(int yuan) {
        List<Object[]> list = dao.getShopsByYuan(yuan);

        // 遍历 list中的图片路径，并将其换成String类型的图片
        for (Object[] os : list) {
            String path = (String) os[1];
            String img = getImgByString(path);
            // 替换
            os[1] = img;
        }
        return list;
    }

    @Override
    public List<RestaurantInfo> showAllDishByShop(String shopName) {

        // 将 img中的路径 改为 String类型的图片，然后再返回
        List<RestaurantInfo> list = dao.getAllDishByShop(shopName);
        for (RestaurantInfo info : list) {
            String img = getImgByString(info.getImg());
            info.setImg(img);
        }
        return list;
    }

    @Override
    public RestaurantInfo getOneDish(int location, int dishId) {

        RestaurantInfo oneDish = dao.getOneDish(location, dishId);
        // 将其中的img属性改为String类型的图片，然后再返回
        String img = getImgByString(oneDish.getImg());
        oneDish.setImg(img);

        return oneDish;
    }

    @Override
    public String getImgPath(int location, int dishId) {
        return dao.getImgPath(location, dishId);
    }

    @Override
    public String getImgByString(String imgPath) {

        String result = null;

        try {
            // 一、通过path，用IO获得byte[]

            FileInputStream fis = new FileInputStream(new File(imgPath));
            // 创建缓存数组，再用byte[]读取
            byte[] b = new byte[fis.available()];
            fis.read(b);

            // 二、将byte[]转成String

            result = Base64.getEncoder().encodeToString(b);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public double countAllCostReal(ArrayList<RestaurantInfo> list) {

        double result = 0;
        for (RestaurantInfo info : list) {
            result += info.getDishPrice();
        }
        return result;
    }
}
