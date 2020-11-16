package dao;

import domain.WorkStudyInfo;

import java.util.List;

/**

        勤工俭学信息平台的显示

 */
public interface WorkStudyDao {

    // 设计一个方法   从数据库查询一页的信息
    List<WorkStudyInfo> showOnePageInfo(int beginIndex,int pageSize,String content);

    // 设计一个方法   得到总记录数
    int getTotalCount(String content);

}
