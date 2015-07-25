package com.changeme.todolist;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class ToDoListFragment extends ListFragment {
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View taskInfoTodayView= LayoutInflater.from(this.getActivity()).inflate(R.layout.taskinfo_today, null);
        this.getListView().addHeaderView(taskInfoTodayView);
    }
}
