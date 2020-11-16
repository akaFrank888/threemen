package web.servlet;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.xdevapi.JsonArray;
import domain.ResultInfo;
import org.junit.Test;
import util.utils.NonceRedisUtil;
import util.utils.RSAUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @Auther: yxl15
 * @Date: 2020/11/12 11:43
 * @Description:
 */

@WebServlet("/saveServlet/*")
public class SaveServlet extends BaseServlet {

    private static final long serialVersionUID = 5026999713214749581L;

/*
    // 前端申请一个nonce
    public void getNonce(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nonce = NonceRedisUtil.generateNonce();
        ResultInfo info = new ResultInfo();

        info.setErrorMsg("true");
        info.setDataObj(nonce);
        info.setErrorMsg("nonce返回成功！");

        this.writeValue(response, info);
    }

    // 生成一对 公钥和私钥，返回公钥
    public void generateKeys (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ResultInfo info = new ResultInfo();

        try {
            // 生成一对 私钥和公钥
            RSAUtil.getKeyPair();

            // 将公钥返回给前端
            info.setFlag(true);
            info.setErrorMsg("公钥成功返回");
            info.setDataObj(RSAUtil.getPublicKey());

        } catch (NoSuchAlgorithmException e) {
            info.setFlag(false);
            info.setErrorMsg("报错，公钥返回失败");

            e.printStackTrace();
        }

        this.writeValue(response, info);
    }
*/

    public void getNonceAndPublicKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONArray array = new JSONArray();
        ResultInfo info = new ResultInfo();
        // 1. 获得nonce
        String nonce = NonceRedisUtil.generateNonce();

        try {
            // 2. 获得一对私钥和公钥
            RSAUtil.getKeyPair();
            String publicKey = RSAUtil.getPublicKeyTest();

            array.add(nonce);
            array.add(publicKey);

            info.setFlag(true);
            info.setErrorMsg("成功返回nonce和公钥");
            info.setDataObj(array);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            info.setFlag(false);
            info.setErrorMsg("返回公钥时报错");

        }
        this.writeValue(response, info);
    }

        @Test
    public void test() {
    }

}
