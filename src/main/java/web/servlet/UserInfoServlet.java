package web.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import domain.ResultInfo;
import domain.User;
import domain.UserInfo;
import domain.UserRealize;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import service.UserInfoService;
import service.WorkerInfoService;
import service.impl.UserInfoServiceImpl;
import service.impl.WorkerInfoServiceImpl;
import util.utils.MD5Util;
import util.utils.SqlUtil;
import util.utils.TokenRedisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet("/userInfo/*")
public class UserInfoServlet extends BaseServlet {

    private static final long serialVersionUID = -6837159350776215679L;

    public void showImg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = TokenRedisUtil.getUserByCookie(request.getCookies());


        UserInfoService service = new UserInfoServiceImpl();
        ResultInfo info = new ResultInfo();
        String base64 = service.showImg(user);
        if (base64 != null) {
            info.setFlag(true);
            info.setErrorMsg("头像传送成功");
            info.setDataObj(base64);

        } else {
            info.setFlag(false);
            info.setErrorMsg("该用户未设置头像 或 头像传送失败");
        }
        this.writeValue(response, info);
    }

    // 保存从前端传来的图片（头像）
    public void saveImg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());

        // 1. 先判断上传的数据是否是多段的，只有是多端的数据才是文件上传的
        if (ServletFileUpload.isMultipartContent(request)) {

            // 1. 创建工厂实现类
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            // 2. 创建用于解析上传数据的工具类
            ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
            servletFileUpload.setHeaderEncoding("utf-8");
            try {
                // 3. 解析上传的数据，得到每一个表单项FileItem
                List<FileItem> list = servletFileUpload.parseRequest(request);
                // 循环判断每一个表单项，是普通类型还是上传的文件
                for (FileItem fileItem : list) {
                    if (!fileItem.isFormField()) {
                        // 不是普通的表单项，即是文件类型
                        // 用fileItem得到输入流
                        // 调用service，最终存入数据库
                        InputStream inputStream = fileItem.getInputStream();
                        UserInfoService service = new UserInfoServiceImpl();
                        ResultInfo info = new ResultInfo();
                        if (service.saveImg(token, inputStream)) {
                            info.setFlag(true);
                            info.setErrorMsg("成功保存用户头像");
                        } else {
                            info.setFlag(false);
                            info.setErrorMsg("保存用户头像失败");
                        }
                        this.writeValue(response, info);
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }

    }


    // 展示用户信息
    public void showUserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());
        UserInfoService service = new UserInfoServiceImpl();
        UserInfo userInfo = service.showUserInfo(token);
        User user = service.showUserInfoElse(token);

        ResultInfo info = new ResultInfo();
        if (user != null) {
            // 建立 JSONArray 传给前端
            JSONArray array = new JSONArray();

            // 直接存入对象

            array.add(userInfo);
            array.add(user);

            info.setFlag(true);
            info.setErrorMsg("JSONArray成功传给前端，里面有两个直接存的对象，不是JSONObject");
            info.setDataObj(array);
        } else {
            info.setFlag(false);
            info.setErrorMsg("user为null");
        }

        this.writeValue(response, info);
    }

    // 保存用户信息
    public void saveUserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserInfoService service = new UserInfoServiceImpl();

        // 除了account、email，其他都可以改
        Map<String, String[]> map = request.getParameterMap();
        String nickname = request.getParameter("nickname");
        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());
        UserInfo userInfo = new UserInfo();
        try {
            BeanUtils.populate(userInfo, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // 再将 yuan 和 dong 连接成 address 保存进userInfo的address属性中
        String yuan = request.getParameter("yuan");
        String dong = request.getParameter("dong");
        // 以 苑#栋 的形式连接成 address
        String address = service.join(yuan, dong);
        userInfo.setAddress(address);

        // 再将token封装进去
        userInfo.setToken(token);
        // 将封装好的info对象，存入数据库
        ResultInfo info = new ResultInfo();
        if (service.saveUserInfo(userInfo) && service.alterNickname(nickname, token)) {
            info.setFlag(true);
            info.setErrorMsg("保存成功");
        } else {
            info.setFlag(false);
            info.setErrorMsg("保存失败");
        }


        this.writeValue(response, info);
    }

    // 用户修改密码
    public void updatePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        ResultInfo info = new ResultInfo();
        if (oldPassword.equalsIgnoreCase(newPassword)) {
            info.setFlag(false);
            info.setErrorMsg("新旧密码太相似，建议您重新更改密码!");
            return;
        }
        // 加密
        String oldPasswordMD5 = MD5Util.encode(oldPassword, "MD5");
        String newPasswordMd5 = MD5Util.encode(newPassword, "MD5");
        // 也要取到user的account
        User user = TokenRedisUtil.getUserByCookie(request.getCookies());
        String token = user.getKeyCode();
        String password = user.getPassword();

        UserInfoService service = new UserInfoServiceImpl();
        if (service.checkPassword(oldPasswordMD5, password)) {
            // “确认密码” 正确
            if (service.updatePassword(token, newPasswordMd5)) {
                info.setFlag(true);
                info.setErrorMsg("用户修改密码成功");
            }
        } else {
            // ”修改密码“错误
            info.setFlag(false);
            info.setErrorMsg("用户修改密码错误");
        }
        this.writeValue(response, info);

    }


    // 小哥实名认证  （填写教务系统的账号、密码）
    public void userRealize(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String realizeAccount = request.getParameter("realizeAccount");
        String realizePassword = request.getParameter("realizePassword");

        // 两个判断：一、注入漏洞 二、教务系统的验证
        // 一、先判断有无注入漏洞
        ResultInfo resultInfo = new ResultInfo();
        if (SqlUtil.TransactSQLInjection(realizeAccount, realizePassword)) {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("表单存在注入漏洞");
            this.writeValue(response, resultInfo);
            return;
        }
        // 二、教务系统接口的验证  http://jwxt.gduf.edu.cn/app.do?method=authUser?method=authUser&xh={$学号}&pwd={$密码}

        // 1. 先拼接address
        String schoolAddress = "http://jwxt.gduf.edu.cn/app.do?method=authUser";
        // 2. 创建URL实例，打开URLConnection
        String address = schoolAddress + "&xh=" + realizeAccount + "&pwd=" + realizePassword;
        URL url = new URL(address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 3. 设置参数
        connection.setRequestMethod("GET");// HttpURLConnection默认就是GET发送请求，所以这句可以省略
        connection.setDoInput(true);// HttpURLConnection默认值从服务端读取结果流，所以这句可以省略
        connection.setUseCaches(false);// 禁用网络缓存
        connection.setConnectTimeout(5 * 1000);

        // 4. 调用getInputStream后，服务端才会收到请求，并阻塞式地接收服务端返回的数据
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder result = new StringBuilder();
        String s = br.readLine();
        while (s != null) {
            result.append(s);
            s = br.readLine();
        }

        // 5. 关闭流管道
        connection.disconnect();

        System.out.println(result.toString());

        // 三、处理结果
        ResultInfo info = new ResultInfo();
        // 将String类型转成JSONObject
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        if (!jsonObject.getString("flag").equals("1")) {
            // 教务系统验证失败
            info.setFlag(false);
            info.setErrorMsg("无法通过教务系统的验证");
            return;
        }

        // 教务系统验证成功！
        // 四、保存金数据库

        // 从redis中取出user
        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());

        // 封装一个workerRealize，保存进数据库表中
        // 加密后保存密码
        String passwordMD5 = MD5Util.encode(realizePassword, "MD5");
        UserRealize userRealize = new UserRealize(realizeAccount, passwordMD5, token);
        WorkerInfoService service = new WorkerInfoServiceImpl();

        if (service.saveRealize(userRealize)) {
            // 认证成功
            info.setFlag(true);
            info.setErrorMsg("用户实名信息成功保存进数据库");
        } else {
            info.setFlag(false);
            info.setErrorMsg("用户实名失败，未能保存进数据库");
        }

        this.writeValue(response, info);
    }

}
