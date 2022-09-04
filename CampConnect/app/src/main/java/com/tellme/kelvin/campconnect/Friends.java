package com.tellme.kelvin.campconnect;

public class Friends {

    public String date, name, status, regno, user;

    public Friends(){


    }

    public Friends(String date,String name, String status, String regno, String user) {
        this.name = name;
        this.status = status;
        this.regno = regno;
        this.date = date;
    }
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }
    public String getRegno() {

        return regno;
    }

    public void setRegno(String regno) {

        this.regno = regno;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

