package net.scriptgate.dankmemes;


import android.content.Context;

import net.scriptgate.android.common.Point2D;
import net.scriptgate.android.common.Point3D;
import net.scriptgate.android.opengles.face.Point3DFace;
import net.scriptgate.android.opengles.texture.TextureHelper;

import java8.util.function.Consumer;

import static net.scriptgate.dankmemes.Square.ELEMENTS_PER_FACE;
import static net.scriptgate.dankmemes.Square.createSquare;

class Title {

    boolean isOff() {
        return mode == OFF;
    }

    void turnOn() {
        mode = ON;
    }

    void turnOff() {
        mode = OFF;
    }

    private static class Mode {
        private final Square model;

        Mode(float offsetX, float offsetY) {
            float width = 1.0f;
            float height = 0.25f;

            Point3DFace face = new Point3DFace(
                    new Point3D(0, 0, 0),
                    new Point3D(width, 0, 0),
                    new Point3D(0, height, 0),
                    new Point3D(width, height, 0)
            );
            float[] verticesData = new float[ELEMENTS_PER_FACE * face.getNumberOfElements()];
            face.addFaceToArray(verticesData, 0);

            model = createSquare(
                    new Point3D(-1f, 1.4f, 0.0f),
                    new Point3D(180.0f, 0.0f, 0.0f),
                    verticesData,
                    SquareDataFactory.generateTextureData(1.0f, 0.5f, new Point2D(offsetX, offsetY)));
            model.setScale(new Point3D(2,2,1));
        }

        void setTexture(int texture) {
            this.model.setTexture(texture);
        }
    }

    private final Mode ON;
    private final Mode OFF;

    private Mode mode;

    Title() {
        ON = new Mode(0, 0.5f);
        OFF = new Mode(0, 0);

        mode = OFF;
    }

    void loadTexture(Context context) {
        int texture = TextureHelper.loadTexture(context, R.drawable.title);

        ON.setTexture(texture);
        OFF.setTexture(texture);
    }

    void render(Consumer<Square> renderer) {
        renderer.accept(mode.model);
    }
}
