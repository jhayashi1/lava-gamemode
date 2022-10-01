package com.jhayashi1.framework;

public class Profile {

    private Group group;

    public Profile(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
