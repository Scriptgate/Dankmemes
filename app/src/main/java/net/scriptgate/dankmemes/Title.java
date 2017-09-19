package net.scriptgate.dankmemes;


import android.content.Context;

import net.scriptgate.android.common.Point2D;
import net.scriptgate.android.common.Point3D;
import net.scriptgate.android.opengles.face.Point3DFace;
import net.scriptgate.android.opengles.texture.TextureHelper;

import java.util.HashMap;
import java.util.Map;

import java8.util.function.Consumer;

import static java8.util.stream.StreamSupport.stream;
import static net.scriptgate.dankmemes.Square.ELEMENTS_PER_FACE;
import static net.scriptgate.dankmemes.Square.createSquare;

class Title implements RenderableAsSquare, Updatable {

    private static class Switch {

        private long timeTillNextMode;
        private Position position = Position.OFF;
        private Map<Position, Square> models;

        Switch() {
            timeTillNextMode = position.timeSpentInMode;
            models = new HashMap<>();
            models.put(Position.ON, Position.ON.createModel());
            models.put(Position.OFF, Position.OFF.createModel());
        }

        boolean isOff() {
            return position == Position.OFF;
        }

        Square getModel() {
            return models.get(position);
        }

        void setTexture(final int texture) {
            stream(models.values()).forEach(new Consumer<Square>() {
                @Override
                public void accept(Square square) {square.setTexture(texture);
                }
            });
        }

        private enum Position {
            ON(500, new Point2D(0, 0.5f)),
            OFF(5000, new Point2D(0, 0));

            private final long timeSpentInMode;
            private final Point2D textureOffset;

            Position(long timeSpentInMode, Point2D textureOffset) {
                this.timeSpentInMode = timeSpentInMode;
                this.textureOffset = textureOffset;
            }

            private Square createModel() {
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

                Square model = createSquare(
                        new Point3D(-1f, 1.4f, 0.0f),
                        new Point3D(180.0f, 0.0f, 0.0f),
                        verticesData,
                        SquareDataFactory.generateTextureData(1.0f, 0.5f, textureOffset));
                model.setScale(new Point3D(2, 2, 1));
                return model;
            }
        }

        void turnOn() {
            position = Position.ON;
        }

        void turnOff() {
            position = Position.OFF;
        }

        void update(long elapsedTime) {
            timeTillNextMode -= elapsedTime;
            if (timeTillNextMode <= 0) {
                position = isOff() ? Position.ON : Position.OFF;
                timeTillNextMode += Math.random() * position.timeSpentInMode;
            }
        }
    }

    private Switch light;

    Title() {
        light = new Switch();
    }

    @Override
    public void loadTexture(Context context) {
        final int texture = TextureHelper.loadTexture(context, R.drawable.title);
        light.setTexture(texture);
    }

    @Override
    public void render(Consumer<Square> renderer) {
        renderer.accept(light.getModel());
    }

    @Override
    public void update(long elapsedTime) {
        //light.update(elapsedTime);
    }

    void turnOn() {
        light.turnOn();
    }

    void turnOff() {
        light.turnOff();
    }

}
