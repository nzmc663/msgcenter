package com.engine.msgcenter.entity;

import java.util.Date;

/**
 * Created by tianzefa on 2018/08/31.
 * 消息配置主表实体类
 */
public class MsgConfigBean {
    /**
     * 编号
     */
    private int id;
    /**
     * 用户编号
     */
    private int userId;
    /**
     *消息类型（消息种类）编号
     */
    private int messageTypeId;
    /**
     *消息类型（消息种类）名称
     */
    private String messageTypeName;
    /**
     * 消息组编号
     */
    private int messageGroupId;
    /**
     * 消息组名称
     */
    private String messageGroupName;
    /**
     * 包含其它类型消息（存入其它消息类型id，多个用逗号隔开）
     */
    private String include;
    /**
     * 是否有详细配置
     */
    private String hasDetail;
    /**
     * 是否启用
     */
    private String enable;
    /**
     * 是否启用右下角弹出
     */
    private String enableTray;
    /**
     * 描述
     */
    private String desc;
    /**
     * 创建人
     */
    private int creater;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 详细设置
     */
    private MsgConfigDetailBean msgConfigDetail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMessageTypeId() {
        return messageTypeId;
    }

    public void setMessageTypeId(int messageTypeId) {
        this.messageTypeId = messageTypeId;
    }

    public String getMessageTypeName() {
        return messageTypeName;
    }

    public void setMessageTypeName(String messageTypeName) {
        this.messageTypeName = messageTypeName;
    }

    public int getMessageGroupId() {
        return messageGroupId;
    }

    public void setMessageGroupId(int messageGroupId) {
        this.messageGroupId = messageGroupId;
    }

    public String getMessageGroupName() {
        return messageGroupName;
    }

    public void setMessageGroupName(String messageGroupName) {
        this.messageGroupName = messageGroupName;
    }

    public String isHasDetail() {
        return hasDetail;
    }

    public void setHasDetail(String hasDetail) {
        this.hasDetail = hasDetail;
    }

    public String isEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String isEnableTray() {
        return enableTray;
    }

    public void setEnableTray(String enableTray) {
        this.enableTray = enableTray;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCreater() {
        return creater;
    }

    public void setCreater(int creater) {
        this.creater = creater;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public MsgConfigDetailBean getMsgConfigDetail() {
        return msgConfigDetail;
    }

    public void setMsgConfigDetail(MsgConfigDetailBean msgConfigDetail) {
        this.msgConfigDetail = msgConfigDetail;
    }
}
