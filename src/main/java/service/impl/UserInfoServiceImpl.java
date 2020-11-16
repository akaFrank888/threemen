package service.impl;

import dao.UserInfoDao;
import dao.impl.UserInfoDaoImpl;
import domain.User;
import domain.UserInfo;
import service.UserInfoService;

import java.io.InputStream;

public class UserInfoServiceImpl implements UserInfoService {

    private UserInfoDao dao = new UserInfoDaoImpl();

    @Override
    public UserInfo showUserInfo(String token) {
        // 不包含头像的部分信息
        return dao.showUserInfo(token);
    }

    @Override
    public User showUserInfoElse(String token) {
        return dao.showUserInfoElse(token);
    }


    @Override
    public boolean saveUserInfo(UserInfo info) {
        return dao.saveInfo(info);
    }

    @Override
    public boolean alterNickname(String nickname,String token) {
        return dao.alterNickname(nickname, token);
    }

    @Override
    public String showImg(User user) {
        return dao.showImg(user.getKeyCode());
    }


    @Override
    public boolean saveImg(String token,InputStream inputStream) {
        return dao.saveImg(token, inputStream);
    }

    @Override
    public boolean checkPassword(String oldPassword, String password) {
        return oldPassword.equals(password);
    }

    @Override
    public boolean updatePassword(String token, String newPassword) {
        return dao.updatePassword(token, newPassword);
    }


    @Override
    public String join(String yuan, String dong) {
        StringBuilder sb = new StringBuilder(yuan);
        sb.append("#");
        sb.append(dong);
        return sb.toString();
    }


    // 但只需要userInfo里面的address和phone
    @Override
    public UserInfo getUserInfoByToken(String token) {
        return dao.getUserInfoByToken(token);
    }
}
