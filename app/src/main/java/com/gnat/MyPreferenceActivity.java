package com.gnat;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Patterns;

public class MyPreferenceActivity extends PreferenceActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public boolean validateIP(String ip){
        return Patterns.IP_ADDRESS.matcher(ip).matches();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Context context = getActivity();
            String log_location = context.getExternalCacheDir().toString();
            SwitchPreference logging_toggle = (SwitchPreference) findPreference("logging_toggle");
            logging_toggle.setSummary("Logs can be found at " + log_location);
        }
    }



}