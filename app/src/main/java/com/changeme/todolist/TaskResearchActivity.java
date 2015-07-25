package com.changeme.todolist;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.changeme.todolist.sql.ToDoContentProvider;


public class TaskResearchActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String QUERY_EXTRA_KEY="QUERY_EXTRA_KEY";

    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,null,
                new String[]{ToDoContentProvider.NAME_COLUMN},new int[]{android.R.id.text1},0);
        setListAdapter(adapter);

        parseIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        parseIntent(getIntent());
    }

    private void parseIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String searchQuery=intent.getStringExtra(SearchManager.QUERY);
            performSearch(searchQuery);
        }
    }

    private void performSearch(String query){
        Bundle args=new Bundle();
        args.putString(QUERY_EXTRA_KEY,query);

        getLoaderManager().restartLoader(0,args,this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String query="0";

        if(args!=null){
            query=args.getString(QUERY_EXTRA_KEY);
        }

        String[] projection={ToDoContentProvider.KEY_ID,ToDoContentProvider.NAME_COLUMN};
        String where =ToDoContentProvider.NAME_COLUMN +" like \"%"+query+"%\"";
        String[] whereArgs=null;
        String sortOrder=ToDoContentProvider.NAME_COLUMN +" COLLATE LOCALIZED DESC";

        return new CursorLoader(this,ToDoContentProvider.CONTENT_URI,projection,where,whereArgs,sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Uri uri=ContentUris.withAppendedId(ToDoContentProvider.CONTENT_URI, id);
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }
}
