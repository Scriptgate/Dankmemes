package net.scriptgate.dankmemes.object;


import android.content.Context;

import net.scriptgate.dankmemes.Square;

import java8.util.function.Consumer;

public interface RenderableAsSquare {

    void loadTexture(Context context);

    void render(Consumer<Square> draw);
}
