package com.example.woomansi.ui.screen.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cometj03.composetimetable.ComposeTimeTable
import com.example.woomansi.R
import com.example.woomansi.ui.viewmodel.Main1ViewModel

class Main1Fragment : Fragment(R.layout.fragment_main1) {

    companion object {
        @JvmStatic
        fun newInstance(): Main1Fragment = Main1Fragment()
    }

    private lateinit var viewModel: Main1ViewModel

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(Main1ViewModel::class.java)

        progressBar = view.findViewById(R.id.pb_loading)

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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar_with_plus_btn, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_plus -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
