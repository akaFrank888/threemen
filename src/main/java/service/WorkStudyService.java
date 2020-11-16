package service;

import domain.PageBean;
import domain.WorkStudyInfo;


public interface WorkStudyService {

    // 显示所有勤工俭学信息平台的信息
    PageBean<WorkStudyInfo> showOnePageInfo(int currentPage, int pageSize,String content);


}
