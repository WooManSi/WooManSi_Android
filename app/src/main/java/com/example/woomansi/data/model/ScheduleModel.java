package com.example.woomansi.data.model;

public class ScheduleModel {
    private String name;
    private String description;
    private String startTime;   // format: "HH:mm" (예: "14:30")
    private String endTime;
    private String color;       // format: "#ffffff"

    public ScheduleModel() {}

    public ScheduleModel(String name, String description, String startTime, String endTime, String color) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
