package net.scriptgate.dankmemes.switchinglivewallpaper;

import android.opengl.GLSurfaceView.Renderer;

import net.scriptgate.dankmemes.DankmemesRenderer;

import static net.scriptgate.android.opengles.activity.adapter.GLSurfaceViewAdapter.adaptToGLSurfaceViewRenderer;

public class WallpaperService extends OpenGLES2WallpaperService {
	@Override
    Renderer getNewRenderer() {
		return adaptToGLSurfaceViewRenderer(new DankmemesRenderer(this));
	}
}
