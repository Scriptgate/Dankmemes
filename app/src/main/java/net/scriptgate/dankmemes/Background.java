package net.scriptgate.dankmemes;


import android.content.Context;

import net.scriptgate.android.common.Point3D;
import net.scriptgate.android.opengles.face.Point3DFace;
import net.scriptgate.android.opengles.texture.TextureHelper;
import net.scriptgate.dankmemes.object.Renderable;
import net.scriptgate.dankmemes.object.RenderableAsSquare;
import net.scriptgate.dankmemes.object.Updatable;

import java.util.ArrayList;
import java.util.List;

import java8.util.function.Consumer;

import static java8.util.stream.StreamSupport.stream;

class Background implements Renderable {

    private static final float SCALE = 50;

    private List<Square> background;
    private boolean lock;

    Background() {
        background = new ArrayList<>();

        Point3DFace face = new Point3DFace(
                new Point3D(0, 0, 0),
                new Point3D(1.0f, 0, 0),
                new Point3D(0, 1.0f, 0),
                new Point3D(1.0f, 1.0f, 0)
        );
        float[] verticesData = new float[Square.ELEMENTS_PER_FACE * face.getNumberOfElements()];
        face.addFaceToArray(verticesData, 0);
        float[] textureData = SquareDataFactory.generateTextureData(5.0f, 5.0f);

        background.add(Square.createSquare(new Point3D(-SCALE / 2, 0, -7.5f), new Point3D(), verticesData, textureData));
        background.add(Square.createSquare(new Point3D(-SCALE / 2, -SCALE, -7.5f), new Point3D(), verticesData, textureData));
        background.add(Square.createSquare(new Point3D(-SCALE / 2, -2 * SCALE, -7.5f), new Point3D(), verticesData, textureData));

        stream(background).forEach(new Consumer<Square>() {
            @Override
            public void accept(Square square) {
                square.setScale(new Point3D(SCALE+0.01f, SCALE+0.01f, 1));
            }
        });
    }

    @Override
    public void render(Consumer<Square> draw) {
        stream(background).forEach(draw);
    }

    @Override
    public void update(long elapsedTime) {
        if(!lock) {
            final float distance = (1f / 2000.0f) * ((int) elapsedTime);

            stream(background).forEach(new Consumer<Square>() {
                @Override
                public void accept(Square square) {
                    if (square.position().y() > SCALE) {
                        square.translate(new Point3D(0, -SCALE * background.size(), 0));
                    }
                    square.translate(new Point3D(0, distance, 0));
                }
            });
        }
    }

    @Override
    public void loadTexture(Context activityContext) {
        final int texture = TextureHelper.loadTexture(activityContext, R.drawable.background);
        stream(background).forEach(new Consumer<Square>() {
            @Override
            public void accept(Square square) {
                square.setTexture(texture);
            }
        });
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}
