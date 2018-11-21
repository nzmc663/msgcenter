package com.engine.msgcenter.cmd.msgsubconfig;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.systeminfo.util.DataUtils;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.HashMap;
import java.util.Map;

/*
 * @ Description：消息订阅设置修改
 * @ Author     ：马宏伟
 * @ Date       ：Created in 2018/10/22 13:06
 */
public class DoUpdateSubConfig extends AbstractCommonCommand<Map<String,Object>> {

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    public DoUpdateSubConfig(Map<String,Object> params,User user) {
        this.user = user;
        this.params = params;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String, Object> apidatas = new HashMap<String, Object>();

        String id = Util.null2String(params.get("id"));
        String name = Util.null2String(params.get("name"));//默认不可修改
        String datatype = Util.null2String(params.get("datatype"));
        String classname = Util.null2String(params.get("classname"));
        String channeltype = Util.null2String(params.get("channeltype"));
        String date = DataUtils.getNowDate();
        String time = DataUtils.getNowTime();

        String sql = "update ecology_message_subscribe set name=?,datatype=?,classname=?,channeltype=?,modifydate=?,modifytime=? where id=?";
        RecordSet recordSet = new RecordSet();
        try{
            recordSet.executeUpdate(sql,name,datatype,classname,channeltype,date,time,id);
            apidatas.put("status", true);
            apidatas.put("msg", SystemEnv.getHtmlLabelName(387621,user.getLanguage()));
        }catch(Exception e){
            e.printStackTrace();
            apidatas.put("status",false );
            apidatas.put("msg",SystemEnv.getHtmlLabelName(31436,user.getLanguage()) + " catch exception : " + e.getMessage());
        }
        return apidatas;
    }
}