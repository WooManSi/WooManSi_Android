package com.example.woomansi.data.model;

import java.util.List;
import java.util.Map;

public class GroupTimeTableWrapper {
    private Map<String, List<Integer>> groupTimeTable; // ([월~일][06~24시를 15분간격으로 나눈값])

    public Map<String, List<Integer>> getGroupTimeTable() {
        return groupTimeTable;
    }

    public void setGroupTimeTable(Map<String, List<Integer>> groupTimeTable) {
        this.groupTimeTable = groupTimeTable;
    }
}
