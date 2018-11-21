package com.engine.msgcenter.constant;
/**
 * 请使用com.cloudstore.dev.api.bean.MessageType
 */
@Deprecated
public enum MessageType {

	TW_WMENTION_ME_WORKFLOW(1, 387536),	//流程模块                提到我的
    TW_WMENTION_ME_SCHEDULE(2, 387537),	//日程模块                提到我的
    TW_WMENTION_ME_MEETING(3, 387538),	//会议模块                提到我的
    TW_WMENTION_ME_COLLABORATION(4, 387539),	//协作模块        提到我的
    TW_WMENTION_ME_WEIBO(5, 387540),	//微博模块                提到我的

    TW_COMMUNICATION_WORKFLOW(6, 387536),	//流程模块            相关交流
    TW_COMMUNICATION_SCHEDULE(7, 387537),	//日程模块            相关交流
    TW_COMMUNICATION_MEETING(8, 387538),	//会议模块            相关交流
    TW_COMMUNICATION_PROJECT(9, 387541),	//项目模块            相关交流

    WF_NEW_ARRIVAL(10, 19154),		//新到达流程                  流程提醒
    WF_RETURN(11, 24449),		//退回流程                        流程提醒
    WF_FORWARD(12, 387512),		//转发流程                        流程提醒
    WF_COPY(13, 387513),			//抄送流程                    流程提醒
    WF_INQUIRY(14, 387514),		//意见征询流程                    流程提醒
    WF_COMPLETED(15, 84242),		//已完成流程                  流程提醒
    WF_TIMEOUT(16, 84380),			//超时流程                    流程提醒

    STOCK_ACCEPTANCE_REMIND(17, 387515),//入库验收提醒            资产变动提醒
    STOCK_LOW_WARNING(18, 387532),		 //低库存预警             资产变动提醒
    STOCK_DOWN_WARNING(19,125645),//库存下限预警                  资产变动提醒
    STOCK_TOP_WARNING(20,125646), //库存上限预警                  资产变动提醒
    STOCK_WARNING(21,125648),     //库存呆滞预警                  资产变动提醒

    DOC_NEW_DOC(22, 131339),  	//新文档阅读提醒
    WKP_SCHEDULE(23, 20215),	//日程提醒
    WKP_REMIND(24, 18822),		//协作提醒
    TASK_ARRIVAL(25,  387520),	//新到达任务
    MEETING_REMIND(26, -81334),	//会议提醒
    HR_BIRTHDAY_REMIND(27, 17534), //生日提醒
    HR_PASSWORD_CHANGE(28, 18710),	//密码变更提醒
    SYS_PUBLIC_CHANGE(29,387521),	 //公共组调整
    SYS_MAIL_REMIND(30,18845),		 //邮件提醒
    OTHER(31,129326),			 //异构系统新到达流程
    SYS_BROADCAST(32,387522),		 //广播
    SYS_ERROR(99,25700),		 //错误信息
	;

    /**
     * code
     */
    protected int code;
    
    protected int parentCode;

    protected int labelId;

    public int getCode() {
        return code;
    }
    
    public int getParentCode() {
        return parentCode;
    }

    public int getLableId() {
        return labelId;
    }

    MessageType(int code, int labelId) {
        this.code = code;
        this.labelId = labelId;
    }

}
