package com.example.woomansi.ui.screen.vote

import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
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
import com.example.woomansi.ui.viewmodel.VoteCreateViewModel
import com.google.android.material.appbar.MaterialToolbar

class VoteCreateActivity : AppCompatActivity() {

    private lateinit var topAppBar: MaterialToolbar
    private lateinit var completeBtn: Button
    private lateinit var spinner: Spinner

    private lateinit var viewModel: VoteCreateViewModel

    private val dayNameList by lazy { resources.getStringArray(R.array.day_name) }

    private val groupData by lazy {
        intent.getSerializableExtra("group") as GroupModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote_create_with_appbar)

        topAppBar = findViewById(R.id.voteCreate_topAppBar)
        topAppBar.setNavigationOnClickListener { finish() }

        completeBtn = findViewById(R.id.voteCreate_btn_complete)
        completeBtn.setOnClickListener {
            //TODO : 사용자가 클릭한 시간들을 vote 관련 데이터 모델 클래스에 담아 서버에 저장하는 코드 구현
            finish()
        }

        spinner = findViewById(R.id.voteCreate_spinner)

        viewModel = ViewModelProvider(this).get(VoteCreateViewModel::class.java)

        val composeView: ComposeView = findViewById(R.id.voteCreate_cv_time_table)
        composeView.setContent {
                val tableData = viewModel.getTimeTableData(dayNameList.toList(), groupData, 0).observeAsState()
                val scrollState = rememberScrollState()

                tableData.value?.let {
                    ComposeTimeTable(
                            timeTableData = it,
                            onCellClick = { _, _ -> },
                            modifier = Modifier.verticalScroll(scrollState)
                    )
                }
        }
    }
}