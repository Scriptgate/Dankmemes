package net.scriptgate.dankmemes.livewallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import net.scriptgate.dankmemes.R;

import java.util.List;

public class WallpaperSettings extends PreferenceActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return ToggleVisibilityFragment.class.getName().equals(fragmentName);
    }

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    /**
     * This fragment shows the components preferences.
     */
    public static class ToggleVisibilityFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.
            // In a real app, you would want this in a shared function that is used to retrieve the SharedPreferences wherever they are needed.
            PreferenceManager.setDefaultValues(getActivity(), "dankmemes_settings", Context.MODE_PRIVATE, R.xml.components_preferences, false);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.components_preferences);

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
}