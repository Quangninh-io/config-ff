package com.bkit.skinff.model;


import java.io.Serializable;

public class FileData  implements Serializable {
    private String image;
    private String model;
    private String name;
    private String time;
    private String type;
    private String documentId;
    private String nameFile;
    private boolean checkName;

    public boolean getCheckName() {
        return checkName;
    }

    public void setCheckName(boolean checkName) {
        this.checkName = checkName;
    }

    public FileData(String image, String model, String name, String time, String type, String documentId, String nameFile) {
        this.image = image;
        this.model = model;
        this.name = name;
        this.time = time;
        this.type = type;
        this.documentId = documentId;
        this.nameFile = nameFile;
    }

    public FileData(String image, String model, String name, String time, String type, String documentId, String nameFile, boolean checkName) {
        this.image = image;
        this.model = model;
        this.name = name;
        this.time = time;
        this.type = type;
        this.documentId = documentId;
        this.nameFile = nameFile;
        this.checkName = checkName;
    }

    public FileData() {
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
