package com.tellme.kelvin.campconnect;

public class Camp {

    private String head, office, body, date, user,campid,time,state;
    public long timestamp;
    public Camp(){

    }


    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCampid() {
        return campid;
    }

    public void setCampid(String campid) {
        this.campid = campid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Camp(String head, String office, String body, String date, String user, String campid, String time, String state) {

        this.head = head;
        this.office = office;
        this.body = body;
        this.date = date;
        this.user = user;
        this.campid = campid;
        this.time = time;
        this.state = state;


    }
    public Camp(long timestamp) {
        this.timestamp = timestamp;
    }

}
