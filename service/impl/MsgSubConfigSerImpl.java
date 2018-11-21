package com.engine.msgcenter.service.impl;

import com.engine.core.impl.Service;
import com.engine.msgcenter.cmd.msgsubconfig.*;
import com.engine.msgcenter.service.MsgSubConfigService;
import weaver.hrm.User;

import java.util.Map;

/*
 * @ Description：消息订阅设置
 * @ Author     ：马宏伟
 * @ Date       ：Created in 2018/10/22 13:07
 */
public class MsgSubConfigSerImpl extends Service implements MsgSubConfigService {

    @Override
    public Map<String, Object> queryMsgSubConfig(Map<String, Object> params, User user) {
        return commandExecutor.execute(new DoQuerySubConfig(params, user));
    }

    @Override
    public Map<String, Object> updateMsgSubConfig(Map<String, Object> params, User user) {
        return commandExecutor.execute(new DoUpdateSubConfig(params, user));
    }

    @Override
    public Map<String, Object> deleteMsgSubConfig(Map<String, Object> params, User user) {
        return commandExecutor.execute(new DoDeleteSubConfig(params, user));
    }

    @Override
    public Map<String, Object> addMsgSubConfig(Map<String, Object> params, User user) {
        return commandExecutor.execute(new DoAddSubConfig(params, user));
    }

    @Override
    public Map<String, Object> getCondition(Map<String, Object> params, User user) {
        return commandExecutor.execute(new SearchCondition(params, user));
    }

    @Override
    public Map<String, Object> changeStatus(Map<String, Object> params, User user) {
        return commandExecutor.execute(new ChangeStatus(params, user));
    }
}
