package com.engine.msgcenter.service;

import weaver.hrm.User;

import java.util.Map;

/*
 * @ Description：消息订阅设置
 * @ Author     ：马宏伟
 * @ Date       ：Created in 2018/10/22 13:07
 */
public interface MsgSubConfigService {

    public Map<String,Object> queryMsgSubConfig(Map<String,Object> params,User user);

    public Map<String,Object> updateMsgSubConfig(Map<String,Object> params,User user);

    public Map<String,Object> deleteMsgSubConfig(Map<String,Object> params,User user);

    public Map<String,Object> addMsgSubConfig(Map<String,Object> params,User user);

    public Map<String,Object> getCondition(Map<String,Object> params,User user);

    public Map<String,Object> changeStatus(Map<String,Object> params,User user);


}
