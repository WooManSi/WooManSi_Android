package com.example.woomansi.data.model;

public class VoteResultModel {
  private String dayOfWeek;   // 요일 (예: "월")
  private String startTime;   // format: "HH:mm" (예: "14:30")
  private String endTime;

  public VoteResultModel(String dayOfWeek, String startTime, String endTime) {
    this.dayOfWeek = dayOfWeek;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public String getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(String dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
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
}
