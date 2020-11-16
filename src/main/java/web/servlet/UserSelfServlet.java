package web.servlet;

import domain.UserSelf;
import domain.ResultInfo;
import service.UserSelfService;
import service.impl.UserSelfServiceImpl;
import util.utils.TokenRedisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: yxl15
 * @Date: 2020/10/23 09:34
 * @Description:
 */

@WebServlet("/userSelf/*")
public class UserSelfServlet extends BaseServlet {

    private static final long serialVersionUID = -3813577698543548468L;

    // 充值
    public void recharge(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取 token、rechargeNum
        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());
        String rechargeNumStr = request.getParameter("rechargeNum");
        int rechargeNum = Integer.parseInt(rechargeNumStr);

        // 调用service中的方法
        UserSelfService selfService = new UserSelfServiceImpl();
        ResultInfo info = new ResultInfo();
        if (selfService.recharge(rechargeNum, token)) {
            info.setFlag(true);
            info.setErrorMsg("成功充值金子数" + rechargeNum);
            // 返回 支付金额
            info.setDataObj(rechargeNum / 10);

        } else {
            info.setFlag(false);
            info.setErrorMsg("充值失败");
        }
        this.writeValue(response, info);
    }

    // 提现
    public void withdraw(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 取得get请求中的coinNum
        String coinNumStr = request.getParameter("coinNum");
        int coinNum = Integer.parseInt(coinNumStr);

        // 取得user的level，只有特级用户才可享受提现服务
        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());
        UserSelfService selfService = new UserSelfServiceImpl();
        ResultInfo info = new ResultInfo();
        int level = selfService.getLevelByToken(token);
        // level=3才可以提现
        if (level < 3) {
            info.setErrorMsg("非特级用户不能体现");
            info.setFlag(false);
        } else {
            // 再看是否符合最低提现要求
            if (selfService.getBalanceByToken(token) >= UserSelf.MINI_WITHDRAW_NUM) {
                // 符合最低提现要求
                if (selfService.withdraw(coinNum, token)) {

                    info.setErrorMsg("成功将数目为 " + coinNum + "的金子提现");
                    info.setFlag(true);
                    info.setDataObj(coinNum / 10);
                } else {
                    info.setFlag(false);
                    info.setErrorMsg("提现失败");
                }
            } else {
                info.setErrorMsg("金子余额不足100，无法进行提现");
                info.setFlag(false);
            }
        }
        this.writeValue(response, info);
    }

    // 查看个人纪录
    public void showUserSelf(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 取得 token
        String token = TokenRedisUtil.getTokenByCookie(request.getCookies());

        // 调用service方法
        UserSelfService selfService = new UserSelfServiceImpl();
        ResultInfo info = new ResultInfo();

        UserSelf userSelf = selfService.showUserSelf(token);
        if (userSelf != null) {
            info.setFlag(true);
            info.setErrorMsg("成功取得用户的个人纪录");
            info.setDataObj(userSelf);
        } else {
            info.setFlag(false);
            info.setErrorMsg("无法取得用户的个人纪录");
        }
        this.writeValue(response, info);
    }
}
