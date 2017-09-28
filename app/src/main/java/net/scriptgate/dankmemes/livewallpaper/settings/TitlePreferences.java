package net.scriptgate.dankmemes.livewallpaper.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import net.scriptgate.dankmemes.R;
import net.scriptgate.dankmemes.livewallpaper.Preferences;

public class TitlePreferences extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make sure default values are applied.
        // In a real app, you would want this in a shared function that is used to retrieve the SharedPreferences wherever they are needed.
        PreferenceManager.setDefaultValues(getActivity(), "dankmemes_settings", Context.MODE_PRIVATE, R.xml.title_preferences, false);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.title_preferences);

    }

    @Override
    public void onResume() {
        super.onResume();
        Preferences.registerListener(getPreferences());
    }

    @Override
    public void onPause() {
        super.onPause();
        Preferences.unregisterListener(getPreferences());
    }

    private SharedPreferences getPreferences() {
        return getPreferenceScreen().getSharedPreferences();
    }
}
