package domain;

import util.annontation.DBTable;

/**
 * @Auther: yxl15
 * @Date: 2020/10/27 08:47
 * @Description:
 */

@DBTable(tableName = "service_type")
public enum ServiceType {

    // 三个对象：marketService、restaurantService、deliveryService
    marketService("超市代购",1),restaurantService("餐厅代购",2), deliveryService("快递代拿", 3);

    // 对象属性
    private String name;
    private int index;

    // 构造方法
    ServiceType() {

    }
    ServiceType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
