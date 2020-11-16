package domain;

import util.annontation.DBTable;

/**
 * @Auther: yxl15
 * @Date: 2020/10/23 02:45
 * @Description:
 */

@DBTable(tableName = "user_level")
public enum Level {

    // 描述当前类的5个对象

    lower("初级", 1), higher("高级", 2), special("特级", 3);

    // 对象的属性
    private String name;
    private int index;

    // 构造方法
    Level() {
    }

    Level(String name, int index) {
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
