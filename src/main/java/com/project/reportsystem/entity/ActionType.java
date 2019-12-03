package com.project.reportsystem.entity;

public enum ActionType {
    VIEW(1), ACCEPT(2), REJECT(3), REQUEST_CHANGES(4);

    private int index;

    ActionType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
