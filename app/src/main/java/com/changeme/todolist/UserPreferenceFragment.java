package com.changeme.todolist;


import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.userpreferences);
    }
}
