package service;

import domain.WorkerInfo;
import domain.UserRealize;

public interface WorkerInfoService {

    // 小哥的实名认证
    boolean saveRealize(UserRealize userRealize);

    // 展示小哥资料
    WorkerInfo showWorkerInfo(String token);

/*    // 保存小哥资料
    boolean saveWorkerInfo(WorkerInfo info);*/

    // 订单数+1，好评数/污点数+1
    boolean successForWorker(String worker_token, int good, int bad);

    // 判断用户有无实名认证
    boolean isRealize(String token);
}
