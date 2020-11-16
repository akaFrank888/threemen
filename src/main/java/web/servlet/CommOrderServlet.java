package web.servlet;

import com.alibaba.fastjson.JSONArray;
import domain.*;
import domain.Status;
import domain.User;
import domain.UserContact;
import domain.UserInfo;
import domain.UserSelf;
import domain.Order;
import org.apache.commons.beanutils.BeanUtils;
import org.omg.SendingContext.RunTime;
import service.*;
import service.impl.*;
import util.utils.OrderRedisUtil;
import util.utils.TokenRedisUtil;
import util.utils.UuidUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@WebServlet("/order/*")
public class CommOrderServlet extends BaseServlet {

    private static final long serialVersionUID = -4445849289724417829L;

    // 展示订单后，先自动填充——————nickName、邮箱、地址、手机号
    public void preShowOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        UserInfoService userInfoService = new UserInfoServiceImpl();
        UserService userService = new UserServiceImpl();
        RestaurantInfoService infoService = new RestaurantInfoServiceImpl();


        // 一、先取得token
        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());

/*        // 如果是餐厅代购，那也要取得session中的购物车，并返回购物车和总价格
        String serviceType = request.getParameter("serviceType");
        double costReal = 0;
        ArrayList<RestaurantInfo> myCart = null;
        if (serviceType.equals("restaurantService")) {
             myCart = (ArrayList<RestaurantInfo>) request.getSession().getAttribute("myCart");
            costReal = infoService.countAllCostReal(myCart);
        }*/

        // 二、由token得到user
        User user = userService.getUserByToken(token);
            /*// 或者从redis中取user
            User user = UserTokenUtil.getUserByCookie(request.getCookies());*/
        // （只需要userInfo里面的address和phone）
        UserInfo userInfo = userInfoService.getUserInfoByToken(token);

        // 三、返回结果
        ResultInfo info = new ResultInfo();
        JSONArray array = new JSONArray();


        // 还是直接将 Object 存入 JSONObject吧
        array.add(user);
        array.add(userInfo);

        if (user != null && userInfo != null) {
            info.setFlag(true);
            info.setDataObj(array);
            info.setErrorMsg("预显示的对象返回成功。若是餐厅代购，则还会返回购物车和总价格");
        } else {
            info.setFlag(false);
            info.setErrorMsg("预显示的对象返回失败，或者该用户没填个人信息");
        }
        this.writeValue(response, info);
    }

    // 订单状态一、待支付（用户已经保存了订单）
    @SuppressWarnings("unchecked")
    public void saveOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 先生成一个订单号
        String commNum = UuidUtil.getUuid();

        // 获取参数：服务类型和表单数据
        // marketService、restaurantService、deliveryService;     若是 餐厅代购，则把购物车保存进redis中
        String serviceType = request.getParameter("serviceType");
        if (serviceType.equals("restaurantService")) {

            // 从session中获得购物车
            ArrayList<RestaurantInfo> myCart = (ArrayList<RestaurantInfo>) request.getSession().getAttribute( "myCart");
            // 把购物车保存进redis中
            boolean b = OrderRedisUtil.saveMyCartByRedis(commNum, myCart);
            if (!b) {
                throw new RuntimeException("购物车没有成功保存进 redis 中！！！");
            }
        }

        Map<String, String[]> map = request.getParameterMap();
        OrderService service = new OrderServiceImpl();
        ResultInfo info = new ResultInfo();
        Order order = new Order();
        try {
            // 封装 order
            BeanUtils.populate(order, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // 1. 拼接 苑和栋
        String yuan = request.getParameter("yuan");
        String dong = request.getParameter("dong");
        String address = service.join(yuan, dong);
        order.setAddress(address);
        // 2. 设置 token属性
        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());
        if (token != null) {
            order.setUserToken(token);
        } else {
            throw new RuntimeException("OrderServlet的token为空");
        }

        // 3. 设置 服务类型的索引
        order.setServiceType(serviceType);
        service.saveOrder(order, serviceType, commNum);

        // 因为 service.saveOrder中才插入的时间，所以 要再给order封装上时间 之后，才能将完整的order返回给前端
        order.setDate(service.getDateTime(commNum));

        // 订单保存成功
        // （1）将订单存入redis中     key=commNum，value=order。   但不能将订单号存入cookie，因为一个用户可以有多个订单
        if (OrderRedisUtil.saveOrderByRedis(commNum, order)) {

            info.setFlag(true);
            info.setErrorMsg("订单信息保存成功");
            info.setDataObj(commNum);
        } else {
            info.setFlag(false);
            info.setErrorMsg("redis保存失败");
        }
        this.writeValue(response, info);

    }

    // 显示支付界面
    public void showPayOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 一、获取订单号
        String commNum = request.getParameter("commNum");

        // 二、获取订单进而获得 所支付金子数（支付金子数=实际花费+打赏）
        Order order = OrderRedisUtil.getOrderByCommNum(commNum);
        int commCostCoin = order.getCommCostCoin();
        int commReward = order.getCommReward();


        // 三、获取token，进而获得 余额
        UserSelfService selfService = new UserSelfServiceImpl();
        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());
        int balance = selfService.getBalanceByToken(token);

        // 依次将 订单号、所支付金子数和余额 返回给前端
        ResultInfo info = new ResultInfo();
        JSONArray array = new JSONArray();
        array.add(commNum);
        array.add(commCostCoin);
        array.add(commReward);
        array.add(balance);

        info.setFlag(true);
        info.setDataObj(array);
        info.setErrorMsg("返回了：订单号、所需金子数、打赏金子数、用户余额");

        this.writeValue(response, info);
    }

    // 订单状态二、待接单（用户已经支付了订单）
    public void payOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获得token、所需的金子数、打赏金子数 和 订单号（订单号用来修改订单状态）
        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());
        String commCostCoinStr = request.getParameter("commCostCoin");
        String commRewardStr = request.getParameter("commReward");
        int commCostCoin = Integer.parseInt(commCostCoinStr);
        int commReward = Integer.parseInt(commRewardStr);
        String commNum = request.getParameter("commNum");

        // 调用service方法，将userSelf中的余额-支付的金子数
        UserSelfService selfService = new UserSelfServiceImpl();
        OrderService service = new OrderServiceImpl();
        ResultInfo info = new ResultInfo();
        if (selfService.payOrder(token, commCostCoin, commReward)) {
            if (service.alterOrderStatus(commNum, Status.unpicked.getIndex())) {
                info.setErrorMsg("扣金成功");
                info.setFlag(true);
            }
        } else {
            info.setErrorMsg("扣金失败");
            info.setFlag(false);
        }
        this.writeValue(response, info);

    }


    // 订单状态三、待收货（小哥已接单）     先判断小哥是否已经实名认证
    public void pickOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取 用户（小哥）的token 和 订单号
        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());
        String commNum = request.getParameter("commNum");

        // 先判断小哥是否已经实名认证
        // 从cookie中获取token

        // 调用service
        WorkerInfoService infoService = new WorkerInfoServiceImpl();
        ResultInfo info = new ResultInfo();
        if (!infoService.isRealize(token)) {
            // 小哥没有认证
            info.setFlag(false);
            info.setErrorMsg("小哥还没有认证，请先实名认证");
            this.writeValue(response, info);
            return;
        }

        // 实现接单：
        // 先将 订单的状态改为 3
        OrderService service = new OrderServiceImpl();
        if (service.alterOrderStatus(commNum,Status.unreceived.getIndex())) {
            // 再将 小哥的token 写入该订单中
            if (service.workerPickOrder(commNum, token)) {
                // 获取redis中order对象，并修改的worker_token，再存进去
                Order order = OrderRedisUtil.getOrderByCommNum(commNum);
                order.setWorkerToken(token);
                OrderRedisUtil.saveOrderByRedis(commNum, order);

                info.setFlag(true);
                info.setErrorMsg("小哥成功接单！");
            } else {
                info.setFlag(false);
                info.setErrorMsg("插入小哥token失败");
            }
        } else {
            // 修改订单状态失败
            info.setFlag(false);
            info.setErrorMsg("修改订单状态失败");
        }
        this.writeValue(response, info);
    }


    // 订单状态四、订单完成（用户已收货，并付款给小哥）

    public void successOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取订单号
        String commNum = request.getParameter("commNum");

        // 修改订单状态
        OrderService service = new OrderServiceImpl();
        ResultInfo info = new ResultInfo();
        if (service.alterOrderStatus(commNum, Status.finished.getIndex())) {
            // 再插入 确认送达 的时间
            if (service.insertReachTime(commNum)) {
                info.setFlag(true);
                info.setErrorMsg("订单状态修改成功，订单到达时间也成功插入");

            }

        } else {
            info.setFlag(false);
            info.setErrorMsg("修改订单状态失败");
        }

        this.writeValue(response, info);
    }

    // 订单完成后，对小哥的影响：
    // 1. 接单数+1，好评数/污点数+1                                worker_info---worker_token
    // 2. 用户对小哥的评价                                        order_worker--commNum
    // 3. 获得全部赏金和八成的订单金子，并积累经验+10（先把10写死）      user_self---user_token

    public void successForWorker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取评价 和 文字评价
        String evaluate = request.getParameter("evaluate");
        String comment = request.getParameter("comment");
        // result的值：0--污点，1--好评
        int result = Integer.parseInt(evaluate);
        // 定义两个变量，分别表示 好评数、污点数
        int good = 0; int bad = 0; int temp;
        temp = (result == 0) ? bad++ : good++;
        // 获取订单号，通过订单号获取order对象，继而获得worker_token
        String commNum = request.getParameter("commNum");
        Order order = OrderRedisUtil.getOrderByCommNum(commNum);
        String workerToken = order.getWorkerToken();

        // 1. 接单数+1，好评数/污点数+1
        WorkerInfoService workerInfoService = new WorkerInfoServiceImpl();
        ResultInfo info = new ResultInfo();
        if (workerInfoService.successForWorker(workerToken, good, bad)) {
            // 操作1修改成功，再进行操作2和3：
            OrderService service = new OrderServiceImpl();
            // 操作2：
            boolean b = service.userComment(commNum, comment);
            if (!b) {
                // 评论失败
                info.setFlag(false);
                info.setErrorMsg("评论失败");
                return;
            }

            // 操作3：
            // 通过订单号获得金子数和打赏数，计算出小哥的收入
            double costCoin = order.getCommCostCoin() * UserSelf.WORKER_EARN;
            int reward = order.getCommReward();
            double workerEarn = costCoin + reward;

            // workerToken就是userToken，通过它来“入账”，并且增加了经验（隐藏在了赚金子的方法中了）
            UserSelfService userSelfService = new UserSelfServiceImpl();
            if (userSelfService.earnAndAccumulate(workerToken, workerEarn)) {
                info.setFlag(true);
                info.setErrorMsg("successForWorker成功");
            } else {
                info.setFlag(false);
                info.setErrorMsg("小哥的入账失败");
            }
        } else {
            info.setFlag(false);
            info.setErrorMsg("小哥的接单好评差评的数据更新失败");
        }
        this.writeValue(response, info);
    }
    // 订单完成后，对用户的影响：
        // 1. 叫单数+1
        // 2. 积累经验+5（先把5写死）
    public void successForUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 通过cookie获取用户的token
        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());
        // 调用service方法
        UserSelfService selfService = new UserSelfServiceImpl();
        ResultInfo info = new ResultInfo();
        if (selfService.increaseCountAndAccumulateExp(token)) {
            info.setFlag(true);
            info.setErrorMsg("successForUser成功");
        } else {
            info.setFlag(false);
            info.setErrorMsg("successForUser失败");
        }
        this.writeValue(response, info);
    }

    // 联系小哥
    public void contactWorker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 先获取订单号
        String commNum = request.getParameter("commNum");

        // 通过小哥的token相等，连表查询，得到小哥的邮箱、手机号和称呼
        OrderService service = new OrderServiceImpl();
        ResultInfo info = new ResultInfo();
        UserContact userContact = service.getContactWithWorker(commNum);
        if (userContact != null) {
            info.setFlag(true);
            info.setErrorMsg("成功得到小哥联系方式");
            info.setDataObj(userContact);
        } else {
            info.setFlag(false);
            info.setErrorMsg("获取小哥联系方式失败");
        }
        this.writeValue(response, info);

    }
    // 联系用户
    public void contactUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 先获取订单号
        String commNum = request.getParameter("commNum");

        // 通过用户的token相等，连表查询，得到用户的邮箱、手机号和称呼
        OrderService service = new OrderServiceImpl();
        ResultInfo info = new ResultInfo();
        UserContact contactWithUser = service.getContactWithUser(commNum);
        if (contactWithUser != null) {
            info.setFlag(true);
            info.setErrorMsg("成功得到用户联系方式");
            info.setDataObj(contactWithUser);
        } else {
            info.setFlag(false);
            info.setErrorMsg("获取用户联系方式失败，或者用户并未填写个人资料卡");
        }
        this.writeValue(response, info);
    }

   /* // 用户对小哥进行评价
    public void comment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String commNum = request.getParameter("commNum");
        String comment = request.getParameter("comment");

        OrderService service = new OrderServiceImpl();
        ResultInfo info = new ResultInfo();
        boolean b = service.userComment(commNum, comment);

        if (b) {
            info.setFlag(true);
            info.setErrorMsg("评价成功");
        } else {
            info.setFlag(false);
            info.setErrorMsg("评价失败");
        }
        this.writeValue(response, info);
    }*/

        // 订单状态五、订单关闭（用户保存了订单，但长时间未付款 / 用户取消了订单）
    public void failOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取订单号
        String commNum = request.getParameter("commNum");

        // 设置订单状态为5
        OrderService service = new OrderServiceImpl();
        ResultInfo info = new ResultInfo();
        if (service.alterOrderStatus(commNum, Status.failed.getIndex())) {
            // 修改成功
            info.setFlag(true);
            info.setErrorMsg("订单已被取消");
        } else {
            info.setFlag(false);
            info.setErrorMsg("订单未取消成功");
        }
        this.writeValue(response, info);
    }



    // 在小哥界面显示  平台中所有待接单的订单
    public void showAllOrderForWorker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 定义 订单状态
        String status = "unpicked";

        // 一、获得前端传来的的参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String serviceType = request.getParameter("serviceType");

        // 用这些参数之前，先处理成int！
        int currentPage;
        // （！=null是防止空指针异常；length>0是确保有参数）
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
            System.out.println("这是currentPage" + currentPage);
        } else {
            // 如果currentPage没有值，则默认为第1页
            currentPage = 1;
        }
        // pageSize与currentPage同理
        int pageSize;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            // 默认每页显示4条订单
            pageSize = 4;
        }


        // 二、获得存放一页信息的list
        OrderInfoService service = new OrderInfoServiceImpl();
        PageBean<Order> page = service.showOnePageOrder(currentPage, pageSize, serviceType, status);
        ResultInfo info = new ResultInfo();
        // 三、判断list是否为空
        if (page == null) {
            info.setFlag(false);
            info.setErrorMsg("订单信息的page为空！");
        } else {
            info.setFlag(true);
            info.setErrorMsg("订单信息的page返回成功！");
            info.setDataObj(page);
        }
        this.writeValue(response, info);
    }

    // 用户/小哥 查询 我的订单
    public void showMyOrderForUserOrWorker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 接收get请求isUser、订单状态、服务类型
        String serviceType = request.getParameter("serviceType");
        String isUserStr = request.getParameter("isUser");
        String status = request.getParameter("order_status");
        int isUser = Integer.parseInt(isUserStr);

        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());

        // 调用service方法查询 特定状态的 订单
        OrderService service = new OrderServiceImpl();
        ResultInfo info = new ResultInfo();
        List<Order> list = service.findSpecificOrder(isUser, status, token, serviceType);

        if (list != null && list.size() > 0) {
            info.setFlag(true);
            info.setErrorMsg("成功返回状态为 " + status + " 的订单，条数为：" + list.size());
            info.setDataObj(list);
        } else {
            info.setFlag(false);
            info.setErrorMsg("订单为空或返回订单失败");
        }
        this.writeValue(response, info);
    }


    // 两种情况的同一个方法：   用户查询订单状态 或 小哥显示其中一个订单的详情，小哥接单后查询订单状态
    public void showOneOrderForUserOrWorker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取 订单号
        String commNum = request.getParameter("commNum");

        /*// 法一、通过redis获取order对象
        Order order = OrderRedisUtil.getOrderByCommNum(commNum);*/

        //  法二、通过订单号从数据库中获取order对象
        OrderService service = new OrderServiceImpl();
        Order order = service.showOrder(commNum);

        // 还需要从redis中取得购物车
        ArrayList<RestaurantInfo> list = OrderRedisUtil.getMyCartByKey(commNum);

        ResultInfo info = new ResultInfo();
        JSONArray array = new JSONArray();
        array.add(order);
        array.add(list);
        if (order != null) {
            info.setFlag(true);
            info.setErrorMsg("order对象和list购物车，依次返回成功");
            info.setDataObj(array);
        } else {
            info.setFlag(false);
            info.setErrorMsg("order对象和list购物车返回失败，或该用户并未下单！");
        }
        this.writeValue(response, info);

    }

}