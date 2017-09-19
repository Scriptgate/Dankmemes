package net.scriptgate.dankmemes.switchinglivewallpaper;

import android.app.ActivityManager;
import android.opengl.GLSurfaceView.Renderer;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import net.scriptgate.dankmemes.DankmemesRenderer;

import static net.scriptgate.android.opengles.activity.adapter.GLSurfaceViewAdapter.adaptToGLSurfaceViewRenderer;

public class OpenGLES2WallpaperService extends GLWallpaperService {
    private static final String SETTINGS_KEY = "use_gl_surface_view";

    @Override
    public WallpaperService.Engine onCreateEngine() {
        boolean useGlSurfaceView = PreferenceManager.getDefaultSharedPreferences(OpenGLES2WallpaperService.this).getBoolean(SETTINGS_KEY, true);

        System.out.println(SETTINGS_KEY + ": " + useGlSurfaceView);
        return new ES2GLSurfaceViewEngine();
    }

    private class ES2GLSurfaceViewEngine extends GLWallpaperService.GLSurfaceViewEngine {

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            init(this);
        }
    }

    void init(OpenGLEngine engine) {
        if (supportsOpenGLES20()) {
            engine.setEGLContextClientVersion(2);
            engine.setRenderer(getNewRenderer());
        } else {
            throw new UnsupportedOperationException("This activity requires OpenGL ES 2.0");
        }
    }

    private boolean supportsOpenGLES20() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        return activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000;
    }

    Renderer getNewRenderer() {
        return adaptToGLSurfaceViewRenderer(new DankmemesRenderer(this));
    }
}
