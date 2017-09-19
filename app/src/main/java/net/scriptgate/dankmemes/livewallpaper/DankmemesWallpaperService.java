package net.scriptgate.dankmemes.livewallpaper;


import android.app.ActivityManager;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import net.scriptgate.android.component.Interactable;
import net.scriptgate.android.opengles.activity.adapter.GLSurfaceViewAdapter;

import net.scriptgate.dankmemes.DankmemesRenderer;

public class DankmemesWallpaperService extends GLWallpaperService {
    private static final String SETTINGS_KEY = "use_gl_surface_view";

    @Override
    public WallpaperService.Engine onCreateEngine() {
        boolean useGlSurfaceView = PreferenceManager.getDefaultSharedPreferences(DankmemesWallpaperService.this).getBoolean(SETTINGS_KEY, true);
        System.out.println(SETTINGS_KEY + ": " + useGlSurfaceView);

        return new OpenGLES2Engine();
    }

    private class OpenGLES2Engine extends GLEngine {

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            final DankmemesRenderer renderer = new DankmemesRenderer(DankmemesWallpaperService.this);

            if (supportsOpenGLES20()) {
                setEGLContextClientVersion(2);
                setPreserveEGLContextOnPause(true);
                setRenderer(GLSurfaceViewAdapter.adaptToGLSurfaceViewRenderer(renderer));
            } else {
                throw new UnsupportedOperationException("This activity requires OpenGL ES 2.0");
            }

            SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            addComponent(new SensorService(sensorManager, renderer));
            addComponent(new Interactable() {
                @Override
                public void onDown(float x, float y) {
                    renderer.turnTitleOn();
                    renderer.reset();
                }

                @Override
                public void onUp(float x, float y) {
                    renderer.turnTitleOff();
                }

                @Override
                public void onMove(float x, float y) {
                    renderer.turnTitleOn();
                }
            });
        }

        private boolean supportsOpenGLES20() {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            return activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000;
        }
    }
}
