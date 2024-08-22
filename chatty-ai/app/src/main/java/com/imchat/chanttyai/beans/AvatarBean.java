package com.imchat.chanttyai.beans;

public class AvatarBean {
    //存在后端的pos标识
    private String pic;
    //头像的res
    private int head;
    //背景图的res
    private int bg;

    public AvatarBean(String pic, int head) {
        this.pic = pic;
        this.head = head;
    }

    public AvatarBean(String pic, int head, int bg) {
        this.pic = pic;
        this.head = head;
        this.bg = bg;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
