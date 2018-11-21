package com.engine.msgcenter.bean;


import java.util.List;
import java.util.Map;

/**
 * table item实体类（前端采用自定义table组件，无业务复用性）
 * add by tianzefa on 2018/10/15
 */
public class WeaTableItem {
    private String key;
    private String title;
    private WeaCheckbox open;
    private WeaCheckbox openRight;
    private List<WeaTableItem> subList;
    private String details;
    private Map<Object,Object> otherParams;
    public WeaTableItem() {
    }

    public WeaTableItem(String key, String title, WeaCheckbox open, WeaCheckbox openRight) {
        this.key = key;
        this.title = title;
        this.open = open;
        this.openRight = openRight;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public WeaCheckbox getOpen() {
        return open;
    }

    public void setOpen(WeaCheckbox open) {
        this.open = open;
    }

    public WeaCheckbox getOpenRight() {
        return openRight;
    }

    public void setOpenRight(WeaCheckbox openRight) {
        this.openRight = openRight;
    }

    public List<WeaTableItem> getSubList() {
        return subList;
    }

    public void setSubList(List<WeaTableItem> subList) {
        this.subList = subList;
    }

    public String getDetails() { return details; }

    public void setDetails(String details) { this.details = details; }

    public Map<Object, Object> getOtherParams() { return otherParams; }

    public void setOtherParams(Map<Object, Object> otherParams) { this.otherParams = otherParams; }
}