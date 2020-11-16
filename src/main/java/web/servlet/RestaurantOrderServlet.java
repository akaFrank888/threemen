package web.servlet;

import com.alibaba.fastjson.JSONArray;
import domain.Order;
import domain.RestaurantInfo;
import domain.ResultInfo;
import service.RestaurantInfoService;
import service.impl.RestaurantInfoServiceImpl;
import util.utils.OrderRedisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @Auther: yxl15
 * @Date: 2020/10/26 15:03
 * @Description:
 */

@WebServlet("/restaurantOrder/*")
public class RestaurantOrderServlet extends BaseServlet {
    private static final long serialVersionUID = -5558545675437023481L;

    // 将 菜品 添加进购物车
    @SuppressWarnings("unchecked")
    public void addToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取 location 和 dishId ，就能直接锁定 菜品
        String locationStr = request.getParameter("location");
        String dishIdStr = request.getParameter("dishId");
        int location = Integer.parseInt(locationStr);
        int dishId = Integer.parseInt(dishIdStr);

        // 调用service
        RestaurantInfoService service = new RestaurantInfoServiceImpl();
        RestaurantInfo oneDish = service.getOneDish(location, dishId);

        // 获得用户的购物车session      key=myCart+ 当前时间（单位为s）
        HttpSession session =  request.getSession();
        ArrayList<RestaurantInfo> myCart = (ArrayList<RestaurantInfo>) session.getAttribute("myCart");
        boolean result = false;

        if (myCart == null) {
            // 用户首次加入购物车
            myCart = new ArrayList<>();
            // 将购物车存入session中
            session.setAttribute("myCart", myCart);

        }

        // 将菜品放入购物车
        result = myCart.add(oneDish);
        ResultInfo info = new ResultInfo();
        if (result) {
            // 成功添加购物车
            info.setFlag(true);
            info.setErrorMsg("成功添加购物车");
        } else {
            info.setFlag(false);
            info.setErrorMsg("购物车添加失败");
        }

        // 创建Cookie存放Session的标识号
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        System.out.println("这是sessionId：        "+session.getId());
        cookie.setMaxAge(60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        this.writeValue(response, info);


    }

    // 将菜品从购物车中删除
    @SuppressWarnings("unchecked")
    public void removeFromCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取 location 和 dishId ，就能直接锁定 菜品
        String locationStr = request.getParameter("location");
        String dishIdStr = request.getParameter("dishId");
        int location = Integer.parseInt(locationStr);
        int dishId = Integer.parseInt(dishIdStr);

        // 调用service
        RestaurantInfoService service = new RestaurantInfoServiceImpl();
        RestaurantInfo oneDish = service.getOneDish(location, dishId);

        // 获得用户的购物车session      key=myCart+ 当前时间（单位为s）
        HttpSession session =  request.getSession();
        ArrayList<RestaurantInfo> myCart = (ArrayList<RestaurantInfo>) session.getAttribute("myCart");
        ResultInfo info = new ResultInfo();

        if (myCart != null) {

            // 将菜品从购物车中删除
            boolean result = myCart.remove(oneDish);
            if (result) {
                // 成功添加购物车
                info.setFlag(true);
                info.setErrorMsg("成功从购物车中删除");
            } else {
                info.setFlag(false);
                info.setErrorMsg("从购物车中删除失败");
            }

            // 创建Cookie存放Session的标识号
            Cookie cookie = new Cookie("JSESSIONID", session.getId());
            System.out.println("这是sessionId：        " + session.getId());
            cookie.setMaxAge(60 * 60);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            // 若 myCart=null
            info.setFlag(false);
            info.setErrorMsg("该用户的购物车为空");
        }

        this.writeValue(response, info);
    }

    // 在订单页面，展示我的购物车
    @SuppressWarnings("unchecked")
    public void showMyCartAfterAddToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 通过session获取myCart
        ArrayList<RestaurantInfo> myCart = (ArrayList<RestaurantInfo>) request.getSession().getAttribute("myCart");
        // 计算得到 购物车的总价格
        RestaurantInfoService service = new RestaurantInfoServiceImpl();
        double costAll = service.countAllCostReal(myCart);
        ResultInfo info = new ResultInfo();
        JSONArray array = new JSONArray();
        array.add(myCart);
        array.add(costAll);

        if (myCart == null) {
            // 购物车为空
            info.setFlag(false);
            info.setErrorMsg("购物车为空");
        } else {
            info.setFlag(true);
            info.setErrorMsg("购物车不为空，且成功返回购物车和总价格");
            info.setDataObj(array);
        }
        this.writeValue(response, info);
    }

    // 在保存订单后，展示我的购物车和订单
    public void showMyCartAfterSaveOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取订单号
        String commNum = request.getParameter("commNum");

        // 通过订单号，从redis中查询购物车
        ArrayList<RestaurantInfo> list = OrderRedisUtil.getMyCartByKey(commNum);
        // 通过订单号，从redis中查询订单Order
        Order order = OrderRedisUtil.getOrderByCommNum(commNum);
        ResultInfo info = new ResultInfo();
        JSONArray array = new JSONArray();
        array.add(list);
        array.add(order);
        if (list != null) {
            info.setFlag(true);
            info.setErrorMsg("成功展示购物车和订单");
            info.setDataObj(array);
        } else {
            info.setFlag(false);
            info.setErrorMsg("展示购物车和订单失败");
        }
        this.writeValue(response, info);
    }

    // 清空购物车
    public void clearMyCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commNum = request.getParameter("commNum");
        boolean result = OrderRedisUtil.clearMyCart(commNum);

        ResultInfo info = new ResultInfo();
        if (result) {
            info.setFlag(true);
            info.setErrorMsg("清空成功");
        } else {
            info.setFlag(false);
            info.setErrorMsg("清空失败");
        }

        this.writeValue(response, info);

    }
}
