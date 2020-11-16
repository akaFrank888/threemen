package web.servlet;

import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *      点击邮箱中的超链接后，会访问该servlet
 */
@WebServlet("/activeUserServlet")
public class ActiveUserServlet extends HttpServlet {

    private static final long serialVersionUID = -9002605151676417104L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取激活码     request.getParameter还可以获取url中的参数
        String key = request.getParameter("key");
        // 2. 判断
        if (key != null) {
            // 调用service将其激活
            UserService service = new UserServiceImpl();
            boolean flag = service.active(key);

            // 3. 判断标记，将msg写到页面中
            String msg = null;
            if (flag) {
                // 激活成功，跳转页面
                msg = "激活成功，请<a href='login/index.html'>登录</a>";
            } else {
                // 激活失败
                msg = "激活失败，请联系管理员";
            }

            // 因为是直接写到页面上
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
