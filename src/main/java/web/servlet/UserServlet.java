package web.servlet;

import domain.ResultInfo;
import domain.User;
import org.apache.commons.beanutils.BeanUtils;
import service.UserService;
import service.impl.UserServiceImpl;
import util.utils.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
        用户的 登录、注册、退出
 */

@WebServlet("/user/*")

public class UserServlet extends BaseServlet {

    private static final long serialVersionUID = 6945448165790295959L;

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

/*        // 先验证是否是 重放攻击！！！！

        // 1. nonce的检查
        String nonce = request.getParameter("nonce");
        boolean result = NonceRedisUtil.checkAndDeleteNonce(nonce);
        ResultInfo info = new ResultInfo();

        if (!result) {
            // redis中并未保存该nonce
            info.setFlag(false);
            info.setErrorMsg("redis中未保存该nonce，请求失败");
            this.writeValue(response, info);
            return;
        }*/

        String account = request.getParameter("account");
        String password = request.getParameter("password");
        ResultInfo info = new ResultInfo();
/*        try {
            // 2. 若redis中有该nonce，则用私钥对sign解密
            String sign = request.getParameter("sign");
            System.out.println("这是私钥："+RSAUtil.getPrivateKey());

            *//*String decryptStr = RSAUtil.decrypt(sign, RSAUtil.getPrivateKey());*//*
            String decryptStr = RSAUtil.decrypt(sign, RSAUtil.getPrivateKeyTest());
            // 对比解密后的Str与account，password，nonce

            StringBuilder sb = new StringBuilder(account);
            sb.append("&");
            sb.append(password);
            sb.append("&");
            sb.append(nonce);

            System.out.println("这是sb：" + sb.toString());
            System.out.println("这是解密后的Str：" + sign);

            // 比较，如果不相等
            if (!sb.toString().equals(decryptStr)) {
                // 解密失败 / nonce不一 / 参数被修改
                info.setFlag(false);
                info.setErrorMsg("请求失败，原因可能是：解密失败 / nonce不一 / 参数被修改");
                this.writeValue(response, info);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        // 一、获取前端的数据，封装user
        User user = new User();
        user.setAccount(account);

        String passwordMD5 = MD5Util.encode(password, "MD5");
        user.setPassword(passwordMD5);

        // 三、调用service，并封装resultInfo
        UserService service = new UserServiceImpl();
        User u= service.login(user);
        if (u != null) {
            // 存在该用户，登录成功！
            info.setFlag(true);
            info.setErrorMsg("登录成功，且已签发token");

            System.out.println();

            String token = service.getKeyCodeByAccount(u.getAccount());
            u.setKey(token);
            // 将key作为token存入redis：  key = token，value=user
            TokenRedisUtil.saveToken(token,u);
            // 用cookie给用户签发token
            Cookie cookie = new Cookie("adminId", token);
            cookie.setMaxAge(60 * 60);
            cookie.setPath("/");
            // 不设置生命周期，即关闭网页就销毁cookie
            response.addCookie(cookie);

        } else {
            // 登录失败
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误！");
        }

        // 四、响应数据
        this.writeValue(response, info);
    }

    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        ResultInfo info = new ResultInfo();
        // 一、获取前端参数、密码
        Map<String, String[]> map = request.getParameterMap();
        // 先判断有无注入漏洞
        if (SqlUtil.TransactSQLInjection(map)) {
            info.setFlag(false);
            info.setErrorMsg("表单存在注入漏洞");
            this.writeValue(response, info);
            return;
        }

/*        // 先进行验证码的校验！

        // 1. 获取填写的验证码
        String securityCode = request.getParameter("securityCode");
        // 2. 从session中获取（正确的）验证码
        HttpSession session = request.getSession();
        String checkCodeServer = (String) session.getAttribute("CHECKCODE_SERVER");
        // 为保证验证码只能使用一次且避免用户点击返回导致验证码的复用或不能及时生成，应该在验证码获取之后，立刻从session中清除掉该验证码
        session.removeAttribute("CHECKCODE_SERVER");

        System.out.println("试一下这里是不是乱码");

        // 3. 比较
        if (checkCodeServer == null || !checkCodeServer.equalsIgnoreCase(securityCode)) {

            // 验证码错误，响应结果给客户端
            info.setFlag(false);
            info.setErrorMsg("验证码错误");

            // 可直接调用封装json的方法(父类里的)
            this.writeValue(response,info);

            // 直接结束
            return;
        }*/


        // 二、封装user，再将加密后的password封装进user中
        User user = new User();
        try {
            BeanUtils.populate(user, map);
            String password = request.getParameter("password");
            String passwordMD5 = MD5Util.encode(password, "MD5");
            user.setPassword(passwordMD5);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // 三、调用service，并封装resultInfo
        UserService service = new UserServiceImpl();
        String result = service.register(user);

        // 四、判断
        switch (result) {
            case "注册成功":
                // 注册成功
                info.setFlag(true);
                info.setErrorMsg("注册成功！请登录您的注册邮箱进行激活您的帐号，激活后才能登录");
                break;
            case "该账户已存在":
                // 注册失败
                info.setFlag(false);
                info.setErrorMsg("该用户已存在，注册失败！");
                break;
            case "该邮箱已被用于注册":
                // 注册失败
                info.setFlag(false);
                info.setErrorMsg("该邮箱已被用于注册，注册失败！");
                break;
            case "未用邮箱激活":
                // 注册失败
                info.setFlag(false);
                info.setErrorMsg("未用邮箱激活，注册失败！");
                break;
            case "请完整填写表单":
                // 表单有空
                info.setFlag(false);
                info.setErrorMsg("表单未完整填写，注册失败！");
        }
        // 五、响应数据
        this.writeValue(response,info);
    }

