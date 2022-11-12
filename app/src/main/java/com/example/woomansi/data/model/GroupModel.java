package com.example.woomansi.data.model;

import java.util.ArrayList;

public class GroupModel {
  private String groupName;
  private String groupPassword;
  private String groupCreateDate;
//  private String leaderId;
//  private ArrayList<String> memberList; //리더포함

  public GroupModel(String groupName, String groupPassword, String groupCreateDate) {
    this.groupName = groupName;
    this.groupPassword = groupPassword;
    this.groupCreateDate = groupCreateDate + " ~";
  }

  public String getGroupName() {
    return groupName;
  }

  public String getGroupPassword() {
    return groupPassword;
  }

  public String getGroupCreateDate() {
    return groupCreateDate;
  }
}
