package com.example.woomansi.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import com.cometj03.composetimetable.ComposeTimeTable
import com.example.woomansi.R

class Main1Fragment : Fragment(R.layout.fragment_main1) {

    companion object {
        @JvmStatic
        fun newInstance(): Main1Fragment = Main1Fragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    }
}
