package domain;

/**
 * @Auther: yxl15
 * @Date: 2020/10/23 07:54
 * @Description:
 *
 *          只用来存储用户的   手机号和邮箱（为了小哥联系）
 */
public class UserContact {

    private String nickname;
    private String phone;
    private String email;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserContact{" +
                "nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
