package web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;

/**
        解决全站乱码问题，处理所有的请求

        优化：
            利用JDK动态代理，通过包装request对象，解决get请求下的乱码问题
 */
@WebFilter("/*")
public class CharacterFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, final ServletResponse rep, FilterChain filterChain) throws IOException, ServletException {
        // 将父接口转为子接口
        final HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;


        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;character=UTF-8");

        // 因为解决get请求乱码的方式是取得request.getParameter的String后进行操作
        // 放行，让servlet拿到request的代理对象
        // 然后我们再对request调用getParameter()后的String进行操作

        ServletRequest requestProxy = (ServletRequest) Proxy.newProxyInstance(CharacterFilter.class.getClassLoader(), request.getClass().getInterfaces(), new InvocationHandler() {
            // 匿名内部类
            /*
                    关于invoke()的3个参数：
                        D1：委托类的实例
                        D2：委托类调用的方法
                        D3：方法的参数
             */
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {

                        /*判断Servlet那端调用的是不是getParameter方法，
                        --如果不是则代理完成方法调用后直接结束然后放行，不需要额外操作
                        --如果是则调用方法后，再进行额外操作*/


                // 判断是不是get请求
                if (!request.getMethod().equalsIgnoreCase("get")) {
                    // 如果不是，则用反射调完方法直接结束（return）然后放行doFilter,不对返回值（String）做处理
                    return method.invoke(request, args);
                }
                // 判断是不是getParameter()
                if (!method.getName().equals("getParameter")) {
                    // 如果不是，同理
                    return method.invoke(request, args);
                }

                // ------------------------------------

                // 拦截，并对返回值操作

                String value = (String) method.invoke(request, args);
                if (value == null) {
                    return null;
                }

                // 将tomcat默认编码后的get参数再用urf-8编码一次
                return new String(value.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            }
        });

        // 最后统一放行
        filterChain.doFilter(requestProxy, response);

    }

    @Override
    public void destroy() {

    }
}
