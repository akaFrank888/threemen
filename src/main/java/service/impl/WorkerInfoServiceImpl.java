package service.impl;

import dao.WorkerInfoDao;
import dao.impl.WorkerInfoDaoImpl;
import domain.WorkerInfo;
import domain.UserRealize;
import service.WorkerInfoService;

public class WorkerInfoServiceImpl implements WorkerInfoService {

    private WorkerInfoDao dao = new WorkerInfoDaoImpl();


    // 用于小哥实名认证

    @Override
    public boolean saveRealize(UserRealize userRealize) {
        return dao.insertRealizeAccountAndPassword(userRealize);
    }

    // 通过token，展示小哥信息

    @Override
    public WorkerInfo showWorkerInfo(String token) {
        return dao.showByToken(token);
    }

    @Override
    public boolean successForWorker(String worker_token, int good, int bad) {

        return dao.successForWorker(worker_token, good, bad);
    }

    @Override
    public boolean isRealize(String token) {
        return dao.isRecorded(token);
    }

/*    @Override
    public boolean saveWorkerInfo(WorkerInfo info) {
        return dao.saveInfo(info);
    }*/

}
