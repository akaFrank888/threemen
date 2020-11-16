package domain;

import util.annontation.DBTable;

/**
 * @Auther: yxl15
 * @Date: 2020/10/19 20:37
 * @Description:
 */


@DBTable(tableName = "order_status")


public enum Status {


    // 描述当前类的5个对象
    unpaid("待支付",1),unpicked("待接单",2), unreceived("待收货", 3),finished("订单完成",4), failed("订单关闭", 5);

    // 对象的属性
    private String name;
    private int index;

    // 构造方法
    Status(){}
    Status(String name, int index) {
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
