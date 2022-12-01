package com.example.woomansi.util

import com.cometj03.composetimetable.ScheduleDayData
import com.cometj03.composetimetable.ScheduleEntity
import com.cometj03.composetimetable.TimeTableData
import com.example.woomansi.data.model.ScheduleModel

class ScheduleTypeTransform {
    companion object {
        @JvmStatic
        fun scheduleMapToTimeTableData(
            keyList: List<String>,
            scheduleModelMap: Map<String, List<ScheduleModel>?>
        ) = TimeTableData(
            keyList.map { key ->
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