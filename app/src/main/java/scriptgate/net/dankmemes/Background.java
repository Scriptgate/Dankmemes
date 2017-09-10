package scriptgate.net.dankmemes;


import android.content.Context;
import android.os.SystemClock;

import net.scriptgate.android.common.Point3D;
import net.scriptgate.android.opengles.face.Point3DFace;
import net.scriptgate.android.opengles.texture.TextureHelper;

import java.util.ArrayList;
import java.util.List;

import java8.util.function.Consumer;

import static java8.util.stream.StreamSupport.stream;
import static scriptgate.net.dankmemes.Square.ELEMENTS_PER_FACE;
import static scriptgate.net.dankmemes.Square.createSquare;
import static scriptgate.net.dankmemes.SquareDataFactory.generateTextureData;

class Background {

    private static final float SCALE = 50;

    private List<Square> background;

    Background() {
        background = new ArrayList<>();

        Point3DFace face = new Point3DFace(
                new Point3D(0, 0, 0),
                new Point3D(1.0f, 0, 0),
                new Point3D(0, 1.0f, 0),
                new Point3D(1.0f, 1.0f, 0)
        );
        float[] verticesData = new float[ELEMENTS_PER_FACE * face.getNumberOfElements()];
        face.addFaceToArray(verticesData, 0);
        float[] textureData = generateTextureData(5.0f, 5.0f);

        background.add(createSquare(new Point3D(-SCALE / 2, 0, -7.5f), new Point3D(), verticesData, textureData));
        background.add(createSquare(new Point3D(-SCALE / 2, -SCALE, -7.5f), new Point3D(), verticesData, textureData));
        background.add(createSquare(new Point3D(-SCALE / 2, -2 * SCALE, -7.5f), new Point3D(), verticesData, textureData));

        stream(background).forEach(new Consumer<Square>() {
            @Override
            public void accept(Square square) {
                square.setScale(new Point3D(SCALE, SCALE, 1));
            }
        });
    }

    void render(Consumer<Square> draw) {
        stream(background).forEach(draw);
    }

    void update() {
        long time = SystemClock.uptimeMillis() % 10_000L;
        final float distance = (0.03f / 10_000.0f) * ((int) time);

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

    void loadTexture(Context activityContext) {
        final int texture = TextureHelper.loadTexture(activityContext, R.drawable.background);
        stream(background).forEach(new Consumer<Square>() {
            @Override
            public void accept(Square square) {
                square.setTexture(texture);
            }
        });
    }
}
