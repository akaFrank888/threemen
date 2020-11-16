package domain;

import util.annontation.DBField;
import util.annontation.DBTable;

import java.io.Serializable;

/**
 * 勤工俭学信息平台的每一条信息
 */

@DBTable(tableName = "workstudy_platform")
public class WorkStudyInfo implements Serializable {

    // 自动生成序列号
    private static final long serialVersionUID = -4295142480293119025L;

    // 工作地点
    @DBField("workstudy_position")
    private String position;

    private String workstudy_position;
    // 固定工作
    @DBField("workstudy_fix_seat")
    private String fixSeat;
    // 临时工作
    @DBField("workstudy_tem_seat")
    private String temSeat;


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFixSeat() {
        return fixSeat;
    }

    public void setFixSeat(String fixSeat) {
        this.fixSeat = fixSeat;
    }

    public String getTemSeat() {
        return temSeat;
    }

    public void setTemSeat(String temSeat) {
        this.temSeat = temSeat;
    }

    public WorkStudyInfo() {

    }

    public WorkStudyInfo(String position, String workstudy_position, String fixSeat, String temSeat) {
        this.position = position;
        this.workstudy_position = workstudy_position;
        this.fixSeat = fixSeat;
        this.temSeat = temSeat;
    }

    @Override
    public String toString() {
        return "WorkStudyInfo{" +
                "position='" + position + '\'' +
                ", fixSeat='" + fixSeat + '\'' +
                ", temSeat='" + temSeat + '\'' +
                '}';
    }
}
