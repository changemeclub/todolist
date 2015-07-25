package com.changeme.todolist;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.app.ListFragment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ContactSearchFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;

    private ArrayList<String> contacts;
    private ArrayAdapter<String> adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactSearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contacts=new ArrayList<String>();
        adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1,contacts);
        setListAdapter(adapter);
        popluateContacts();
    }

    private  void popluateContacts(){
        ContentResolver contentResolver=getActivity().getContentResolver();
        String[] projection=new String[]{ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME};
        Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);

        while(cursor.moveToNext()){
            String name=cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            contacts.add(name);
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
