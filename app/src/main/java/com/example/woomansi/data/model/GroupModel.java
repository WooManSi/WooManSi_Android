package com.example.woomansi.data.model;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupModel implements Serializable {
  private String groupName;
  private String groupPassword;
  private String groupCreateDate;
  private String leaderUid;
  private ArrayList<String> memberList; //리더포함

  public GroupModel() {}

  public GroupModel(String groupName, String groupPassword, String groupCreateDate, String leaderUid, ArrayList<String> memberList) {
    this.groupName = groupName;
    this.groupPassword = groupPassword;
    this.groupCreateDate = groupCreateDate;
    this.leaderUid = leaderUid;
    this.memberList = memberList;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getGroupPassword() {
    return groupPassword;
  }

  public void setGroupPassword(String groupPassword) {
    this.groupPassword = groupPassword;
  }

  public String getGroupCreateDate() {
    return groupCreateDate;
  }

  public void setGroupCreateDate(String groupCreateDate) {
    this.groupCreateDate = groupCreateDate;
  }

  public String getLeaderUid() {
    return leaderUid;
  }

  public void setLeaderUid(String leaderUid) {
    this.leaderUid = leaderUid;
  }

  public ArrayList<String> getMemberList() {
    return memberList;
  }

  public void setMemberList(ArrayList<String> memberList) {
    this.memberList = memberList;
  }
}
