package com.engine.msgcenter.entity;

import java.util.Date;

/**
 * Created by tianzefa on 2018/08/31.
 * 消息详细配置子表实体类
 */
public class MsgConfigDetailBean {
    /**
     * 编号
     */
    private int id;
    /**
     * 配置主表id
     */
    private int msgConfigId;
    /**
     * 类型
     */
    private String type;
    /**
     * 路径
     */
    private String path;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMsgConfigId() {
        return msgConfigId;
    }

    public void setMsgConfigId(int msgConfigId) {
        this.msgConfigId = msgConfigId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
}
