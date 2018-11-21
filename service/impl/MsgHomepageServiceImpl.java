package com.engine.msgcenter.service.impl;

import com.engine.core.impl.Service;
import com.engine.msgcenter.cmd.homepage.GetModuleCmd;
import com.engine.msgcenter.cmd.homepage.GetMsgListCmd;
import com.engine.msgcenter.cmd.homepage.GetMsgPopListCmd;
import com.engine.msgcenter.cmd.homepage.GetMsgTypeNumberListCmd;
import com.engine.msgcenter.service.MsgHomepageService;
import weaver.hrm.User;

import java.util.Map;

public class MsgHomepageServiceImpl extends Service implements MsgHomepageService {
    @Override
    public Map<String, Object> getMsgTypeNumberList(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetMsgTypeNumberListCmd(params,user));
    }

    @Override
    public Map<String, Object> getMsgList(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetMsgListCmd(params,user));
    }

    @Override
    public Map<String, Object> getPopList(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetMsgPopListCmd(params,user));
    }

    @Override
    public Map<String, Object> getModule(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetModuleCmd(params,user));
    }
}
