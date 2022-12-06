package com.example.woomansi.ui.screen.group

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
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
import com.example.woomansi.ui.screen.vote.VoteCreateActivity
import com.example.woomansi.ui.screen.vote.VoteJoinActivity
import com.example.woomansi.ui.screen.vote.VoteResultActivity
import com.example.woomansi.ui.viewmodel.GroupDetailViewModel
import com.example.woomansi.ui.viewmodel.VoteCreateViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog

class GroupDetailActivity : AppCompatActivity() {

    private lateinit var groupInfoBottomSheet: BottomSheetDialog
    private lateinit var voteInfoBottomSheet: BottomSheetDialog

    private lateinit var viewModel: GroupDetailViewModel
    private val dayNameList by lazy { resources.getStringArray(R.array.day_name) }

    private val groupData by lazy {
        intent.getSerializableExtra("group") as GroupModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_detail_with_appbar)

        initializeBottomSheets()

        findViewById<MaterialToolbar>(R.id.groupDetail_topAppBar).apply {
            title = groupData.groupName
            setNavigationOnClickListener { finish() }
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.item_group_info -> {
                        groupInfoBottomSheet.show()
                        true
                    }
                    R.id.item_group_vote -> {
                        voteInfoBottomSheet.show()
                        true
                    }
                    else -> false
                }
            }
        }

        viewModel = ViewModelProvider(this).get(GroupDetailViewModel::class.java)

        val composeView: ComposeView = findViewById(R.id.voteCreate_cv_time_table)
        composeView.setContent {
            val tableData = viewModel.getTimeTableData(dayNameList.toList(), groupData).observeAsState()
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

    private fun initializeBottomSheets() {
        groupInfoBottomSheet = BottomSheetDialog(this).apply {
            setContentView(R.layout.bottom_sheet_group_info)

            // 멤버 리스트 버튼
            findViewById<Button>(R.id.bottomSheet_groupInfo_btn_memberList)?.apply {
                setOnClickListener {
                    Intent(this@GroupDetailActivity, MemberListActivity::class.java).also {
                        it.putExtra("group", groupData)
                        startActivity(it)
                    }
                    cancel()
                }
            }
            // 그룹 정보 및 탈퇴 버튼
            findViewById<Button>(R.id.bottomSheet_groupInfo_btn_groupInfo)?.apply {
                setOnClickListener {
                    Intent(this@GroupDetailActivity, GroupInfoActivity::class.java).also {
                        it.putExtra("group", groupData)
                        startActivity(it)
                    }
                    cancel()
                }
            }
        }

        voteInfoBottomSheet = BottomSheetDialog(this).apply {
            setContentView(R.layout.bottom_sheet_vote)

            // 투표 생성하기 버튼
            findViewById<Button>(R.id.bottomSheet_vote_btn_createVote)?.apply {
                setOnClickListener {
                    Intent(this@GroupDetailActivity, VoteCreateActivity::class.java).also {
                        it.putExtra("group", groupData)
                        startActivity(it)
                    }
                    cancel()
                }
            }
            // 투표 참여하기 버튼
            findViewById<Button>(R.id.bottomSheet_vote_btn_joinVote)?.apply {
                setOnClickListener {
                    Intent(this@GroupDetailActivity, VoteJoinActivity::class.java).also {
                        it.putExtra("group", groupData)
                        startActivity(it)
                    }
                    cancel()
                }
            }
            // 투표 결과 확인 버튼
            findViewById<Button>(R.id.bottomSheet_vote_btn_voteResult)?.apply {
                setOnClickListener {
                    Intent(this@GroupDetailActivity, VoteResultActivity::class.java).also {
                        it.putExtra("group", groupData)
                        startActivity(it)
                    }
                    cancel()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_appbar_for_group_detail, menu)
        return true
    }
}