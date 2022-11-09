package com.example.woomansi.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.woomansi.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class GroupCreateActivity extends AppCompatActivity {

    ImageButton cancelBtn;
    Button createGroupBtn;
/*
    //아래로는 캘린더 코드
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener startDatePicker = (view, year, month, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateStartDate();
    };

    DatePickerDialog.OnDateSetListener endDatePicker = (view, year, month, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateEndDate();
    };*/

    //onCreate 함수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        //취소 버튼
        cancelBtn = findViewById(R.id.groupCreate_cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroupMainActivity.class);
                startActivity(intent);
            }
        });

        //그룹생성 버튼
        createGroupBtn = findViewById(R.id.groupCreate_createGroupButton);
        createGroupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*TODO: 그룹명, 비밀번호, 날짜데이터 들어왔는지 모두 검사하고,
                   정상이면 서버에 그룹데이터 넣고 사용자 그룹리스트에 그룹 추가해줌.
                 */
                Intent intent = new Intent(getApplicationContext(), GroupMainActivity.class);
                startActivity(intent);
            }
        });
/*
        //시작 날짜
        EditText start_Date = (EditText) findViewById(R.id.editStartDate);
        start_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(GroupCreateActivity.this, startDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //종료 날짜
        EditText end_Date = (EditText) findViewById(R.id.editEndDate);
        end_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(GroupCreateActivity.this, endDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //시작 시간
        //TODO: 시간단위를 1시간으로? 시간표에 넣을 최소시간? (만약 사용자가 11시부터 11시 반까지 했다면 그룹시간표는 30분밖에 안보여짐
        final EditText et_time = (EditText) findViewById(R.id.editStartTime);
        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(GroupCreateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "오전";
                        // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "오후";
                        }


                        // EditText에 출력할 형식 지정
                        if(selectedMinute == 0) et_time.setText(state + " " + selectedHour + ":" + "00");
                        if(selectedHour == 0) et_time.setText(state + " " + "12" + ":" + selectedMinute);
                        if(selectedMinute == 0 && selectedHour == 0) et_time.setText(state + " " + "12" + ":" + "00");
                        else et_time.setText(state + " " + selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });*/
    }
/*

    private void updateStartDate() {
        String myFormat = "yyyy년 MM월 dd일 (E)";    // 출력형식: 2022년 11월 1일 (화)
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText start_date = (EditText) findViewById(R.id.editStartDate);
        start_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEndDate() {
        String myFormat = "yyyy년 MM월 dd일 (E)";    // 출력형식: 2022년 11월 1일 (화)
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText end_date = (EditText) findViewById(R.id.editEndDate);
        end_date.setText(sdf.format(myCalendar.getTime()));
    }
*/

}

