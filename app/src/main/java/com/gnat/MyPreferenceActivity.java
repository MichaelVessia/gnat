package com.gnat;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

public class MyPreferenceActivity extends PreferenceActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // IP of the user configured server
        String ip = preferences.getString("pref_server_ip", "No IP");
    }

    public boolean validateIP(String ip){
        return Patterns.IP_ADDRESS.matcher(ip).matches();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }



}