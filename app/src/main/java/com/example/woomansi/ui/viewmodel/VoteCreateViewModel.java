package com.example.woomansi.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.cometj03.composetimetable.TimeTableData;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.repository.FirebaseGroupSchedule;
import com.example.woomansi.util.GroupScheduleTypeTransform;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class VoteCreateViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<TimeTableData> timeTableData;

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<TimeTableData> getTimeTableData(List<String> dayNameList, GroupModel groupModel) {
        if (timeTableData == null) {
            timeTableData = new MutableLiveData<>();
            loadSchedules(dayNameList, groupModel);
        }
        return timeTableData;
    }

    public void loadSchedules(List<String> dayNameList, GroupModel groupModel) {
        if (groupModel == null)
            return;

        FirebaseFirestore
            .getInstance()
            .collection("groups")
            .whereEqualTo("groupName", groupModel.getGroupName())
            .whereEqualTo("groupPassword", groupModel.getGroupPassword())
            .get()
            .addOnCompleteListener(task -> {
                //그룹을 찾았을 경우
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    DocumentReference group = document.getReference();
                    group.getId();
                    System.out.println(group.getId());

                    isLoading.setValue(true);
                    FirebaseGroupSchedule.fetchGroupSchedule(
                        group.getId(),
                        dayNameList,
                        groupSchedule-> {
                            TimeTableData tableData
                                = GroupScheduleTypeTransform.groupScheduleMapToTimeTableData(dayNameList, groupSchedule);
                            timeTableData.setValue(tableData);
                            isLoading.setValue(false);
                        },
                        message -> {
                            errorMessage.setValue(message);
                            isLoading.setValue(false);
                        });
                }
            });
    }
}
