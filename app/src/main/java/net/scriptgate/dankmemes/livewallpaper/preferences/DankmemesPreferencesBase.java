package net.scriptgate.dankmemes.livewallpaper.preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public abstract class DankmemesPreferencesBase extends PreferenceFragment {

    private final int preferencesResourceId;

    protected DankmemesPreferencesBase(int preferencesResourceId) {
        this.preferencesResourceId = preferencesResourceId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make sure default values are applied.
        // In a real app, you would want this in a shared function that is used to retrieve the SharedPreferences wherever they are needed.
        PreferenceManager.setDefaultValues(getActivity(), "dankmemes_settings", Context.MODE_PRIVATE, getPreferencesResourceId(), false);

        // Load the preferences from an XML resource
        addPreferencesFromResource(getPreferencesResourceId());

    }

    private int getPreferencesResourceId() {
         return preferencesResourceId;
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
