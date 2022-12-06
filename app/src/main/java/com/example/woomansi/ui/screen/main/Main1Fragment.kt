package com.example.woomansi.ui.screen.main

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cometj03.composetimetable.ComposeTimeTable
import com.example.woomansi.R
import com.example.woomansi.data.model.ScheduleModel
import com.example.woomansi.ui.viewmodel.Main1ViewModel
import com.example.woomansi.util.TimeFormatUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.ChipGroup
import java.time.LocalTime
import kotlin.random.Random

class Main1Fragment : Fragment(R.layout.fragment_main1) {

    companion object {
        @JvmStatic
        fun newInstance(): Main1Fragment = Main1Fragment()
    }

    private lateinit var viewModel: Main1ViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var scheduleCreateDialog: BottomSheetDialog

    private val dayNameList by lazy { resources.getStringArray(R.array.day_name) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(Main1ViewModel::class.java)

        progressBar = view.findViewById(R.id.pb_loading)
        bottomSheetSetting()

        view.findViewById<ComposeView>(R.id.cv_time_table).apply {
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val tableData = viewModel.getTimeTableData(dayNameList.toList()).observeAsState()
                val scrollState = rememberScrollState()

                tableData.value?.let {
                    ComposeTimeTable(
                        timeTableData = it,
                        onCellClick = { column, row, _ ->
                            val key = dayNameList[column]
                            viewModel.scheduleMap[key]?.get(row)?.let { scheduleModel ->
                                showScheduleDialog(key, scheduleModel)
                            }
                        },
                        modifier = Modifier.verticalScroll(scrollState)
                    )
                }
            }
        }

        // viewModel data observing
        viewModel.isLoading.observe(viewLifecycleOwner) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            if (msg == null) {
                // 에러가 없다는 뜻
                scheduleCreateDialog.cancel()
            } else {
                showToast(msg)
            }
            scheduleCreateDialog.setCancelable(true)
        }
    }

    private fun showScheduleDialog(dayName: String, scheduleModel: ScheduleModel) {
        AlertDialog.Builder(requireContext())
            .setTitle(scheduleModel.name)
            .setMessage("${scheduleModel.startTime} ~ ${scheduleModel.endTime}\n\n" +
                    scheduleModel.description)
            .setPositiveButton("확인", null)
            .setNegativeButton("삭제하기") { _, _ -> showDeleteDialog(dayName, scheduleModel)}
            .create()
            .show()
    }

    private fun showDeleteDialog(dayName: String, scheduleModel: ScheduleModel) {
        AlertDialog.Builder(requireContext())
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("알림")
            .setMessage("정말 \"${scheduleModel.name}\" 일정을 삭제하시겠습니까?")
            .setPositiveButton("확인") { _, _, ->
                viewModel.deleteSchedule(dayName, scheduleModel)
            }
            .setNegativeButton("취소", null)
            .create()
            .show()
    }

    private fun bottomSheetSetting() {
        scheduleCreateDialog = BottomSheetDialog(requireActivity()).apply {
            setContentView(R.layout.bottom_sheet_create_schedule)

            val etTitle = findViewById<EditText>(R.id.et_title)!!
            val etDescription = findViewById<TextView>(R.id.et_description)!!
            val tvCreate = findViewById<TextView>(R.id.tv_create)!!
            val chipGroup = findViewById<ChipGroup>(R.id.cg_days)!!
            val tvStartTime = findViewById<TextView>(R.id.tv_start_time)!!
            val tvEndTime = findViewById<TextView>(R.id.tv_end_time)!!

            var curStartTime = LocalTime.now()
            var curEndTime = LocalTime.now()
            var curDayOfWeek = 0 // 1 ~ 7 == 월 ~ 일

            tvCreate.setOnClickListener {
                val title = etTitle.text.toString()
                val description = etDescription.text.toString()
                if (!validateInputData(title, curDayOfWeek, curStartTime, curEndTime))
                    return@setOnClickListener

                val colorArray = resources.getStringArray(R.array.timetable_color_array)
                val randInt = Random.nextInt(colorArray.size)

                setCancelable(false)
                viewModel.createSchedule(
                    title, description,
                    dayNameList[curDayOfWeek-1],
                    curStartTime,
                    curEndTime,
                    colorArray[randInt],
                    dayNameList.toMutableList()
                )
            }

            tvStartTime.setOnClickListener {
                showTimePickerDialog(curStartTime) { hour, minute ->
                    curStartTime = LocalTime.of(hour, minute)
                    (it as TextView).text = TimeFormatUtil.formatWithAmPm(hour, minute)
                }
            }

            tvEndTime.setOnClickListener {
                showTimePickerDialog(curEndTime) { hour, minute ->
                    curEndTime = LocalTime.of(hour, minute)
                    (it as TextView).text = TimeFormatUtil.formatWithAmPm(hour, minute)
                }
            }

            chipGroup.invalidate()
            (0 until chipGroup.childCount).forEach {
                chipGroup.getChildAt(it).id = it + 1
            }
            chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                val id = checkedIds.first()
                curDayOfWeek = id
            }
        }
    }

    private fun validateInputData(
        title: String,
        dayOfWeekIndex: Int,
        startTime: LocalTime,
        endTime: LocalTime
    ): Boolean {
        if (title.isEmpty()) {
            showToast("제목을 입력해주세요")
            return false
        }
        if (dayOfWeekIndex !in 1..7) {
            showToast("요일을 선택해주세요")
            return false
        }
        if (!startTime.isBefore(endTime)) {
            showToast("시작 시간이 종료 시간보다 앞서 있어야 합니다")
            return false
        }
        return true
    }

    private fun showTimePickerDialog(
        time: LocalTime,
        onTimeSelect: (Int, Int) -> Unit
    ) {
        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute -> onTimeSelect(hourOfDay, minute)},
            time.hour, time.minute, false
        ).show()
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar_with_plus_btn, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_plus -> {
                scheduleCreateDialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
