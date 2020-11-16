package web.servlet;

import domain.ResultInfo;
import domain.RestaurantInfo;
import service.RestaurantInfoService;
import service.impl.RestaurantInfoServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Auther: yxl15
 * @Date: 2020/10/25 21:13
 * @Description:
 *
 *      用于展示各种店铺，菜品
 */

@WebServlet("/restaurantInfo/*")
public class RestaurantInfoServlet extends BaseServlet {


    private static final long serialVersionUID = 6858850081517901647L;


    // 获取图片路径，再以字符串的形式传给前端
    public void showOneDishImg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 通过location + dishId 获取存在数据库表中的图片路径
        String locationStr = request.getParameter("location");
        String dishIdStr = request.getParameter("dishId");
        int location = Integer.parseInt(locationStr);
        int dishId = Integer.parseInt(dishIdStr);

        // 调用service方法，获得String类型的img
        RestaurantInfoService service = new RestaurantInfoServiceImpl();
        String path = service.getImgPath(location, dishId);
        String result = service.getImgByString(path);

        ResultInfo info = new ResultInfo();
        if (result != null) {
            info.setFlag(true);
            info.setErrorMsg("String类型的图片返回成功");
            info.setDataObj(result);
        } else {
            info.setFlag(false);
            info.setErrorMsg("String类型的图片返回失败");
        }

        this.writeValue(response, info);
    }


        // 按location分类展示所有商铺，若location==0，则默认返回所有店铺
    public void showShopsByLocation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取 location 的id
        String locationIdStr = request.getParameter("location_id");
        int locationId = Integer.parseInt(locationIdStr);

        // 调用service方法
        RestaurantInfoService service = new RestaurantInfoServiceImpl();
        ResultInfo info = new ResultInfo();
        List<Object[]> list = service.showAllShops(locationId);
        if (list != null && list.size() > 0) {
            info.setFlag(true);
            info.setErrorMsg("成功返回所有商铺");
            info.setDataObj(list);
        } else {
            info.setFlag(false);
            info.setErrorMsg("返回失败");
        }
        this.writeValue(response, info);
    }

    // 分 南苑，北苑 进行展示
    public void showShopByYuan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String yuanStr = request.getParameter("yuan");
        int yuan = Integer.parseInt(yuanStr);

        RestaurantInfoService service = new RestaurantInfoServiceImpl();
        List<Object[]> list = service.showAllShopsByYuan(yuan);
        ResultInfo info = new ResultInfo();

        if (list != null && list.size() > 0) {
            info.setFlag(true);
            info.setErrorMsg("成功返回所有商铺");
            info.setDataObj(list);
        } else {
            info.setFlag(false);
            info.setErrorMsg("返回失败");
        }
        this.writeValue(response, info);

    }

        // 根据店铺名称 查询 该店铺下的 所有 菜品
    public void showDishByShop(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取店铺名称
        String shopName = request.getParameter("shopName");
        System.out.println(shopName);

        // 调用service方法
        RestaurantInfoService service = new RestaurantInfoServiceImpl();
        ResultInfo info = new ResultInfo();
        List<RestaurantInfo> list = service.showAllDishByShop(shopName);

        if (list != null && list.size() > 0) {
            info.setFlag(true);
            info.setErrorMsg("成功返回所有商铺");
            info.setDataObj(list);
        } else {
            info.setFlag(false);
            info.setErrorMsg("返回失败");
        }
        this.writeValue(response, info);
    }


}
