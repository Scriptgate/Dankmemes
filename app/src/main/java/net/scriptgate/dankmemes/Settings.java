package net.scriptgate.dankmemes;

import android.content.Context;

import net.scriptgate.dankmemes.livewallpaper.preferences.Preferences;

public class Settings {

    public boolean titleVisible;
    public boolean titleNeon;
    public boolean deloreanLock;
    public boolean backgroundLock;
    public boolean gridLock;

    boolean needsUpdate() {
        return Preferences.needsUpdate();
    }

    void update(DankmemesRenderer renderer) {
        Preferences.updateSettings(this);
        renderer.setTitleVisibility(titleVisible);
        renderer.setTitleNeon(titleNeon);
        renderer.setDeloreanLock(deloreanLock);
        renderer.setBackgroundLock(backgroundLock);
        renderer.setGridLock(gridLock);
    }

    public static Settings defaultSettings() {
        Settings settings = new Settings();
        settings.titleVisible = true;
        settings.titleNeon = false;
        settings.deloreanLock = false;
        settings.backgroundLock = false;
        settings.gridLock = false;
        return settings;
    }

    public static Settings sharedSettings(Context context) {
        return Preferences.getSettingsFromPreferences(context);
    }
}
