package domain;

import util.annontation.DBField;
import util.annontation.DBTable;

@DBTable(tableName = "worker_info")
public class WorkerInfo {


    // 综合评分
    @DBField("worker_mark")
    private int mark;

    // 总接单数、好评数、污点数
    @DBField("worker_total_count")
    private int totalCount;
    @DBField("worker_good_count")
    private int goodCount;
    @DBField("worker_bad_count")
    private int badCount;

    // token
    @DBField("worker_token")
    private String token;

    // 还有user_realize表中的两个字段
    private String realName;
    private String stuId;

    public WorkerInfo() {

    }




    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public int getBadCount() {
        return badCount;
    }

    public void setBadCount(int badCount) {
        this.badCount = badCount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    @Override
    public String toString() {
        return "WorkerInfo{" +
                "mark=" + mark +
                ", totalCount=" + totalCount +
                ", goodCount=" + goodCount +
                ", badCount=" + badCount +
                ", token='" + token + '\'' +
                ", realName='" + realName + '\'' +
                ", stuId='" + stuId + '\'' +
                '}';
    }
}
