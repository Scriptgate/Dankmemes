package net.scriptgate.dankmemes.switchinglivewallpaper;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import net.scriptgate.dankmemes.LoggerConfig;

import static net.scriptgate.dankmemes.LoggerConfig.DEBUG;

public abstract class GLWallpaperService extends WallpaperService {

    private static final String TAG = "GLWallpaperService";

    interface OpenGLEngine {
        void setEGLContextClientVersion(int version);

        void setRenderer(Renderer renderer);
    }

    class GLSurfaceViewEngine extends Engine implements OpenGLEngine {
        class WallpaperGLSurfaceView extends GLSurfaceView {
            private static final String TAG = "WallpaperGLSurfaceView";

            WallpaperGLSurfaceView(Context context) {
                super(context);
                DEBUG.accept(TAG, "WallpaperGLSurfaceView(" + context + ")");
            }

            @Override
            public SurfaceHolder getHolder() {
                DEBUG.accept(TAG, "getHolder(): returning " + getSurfaceHolder());
                return getSurfaceHolder();
            }

            public void onDestroy() {
                DEBUG.accept(TAG, "onDestroy()");
                super.onDetachedFromWindow();
            }
        }

        private static final String TAG = "GLSurfaceViewEngine";

        private WallpaperGLSurfaceView glSurfaceView;
        private boolean rendererHasBeenSet;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            DEBUG.accept(TAG, "onCreate(" + surfaceHolder + ")");
            super.onCreate(surfaceHolder);

            glSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            DEBUG.accept(TAG, "onVisibilityChanged(" + visible + ")");
            super.onVisibilityChanged(visible);

            if (rendererHasBeenSet) {
                if (visible) {
                    glSurfaceView.onResume();
                } else {
                    glSurfaceView.onPause();
                }
            }
        }

        @Override
        public void onDestroy() {
            DEBUG.accept(TAG, "onDestroy()");
            super.onDestroy();
            glSurfaceView.onDestroy();
        }

        public void setRenderer(Renderer renderer) {
            DEBUG.accept(TAG, "setRenderer(" + renderer + ")");
            glSurfaceView.setRenderer(renderer);
            rendererHasBeenSet = true;
        }

        public void setEGLContextClientVersion(int version) {
            DEBUG.accept(TAG, "setEGLContextClientVersion(" + version + ")");
            glSurfaceView.setEGLContextClientVersion(version);
        }
    }
}