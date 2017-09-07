package scriptgate.net.dankmemes.livewallpaper;


import android.app.ActivityManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import net.scriptgate.opengles.activity.adapter.GLSurfaceViewAdapter;

import scriptgate.net.dankmemes.DankmemesRenderer;

public class DankmemesWallpaperService extends GLWallpaperService {

    @Override
    public WallpaperService.Engine onCreateEngine() {
        return new OpenGLES2Engine();
    }

    private class OpenGLES2Engine extends GLEngine {

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            DankmemesRenderer renderer = new DankmemesRenderer(DankmemesWallpaperService.this);

            if (supportsOpenGLES20()) {
                setEGLContextClientVersion(2);
                setPreserveEGLContextOnPause(true);
                setRenderer(GLSurfaceViewAdapter.adaptToGLSurfaceViewRenderer(renderer));
            } else {
                throw new UnsupportedOperationException("This activity requires OpenGL ES 2.0");
            }

            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            addComponent(new SensorService(sensorManager, renderer));
            addComponent(renderer);
        }

        private boolean supportsOpenGLES20() {
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            return activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000;
        }
    }
}
