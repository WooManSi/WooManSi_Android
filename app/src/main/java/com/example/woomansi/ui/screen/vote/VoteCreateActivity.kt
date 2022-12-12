package com.example.woomansi.ui.screen.vote

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.example.woomansi.data.repository.FirebaseGroup
import com.example.woomansi.data.repository.FirebaseGroupVote
import com.example.woomansi.ui.viewmodel.VoteCreateViewModel
import com.example.woomansi.util.SharedPreferencesUtil
import com.google.android.material.appbar.MaterialToolbar

class VoteCreateActivity : AppCompatActivity() {

    private lateinit var topAppBar: MaterialToolbar
    private lateinit var completeBtn: Button

    private lateinit var viewModel: VoteCreateViewModel

    private val dayNameList by lazy { resources.getStringArray(R.array.day_name) }

    private val PEOPLE_LIMIT_KEY by lazy { "${groupData.groupName}-peopleOverlapLimit" }

    private val groupData by lazy {
        intent.getSerializableExtra("group") as GroupModel
    }

    var curPeopleOverlapLimit: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote_create_with_appbar)

        topAppBar = findViewById(R.id.voteCreate_topAppBar)
        topAppBar.setNavigationOnClickListener { finish() }

        completeBtn = findViewById(R.id.voteCreate_btn_complete)
        completeBtn.setOnClickListener {
            createVote()
        }

        viewModel = ViewModelProvider(this).get(VoteCreateViewModel::class.java)

        val composeView: ComposeView = findViewById(R.id.voteCreate_cv_time_table)
        curPeopleOverlapLimit = SharedPreferencesUtil.getInt(this, PEOPLE_LIMIT_KEY)
        composeView.setContent {
            val tableData = viewModel.getTimeTableData(
                dayNameList.toList(), groupData, curPeopleOverlapLimit
            ).observeAsState()
            val scrollState = rememberScrollState()

            tableData.value?.let {
                ComposeTimeTable(
                    timeTableData = it,
                    onCellClick = { _, _, _ -> },
                    modifier = Modifier.verticalScroll(scrollState)
                )
            }
        }

        val progressBar = findViewById<ProgressBar>(R.id.pb_loading)
        viewModel.isLoading.observe(this) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.errorMessage.observe(this) {
            if (it != null) {
                Log.d("TEST", "onCreate: $it")
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                if (it.equals("success"))
                    finish()
            }
        }

        initialSpinner()
    }

    private fun createVote() {
        FirebaseGroup.getSpecificGroupId(
            groupData.groupName, groupData.groupPassword, { groupId ->
                FirebaseGroupVote.checkIfVoteExists(groupId) { exist ->
                    if (exist)
                        showAlertDialog()
                    else
                        viewModel.createVote(dayNameList.toList(), groupData, curPeopleOverlapLimit)
                }
            }, { _ -> }
        )
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("알림")
            .setMessage("새 투표를 생성하시면 진행중인 투표는 삭제됩니다. 계속하시겠습니까?")
            .setNegativeButton("취소") { _, _ -> }
            .setPositiveButton("생성하기") { _, _ ->
                viewModel.createVote(dayNameList.toList(), groupData, curPeopleOverlapLimit)
            }
            .create().show()
    }

    private fun initialSpinner() {
        val peopleSpinner = findViewById<Spinner>(R.id.spinner_people_count)
        val groupMemberCount = groupData.memberList.size
        val spinnerAdapter = ArrayAdapter(
            this@VoteCreateActivity,
            android.R.layout.simple_spinner_item,
            (0 until groupMemberCount).toList()
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        peopleSpinner.apply {
            adapter = spinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    viewModel.updateOverlapedPeople(dayNameList.toList(), pos)
                    curPeopleOverlapLimit = pos
                }

                override fun onNothingSelected(p0: AdapterView<*>?) = Unit
            }
            setSelection(curPeopleOverlapLimit)
        }
    }
}