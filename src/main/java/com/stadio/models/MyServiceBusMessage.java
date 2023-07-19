package com.stadio.models;

public class MyServiceBusMessage {
    private int id;
    private String fileName;
    private int status;

    public MyServiceBusMessage() {
    }
    public MyServiceBusMessage(String fileName) {
        this.fileName = fileName;
    }
    public MyServiceBusMessage(int id, String fileName, int status) {
        this.id = id;
        this.fileName = fileName;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
