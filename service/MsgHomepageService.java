package com.engine.msgcenter.service;

import weaver.hrm.User;

import java.util.Map;

public interface MsgHomepageService {
    /**
     * 获取消息类型及消息数
     * @param params
     * @param user
     * @return
     */
    public Map<String, Object> getMsgTypeNumberList(Map<String, Object> params, User user);
    public Map<String, Object> getMsgList(Map<String, Object> params, User user);
    public Map<String, Object> getPopList(Map<String, Object> params, User user);
    Map<String, Object> getModule(Map<String, Object> params, User user);
}
