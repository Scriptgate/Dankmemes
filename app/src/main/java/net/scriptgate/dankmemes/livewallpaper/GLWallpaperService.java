package net.scriptgate.dankmemes.livewallpaper;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;


import net.scriptgate.android.component.Interactable;
import net.scriptgate.android.component.Resumable;

import java.util.ArrayList;
import java.util.Collection;

import java8.util.function.Consumer;



import static java8.util.stream.StreamSupport.stream;
import static net.scriptgate.dankmemes.LoggerConfig.DEBUG;

public abstract class GLWallpaperService extends WallpaperService {

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
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    stream(interactables).forEach(new Consumer<Interactable>() {
                        @Override
                        public void accept(Interactable interactable) {
                            interactable.onUp(x, y);
                        }
                    });
                    break;
                case MotionEvent.ACTION_MOVE:
                    stream(interactables).forEach(new Consumer<Interactable>() {
                        @Override
                        public void accept(Interactable interactable) {
                            interactable.onMove(x, y);
                        }
                    });
                    break;
            }
            super.onTouchEvent(event);
        }

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
            DEBUG.accept(TAG, "onDestroy()");

            super.onDestroy();
            glSurfaceView.onDestroy();
        }

        void setRenderer(Renderer renderer) {
            DEBUG.accept(TAG, "setRenderer(" + renderer + ")");
            glSurfaceView.setRenderer(renderer);
            rendererHasBeenSet = true;
        }

        void setPreserveEGLContextOnPause(boolean preserve) {
            DEBUG.accept(TAG, "setPreserveEGLContextOnPause(" + preserve + ")");
            glSurfaceView.setPreserveEGLContextOnPause(preserve);
        }

        void setEGLContextClientVersion(int version) {
            DEBUG.accept(TAG, "setEGLContextClientVersion(" + version + ")");
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
