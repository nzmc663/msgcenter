package com.engine.msgcenter.bean;

import com.engine.msgcenter.constant.MsgConfigConstant;

/**
 *  check box 实体类
 */
public class WeaCheckbox {

    private String key;

    private String value = MsgConfigConstant.MSG_FRONT_CONFIG_DISABLED;

    private String type = "CHECKBOX";

    public WeaCheckbox() {
    }

    public WeaCheckbox(String key) {
        this.key = key;
    }

    public WeaCheckbox(String key,String value) {
        this.key = key;
        this.value = value;
    }

    public WeaCheckbox(String key,String value,String type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public WeaCheckbox setType(String type) {
        this.type = type;
        return this;
    }

    public String getKey() {
        return key;
    }

    public WeaCheckbox setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {return value; }

    public WeaCheckbox setValue(String value) {
        this.value = value;
        return this;
    }
}