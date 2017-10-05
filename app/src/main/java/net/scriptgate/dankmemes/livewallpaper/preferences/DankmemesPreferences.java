package net.scriptgate.dankmemes.livewallpaper.preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import net.scriptgate.dankmemes.R;

import java.util.Collection;

import static java.util.Arrays.asList;

public abstract class DankmemesPreferences extends PreferenceFragment {

    //@formatter:off
    public static class BackgroundPreferences extends DankmemesPreferences {@Override int getPreferencesResourceId() {return R.xml.background_preferences;}}
    public static class DeloreanPreferences extends DankmemesPreferences {@Override int getPreferencesResourceId() {return R.xml.delorean_preferences;}}
    public static class GridPreferences extends DankmemesPreferences {@Override int getPreferencesResourceId() {return R.xml.grid_preferences;}}
    public static class TitlePreferences extends DankmemesPreferences {@Override int getPreferencesResourceId() {return R.xml.title_preferences;}}
    //@formatter:on

    public static Collection<String> getImplementations() {
        return asList(
                TitlePreferences.class.getName(),
                DeloreanPreferences.class.getName(),
                BackgroundPreferences.class.getName(),
                GridPreferences.class.getName()
        );
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

    abstract int getPreferencesResourceId();

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
