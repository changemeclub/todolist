package com.changeme.todolist.model;

/**
 * Created by ldc on 15-7-23.
 */
public class TaskDoLog {
    private int taskId;
    private String taskName;
    private String doDate;//当天完成时间

    public TaskDoLog(int taskId, String taskName, String doDate) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.doDate = doDate;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDoDate() {
        return doDate;
    }

    public void setDoDate(String doDate) {
        this.doDate = doDate;
    }
}
