package com.changeme.todolist;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.changeme.todolist.adapter.TaskListAdapter;
import com.changeme.todolist.model.ToDoTask;
import com.changeme.todolist.sql.ToDoContentProvider;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends Activity implements NewItemFragment.OnAddNewItemListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    private TaskListAdapter listAdapter;
    private ArrayList<ToDoTask> taskList;
    private ToDoListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        initTaskListView();
        initTaskLoader();
        intiTaskInfoToday();
    }

    private void initTaskListView(){
        listFragment=(ToDoListFragment)getFragmentManager().findFragmentById(R.id.todoList);
        taskList=new ArrayList<ToDoTask>();
        listAdapter=new TaskListAdapter(this,R.layout.task_list_item,taskList);
        listFragment.setListAdapter(listAdapter);
    }

    private void intiTaskInfoToday(){
        ToDoListFragment toDoListFragment=(ToDoListFragment)this.getFragmentManager().findFragmentById(R.id.todoList);
        ListView todoListView=toDoListFragment.getListView();
        TextView number_today_View=(TextView)todoListView.findViewById(R.id.number_today);
        TextView number_today_complete_View=(TextView)todoListView.findViewById(R.id.number_today_complete);

        number_today_View.setText( String.valueOf(taskList.size()));
        number_today_complete_View.setText(String.valueOf(getSumOfTastTodayDo()));
    }

    //查询今天完成的工作
    private int getSumOfTastTodayDo(){
        int result=0;
        for(ToDoTask task:taskList){
            if(task.isTodayIsDo()==1){
                result+=1;
            }
        }
        return result;
    }

    private void initTaskLoader(){
        getLoaderManager().initLoader(0, null, this);
        listFragment.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "list 被点击", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAddNewItem(ToDoTask item) {
        if(item==null) return;

        ContentResolver contentResolver=getContentResolver();
        ContentValues contentValues=new ContentValues();
        contentValues.put(ToDoContentProvider.NAME_COLUMN,item.getName());
        contentValues.put(ToDoContentProvider.DATE_COLUMN,item.getCreateDate());
        contentValues.put(ToDoContentProvider.PLANDAY_COLUMN,item.getPlanDays());
        contentValues.put(ToDoContentProvider.HABBIT_COLUMN,item.isHabbit());
        contentValues.put(ToDoContentProvider.TODAY_ISDO_COLUMN,item.isTodayIsDo());
        contentValues.put(ToDoContentProvider.DURING_DATE_COLUMN,item.getDuringDays());
        contentValues.put(ToDoContentProvider.INTERRUPT_DAY_COLUMN,item.getInterruptedDays());
        contentValues.put(ToDoContentProvider.IS_COMPLETED_COLUMN,item.isCompleted());
        contentResolver.insert(ToDoContentProvider.CONTENT_URI, contentValues);

        getLoaderManager().restartLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader=new CursorLoader(this,ToDoContentProvider.CONTENT_URI,null,ToDoContentProvider.IS_COMPLETED_COLUMN+"=0",null,ToDoContentProvider.TODAY_ISDO_COLUMN+" ASC,"+ToDoContentProvider.KEY_ID+" DESC");
        return loader;
    }

    @Override
    //TODO 修改任务信息
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        restartInitTaskListAdapter(loader, cursor);
        intiTaskInfoToday();
    }

    private void restartInitTaskListAdapter(Loader<Cursor> loader, Cursor cursor){
        int keyTaskIndex=cursor.getColumnIndexOrThrow(ToDoContentProvider.NAME_COLUMN);
        int createDateIndex=cursor.getColumnIndexOrThrow(ToDoContentProvider.DATE_COLUMN);
        taskList.clear();
        while (cursor.moveToNext()){
            String name=cursor.getString(keyTaskIndex);
            String createDate=cursor.getString(createDateIndex);
            int planDays=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContentProvider.PLANDAY_COLUMN));
            int interruptDays=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContentProvider.INTERRUPT_DAY_COLUMN));
            int duaringDay=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContentProvider.DURING_DATE_COLUMN));
            int isTodayDo=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContentProvider.TODAY_ISDO_COLUMN));
            int habbit=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContentProvider.HABBIT_COLUMN));
            int completed=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContentProvider.IS_COMPLETED_COLUMN));

            ToDoTask toDoTask=new ToDoTask(name,createDate,null, planDays, habbit, interruptDays, duaringDay, completed,isTodayDo);
            taskList.add(toDoTask);
        }
        cursor.close();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo=searchManager.getSearchableInfo(getComponentName());
        SearchView searchView=(SearchView)menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchableInfo);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri uri = Uri.fromFile(new File(getFilesDir(), "test_1.jpg"));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        ShareActionProvider shareProvider = new ShareActionProvider(this);
        shareProvider.setShareIntent(shareIntent);

        MenuItem shareMenuItem=menu.findItem(R.id.menu_share);
        shareMenuItem.setActionProvider(shareProvider);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Toast.makeText(this,id+"",Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}
