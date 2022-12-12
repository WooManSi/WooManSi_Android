package com.example.woomansi.ui.screen.vote

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import com.cometj03.composetimetable.ComposeTimeTable
import com.example.woomansi.R
import com.example.woomansi.data.model.GroupModel
import com.example.woomansi.data.repository.FirebaseGroup
import com.example.woomansi.data.repository.FirebaseGroupVote
import com.example.woomansi.ui.viewmodel.VoteJoinViewModel
import com.example.woomansi.util.SharedPreferencesUtil
import com.example.woomansi.util.UserCache
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
        composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        composeView.setContent {
            val tableData = viewModel.getTimeTableData(
                dayNameList.toList(), groupData
            ).observeAsState()
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

        val btnVote = findViewById<Button>(R.id.voteJoin_btn_vote)
        btnVote.setOnClickListener {
            viewModel.castVote(
                UserCache.getUser(this).idToken,
                groupData
            )
        }

        viewModel.errorMessage.observe(this) {
            if (it == null) {
                Toast.makeText(this, "투표 완료!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        checkIsAlreadyVoted()
    }

    private fun checkIsAlreadyVoted() {
        FirebaseGroup.getSpecificGroupId(groupData.groupName, groupData.groupPassword, { groupId ->
            FirebaseGroupVote.getVoteModel(groupId, { voteModel ->
                val userId = UserCache.getUser(this).idToken
                if (voteModel.voteFinishedMember.contains(userId))
                    showWarnDialog()
            }, {})
        }, {})
    }

    private fun showWarnDialog() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("알림")
            .setMessage("이미 투표하셨습니다.")
            .setPositiveButton("뒤로가기") { _, _ -> finish() }
            .setCancelable(false)
            .create().show()
    }
}