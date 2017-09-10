package scriptgate.net.dankmemes;


import android.opengl.GLES20;

import net.scriptgate.android.common.Point3D;
import net.scriptgate.android.opengles.matrix.ModelMatrix;
import net.scriptgate.android.opengles.matrix.ModelViewProjectionMatrix;
import net.scriptgate.android.opengles.matrix.ProjectionMatrix;
import net.scriptgate.android.opengles.matrix.ViewMatrix;
import net.scriptgate.android.opengles.program.AttributeVariable;
import net.scriptgate.android.opengles.program.Program;

import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glTexParameteri;
import static net.scriptgate.android.nio.BufferHelper.allocateBuffer;
import static net.scriptgate.android.opengles.program.UniformVariable.MVP_MATRIX;

class Square {

    static final int ELEMENTS_PER_FACE = 6;

    private FloatBuffer verticesBuffer;
    private FloatBuffer textureCoordinateBuffer;

    private Point3D position;
    private Point3D rotation;
    private int texture;
    private Point3D scale = new Point3D(1,1,1);



    static Square createSquare(Point3D position, Point3D rotation, float[] verticesData, float[] textureData) {
        FloatBuffer verticesBuffer = allocateBuffer(verticesData);
        FloatBuffer textureCoordinateBuffer = allocateBuffer(textureData);
        return new Square(position, rotation, verticesBuffer, textureCoordinateBuffer);
    }

    private Square(Point3D position, Point3D rotation, FloatBuffer verticesBuffer, FloatBuffer textureCoordinateBuffer) {
        this.position = position;
        this.rotation = rotation;
        this.verticesBuffer = verticesBuffer;
        this.textureCoordinateBuffer = textureCoordinateBuffer;
    }

    void draw(Program program, ModelMatrix modelMatrix, ViewMatrix viewMatrix, ProjectionMatrix projectionMatrix, ModelViewProjectionMatrix mvpMatrix) {
        modelMatrix.setIdentity();
        modelMatrix.translate(position);
        modelMatrix.rotate(rotation);
        modelMatrix.scale(scale);

        mvpMatrix.multiply(modelMatrix, viewMatrix, projectionMatrix);
        mvpMatrix.passTo(program.getHandle(MVP_MATRIX));


        program.bindTexture(texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,  GLES20.GL_NEAREST);

        program.pass(verticesBuffer).at(0).to(AttributeVariable.POSITION);

        program.pass(textureCoordinateBuffer).at(0).to(AttributeVariable.TEXTURE_COORDINATE);

        glDrawArrays(GL_TRIANGLES, 0, ELEMENTS_PER_FACE);
    }

    void setTexture(int texture) {
        this.texture = texture;
    }

    void setScale(Point3D scale) {
        this.scale = scale;
    }

    void translate(Point3D offset) {
        this.position = Point3D.addition(position, offset);
    }

    Point3D position() {
        return position;
    }

    void setPosition(Point3D position) {
        this.position = position;
    }
}
