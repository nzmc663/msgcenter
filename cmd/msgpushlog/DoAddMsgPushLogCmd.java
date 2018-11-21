package com.engine.msgcenter.cmd.msgpushlog;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.msgcenter.util.MsgPushLogUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;

import java.util.HashMap;
import java.util.Map;

/*
 * @ Description：添加消息推送日志
 * @ Author     ：马宏伟
 * @ Date       ：Created in 10:01 2018/09/04
 */

public class DoAddMsgPushLogCmd extends AbstractCommonCommand<Map<String,Object>> {

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    public DoAddMsgPushLogCmd(Map<String,Object> params, User user) {
        this.user = user;
        this.params = params;
    }

    @Override
    public Map<String,Object> execute(CommandContext commandContext) {

        Map<String, Object> apidatas = new HashMap<String, Object>();

        //从前端获取值
        String msgid = Util.null2String(params.get("MESSAGEID"));
        String msgtype = Util.null2String(params.get("MESSAGETYPE"));
        String usertype = Util.null2String(params.get("USERTYPE"));
        String userid = "," + Util.null2String(params.get("USERID")) + ",";
        String context = Util.null2String(params.get("CONTEXTS"));
        int creater = Util.getIntValue(Util.null2String(params.get("CREATER")));
        String desc = Util.null2String(params.get("DESCRIPTION"));
        String state = Util.null2String(params.get("STATE"));
        String credate = MsgPushLogUtil.getNowDate();
        String cretime = MsgPushLogUtil.getNowTime();

        String insertSql1 = "insert into ECOLOGY_MESSAGE_LOG (MESSAGEID,MESSAGETYPE,USERTYPE,USERID,CONTEXTS,CREATER,DESCRIPTION,STATE,CREATEDATE,CREATETIME) values(?,?,?,?,?,?,?,?,?,?) ";
        RecordSet rs = new RecordSet();
        try{
            rs.executeUpdate(insertSql1,msgid,msgtype,usertype,userid,context,creater,desc,state,credate,cretime);
            apidatas.put("status", true);
        }catch(Exception e){
            e.printStackTrace();
            apidatas.put("status",false);
            apidatas.put("msg","catch exception：" + e.getMessage());
        }
        return apidatas;
    }
}
