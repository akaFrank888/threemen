package dao;

import domain.WorkerInfo;
import domain.UserRealize;

public interface WorkerInfoDao {

    // 用于小哥的实名认证
    boolean insertRealizeAccountAndPassword(UserRealize userRealize);


    // 通过token查找workerInfo对象
    WorkerInfo showByToken(String account);


    boolean successForWorker(String worker_token, int good, int bad);

    // 判断有无记录
    boolean isRecorded(String token);
}
