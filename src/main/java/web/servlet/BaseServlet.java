package web.servlet;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 *      为减少Servlet的数量，将一个功能一个Servlet优化为一个模块一个Servlet（都继承该类），相当于在数据库中一张表对应一个Servlet.
 *      在Servlet中提供不同的方法，完成用户的请求。
 *      而该类的作用就是  利用service()，完成方法的分发
 *
 */


@WebServlet("/baseServlet")
public class BaseServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 一、 获取请求路径uri
        String uri = request.getRequestURI();
        System.out.println("请求的uri：" + uri);
        // 二、取方法名称
        // subString()方法的特点是：含头不含尾。当然这里没有尾。
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        System.out.println("方法的名称：" + methodName);
        // 三、获取Method对象，并执行。
            // 注意：此处的service()方法是BaseServlet对象调用还是UserServlet对象调用？
            // 答案是 UserServlet对象(子类对象)，即System.out.println(this);的结果是UserServlet的对象
            // 所以获取UserServlet类的方法就可以利用this
        try {
            // 此处要避免暴力反射取得UserServlet中的protected方法
            // 所以UserServlet的方法都写成public
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, request, response);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    // 此方法只与json有关！     直接写到页面上的情况，需要令设置（text/html）
    // 设计一个方法    事先将对象序列化为json并直接写回客户端
    public void writeValue(HttpServletResponse response, Object obj) throws IOException {
        // （ObjectMapper类是jackson包下的一个类，用于实现domain对象与json的转换）
        ObjectMapper mapper = new ObjectMapper();
        /*response.setHeader("Access-Control-Allow-Origin","http://localhost:8080");
        response.setHeader("Access-Control-Allow-Credentials", "true");*/
        //response.setContentType(MIME) 的作用是使客户端浏览器区分不同种类的数据
        // 并根据不同的MIME调用浏览器内不同的程序嵌入模块来处理相应的数据。
        // （浏览器的解码）
        response.setContentType("application/json;charset=utf-8");
        // 用response的字符输出流
        mapper.writeValue(response.getOutputStream(), obj);
    }



    // 设计一个方法    实现将对象序列化为json并返回    （代码的优化：因为前后端分离后，前后端交互频繁使用Ajax请求）
    //

    public String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
