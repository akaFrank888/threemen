package web.servlet;

import domain.ResultInfo;
import domain.WorkerInfo;
import domain.UserRealize;
import service.WorkerInfoService;
import service.impl.WorkerInfoServiceImpl;
import util.utils.SqlUtil;
import util.utils.TokenRedisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/workerInfo/*")
public class WorkerInfoServlet extends BaseServlet {

    private static final long serialVersionUID = -50462623543926339L;

    // 判断 用户是否已经实名认证
    public void isRealize(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 从cookie中获取token
        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());

        // 调用service
        WorkerInfoService service = new WorkerInfoServiceImpl();
        ResultInfo info = new ResultInfo();

        boolean result = service.isRealize(token);
        if (result) {
            info.setFlag(true);
            info.setErrorMsg("该用户已经实名认证");
        } else {
            info.setFlag(false);
            info.setErrorMsg("该用户未进行实名认证");
        }
        this.writeValue(response, info);

    }

    // 打工者个人信息的页面信息的显示
    public void showInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());

        WorkerInfoService service = new WorkerInfoServiceImpl();
        WorkerInfo workerInfo = service.showWorkerInfo(token);
        ResultInfo info = new ResultInfo();

        if (workerInfo != null) {
            // 成功
            info.setFlag(true);
            info.setDataObj(workerInfo);
            info.setErrorMsg("打工者的信息显示成功");
        } else {
            info.setFlag(false);
            info.setErrorMsg("打工者的信息显示失败");
        }
        this.writeValue(response, info);
    }
}
