package com.changeme.todolist.sql;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by ldc on 2015/7/1.
 */
public class ToDoContentProvider extends ContentProvider {
    private static final int ALLROWS=1;
    private static final int SINGlE_ROW=2;
    private static final int SEARCH=3;

    private MyOpenSQLHelper myOpenSQLHelper;

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

    private static final HashMap<String,String> SEARCH_PROJECT_MAP;

    public static final Uri CONTENT_URI=Uri.parse("content://com.example.ldc.todolistprovider/elements");

    private static final UriMatcher uriMatcher;

    static {
        SEARCH_PROJECT_MAP=new HashMap<String,String>();
        SEARCH_PROJECT_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_1,NAME_COLUMN +" as "+SearchManager.SUGGEST_COLUMN_TEXT_1);
        SEARCH_PROJECT_MAP.put("_id",KEY_ID+" as _id");
    }

    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.ldc.todolistprovider","elements",ALLROWS);
        uriMatcher.addURI("com.example.ldc.todolistprovider","elements/#",SINGlE_ROW);
        uriMatcher.addURI("com.example.ldc.todolistprovider",SearchManager.SUGGEST_URI_PATH_QUERY,SEARCH);
        uriMatcher.addURI("com.example.ldc.todolistprovider",SearchManager.SUGGEST_URI_PATH_QUERY+"/*",SEARCH);
        uriMatcher.addURI("com.example.ldc.todolistprovider",SearchManager.SUGGEST_URI_PATH_SHORTCUT,SEARCH);
        uriMatcher.addURI("com.example.ldc.todolistprovider",SearchManager.SUGGEST_URI_PATH_SHORTCUT+"/*",SEARCH);
    }

    @Override
    public boolean onCreate() {
        myOpenSQLHelper=new MyOpenSQLHelper(getContext(),MyOpenSQLHelper.TABLE_NAME,null,MyOpenSQLHelper.VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db=myOpenSQLHelper.getWritableDatabase();

        String groupBy=null;
        String having=null;

        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)){
            case SINGlE_ROW:
                String rowId=uri.getPathSegments().get(1);
                queryBuilder.appendWhere(MyOpenSQLHelper.KEY_ID+"="+rowId);
                break;
            case SEARCH :
                if(uri.getPathSegments().size()>1) {
                    queryBuilder.appendWhere(NAME_COLUMN + " like \"%" + uri.getPathSegments().get(1) + "%\"");
                    queryBuilder.setProjectionMap(SEARCH_PROJECT_MAP);
                }else{
                    return null;
                }
                break;
            default:break;
        }

        queryBuilder.setTables(MyOpenSQLHelper.TABLE_NAME);

        Cursor cursor=queryBuilder.query(db,projection,selection,selectionArgs,groupBy,having,sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case (ALLROWS): return "vnd.android.cursor.dir/vnd.todolist.elemental";
            case SINGlE_ROW :return "vnd.android.cursor.item/vnd.todolist.elemental";
            case SEARCH:return SearchManager.SUGGEST_MIME_TYPE;
            default: throw  new IllegalArgumentException("Unsupported URI:");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db=myOpenSQLHelper.getWritableDatabase();

        String nullColumnHack=null;

        long id=db.insert(MyOpenSQLHelper.TABLE_NAME,nullColumnHack,values);

        if(id>-1){
            Uri insertId= ContentUris.withAppendedId(CONTENT_URI,id);

            getContext().getContentResolver().notifyChange(uri,null);

            return insertId;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db=myOpenSQLHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case SINGlE_ROW :
                String rowId=uri.getPathSegments().get(1);
                selection=MyOpenSQLHelper.KEY_ID+"="+rowId+(!TextUtils.isEmpty(selection)?" and ("+selection +" )":"");
            default:break;
        }

        if(selection==null) {
            selection="1";
        }

        int deleteCount=db.delete(MyOpenSQLHelper.TABLE_NAME,selection,selectionArgs);

        getContext().getContentResolver().notifyChange(uri,null);
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db=myOpenSQLHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case SINGlE_ROW :
                String rowId=uri.getPathSegments().get(1);
                selection=MyOpenSQLHelper.KEY_ID+"="+rowId+(!TextUtils.isEmpty(selection)?" and ("+selection +" )":"");
            default:break;
        }

        int updateCount=db.update(MyOpenSQLHelper.TABLE_NAME, values, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri,null);
        return updateCount;
    }
}
