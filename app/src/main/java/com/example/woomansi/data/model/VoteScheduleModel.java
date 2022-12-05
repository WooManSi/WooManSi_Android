package com.example.woomansi.data.model;

public class VoteScheduleModel {
  private String startTime; //시작 시간 // format: "HH:mm" (예: "14:30")
  private String endTime; //끝나는 시간
  private int voteNum; //투표수

  public VoteScheduleModel() {
  }

  public VoteScheduleModel(String startTime, String endTime, int voteNum) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.voteNum = voteNum;
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

  public int getVoteNum() {
    return voteNum;
  }

  public void setVoteNum(int voteNum) {
    this.voteNum = voteNum;
  }
}
