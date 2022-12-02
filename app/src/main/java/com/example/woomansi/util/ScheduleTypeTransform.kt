package com.example.woomansi.util

import com.cometj03.composetimetable.ScheduleDayData
import com.cometj03.composetimetable.ScheduleEntity
import com.cometj03.composetimetable.TimeTableData
import com.example.woomansi.data.model.ScheduleModel

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
                            // TODO: Color parse
                        )
                    } ?: emptyList()
                )
            }
        )
    }
}