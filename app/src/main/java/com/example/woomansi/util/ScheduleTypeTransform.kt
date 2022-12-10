package com.example.woomansi.util

import androidx.compose.ui.graphics.Color
import com.cometj03.composetimetable.ScheduleDayData
import com.cometj03.composetimetable.ScheduleEntity
import com.cometj03.composetimetable.TimeTableData
import com.example.woomansi.data.model.ScheduleModel
import com.example.woomansi.data.model.VoteModel
import com.example.woomansi.data.model.VoteScheduleModel

class ScheduleTypeTransform {
    companion object {
        // 스케줄 데이터 -> TimeTableData로 변환하는 함수
        @JvmStatic
        fun scheduleMapToTimeTableData(
            dayNameList: List<String>, // 요일 이름
            scheduleModelMap: Map<String, List<ScheduleModel>?> // 스케줄 데이터
        ) = TimeTableData(
            dayNameList.map { key ->
                val scheduleModel = scheduleModelMap[key]
                ScheduleDayData(
                    key,
                    scheduleModel?.map { model ->
                        ScheduleEntity(
                            model.name,
                            model.description,
                            TimeFormatUtil.stringToTime(model.startTime),
                            TimeFormatUtil.stringToTime(model.endTime),
                            Color(android.graphics.Color.parseColor(model.color))
                        )
                    } ?: emptyList()
                )
            }
        )

        // 그룹 스케줄 데이터 -> 비는 시간 TimeTableData로 변환하는 함수
        @JvmStatic
        fun groupScheduleMapToTimeTableData(
                dayNameList: List<String>, // 요일 이름
                groupScheduleMap: Map<String, List<Int>>, // 그룹 스케줄 데이터
                overlapPeople: Int
        ) = TimeTableData(
                dayNameList.map { key ->
                    val intList = groupScheduleMap[key]
                    val groupScheduleModel = CalculationUtil.calculateIndexToTime(intList, overlapPeople)
                    ScheduleDayData(
                            key,
                            groupScheduleModel.map { model ->
                                ScheduleEntity(
                                        "${model.startTime} ~ ${model.endTime}",
                                        "",
                                        TimeFormatUtil.stringToTime(model.startTime),
                                        TimeFormatUtil.stringToTime(model.endTime),
                                        // TODO: Color parse
                                )
                            } ?: emptyList()
                    )
                }
        )

        // 그룹 스케줄 데이터 -> 투표화면 시간표에 넣을 data로 변환하는 함수
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