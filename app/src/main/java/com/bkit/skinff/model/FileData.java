package com.bkit.skinff.model;


import java.io.Serializable;

public class FileData  implements Serializable {
    private String time;
    private String image;

    public FileData(String time, String image) {
        this.time = time;
        this.image = image;
    }

    public FileData() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
