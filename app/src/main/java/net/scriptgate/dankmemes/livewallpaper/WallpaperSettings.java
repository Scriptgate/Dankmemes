package net.scriptgate.dankmemes.livewallpaper;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import net.scriptgate.dankmemes.R;
import net.scriptgate.dankmemes.livewallpaper.settings.TitleOptionsFragment;

import java.util.List;

public class WallpaperSettings extends PreferenceActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return TitleOptionsFragment.class.getName().equals(fragmentName);
    }

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }


}