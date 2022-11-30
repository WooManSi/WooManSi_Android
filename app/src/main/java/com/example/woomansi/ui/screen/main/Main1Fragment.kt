package com.example.woomansi.ui.screen.main

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cometj03.composetimetable.ComposeTimeTable
import com.example.woomansi.R
import com.example.woomansi.ui.viewmodel.Main1ViewModel
import com.example.woomansi.util.DateFormatUtil
import com.example.woomansi.util.UserCache
import com.google.android.material.bottomsheet.BottomSheetDialog

class Main1Fragment : Fragment(R.layout.fragment_main1) {

    companion object {
        @JvmStatic
        fun newInstance(): Main1Fragment = Main1Fragment()
    }

    private lateinit var viewModel: Main1ViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var scheduleCreateDialog: BottomSheetDialog

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
                    dayNames = listOf("월", "화", "수", "목", "금"),
                    timeTableData = timeTableData,
                    onCellClick = {}
                )
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        val uid = UserCache.getUser(requireContext())?.idToken ?: "test"
        viewModel.getSchedules(uid)
    }

    private fun bottomSheetSetting() {
        scheduleCreateDialog = BottomSheetDialog(requireActivity()).apply {
            setContentView(R.layout.bottom_sheet_create_schedule)

            val etTitle = findViewById<EditText>(R.id.et_title)!!
            val tvCreate = findViewById<TextView>(R.id.tv_create)!!
            val tvStartTime = findViewById<TextView>(R.id.tv_start_time)!!
            val tvEndTime = findViewById<TextView>(R.id.tv_end_time)!!

            var curStartTime: String = "00:00"
            var curEndTime: String = "00:00"

            tvCreate.setOnClickListener {
                viewModel.createSchedule(
                    etTitle.text.toString(),
                    curStartTime,
                    curEndTime
                )
            }

            tvStartTime.setOnClickListener {
                showTimePickerDialog(curStartTime) { hour, minute ->
                    curStartTime = DateFormatUtil.dateToString(hour, minute)
                    val s = if (hour <= 12) "오전" else "오후"
                    val h = if (hour <= 12) hour else hour - 12
                    (it as TextView).text = String.format("%s %02d:%02d", s, h, minute)
                }
            }

            tvEndTime.setOnClickListener {
                showTimePickerDialog(curEndTime) { hour, minute ->
                    curEndTime = DateFormatUtil.dateToString(hour, minute)
                    val s = if (hour <= 12) "오전" else "오후"
                    val h = if (hour <= 12) hour else hour - 12
                    (it as TextView).text = String.format("%s %02d:%02d", s, h, minute)
                }
            }
        }
    }

    private fun showTimePickerDialog(
        initialTime: String,
        onTimeSelect: (Int, Int) -> Unit
    ) {
        val (curHour, curMin) = initialTime.let {
            val date = DateFormatUtil.stringToDate(it)
            listOf(date.hour, date.minute)
        }

        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute -> onTimeSelect(hourOfDay, minute)},
            curHour, curMin, false
        ).show()
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
