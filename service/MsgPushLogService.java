package com.engine.msgcenter.service;

import weaver.hrm.User;

import java.util.Map;

/*
 * @ Description：消息推送日志service类
 * @ Author     ：马宏伟
 * @ Date       ：Created in 10:01 2018/09/04
 */

public interface MsgPushLogService {


    public Map<String,Object> queryMsgLog(Map<String, Object> params, User user);

    public Map<String,Object> queryMsgSubLog(Map<String, Object> params, User user);

    public Map<String,Object> showUsername(Map<String, Object> params, User user);

    public Map<String,Object> updateMsgLog(Map<String, Object> params, User user);

    public Map<String,Object> addMsgLog(Map<String, Object> params, User user);

    public Map<String,Object> deleteMsgLog(Map<String, Object> params, User user);

    public Map<String,Object> getCondition(Map<String, Object> params, User user);
}

