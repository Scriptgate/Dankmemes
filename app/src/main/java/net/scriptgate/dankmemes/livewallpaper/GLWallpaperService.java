package net.scriptgate.dankmemes.livewallpaper;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;


import net.scriptgate.android.component.Interactable;
import net.scriptgate.android.component.Resumable;

import java.util.ArrayList;
import java.util.Collection;

import java8.util.function.BiConsumer;
import java8.util.function.Consumer;

import net.scriptgate.dankmemes.LoggerConfig;

import static java8.util.stream.StreamSupport.stream;

public abstract class GLWallpaperService extends WallpaperService {

    private static BiConsumer<String, String> LOG = new BiConsumer<String, String>() {
        @Override
        public void accept(String TAG, String message) {
            if (LoggerConfig.ON) {
                Log.d(TAG, message);
            }
        }
    };

    Collection<Resumable> resumables;
    Collection<Interactable> interactables;

    public GLWallpaperService() {
        this.resumables = new ArrayList<>();
        this.interactables = new ArrayList<>();
    }

    class GLEngine extends Engine {
        class WallpaperGLSurfaceView extends GLSurfaceView {
            private static final String TAG = "WallpaperGLSurfaceView";

            WallpaperGLSurfaceView(Context context) {
                super(context);

                LOG.accept(TAG, "WallpaperGLSurfaceView(" + context + ")");
            }

            @Override
            public SurfaceHolder getHolder() {
                LOG.accept(TAG, "getHolder(): returning " + getSurfaceHolder());
                return getSurfaceHolder();
            }

            public void onDestroy() {
                LOG.accept(TAG, "onDestroy()");
                super.onDetachedFromWindow();
            }
        }

        private static final String TAG = "GLEngine";

        private WallpaperGLSurfaceView glSurfaceView;
        private boolean rendererHasBeenSet;

        @Override
        public void onTouchEvent(MotionEvent event) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    stream(interactables).forEach(new Consumer<Interactable>() {
                        @Override
                        public void accept(Interactable interactable) {
                            interactable.onDown(x, y);
                        }
                    });
                    break;
            }
            super.onTouchEvent(event);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            LOG.accept(TAG, "onCreate(" + surfaceHolder + ")");
            super.onCreate(surfaceHolder);

            glSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            LOG.accept(TAG, "onVisibilityChanged(" + visible + ")");

            super.onVisibilityChanged(visible);

            if (rendererHasBeenSet) {
                if (visible) {
                    stream(resumables).forEach(new Consumer<Resumable>() {
                        @Override
                        public void accept(Resumable resumable) {
                            resumable.onResume();
                        }
                    });
                    glSurfaceView.onResume();
                } else {
                    stream(resumables).forEach(new Consumer<Resumable>() {
                        @Override
                        public void accept(Resumable resumable) {
                            resumable.onPause();
                        }
                    });
                    glSurfaceView.onPause();
                }
            }
        }

        @Override
        public void onDestroy() {
            LOG.accept(TAG, "onDestroy()");

            super.onDestroy();
            glSurfaceView.onDestroy();
        }

        void setRenderer(Renderer renderer) {
            LOG.accept(TAG, "setRenderer(" + renderer + ")");
            glSurfaceView.setRenderer(renderer);
            rendererHasBeenSet = true;
        }

        void setPreserveEGLContextOnPause(boolean preserve) {
            LOG.accept(TAG, "setPreserveEGLContextOnPause(" + preserve + ")");
            glSurfaceView.setPreserveEGLContextOnPause(preserve);
        }

        void setEGLContextClientVersion(int version) {
            LOG.accept(TAG, "setEGLContextClientVersion(" + version + ")");
            glSurfaceView.setEGLContextClientVersion(version);
        }
    }

    void addComponent(Resumable resumable) {
        resumables.add(resumable);
    }

    void addComponent(Interactable interactable) {
        interactables.add(interactable);
    }
}
