package com.imchat.chanttyai.beans;

public class NavBean {
    private int iconUnchecked;

    private int iconChecked;

    private int unRead;

    public int getUnRead() {
        return unRead;
    }

    public void setUnRead(int unRead) {
        this.unRead = unRead;
    }

    public int getIconChecked() {
        return iconChecked;
    }

    public void setIconChecked(int iconChecked) {
        this.iconChecked = iconChecked;
    }

    public NavBean(int iconUnchecked, int iconChecked, String title) {
        this.iconUnchecked = iconUnchecked;
        this.iconChecked = iconChecked;
        this.title = title;
    }

    private String title;

    public int getIconUnchecked() {
        return iconUnchecked;
    }

    public void setIconUnchecked(int iconUnchecked) {
        this.iconUnchecked = iconUnchecked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
