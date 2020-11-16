package domain;

import util.annontation.DBField;
import util.annontation.DBTable;

import java.io.Serializable;

/**
 * 用于登录、注册的user
 */

@DBTable(tableName = "user_register")
public class User implements Serializable {

    private static final long serialVersionUID = -7847245644717289242L;
    // 账号、密码、昵称     ————  注册所需
    @DBField("user_account")
    private String account;
    @DBField("user_password")
    private String password;
    @DBField("user_nickname")
    private String nickname;

    // 邮箱
    @DBField("user_email")
    private String email;
    // 激活状态（Y是激活，N是未激活）
    @DBField("user_status")
    private String status;
    // 激活码（唯一）/token
    @DBField("user_key")
    private String keyCode;  // 不能用key，因为造成关键字冲突

    public User() {
    }

    public User(String account, String password, String nickname, String email, String status, String keyCode) {
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.status = status;
        this.keyCode = keyCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKey(String keyCode) {
        this.keyCode = keyCode;
    }


    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", keyCode='" + keyCode + '\'' +
                '}';
    }
}
