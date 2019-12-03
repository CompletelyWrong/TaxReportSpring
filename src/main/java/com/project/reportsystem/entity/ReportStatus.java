package com.project.reportsystem.entity;

public enum ReportStatus {
    NEW(1), ACCEPTED(2), REJECTED(3), UPDATED(4);
    private int index;

    ReportStatus(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
