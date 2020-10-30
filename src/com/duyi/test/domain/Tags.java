package com.duyi.test.domain;

public class Tags {

    private long tid;
    private String tagsName;

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getTagsName() {
        return tagsName;
    }

    public void setTagsName(String tagsName) {
        this.tagsName = tagsName;
    }

    @Override
    public String toString() {
        return "Tags{" +
                "tid=" + tid +
                ", tagsName='" + tagsName + '\'' +
                '}';
    }
}
