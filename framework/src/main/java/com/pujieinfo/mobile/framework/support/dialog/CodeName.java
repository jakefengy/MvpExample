package com.pujieinfo.mobile.framework.support.dialog;

/**
 * 2016-12-09.
 */
public class CodeName {

    private String Code;
    private String Name;
    private boolean isSelected;

    public String getCode() {
        return Code;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
