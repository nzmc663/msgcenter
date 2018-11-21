package com.engine.msgcenter.service.impl;

import com.engine.core.impl.Service;
import com.engine.msgcenter.cmd.msgpushlog.*;
import com.engine.msgcenter.service.MsgPushLogService;
import weaver.hrm.User;

import java.util.Map;

//import com.engine.systeminfo.cmd.democmd.QueryMessageCmd;

/*
 * @ Description：消息推送日志service实现类
 * @ Author     ：马宏伟
 * @ Date       ：Created in 10:01 2018/09/04
 */

public class MsgPushLogServiceImpl extends Service implements MsgPushLogService {

    /**
     * @description ：查询消息推送列表
     */
    @Override
    public Map<String, Object> queryMsgLog(Map<String, Object> params, User user) {
        return commandExecutor.execute(new DoQueryMsgPushLogCmd(params,user));
    }

    /**
     * @description ：查询消息对外推送列表
     */
    @Override
    public Map<String, Object> queryMsgSubLog(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetMsgSubLogListCmd(params,user));
    }

    /**
     * @description ：修改
     */
    @Override
    public Map<String, Object> updateMsgLog(Map<String, Object> params, User user) {
        return commandExecutor.execute(new DoUpdateMsgPushLogCmd(params,user));
    }

    /**
     * @description ：添加
     */
    @Override
    public Map<String, Object> addMsgLog(Map<String, Object> params, User user) {
        return commandExecutor.execute(new DoAddMsgPushLogCmd(params,user));
    }

    /**
     * @description ：删除
     */
    @Override
    public Map<String, Object> deleteMsgLog(Map<String, Object> params, User user) {
        return commandExecutor.execute(new DoDeleteMsgPushLogCmd(params,user));
    }

    /**
     * @description ：高级搜索
     */
    @Override
    public Map<String, Object> getCondition(Map<String, Object> params, User user) {
        return commandExecutor.execute(new DoSearchConditionCmd(params,user));
    }

    /**
     * @description ：显示接收人名字
     */
    @Override
    public Map<String, Object> showUsername(Map<String, Object> params, User user) {
        return commandExecutor.execute(new DoShowUserNameCmd(params,user));
    }
}
