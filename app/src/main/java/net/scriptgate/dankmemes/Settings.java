package net.scriptgate.dankmemes;

import android.content.Context;

import net.scriptgate.dankmemes.livewallpaper.Preferences;

public class Settings {

    public boolean titleVisible;

    boolean needsUpdate() {
        return Preferences.needsUpdate();
    }

    void update(DankmemesRenderer renderer) {
        Preferences.updateSettings(this);
        renderer.setTitleVisibility(titleVisible);
    }

    public static Settings defaultSettings() {
        Settings settings = new Settings();
        settings.titleVisible = true;
        return settings;
    }

    public static Settings sharedSettings(Context context) {
        return Preferences.getSettingsFromPreferences(context);
    }
}
