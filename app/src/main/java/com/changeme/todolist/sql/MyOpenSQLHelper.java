package com.changeme.todolist.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ldc on 2015/7/1.
 */
public class MyOpenSQLHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME="cm_to_do_list";
    public static final int VERSION=1;

    public static final String KEY_ID="_id";
    public static final String NAME_COLUMN="name";
    public static final String DATE_COLUMN="create_date";

    public static final String TAG_COLUMN="task_tag";
    public static final String DURING_DATE_COLUMN="during_date";
    public static final String PLANDAY_COLUMN="plan_days";
    public static final String INTERRUPT_DAY_COLUMN="interrupt_days";
    public static final String IS_COMPLETED_COLUMN="is_completed";
    public static final String TODAY_ISDO_COLUMN="today_isdo";
    public static final String HABBIT_COLUMN="is_habbit";


    public MyOpenSQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sqlBuffer=new StringBuffer().append("create table ").append(TABLE_NAME).append("(").append(KEY_ID)
                .append(" INTEGER PRIMARY KEY autoincrement,").append(NAME_COLUMN).append(" text not null,")
                .append(DATE_COLUMN).append(" text not null,").append(TAG_COLUMN).append(" text,").append(DURING_DATE_COLUMN)
                .append(" Integer ,").append(PLANDAY_COLUMN).append(" Integer not null,").append(INTERRUPT_DAY_COLUMN)
                .append(" Integer not null, ").append(IS_COMPLETED_COLUMN).append( " Integer not null,").append(TODAY_ISDO_COLUMN)
                .append(" Integer not null ,").append(HABBIT_COLUMN).append(" Integer not null)");
        db.execSQL(sqlBuffer.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists "+TABLE_NAME);
    }
}
