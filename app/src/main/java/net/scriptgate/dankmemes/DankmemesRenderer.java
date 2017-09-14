package net.scriptgate.dankmemes;

import android.content.Context;

import net.scriptgate.android.common.Point3D;
import net.scriptgate.android.opengles.matrix.ModelMatrix;
import net.scriptgate.android.opengles.matrix.ModelViewProjectionMatrix;
import net.scriptgate.android.opengles.matrix.ProjectionMatrix;
import net.scriptgate.android.opengles.matrix.ViewMatrix;
import net.scriptgate.android.opengles.program.Program;
import net.scriptgate.android.opengles.renderer.RendererBase;

import java8.util.function.Consumer;

import static android.opengl.GLES20.*;
import static java.lang.System.nanoTime;
import static net.scriptgate.android.common.Color.GREY;
import static net.scriptgate.android.opengles.matrix.ViewMatrix.createViewBehindOrigin;
import static net.scriptgate.android.opengles.program.AttributeVariable.COLOR;
import static net.scriptgate.android.opengles.program.AttributeVariable.POSITION;
import static net.scriptgate.android.opengles.program.ProgramBuilder.program;

public class DankmemesRenderer extends RendererBase {


    private Background background;
    private Grid grid;
    private Horizon horizon;
    private Title title;
    private Delorean delorean;

    private Context activityContext;

    private float[] deltaRotationVector = new float[4];
    private Consumer<Square> renderer;

    public DankmemesRenderer(Context activityContext) {
        super(ProjectionMatrix.createProjectionMatrix(150, 1));
        this.activityContext = activityContext;

        background = new Background();
        grid = new Grid();
        horizon = new Horizon();
        title = new Title();
        delorean = new Delorean();
    }

    @Override
    public void onSurfaceCreated() {
        glClearColor(GREY.red(), GREY.green(), GREY.blue(), GREY.alpha());

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        final ModelMatrix modelMatrix = new ModelMatrix();
        final ViewMatrix viewMatrix = createViewBehindOrigin();
        final ModelViewProjectionMatrix mvpMatrix = new ModelViewProjectionMatrix();

        viewMatrix.onSurfaceCreated();
        viewMatrix.translate(new Point3D(0, -1, 0));

        background.loadTexture(activityContext);
        grid.loadTexture(activityContext);
        horizon.loadTexture(activityContext);
        title.loadTexture(activityContext);
        delorean.loadTexture(activityContext);

        glGenerateMipmap(GL_TEXTURE_2D);

        final Program program = program()
                .withVertexShader(activityContext, R.raw.per_pixel_vertex_shader_texture)
                .withFragmentShader(activityContext, R.raw.per_pixel_fragment_shader_texture)
                .withAttributes(POSITION, COLOR)
                .build();
        program.useForRendering();

        renderer = new Consumer<Square>() {
            @Override
            public void accept(Square square) {
                square.draw(program, modelMatrix, viewMatrix, projectionMatrix, mvpMatrix);
            }
        };
    }

    private static final int ticksPerRun = 60;
    private static final long maximumTicksPerRun = 240;
    private double accumulator = 0;

    private long lastTime = nanoTime();

    @Override
    public void onDrawFrame() {

        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

        long now = nanoTime();
        double nsPerTick = 1_000_000_000.0 / ticksPerRun;
        double unprocessed = (now - lastTime) / nsPerTick;
        lastTime = now;

        if (unprocessed > maximumTicksPerRun) {
            unprocessed = maximumTicksPerRun;
        }

        accumulator += unprocessed;


        while (accumulator >= 1) {
            accumulator -= 1;
            background.update((long) (nsPerTick / 1_000_000));
            grid.update((long) (nsPerTick / 1_000_000));
        }

        background.render(renderer);
        grid.render(renderer);
        horizon.render(renderer);
        title.render(renderer);
        delorean.transform(deltaRotationVector);
        delorean.render(renderer);
    }

    public void setGyroscopeValues(float[] deltaRotationVector) {
        this.deltaRotationVector = deltaRotationVector;
    }

    public void reset() {
        delorean.reset();
    }
}
