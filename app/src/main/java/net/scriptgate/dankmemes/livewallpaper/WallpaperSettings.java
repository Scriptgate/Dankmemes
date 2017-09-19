package net.scriptgate.dankmemes.livewallpaper;

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
        return ToggleComponentsFragment.class.getName().equals(fragmentName);
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
    public static class ToggleComponentsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.
            // In a real app, you would want this in a shared function that is used to retrieve the SharedPreferences wherever they are needed.
            PreferenceManager.setDefaultValues(getActivity(), R.xml.components_preferences, false);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.components_preferences);
        }
    }


}