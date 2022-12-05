package com.example.woomansi.data.model;

import java.util.List;
import java.util.Map;

public class VoteModel {
  private Map<String, List<VoteScheduleModel>> voteScheduleList; // 투표 목록들 리스트 <요일, 시간>
  private List<String> voteFinishedMember; //투표한 사람 목록 (멤버리스트 전원과 일치하면 투표를 끝내기 위함)
}
