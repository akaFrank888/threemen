package web.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/*@WebFilter("/*")*/
public class MyFilter implements javax.servlet.Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();


        // 判断是否是登录/注册
        if (uri.contains("css") || uri.equals("/") || uri.contains("ico") || uri.contains("static") || uri.contains("login") || uri.contains("register") || uri.contains("index") || uri.contains("search") || uri.contains("ordering_food")) {

            // html中的处于白名单的url
            if (!uri.contains("static")) {
                // 不打印图片了，太多了
                System.out.println("这些html，我放行了         " + uri);
            }
            filterChain.doFilter(request, response);
            return;
        }
        if (uri.contains("user") || uri.contains("workStudyServlet") || uri.contains("restaurantInfo") || uri.contains("checkCode") || uri.contains("activeUserServlet")) {
            // servlet中的处于白名单的url
            System.out.println("这些servlet，我放行了      " + uri);
            filterChain.doFilter(request, response);
            return;

        }

        // 获取token，判断是否是登录状态
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("adminId".equals(c.getName())) {
                    token = c.getValue();
                    System.out.println("我取到token了");
                    break;
                }
            }
        }
        if (token != null) {
            // 处于登录状态，放行
            filterChain.doFilter(request, response);
            return;
        }

        // 否则，先登录/注册，再放行
        response.sendRedirect("/login/index.html");
        System.out.println("拦截成功        " + uri);
    }

    @Override
    public void destroy() {

    }
}
