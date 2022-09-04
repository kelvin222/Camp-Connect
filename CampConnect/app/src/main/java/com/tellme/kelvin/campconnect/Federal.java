package com.tellme.kelvin.campconnect;

public class Federal {

    private String head, office, body, date, user,federalid,time;
    public long timestamp;
    public Federal(){

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

    public String getFederalid() {
        return federalid;
    }

    public void setFederalid(String federalid) {
        this.federalid = federalid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Federal(String head, String office, String body, String date, String user, String federalid, String time) {

        this.head = head;
        this.office = office;
        this.body = body;
        this.date = date;
        this.user = user;
        this.federalid = federalid;
        this.time = time;


    }
    public Federal(long timestamp) {
        this.timestamp = timestamp;
    }

}
