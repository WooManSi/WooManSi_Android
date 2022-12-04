package com.example.woomansi.util

import com.cometj03.composetimetable.ScheduleDayData
import com.cometj03.composetimetable.ScheduleEntity
import com.cometj03.composetimetable.TimeTableData

class GroupScheduleTypeTransform {
    companion object {
        // 그룹 스케줄 데이터 -> 비는 시간 TimeTableData로 변환하는 함수
        @JvmStatic
        fun groupScheduleMapToTimeTableData(
            dayNameList: List<String>, // 요일 이름
            groupScheduleMap: Map<String, List<Int>> // 그룹 스케줄 데이터
        ) = TimeTableData(
            dayNameList.map { key ->
                val intList = groupScheduleMap[key]
                println(intList)
                val groupScheduleModel = CalculationUtil.calculateIndexToTime(intList)
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
    }
}