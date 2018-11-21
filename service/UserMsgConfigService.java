package com.engine.msgcenter.service;

import java.util.Map;

/**
 * Created by tianzefa on 2018/08/31.
 * 消息配置service
 */
public interface UserMsgConfigService {

    /**
     * 获取用户消息提醒配置
     */
    Map<String, Object> getUserMsgConfigService(Map<String, Object> params);
    /**
     * 保存用户消息提醒配置
     */
    Map<String, Object> saveUserMsgConfigService(Map<String, Object> params);
    /**
     * 同步用户消息提醒配置
     */
    Map<String, Object> syncUserMsgConfigService(Map<String, Object> params);
    /**
     * 获取流程模块详细配置
     */
    Map<String, Object> getDetailCondition(Map<String, Object> params);
    /**
     * 获取流程类型详细配置
     */
    Map<String, Object> getWorkflowCondition(Map<String, Object> params);
    /**
     * 获取同步表单condition
     */
    Map<String, Object> getSyncFormCondition(Map<String, Object> params);
}
