package net.scriptgate.dankmemes.livewallpaper;

import android.content.SharedPreferences;

import net.scriptgate.dankmemes.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java8.util.function.Consumer;

public class Preferences implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Map<String, PreferenceMapping> mappedPreferences;
    private PreferenceUpdates updates;

    private Preferences() {
        updates = new PreferenceUpdates();

        mappedPreferences = new HashMap<>();
        mappedPreferences.put("show_title", new PreferenceMapping() {
            @Override
            public void update(Settings settings, SharedPreferences sharedPreferences) {
                settings.titleVisible = sharedPreferences.getBoolean("show_title", settings.titleVisible);
            }
        });
    }

    private interface PreferenceMapping {
        void update(Settings settings, SharedPreferences preferences);
    }

    private static class PreferenceUpdates {
        List<Consumer<Settings>> updates;

        private PreferenceUpdates() {
            this.updates = new ArrayList<>();
        }

        private void add(Consumer<Settings> update) {
            updates.add(update);
        }

        private void accept(Settings settings) {
            for (Consumer<Settings> update : updates) {
                update.accept(settings);
            }
        }

        private void clear() {
            updates.clear();
        }

        private boolean hasUpdates() {
            return !updates.isEmpty();
        }
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, String key) {
        final PreferenceMapping preferenceMapping = mappedPreferences.get(key);
        if (preferenceMapping != null) {
            updates.add(new Consumer<Settings>() {
                @Override
                public void accept(Settings settings) {
                    preferenceMapping.update(settings, sharedPreferences);
                }
            });
        }
    }

    private static Preferences instance;

    private static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }

    static void registerListener(SharedPreferences preferences) {
        preferences.registerOnSharedPreferenceChangeListener(getInstance());
    }

    static void unregisterListener(SharedPreferences preferences) {
        preferences.unregisterOnSharedPreferenceChangeListener(getInstance());
    }

    public static boolean needsUpdate() {
        return getInstance().hasUpdates();
    }

    private boolean hasUpdates() {
        return updates.hasUpdates();
    }

    public static Settings getUpdatedSettings() {
        return getInstance().applyUpdatesTo(new Settings());
    }

    private Settings applyUpdatesTo(Settings settings) {
        updates.accept(settings);
        updates.clear();
        return settings;
    }
}
