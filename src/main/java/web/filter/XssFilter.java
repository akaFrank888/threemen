package web.filter;

import web.servlet.XssHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Auther: yxl15
 * @Date: 2020/11/14 22:28
 * @Description:
 */

/*
@WebServlet("/*")
*/
public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        XssHttpServletRequestWrapper request = new XssHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
