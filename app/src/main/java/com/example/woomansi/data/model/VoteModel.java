package com.example.woomansi.data.model;

import java.util.List;
import java.util.Map;

public class VoteModel {
  private Map<String, List<VoteScheduleModel>> voteScheduleList; // 투표 목록들 리스트 <요일, 시간>
  private List<String> voteFinishedMember; //투표한 사람 목록 (멤버리스트 전원과 일치하면 투표를 끝내기 위함)

  public VoteModel() {
  }

  public VoteModel(
      Map<String, List<VoteScheduleModel>> voteScheduleList,
      List<String> voteFinishedMember) {
    this.voteScheduleList = voteScheduleList;
    this.voteFinishedMember = voteFinishedMember;
  }

  public Map<String, List<VoteScheduleModel>> getVoteScheduleList() {
    return voteScheduleList;
  }

  public void setVoteScheduleList(Map<String, List<VoteScheduleModel>> voteScheduleList) {
    this.voteScheduleList = voteScheduleList;
  }

  public List<String> getVoteFinishedMember() {
    return voteFinishedMember;
  }

  public void setVoteFinishedMember(List<String> voteFinishedMember) {
    this.voteFinishedMember = voteFinishedMember;
  }
}
