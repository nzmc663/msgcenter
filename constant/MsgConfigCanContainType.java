package com.engine.msgcenter.constant;
/**
 * @author tianzefa
 *
 * 可包含消息类型
 */
public enum MsgConfigCanContainType {

    CONTAIN_WF_RETURN(21,389074),          //包含退回流程
    CONTAIN_WF_FORWARD(22,389075),         //包含转发流程
    CONTAIN_WF_COPY(23,389076),            //包含抄送流程
    CONTAIN_WF_INQUIRY(24,389077)          //包含意见征询流程
    ;

    private int code;
    private int labelid;

    public int getCode() {
        return code;
    }

    public int getLabelid() {
        return labelid;
    }

    public static int getLabelid(int code){
        if(CONTAIN_WF_RETURN.code==code)return CONTAIN_WF_RETURN.labelid;
        else if(CONTAIN_WF_FORWARD.code==code)return CONTAIN_WF_FORWARD.labelid;
        else if(CONTAIN_WF_COPY.code==code)return CONTAIN_WF_COPY.labelid;
        else if(CONTAIN_WF_INQUIRY.code==code)return CONTAIN_WF_INQUIRY.labelid;
        return 0;
    }

    public static MsgConfigCanContainType getContainType(int code){
        if(CONTAIN_WF_RETURN.code==code)return CONTAIN_WF_RETURN;
        else if(CONTAIN_WF_FORWARD.code==code)return CONTAIN_WF_FORWARD;
        else if(CONTAIN_WF_COPY.code==code)return CONTAIN_WF_COPY;
        else if(CONTAIN_WF_INQUIRY.code==code)return CONTAIN_WF_INQUIRY;
        return CONTAIN_WF_RETURN;
    }

    MsgConfigCanContainType(int code,int labelid)
    {
        this.code=code;
        this.labelid=labelid;
    }
}
