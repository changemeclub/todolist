package com.changeme.todolist;

import android.annotation.TargetApi;
import android.os.Build;
import android.preference.PreferenceActivity;

import java.util.List;

public class MyPreferenceActivity extends PreferenceActivity {

    private static final boolean ALWAYS_SIMPLE_PREFS = false;

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preferenceheaders, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }
}