    // 用cookie保存用户登录所需的账号(因为是唯一的，可区分的)和账号+密钥，以便下次自动登录
    public void saveLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 一、获取账号，并进行MD5加密
/*        // 获取当前客户端保存进session中的数据
        User user = (User)request.getSession().getAttribute("user");*/

        User user = TokenRedisUtil.getUserByCookie(request.getCookies());

        if (user != null) {

            // 获取该用户的账号
            String account = user.getAccount();

/*        // 测试：
        String account = request.getParameter("account");*/

            System.out.println("编码前的账号：" + account);

            // MD5加密
            String accountEncode = MD5Util.encode(account, "MD5");

            System.out.println("编码后的账号：" + accountEncode);

            // 二、创建两个cookie，存入response中
            // 新建cookie
            Cookie accountCookie = new Cookie("account" , account);
            Cookie accountMD5Cookie = new Cookie("accountMD5Cookie" , accountEncode);
            // 设置存活时间   （单位是秒）
            accountCookie.setMaxAge(60 * 60);
            accountMD5Cookie.setMaxAge(60 * 60);
            // 添加到response
            response.addCookie(accountCookie);
            response.addCookie(accountMD5Cookie);

            System.out.println("cookie已成功保存账号与MD5加密后的账号");
            // 响应给前端
            ResultInfo info = new ResultInfo();
            info.setFlag(true);
            info.setErrorMsg("cookie已成功保存");
            this.writeValue(response, info);
        }
    }

    // 判断用户之前有无保存cookie（自动登录）
        public void findUserCookie(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 先定义结果
        boolean isCorrect = false;
        // 获取cookie
        Cookie[] cookies = request.getCookies();
        // 遍历，获取两个cookie ———— account 与 accountMD5Cookie
            String account = null;
            String accountMD5Cookie = null;
            if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("account")) {
                    account = cookie.getValue();
                }
                if (cookie.getName().equals("accountMD5Cookie")) {
                    accountMD5Cookie = cookie.getValue();
                }
            }
            if (account != null && accountMD5Cookie != null) {
                // 通过加密方式来验证该用户是否登录过
                isCorrect = accountMD5Cookie.equals(MD5Util.encode(account, "MD5"));
            }
        }
            ResultInfo info = new ResultInfo();
            if (isCorrect) {
                // 加密方式为MD5，即可自动登录

                info.setFlag(true);
                info.setErrorMsg("自动登录成功");
                System.out.println("cookie实现了自动登录！");

                // 再在cookie中添加token
                // 先通过account获得token
                UserService service = new UserServiceImpl();
                String token = service.getKeyCodeByAccount(account);
                Cookie cookie = new Cookie("adminId", token);
                cookie.setMaxAge(60 * 60);
                cookie.setPath("/");
                response.addCookie(cookie);

            } else {
                info.setFlag(false);
                info.setErrorMsg("未实现自动登录");
            }

            // 响应给前端
            this.writeValue(response, info);
    }


    // 通过session查找该用户（登陆状态）
    public void findUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*        // 从session中获取
        Object user = request.getSession().getAttribute("user");*/

        // 先判断有无cookie
        ResultInfo info = new ResultInfo();
        if (TokenRedisUtil.isUserCookie(request)) {

            System.out.println("有cookie");
            // 从redis中获取
            User user = TokenRedisUtil.getUserByCookie(request.getCookies());
            // 写给前端
            info.setFlag(true);
            info.setDataObj(user);
            info.setErrorMsg("用户登录");
            this.writeValue(response, info);
        } else {
            // 没有cookie（游客看首页）
            info.setFlag(false);
            info.setErrorMsg("游客登录");
            this.writeValue(response, info);
        }

    }

    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. 通过token删除缓存与redis
        boolean flag = TokenRedisUtil.deleteUser(request.getCookies());
        ResultInfo info = new ResultInfo();

        if (flag) {
            // 2. 跳转到首页（response的重定向）
            // 注意重定向中路径的写法：request.getContextPath()获得绝对路径，再加上虚拟路径（不要忘/）

            // 2. 再删除cookie
            Cookie newCookie = new Cookie("adminId", null);
            newCookie.setMaxAge(0);// 立即删除型
            newCookie.setPath("/");// 对项目所有的目录都有效！
            response.addCookie(newCookie);
            info.setFlag(true);
            info.setErrorMsg("用户退出，并跳转成功");
        } else {
            info.setFlag(false);
            info.setErrorMsg("用户退出失败");
        }

        this.writeValue(response, info);
    }
}
