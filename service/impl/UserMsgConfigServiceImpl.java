package com.engine.msgcenter.service.impl;

import com.engine.core.impl.Service;
import com.engine.msgcenter.cmd.config.*;
import com.engine.msgcenter.service.UserMsgConfigService;

import java.util.Map;

/**
 * Created by tianzefa on 2018/08/31.
 * 消息配置实现类
 */
public class UserMsgConfigServiceImpl extends Service implements UserMsgConfigService{

    /**
     * 获取用户消息提醒设置（无配置用户使用默认配置）
     */
    @Override
    public Map<String, Object> getUserMsgConfigService(Map<String, Object> params) {
        return  commandExecutor.execute(new GetUserMsgConfigCmd(user,params));
    }
    /**
     * 保存用户消息提醒设置
     */
    @Override
    public Map<String, Object> saveUserMsgConfigService(Map<String, Object> params) {
        return commandExecutor.execute(new SaveUserMsgConfigCmd(user,params));
    }
    /**
     * 同步用户消息提醒设置
     */
    @Override
    public Map<String, Object> syncUserMsgConfigService(Map<String, Object> params) {
        return commandExecutor.execute(new SyncUserMsgConfigCmd(user,params));
    }
    /**
     * 获取流程模块详细配置
     */
    @Override
    public Map<String, Object> getDetailCondition(Map<String, Object> params) {
        return commandExecutor.execute(new GetDetailConditionCmd(user,params));
    }
    /**
     * 获取流程类型详细配置
     */
    @Override
    public Map<String, Object> getWorkflowCondition(Map<String, Object> params) {
        return commandExecutor.execute(new GetWorkflowConditionCmd(user,params));
    }
    /**
     * 获取同步配置表单condition
     */
    @Override
    public Map<String, Object> getSyncFormCondition(Map<String, Object> params) {
        return commandExecutor.execute(new GetSyncFormConditionCmd(user,params));
    }
}
