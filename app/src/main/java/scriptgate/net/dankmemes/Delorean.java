package scriptgate.net.dankmemes;


import android.content.Context;

import net.scriptgate.common.Point3D;
import net.scriptgate.opengles.face.Point3DFace;
import net.scriptgate.opengles.texture.TextureHelper;

import java8.util.function.Consumer;

import static scriptgate.net.dankmemes.Square.ELEMENTS_PER_FACE;
import static scriptgate.net.dankmemes.Square.createSquare;
import static scriptgate.net.dankmemes.SquareDataFactory.generateTextureData;

class Delorean {

    private static final Point3D CENTER = new Point3D(-0.75f, 1, 0.0f);

    private final Square model;

    Delorean() {
        float width = 1.0f;
        float height = 0.7f;

        Point3DFace face = new Point3DFace(
                new Point3D(0, 0, 0),
                new Point3D(width, 0, 0),
                new Point3D(0, height, 0),
                new Point3D(width, height, 0)
        );
        float[] verticesData = new float[ELEMENTS_PER_FACE * face.getNumberOfElements()];
        face.addFaceToArray(verticesData, 0);

        model = createSquare(
                CENTER,
                new Point3D(180.0f, 0.0f, 0.0f),
                verticesData,
                generateTextureData(1.0f, 1.0f));
        model.setScale(new Point3D(1.5f, 1.5f, 1));
    }

    void loadTexture(Context context) {
        int texture = TextureHelper.loadTexture(context, R.drawable.delorean);
        model.setTexture(texture);
    }

    void transform(float[] deltaRotationVector) {
        model.translate(new Point3D(deltaRotationVector[1]/7.0f, 0,0));

        float MINIMUM = -1.1f;
        float MAXIMUM = -0.4f;

        if (model.position().x() < MINIMUM) {
            model.setPosition(model.position().x(MINIMUM));
        } else if (model.position().x() > MAXIMUM) {
            model.setPosition(model.position().x(MAXIMUM));
        }

    }

    void render(Consumer<Square> renderer) {
        renderer.accept(model);
    }
}
