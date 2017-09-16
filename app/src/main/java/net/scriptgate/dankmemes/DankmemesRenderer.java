package net.scriptgate.dankmemes;

import android.content.Context;

import net.scriptgate.android.common.Point3D;
import net.scriptgate.android.opengles.matrix.ModelMatrix;
import net.scriptgate.android.opengles.matrix.ModelViewProjectionMatrix;
import net.scriptgate.android.opengles.matrix.ProjectionMatrix;
import net.scriptgate.android.opengles.matrix.ViewMatrix;
import net.scriptgate.android.opengles.program.Program;
import net.scriptgate.android.opengles.renderer.RendererBase;

import java.util.ArrayList;
import java.util.List;

import java8.util.function.Consumer;

import static android.opengl.GLES20.*;
import static java.lang.System.nanoTime;
import static java8.util.stream.StreamSupport.stream;
import static net.scriptgate.android.common.Color.GREY;
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

    private List<RenderableAsSquare> renderables;
    private List<Updatable> updatables;

    public DankmemesRenderer(Context activityContext) {
        super(ProjectionMatrix.createProjectionMatrix(150, 1));
        this.activityContext = activityContext;

        background = new Background();
        grid = new Grid();
        horizon = new Horizon();
        title = new Title();
        delorean = new Delorean();

        renderables = new ArrayList<>();
        renderables.add(background);
        renderables.add(grid);
        renderables.add(horizon);
        renderables.add(title);
        renderables.add(delorean);

        updatables = new ArrayList<>();
        updatables.add(background);
        updatables.add(grid);
    }

    @Override
    public void onSurfaceCreated() {
        glClearColor(GREY.red(), GREY.green(), GREY.blue(), GREY.alpha());

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        final ModelMatrix modelMatrix = new ModelMatrix();

        Point3D eye = new Point3D(0, 1, 1.5f);
        Point3D look = new Point3D(0, 1, -5);
        Point3D up = new Point3D(0, 1, 0);
        final ViewMatrix viewMatrix = new ViewMatrix(eye, look, up);


        final ModelViewProjectionMatrix mvpMatrix = new ModelViewProjectionMatrix();

        viewMatrix.onSurfaceCreated();

        stream(renderables).forEach(new Consumer<RenderableAsSquare>() {
            @Override
            public void accept(RenderableAsSquare renderableAsSquare) {
                renderableAsSquare.loadTexture(activityContext);
            }
        });

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
        final double nsPerTick = 1_000_000_000.0 / ticksPerRun;
        double unprocessed = (now - lastTime) / nsPerTick;
        lastTime = now;

        if (unprocessed > maximumTicksPerRun) {
            unprocessed = maximumTicksPerRun;
        }

        accumulator += unprocessed;

        while (accumulator >= 1) {
            accumulator -= 1;
            stream(updatables).forEach(new Consumer<Updatable>() {
                @Override
                public void accept(Updatable updatable) {
                    updatable.update((long) (nsPerTick / 1_000_000));
                }
            });
        }

        delorean.transform(deltaRotationVector);

        stream(renderables).forEach(new Consumer<RenderableAsSquare>() {
            @Override
            public void accept(RenderableAsSquare renderableAsSquare) {
                renderableAsSquare.render(renderer);
            }
        });
    }

    public void setGyroscopeValues(float[] deltaRotationVector) {
        this.deltaRotationVector = deltaRotationVector;
    }

    public void reset() {
        delorean.reset();
    }
}
