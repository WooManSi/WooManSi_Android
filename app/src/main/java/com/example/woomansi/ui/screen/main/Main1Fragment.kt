package com.example.woomansi.ui.screen.main

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cometj03.composetimetable.ComposeTimeTable
import com.cometj03.composetimetable.ScheduleDayData
import com.cometj03.composetimetable.TimeTableData
import com.example.woomansi.R
import com.example.woomansi.ui.viewmodel.Main1ViewModel
import com.example.woomansi.util.DateFormatUtil
import com.example.woomansi.util.UserCache
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import java.time.LocalTime

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
                ComposeTimeTable(
                    timeTableData = TimeTableData(
                        List(dayNameList.size) { ScheduleDayData(dayNameList[it]) }
                    ),
                    onCellClick = {}
                )
            }
        }

        // viewModel data observing
        viewModel.isLoading.observe(viewLifecycleOwner) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.scheduleCreationErrorMsg.observe(viewLifecycleOwner) { msg ->
            if (msg == null) {
                // 에러가 없다는 뜻
                scheduleCreateDialog.cancel()
            } else {
                showToast(msg)
            }
        }
    }

    private fun bottomSheetSetting() {
        scheduleCreateDialog = BottomSheetDialog(requireActivity()).apply {
            setContentView(R.layout.bottom_sheet_create_schedule)

            val etTitle = findViewById<EditText>(R.id.et_title)!!
            val tvCreate = findViewById<TextView>(R.id.tv_create)!!
            val chipGroup = findViewById<ChipGroup>(R.id.cg_days)!!
            val tvStartTime = findViewById<TextView>(R.id.tv_start_time)!!
            val tvEndTime = findViewById<TextView>(R.id.tv_end_time)!!

            var curStartTime = LocalTime.now()
            var curEndTime = LocalTime.now()
            var curDayOfWeek = 0 // 1 ~ 7 == 월 ~ 일

            tvCreate.setOnClickListener {
                val title = etTitle.text.toString()
                if (!validateInputData(title, curDayOfWeek, curStartTime, curEndTime))
                    return@setOnClickListener

                val dayName = dayNameList[curDayOfWeek-1]
                viewModel.createSchedule(
                    title,
                    dayName,
                    curStartTime,
                    curEndTime
                )
            }

            tvStartTime.setOnClickListener {
                showTimePickerDialog(curStartTime) { hour, minute ->
                    curStartTime = LocalTime.of(hour, minute)
                    (it as TextView).text = DateFormatUtil.formatWithAmPm(hour, minute)
                }
            }

            tvEndTime.setOnClickListener {
                showTimePickerDialog(curEndTime) { hour, minute ->
                    curEndTime = LocalTime.of(hour, minute)
                    (it as TextView).text = DateFormatUtil.formatWithAmPm(hour, minute)
                }
            }

            chipGroup.invalidate()
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
