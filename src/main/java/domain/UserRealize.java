package domain;

import util.annontation.DBField;
import util.annontation.DBTable;

@DBTable(tableName = "user_realize")
public class UserRealize {


    // 真实姓名和学号
    @DBField("user_account")
    private String realizeAccount;
    @DBField("user_password")
    private String realizePassword;

    // token
    @DBField("user_token")
    private String token;

    public UserRealize() {

    }

    public UserRealize(String realizeAccount, String realizePassword, String token) {
        this.realizeAccount = realizeAccount;
        this.realizePassword = realizePassword;
        this.token = token;
    }

    public String getRealizeAccount() {
        return realizeAccount;
    }

    public void setRealizeAccount(String realizeAccount) {
        this.realizeAccount = realizeAccount;
    }

    public String getRealizePassword() {
        return realizePassword;
    }

    public void setRealizePassword(String realizePassword) {
        this.realizePassword = realizePassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserRealize{" +
                "realizeAccount='" + realizeAccount + '\'' +
                ", realizePassword='" + realizePassword + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
