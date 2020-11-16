package web.servlet;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @Auther: yxl15
 * @Date: 2020/11/14 22:04
 * @Description:
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public String getParameter(String name) {
        String parameter = super.getParameter(name);
        if (StringUtils.isNotBlank(parameter)) {
            String s = StringEscapeUtils.escapeHtml3(parameter);
            System.out.println(s);
            return s;
        }
        return null;
    }
}
