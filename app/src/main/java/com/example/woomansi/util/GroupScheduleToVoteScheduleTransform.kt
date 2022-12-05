package com.example.woomansi.util

import com.example.woomansi.data.model.VoteModel
import com.example.woomansi.data.model.VoteScheduleModel

// GroupScheduleTypeTransform 클래스에 넣고 싶었으나, @JvmStatic을 사용하기 위해선 companion object가 필수이기 때문에 어쩔 수 없이 별도분리.
// groupSchedule -> VoteSchedule로 변환하는 역할
class GroupScheduleToVoteScheduleTransform {
    companion object {
        @JvmStatic
        fun groupScheduleMapToVoteSchedule(
                dayNameList: List<String>, // 요일 이름
                groupScheduleMap: Map<String, List<Int>>, // 그룹 스케줄 데이터
                overlapPeople: Int
        ): VoteModel {
            val voteScheduleList = mutableMapOf<String, List<VoteScheduleModel>>()
            dayNameList.map { key ->
                val intList = groupScheduleMap[key]
                val voteScheduleModel = CalculationUtil.calculateIndexToTime(intList, overlapPeople)

                voteScheduleList.put(key, voteScheduleModel)
            }

            return VoteModel(voteScheduleList, listOf<String>())
        }
    }
}