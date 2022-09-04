package com.tellme.kelvin.campconnect;

public class Conv {

    public String name, online;
    public boolean seen;
    public long timestamp;

    public Conv(){
        this.name = name;
        this.online = online;

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }
    public String getOnline() {

        return online;
    }

    public void setOnline(String online) {

        this.online = online;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Conv(boolean seen, long timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }
}