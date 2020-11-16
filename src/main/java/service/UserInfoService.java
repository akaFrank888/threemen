package service;

import domain.User;
import domain.UserInfo;

import java.io.InputStream;

public interface UserInfoService {

    // 一、展示用户信息的两个方法：
        // 展示除了头像外的信息
    UserInfo showUserInfo(String token);
        // 展示注册时保存的信息————account、nickname、email
    User showUserInfoElse(String token);

    // 二、提交个人信息时用到的方法：
        // 保存个人信息
    boolean saveUserInfo(UserInfo info);
        // 修改昵称
    boolean alterNickname(String nickname,String token);

    // 显示用户头像
    String showImg(User user);

    // 保存头像
    boolean saveImg(String token, InputStream inputStream);

    // 校验密码
    boolean checkPassword(String oldPassword, String newPassword);

    // 更改密码
    boolean updatePassword(String account, String newPassword);

    // 将 苑和栋 连接起来 再返回一个 address
    String join(String yuan, String dong);

    // 通过token获取UserInfo
    UserInfo getUserInfoByToken(String token);
}
