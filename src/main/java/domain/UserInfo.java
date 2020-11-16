package domain;

import util.annontation.DBField;
import util.annontation.DBTable;

/**

        用于显示个人信息的UserInfo

 */
@DBTable(tableName = "user_info")
public class UserInfo {


    // 头像、昵称
    @DBField("user_img")
    private Object img;

    // 院、系
    @DBField("user_department")
    private String department;
    @DBField("user_master")
    private String master;
    // 宿舍楼  ___苑___栋
    @DBField("user_address")
    private String address;
    // 手机号
    @DBField("user_phone")
    private String phone;

    // token
    @DBField("user_token")
    private String token;


    public UserInfo() {

    }


    public Object getImg() {
        return img;
    }

    public void setImg(Object img) {
        this.img = img;
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "UserInfo{" +
                "address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
