package utils;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import gmath.*;

/**
 * @author Yuan Gao
 * @date 10/01/2024 17:27
 * @email 2581677302@qq.com
 */
public final class Skybox {
    private TextureLibrary textures;
    private Shader shader;
    private Camera camera;
    private String[] faces;

    public Skybox(GL3 gl) {
        textures = new TextureLibrary();
        faces = new String[]{
                "cubemap/right.jpg", "cubemap/left.jpg",
                "cubemap/top.jpg", "cubemap/bottom.jpg",
                "cubemap/back.jpg", "cubemap/front.jpg"};

        textures.loadSkyBox(gl,faces);
//        textures.addSkybox(gl, "skybox", faces);

        fillBuffers(gl);
        shader = new Shader(gl, "shader/vs_skybox", "shader/fs_skybox");
    }

    public void render(GL3 gl) {
        gl.glDepthMask(false);
        gl.glDepthFunc(GL.GL_LEQUAL);

        shader.use(gl);
        Mat4 m = camera.getViewMatrix();
        m = Mat4.multiply(m, Mat4Transform.scale(100,100,100));
        shader.setFloatArray(gl, "view", m.toFloatArrayForGLSL());
        m = camera.getPerspectiveMatrix();
        // m = Mat4Transform.perspective((float) Math.toRadians(45), (float)1024/(float)768);
        shader.setFloatArray(gl, "projection", m.toFloatArrayForGLSL());

        shader.setInt(gl, "skybox", 0);

        gl.glBindVertexArray(vertexArrayId[0]);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, 36);
        gl.glBindVertexArray(0);

        gl.glDepthMask(true);
        gl.glDepthFunc(GL.GL_LESS);
    }

    public void dispose(GL3 gl) {
        gl.glDeleteBuffers(1, vertexBufferId, 0);
        gl.glDeleteVertexArrays(1, vertexArrayId, 0);
        gl.glDeleteBuffers(1, elementBufferId, 0);
    }

    float vertices[] = {
            // positions
            -1.0f,  1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,

            -1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,

            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            -1.0f, -1.0f,  1.0f,
            -1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,

            -1.0f,  1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f, -1.0f,

            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f,  1.0f
    };


    private int vertexStride = 3;
    private int vertexXYZFloats = 3;

    // ***************************************************
    /* THE LIGHT BUFFERS
     */

    private int[] vertexBufferId = new int[1];
    private int[] vertexArrayId = new int[1];
    private int[] elementBufferId = new int[1];

    private void fillBuffers(GL3 gl) {
        gl.glGenVertexArrays(1, vertexArrayId, 0);
        gl.glBindVertexArray(vertexArrayId[0]);
        gl.glGenBuffers(1, vertexBufferId, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vertexBufferId[0]);
        FloatBuffer fb = Buffers.newDirectFloatBuffer(vertices);

        gl.glBufferData(GL.GL_ARRAY_BUFFER, Float.BYTES * vertices.length, fb, GL.GL_STATIC_DRAW);

        int stride = vertexStride;
        int numXYZFloats = vertexXYZFloats;
        int offset = 0;
        gl.glVertexAttribPointer(0, numXYZFloats, GL.GL_FLOAT, false, stride*Float.BYTES, offset);
        gl.glEnableVertexAttribArray(0);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
