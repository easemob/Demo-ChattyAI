package com.imchat.chanttyai.beans;

public class MineOptBean {
    private int icon;
    private String title;
    private String sub;


    public MineOptBean(int icon, String title, String sub) {
        this.icon = icon;
        this.title = title;
        this.sub = sub;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }
}
