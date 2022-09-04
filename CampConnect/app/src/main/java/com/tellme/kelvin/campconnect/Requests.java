package com.tellme.kelvin.campconnect;

public class Requests {
    public String name, status, image, regno;

    public Requests(){

    }

    public Requests(String name, String status,String image) {
        this.name = name;
        this.status = status;
        this.image = image;
        this.regno = regno;
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
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



}
