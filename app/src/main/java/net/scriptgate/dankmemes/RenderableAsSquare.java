package net.scriptgate.dankmemes;


import android.content.Context;

import java8.util.function.Consumer;

interface RenderableAsSquare {

    void loadTexture(Context context);

    void render(Consumer<Square> draw);
}
