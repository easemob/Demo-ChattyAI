package com.imchat.chanttyai.beans;

public class GroupBean {
    private String id;
    private String name;

    private String invitor;

    public GroupBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public GroupBean(String id, String name,String invitor) {
        this.id = id;
        this.name = name;
        this.invitor = invitor;
    }

    public String getInvitor() {
        return invitor;
    }

    public void setInvitor(String invitor) {
        this.invitor = invitor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
