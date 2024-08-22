package com.imchat.chanttyai.beans;

public class UnreadBean {
    private int position;
    private int count;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public UnreadBean(int position, int count) {
        this.position = position;
        this.count = count;
    }
}
