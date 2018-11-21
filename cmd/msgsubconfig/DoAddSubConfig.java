package com.engine.msgcenter.cmd.msgsubconfig;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.systeminfo.constant.em.EmConstant;
import com.engine.systeminfo.util.DataUtils;
import com.sap.db.jdbc.topology.System;
import com.weaver.base.msgcenter.core.WeaReceiveManager;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * @ Description：消息订阅设置添加
 * @ Author     ：马宏伟
 * @ Date       ：Created in 2018/10/22 13:05
 */
public class DoAddSubConfig extends AbstractCommonCommand<Map<String,Object>> {

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    public DoAddSubConfig(Map<String,Object> params,User user) {
        this.user = user;
        this.params = params;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String, Object> map = new HashMap<String, Object>();

        long nowDate = new Date().getTime();
//        String id = Integer.toHexString((int)nowDate);
        String name = Util.null2String(params.get("name"));
        String datatype = Util.null2String(params.get("datatype"));
        String classname = Util.null2String(params.get("classname"));
        String channeltype = Util.null2String(params.get("channeltype"));
        String status = EmConstant.DISABLE_EM;//默认状态禁用
        int createid = user.getUID();
        String date = DataUtils.getNowDate();
        String time = DataUtils.getNowTime();

        String insertSql1 = "insert into ecology_message_subscribe (name,datatype,classname,channeltype,status,createid,createdate,createtime,modifydate,modifytime) values(  ?,?,?,?,?,   ?,?,?,?,?) ";
        RecordSet recordSet = new RecordSet();
        try{
            recordSet.executeUpdate(insertSql1, name, datatype,classname, channeltype,status,createid,date,time,date,time);
            WeaReceiveManager.instance().stopRecevice();
            map.put("status", true);
            map.put("msg", SystemEnv.getHtmlLabelName(83880,user.getLanguage()));
        }catch(Exception e){
            e.printStackTrace();
            map.put("status",false);
            map.put("msg",SystemEnv.getHtmlLabelName(31877,user.getLanguage()) + " catch exception : " + e.getMessage());
        }
        return map;
    }
}
