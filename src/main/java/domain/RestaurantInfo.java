package domain;

import util.annontation.DBField;
import util.annontation.DBTable;

import java.io.Serializable;

/**
 * @Auther: yxl15
 * @Date: 2020/10/25 21:05
 * @Description:
 */

@DBTable(tableName = "restaurant_shop")
public class RestaurantInfo implements Serializable {

    private static final long serialVersionUID = -7233131829070077880L;

    @DBField("shop_name")
    private String shopName;

    @DBField("dish_name")
    private String dishName;

    @DBField("dish_price")
    private double dishPrice;

    @DBField("phone")
    private String phone;

    @DBField("school")
    private String school;

    @DBField("time")
    private String time;

    @DBField("location")
    private String location;

    @DBField("dish_id")
    private int dishId;

    @DBField("shop_img_id")
    private int shop_img;

    @DBField("img")
    private String img;


    public RestaurantInfo() {
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public double getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(double dishPrice) {
        this.dishPrice = dishPrice;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getShop_img() {
        return shop_img;
    }

    public void setShop_img(int shop_img) {
        this.shop_img = shop_img;
    }

    @Override
    public String toString() {
        return "RestaurantInfo{" +
                "shopName='" + shopName + '\'' +
                ", dishName='" + dishName + '\'' +
                ", dishPrice=" + dishPrice +
                ", phone='" + phone + '\'' +
                ", school='" + school + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                ", dishId=" + dishId +
                '}';
    }
}
