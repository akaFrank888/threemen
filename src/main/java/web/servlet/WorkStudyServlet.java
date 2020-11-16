package web.servlet;

import domain.PageBean;
import domain.WorkStudyInfo;
import service.WorkStudyService;
import service.impl.WorkStudyServiceIml;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/workStudyServlet/*")

public class WorkStudyServlet extends BaseServlet {

    // 设计一个方法   将勤工俭学信息返回给前端，并实现分页
    public void showWorkStudy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


/*        String json = FastJsonUtil.readJSONString(request);
        // JSONObject是JSONObject的一个重要的类，用于将json（String）————>Object
        JSONObject jsonObject = JSONObject.parseObject(json);
        String currentPageStr = jsonObject.getString("currentPage");
        String pageSizeStr = jsonObject.getString("pageSize");*/

        // 一、获得前端传来的的参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String content = request.getParameter("content");

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
            // 默认每页显示5条勤工俭学信息
            pageSize = 5;
        }
        

        // 二、获得存放一页信息的list
        WorkStudyService service = new WorkStudyServiceIml();
        PageBean<WorkStudyInfo> page = service.showOnePageInfo(currentPage, pageSize, content);

        // 三、判断list是否为空
        if (page == null) {
            System.out.println("勤工俭学信息的page为空");
        } else {
            // 将list写给前端
            this.writeValue(response, page);
        }
    }
}


