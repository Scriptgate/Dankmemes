package scriptgate.net.dankmemes;

import android.content.Context;

import net.scriptgate.common.Point3D;
import net.scriptgate.opengles.activity.Resumable;
import net.scriptgate.opengles.matrix.ModelMatrix;
import net.scriptgate.opengles.matrix.ModelViewProjectionMatrix;
import net.scriptgate.opengles.matrix.ProjectionMatrix;
import net.scriptgate.opengles.matrix.ViewMatrix;
import net.scriptgate.opengles.program.Program;
import net.scriptgate.opengles.renderer.RendererBase;

import java8.util.function.Consumer;

import static android.opengl.GLES20.*;
import static net.scriptgate.common.Color.GREY;
import static net.scriptgate.opengles.matrix.ViewMatrix.createViewBehindOrigin;
import static net.scriptgate.opengles.program.AttributeVariable.COLOR;
import static net.scriptgate.opengles.program.AttributeVariable.POSITION;
import static net.scriptgate.opengles.program.ProgramBuilder.program;

public class DankmemesRenderer extends RendererBase implements Resumable {


    private Background background;
    private Grid grid;
    private Horizon horizon;
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

    @Override
    public void onDrawFrame() {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

        background.update();
        grid.update();

        background.render(renderer);
        grid.render(renderer);
        horizon.render(renderer);
        delorean.transform(deltaRotationVector);
        delorean.render(renderer);
    }

    public void setGyroscopeValues(float[] deltaRotationVector) {
        this.deltaRotationVector = deltaRotationVector;
    }

    @Override
    public void onResume() {
        delorean.center();
    }

    @Override
    public void onPause() {
        delorean.center();
    }
}
