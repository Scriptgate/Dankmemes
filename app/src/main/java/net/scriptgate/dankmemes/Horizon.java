package net.scriptgate.dankmemes;


import android.content.Context;

import net.scriptgate.android.common.Point3D;
import net.scriptgate.android.opengles.face.Point3DFace;
import net.scriptgate.android.opengles.texture.TextureHelper;
import net.scriptgate.dankmemes.object.RenderableAsSquare;

import java8.util.function.Consumer;

class Horizon implements RenderableAsSquare {


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
        float[] verticesData = new float[Square.ELEMENTS_PER_FACE * face.getNumberOfElements()];
        face.addFaceToArray(verticesData, 0);

        model = Square.createSquare(
                new Point3D(-1.5f, 0.6f, 0),
                new Point3D(),
                verticesData,
                SquareDataFactory.generateTextureData(3.0f, 1.0f));
        model.setScale(new Point3D(3, 1, 1));

    }

    @Override
    public void loadTexture(Context context) {
        int texture = TextureHelper.loadTexture(context, R.drawable.horizon);
        model.setTexture(texture);
    }

    @Override
    public void render(Consumer<Square> renderer) {
        renderer.accept(model);
    }
}
