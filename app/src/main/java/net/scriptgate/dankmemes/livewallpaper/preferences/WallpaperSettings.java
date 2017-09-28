package net.scriptgate.dankmemes.livewallpaper.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import net.scriptgate.dankmemes.R;

import java.util.ArrayList;
import java.util.List;

public class WallpaperSettings extends PreferenceActivity {

    private static final List<String> preferenceFragments;

    static {
        preferenceFragments = new ArrayList<>();
        preferenceFragments.add(TitlePreferences.class.getName());
        preferenceFragments.add(DeloreanPreferences.class.getName());
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return preferenceFragments.contains(fragmentName);
    }

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }
}