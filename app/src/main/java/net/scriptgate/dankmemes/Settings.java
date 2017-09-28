package net.scriptgate.dankmemes;

import android.content.Context;

import net.scriptgate.dankmemes.livewallpaper.preferences.Preferences;

public class Settings {

    public boolean titleVisible;
    public boolean titleNeon;

    boolean needsUpdate() {
        return Preferences.needsUpdate();
    }

    void update(DankmemesRenderer renderer) {
        Preferences.updateSettings(this);
        renderer.setTitleVisibility(titleVisible);
        renderer.setTitleNeon(titleNeon);
    }

    public static Settings defaultSettings() {
        Settings settings = new Settings();
        settings.titleVisible = true;
        settings.titleNeon = false;
        return settings;
    }

    public static Settings sharedSettings(Context context) {
        return Preferences.getSettingsFromPreferences(context);
    }
}
