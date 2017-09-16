package net.scriptgate.dankmemes;


import android.content.Context;

import net.scriptgate.android.common.Point3D;
import net.scriptgate.android.opengles.face.Point3DFace;
import net.scriptgate.android.opengles.texture.TextureHelper;

import java.util.ArrayList;
import java.util.List;

import java8.util.function.Consumer;

import static java8.util.stream.StreamSupport.stream;

class Grid implements RenderableAsSquare, Updatable {

    private static final float SCALE = 25;

    private List<Square> grid;

    Grid() {
        grid = new ArrayList<>();

        Point3DFace face = new Point3DFace(
                new Point3D(-2, 0, -1),
                new Point3D(2, 0, -1),
                new Point3D(-2, 0, 1),
                new Point3D(2, 0, 1)
        );
        float[] verticesData = new float[Square.ELEMENTS_PER_FACE * face.getNumberOfElements()];
        face.addFaceToArray(verticesData, 0);
        float[] textureData = SquareDataFactory.generateTextureData(10, 2);

        grid.add(Square.createSquare(new Point3D(), new Point3D(), verticesData, textureData));
        grid.add(Square.createSquare(new Point3D(0,0,-SCALE), new Point3D(), verticesData, textureData));
        grid.add(Square.createSquare(new Point3D(0,0,-SCALE*2), new Point3D(), verticesData, textureData));


        for (Square square : grid) {
            square.setScale(new Point3D(SCALE, 1.0f, SCALE/2));
        }
    }

    @Override
    public void render(Consumer<Square> draw) {
        stream(grid).forEach(draw);
    }


    @Override
    public void update(long elapsedTime) {

        final float distance = elapsedTime / 200f;

        stream(grid).forEach(new Consumer<Square>() {
            @Override
            public void accept(Square square) {
                if(square.position().z() > SCALE) {
                    square.translate(new Point3D(0,0,-SCALE*grid.size()));
                }
                square.translate(new Point3D(0,0,distance));
            }
        });

    }

    @Override
    public void loadTexture(Context context) {
        final int texture = TextureHelper.loadTexture(context, R.drawable.grid);
        stream(grid).forEach(new Consumer<Square>() {
            @Override
            public void accept(Square square) {
                square.setTexture(texture);
            }
        });
    }
}
