package net.scriptgate.dankmemes;

import net.scriptgate.dankmemes.livewallpaper.Preferences;

public class Settings {

    public boolean titleVisible;

    boolean needsUpdate() {
        return Preferences.needsUpdate();
    }

    void update(DankmemesRenderer renderer) {
        Settings updatedSettings = Preferences.getUpdatedSettings();
        renderer.setTitleVisibility(updatedSettings.titleVisible);
    }
}
