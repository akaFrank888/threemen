package domain;

import java.io.Serializable;


/**

    用于封装后端返回前端的数据对象（data）

    因为要将该对象序列化为json，所以要实现Serializable

 */


public class ResultInfo implements Serializable {

    // 后端返回结果正常为true，发生异常返回false

    private boolean flag;

    // 后端返回结果数据对象

    private Object dataObj;

    // 发生异常的错误消息

    private String errorMsg;

    // 状态码

    private int code;


    public ResultInfo() {
    }

    public ResultInfo(boolean flag, Object dataObj, String errorMsg, int code) {
        this.flag = flag;
        this.dataObj = dataObj;
        this.errorMsg = errorMsg;
        this.code = code;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Object getDataObj() {
        return dataObj;
    }

    public void setDataObj(Object dataObj) {
        this.dataObj = dataObj;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
