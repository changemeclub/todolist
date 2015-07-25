package com.changeme.todolist.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changeme.todolist.R;
import com.changeme.todolist.sql.ToDoContentProvider;
import com.changeme.todolist.model.ToDoTask;

import java.util.List;

/**
 * Created by ldc on 2015/6/30.
 */
public class TaskListAdapter extends ArrayAdapter<ToDoTask> {
    private int resourceId;
    private Context context;

    public TaskListAdapter(Context context, int resource, List<ToDoTask> objects) {
        super(context, resource, objects);
        this.resourceId=resource;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout todoView;
        final ToDoTask toDoTask=getItem(position);
        String taskString=toDoTask.getName();

        if (convertView==null){
            todoView=new LinearLayout(context);
            String inflater=Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater layoutInflater;
            layoutInflater=(LayoutInflater)getContext().getSystemService(inflater);
            layoutInflater.inflate(resourceId,todoView,true);
        }else {
            todoView=(LinearLayout)convertView;
        }

        final TextView taskName=(TextView)todoView.findViewById(R.id.new_task_name);
        taskName.setText(taskString);
        if(toDoTask.isTodayIsDo()==1){
            taskName.setTextColor(Color.GRAY);
        }

        TextView taskDescription=(TextView)todoView.findViewById(R.id.task_current_description);

        taskDescription.setText("计划10天，坚持7天，间断1天");

        final CheckBox isComplete=(CheckBox)todoView.findViewById(R.id.check_task_complete);
        isComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isComplete.isChecked()){
                    toDoTask.setTodayIsDo(1);
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(ToDoContentProvider.TODAY_ISDO_COLUMN, toDoTask.isTodayIsDo());
                    getContext().getContentResolver().update(ToDoContentProvider.CONTENT_URI, contentValues, ToDoContentProvider.KEY_ID + "=?", new String[]{toDoTask.getId() + ""});
//                    TaskListAdapter.this.notifyDataSetChanged();//发起数据状态更新的通知
                    //TODO  1、更新任务状态，并保存到数据库中。
                }
            }
        });

        return todoView;
    }
}
