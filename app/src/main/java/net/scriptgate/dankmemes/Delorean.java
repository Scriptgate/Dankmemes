package net.scriptgate.dankmemes;


import android.content.Context;

import net.scriptgate.android.common.Point3D;
import net.scriptgate.android.opengles.face.Point3DFace;
import net.scriptgate.android.opengles.texture.TextureHelper;

import java8.util.function.Consumer;

import static net.scriptgate.dankmemes.Square.ELEMENTS_PER_FACE;
import static net.scriptgate.dankmemes.Square.createSquare;

class Delorean implements RenderableAsSquare, Updatable {

    private static final Point3D CENTER = new Point3D(-0.75f, 1, 0.0f);

    private static final float RESET_SPEED = 0.025f;

    private final Square model;
    private Point3D gyroscope;
    private boolean lock;
    private Mode mode;

    Delorean() {
        gyroscope = CENTER;
        mode = Mode.GYROSCOPE;

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
                SquareDataFactory.generateTextureData(1.0f, 1.0f));
        model.setScale(new Point3D(1.5f, 1.5f, 1));
    }

    private enum Mode {
        GYROSCOPE,
        RESET,
        LOCKED
    }

    @Override
    public void loadTexture(Context context) {
        int texture = TextureHelper.loadTexture(context, R.drawable.delorean);
        model.setTexture(texture);
    }

    @Override
    public void update(long elapsedTime) {
        switch (mode) {
            case GYROSCOPE:
                model.setPosition(gyroscope);
                break;
            case RESET:
                if (distanceToCenter(model.position().x()) < RESET_SPEED) {
                    model.setPosition(CENTER);
                    mode = lock ? Mode.LOCKED : Mode.GYROSCOPE;
                } else if (model.position().x() < CENTER.x()) {
                    model.translate(new Point3D(RESET_SPEED, 0, 0));
                } else {
                    model.translate(new Point3D(-RESET_SPEED, 0, 0));
                }
                break;
            case LOCKED:
                break;

        }
    }

    private double distanceToCenter(float x) {
        return Math.sqrt(Math.pow((x - CENTER.x()), 2));
    }

    void transform(float[] deltaRotationVector) {
        if (mode == Mode.GYROSCOPE) {
            gyroscope = Point3D.addition(gyroscope, new Point3D(deltaRotationVector[1] / 7.0f, 0, 0));

            float MINIMUM = -1.1f;
            float MAXIMUM = -0.4f;

            if (gyroscope.x() < MINIMUM) {
                gyroscope = gyroscope.x(MINIMUM);
            } else if (gyroscope.x() > MAXIMUM) {
                gyroscope = gyroscope.x(MAXIMUM);
            }
        }
    }

    @Override
    public void render(Consumer<Square> renderer) {
        renderer.accept(model);
    }

    void reset() {
        mode = Mode.RESET;
        gyroscope = CENTER;
    }

    void setLock(boolean lock) {
        this.lock = lock;
        if (lock) {
            reset();
        }
    }
}
