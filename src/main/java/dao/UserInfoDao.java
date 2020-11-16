package dao;

import domain.User;
import domain.UserInfo;
import java.io.InputStream;

public interface UserInfoDao {

/*    // 注册后，保存其account，email，nickname
    boolean saveAfterRegister(User user);*/

    // 展示用户信息的两个方法：
        // 展示除了头像外的信息
    UserInfo showUserInfo(String token);
        // 展示注册时保存的信息————account、nickname、email
    User showUserInfoElse(String token);

    // 提交个人信息时用到的两个方法：
    boolean saveInfo(UserInfo info);
    boolean alterNickname(String nickname,String token);


    String showImg(String account);

    boolean saveImg(String token, InputStream inputStream);

    boolean updatePassword(String account, String newPassword);
    // 但只需要userInfo里面的address和phone

    UserInfo getUserInfoByToken(String token);
}
