package com.changeme.todolist.model;

/**
 * 任务
 * Created by ldc on 15-7-23.
 */
public class ToDoTask {
    private int id;
    private String name;
    private String createDate;
    private String tag;//任务类型
    private int planDays;//计划天数
    private int duringDays;//持续时间
    private int interruptedDays;//间断天数
    private int completed;//整个任务完成状态，是否完成
    private int todayIsDo;//今天是否完成
    private int habbit;//是否是习惯

    public ToDoTask(){

    }
    public  ToDoTask(String name,String createDate,String tag,int planDays,int habbit,int interruptedDays, int duringDays,int completed,int todayIsDo){
        this.name=name;
        this.createDate=createDate;
        this.completed=completed;
        this.tag=tag;
        this.planDays=planDays;
        this.habbit=habbit;
        this.interruptedDays=interruptedDays;
        this.duringDays=duringDays;
        this.completed=completed;
        this.todayIsDo=todayIsDo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getDuringDays() {
        return duringDays;
    }

    public void setDuringDays(int duringDays) {
        this.duringDays = duringDays;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlanDays() {
        return planDays;
    }

    public void setPlanDays(int planDays) {
        this.planDays = planDays;
    }

    public int getInterruptedDays() {
        return interruptedDays;
    }

    public void setInterruptedDays(int interruptedDays) {
        this.interruptedDays = interruptedDays;
    }

    public int isCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int isTodayIsDo() {
        return todayIsDo;
    }

    public void setTodayIsDo(int todayIsDo) {
        this.todayIsDo = todayIsDo;
    }

    public int isHabbit() {
        return habbit;
    }

    public void setHabbit(int habbit) {
        this.habbit = habbit;
    }
}
