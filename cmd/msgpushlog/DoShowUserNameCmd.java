package com.engine.msgcenter.cmd.msgpushlog;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.msgcenter.constant.MsgConfigConstant;
import weaver.conn.DBUtil;
import weaver.conn.RecordSet;
import weaver.conn.constant.DBConstant;
import weaver.general.Util;
import weaver.hrm.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @ Description：显示接收人名字
 * @ Author     ：马宏伟
 * @ Date       ：Created in 2018/9/19 14:51
 */

public class DoShowUserNameCmd extends AbstractCommonCommand<Map<String,Object>> {
    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    public DoShowUserNameCmd(Map<String,Object> params, User user) {
        this.user = user;
        this.params = params;
    }

    @Override
    public Map<String,Object> execute(CommandContext commandContext) {
        Map<String, Object> map = new HashMap<>();
        String userid = Util.null2String(params.get("userid"));
        RecordSet recordSet = new RecordSet();
        List<String> params=new ArrayList<>();
        Object[] objects=DBUtil.transListIn(userid,params);
        String sql = "select id,lastname from hrmresource where id in ("+objects[0]+")";
        StringBuilder sb =new StringBuilder();
        try{
            recordSet.executeQuery(sql,params);
            while(recordSet.next()){
                sb.append(recordSet.getString("lastname")).append(MsgConfigConstant.MSG_DEFAULT_CONFIG_SEPARATOR);
            }
            map.put("status",true);
        }catch(Exception e){
            e.printStackTrace();
            map.put("status",false);
            map.put("msg","catch exception：" + e.getMessage());
        }
        int index=sb.lastIndexOf(",");
        if (index>0){
            sb.deleteCharAt(index);
        }
        map.put("usernames",Util.null2String(sb.toString()));
        return map;
    }
}
