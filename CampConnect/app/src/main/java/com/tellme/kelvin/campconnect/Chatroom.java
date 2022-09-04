package com.tellme.kelvin.campconnect;

import android.support.v4.widget.SwipeRefreshLayout;

public class Chatroom {
    private String name,regno, message,state,batch, type, mtime, mdate, messageID;
    private long  time;
    private boolean seen;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String from,to;

    public Chatroom(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Chatroom(String name, String regno, String message, String state, String batch, String type, String mtime, String mdate, String messageID, long time, boolean seen) {
        this.name = name;
        this.regno = regno;
        this.message = message;
        this.type = type;
        this.mtime = mtime;
        this.state = state;
        this.batch = batch;
        this.mdate = mdate;
        this.time = time;
        this.seen = seen;
        this.messageID = messageID;
    }
    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }


    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Chatroom(){

    }

}
