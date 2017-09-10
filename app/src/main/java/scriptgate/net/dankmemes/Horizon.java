package scriptgate.net.dankmemes;


import android.content.Context;

import net.scriptgate.android.common.Point3D;
import net.scriptgate.android.opengles.face.Point3DFace;
import net.scriptgate.android.opengles.texture.TextureHelper;

import java8.util.function.Consumer;

import static scriptgate.net.dankmemes.Square.ELEMENTS_PER_FACE;
import static scriptgate.net.dankmemes.Square.createSquare;
import static scriptgate.net.dankmemes.SquareDataFactory.generateTextureData;

class Horizon {


    private final Square model;

    Horizon() {
        float width = 1.0f;
        float height = 0.75f;

        Point3DFace face = new Point3DFace(
                new Point3D(0, 0, 0),
                new Point3D(width, 0, 0),
                new Point3D(0, height, 0),
                new Point3D(width, height, 0)
        );
        float[] verticesData = new float[ELEMENTS_PER_FACE * face.getNumberOfElements()];
        face.addFaceToArray(verticesData, 0);

        model = createSquare(
                new Point3D(-1.5f, 0.6f, 0),
                new Point3D(),
                verticesData,
                generateTextureData(3.0f, 1.0f));
        model.setScale(new Point3D(3, 1, 1));

    }

    void loadTexture(Context context) {
        int texture = TextureHelper.loadTexture(context, R.drawable.horizon);
        model.setTexture(texture);
    }

    public void render(Consumer<Square> renderer) {
        renderer.accept(model);
    }
}
