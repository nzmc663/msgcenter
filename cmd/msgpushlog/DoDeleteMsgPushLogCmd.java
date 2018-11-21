package com.engine.msgcenter.cmd.msgpushlog;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import weaver.conn.DBUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @ Description：删除消息推送日志
 * @ Author     ：马宏伟
 * @ Date       ：Created in 10:01 2018/09/04
 */

public class DoDeleteMsgPushLogCmd extends AbstractCommonCommand<Map<String,Object>> {

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    public DoDeleteMsgPushLogCmd(Map<String,Object> params, User user) {
        this.user = user;
        this.params = params;
    }

    @Override
    public Map<String,Object> execute(CommandContext commandContext) {
        Map<String,Object> map = new HashMap<>();

        //批量删除，前台返回的是ids，存放多个要删除的id
        try {
            String ids = Util.null2String(params.get("ids"));
            List<String> params=new ArrayList<>();
            Object[] objects=DBUtil.transListIn(ids,params);
            RecordSet rs = new RecordSet();
            String sql = "delete from ECOLOGY_MESSAGE_LOG where id in ("+objects[0]+")";
            rs.executeUpdate(sql,params);
            map.put("status",true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status",false);
            map.put("msg","catch exception：" + e.getMessage());
        }
        return map;
    }
}
