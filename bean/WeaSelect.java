package com.engine.msgcenter.bean;

/**
 * WeaSelect javabean
 *
 * add by tianzefa 2018/11/21
 */
public class WeaSelect{

    private String key;

    private String showname;

    private boolean selected;

    private boolean disabled;

    public WeaSelect() {

    }

    public WeaSelect(String key, String showname) {
        this.key = key;
        this.showname = showname;
    }

    public WeaSelect(String key, String showname, boolean select) {
        this.key = key;
        this.showname = showname;
        this.selected = select;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
