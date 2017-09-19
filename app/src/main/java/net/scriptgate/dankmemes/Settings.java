package net.scriptgate.dankmemes;

public class Settings {

    private boolean titleVisible;

    public Settings setTitleVisible(boolean visible) {
        this.titleVisible = visible;
        return this;
    }

    boolean isTitleVisible() {
        return titleVisible;
    }
}
