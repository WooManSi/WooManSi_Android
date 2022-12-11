package com.example.woomansi.ui.screen.vote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import com.cometj03.composetimetable.ComposeTimeTable
import com.example.woomansi.R
import com.example.woomansi.data.model.GroupModel
import com.example.woomansi.ui.viewmodel.VoteJoinViewModel
import com.example.woomansi.util.SharedPreferencesUtil
import com.google.android.material.appbar.MaterialToolbar

class VoteJoinActivity : AppCompatActivity() {

    private lateinit var viewModel: VoteJoinViewModel

    private val groupData by lazy {
        intent.getSerializableExtra("group") as GroupModel
    }
    private val dayNameList: List<String> by lazy {
        resources.getStringArray(R.array.day_name).toList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote_join_with_appbar)

        viewModel = ViewModelProvider(this).get(VoteJoinViewModel::class.java)

        findViewById<MaterialToolbar>(R.id.voteJoin_topAppBar).apply {
            setNavigationOnClickListener { finish() }
        }

        val composeView: ComposeView = findViewById(R.id.voteJoin_cv_time_table)
        composeView.setContent {
            val tableData = viewModel.getTimeTableData(
                dayNameList.toList(), groupData).observeAsState()
            val scrollState = rememberScrollState()

            tableData.value?.let {
                ComposeTimeTable(
                    timeTableData = it,
                    onCellClick = { column, row, _ ->
                        val key = dayNameList[column]
                        viewModel.clickVoteSchedule(key, row, dayNameList)
                    },
                    modifier = Modifier.verticalScroll(scrollState)
                )
            }
        }
    }
}