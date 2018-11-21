package com.engine.msgcenter.cmd.msgsubconfig;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.systeminfo.constant.em.EmConstant;
import com.weaver.base.msgcenter.core.WeaReceiveManager;
import weaver.conn.RecordSetTrans;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.HashMap;
import java.util.Map;

/*
 * @ Description：更改状态
 * @ Author     ：马宏伟
 * @ Date       ：Created in 2018/10/22 14:33
 */
public class ChangeStatus extends AbstractCommonCommand<Map<String,Object>> {

    public ChangeStatus(Map<String,Object> params,User user) {
        this.params = params;
        this.user = user;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String,Object> map = new HashMap<>();

        String id = Util.null2String(params.get("id"));
        RecordSetTrans recordSet = new RecordSetTrans();
        String sql = "select status from ecology_message_subscribe where id=?";
        try {
            recordSet.setAutoCommit(false);
            recordSet.executeQuery(sql,id);
            if(recordSet.next()) {
                String status = recordSet.getString("status");//当前状态
                int labelid = 384564;
                sql = "update ecology_message_subscribe set status=? where id=?";
                recordSet.executeUpdate(sql,EmConstant.ABLE_EM.equalsIgnoreCase(status)?EmConstant.DISABLE_EM:EmConstant.ABLE_EM,id);//替换已有状态
                recordSet.commit();
                if(EmConstant.DISABLE_EM.equalsIgnoreCase(status)){//如果为“禁用”状态,已启用32626，已禁用384564
                    labelid = 32626;
                    try{
                        WeaReceiveManager.instance().startRecevice();
                    }catch (Throwable throwable){
                        throwable.printStackTrace();
                    }
                }
                else{
                    WeaReceiveManager.instance().stopRecevice();
                }

                map.put("status",true);
                map.put("msg",SystemEnv.getErrorMsgName(labelid,user.getLanguage()));
                return map;
            }
        }catch (Exception e){
            recordSet.rollback();
            e.printStackTrace();
            map.put("status",false);
            map.put("msg",e.getMessage());
            return map;
        }
        return map;
    }
}
