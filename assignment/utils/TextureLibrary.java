package utils;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.spi.JPEGImage;

import java.io.File;
import java.io.FileInputStream;

public final class TextureLibrary {
    // no mip-mapping (see next example)
    public static Texture loadTexture(GL3 gl3, String filename) {
        Texture t = null;
        try {
            File f = new File(filename);
            t = (Texture) TextureIO.newTexture(f, true);
            t.bind(gl3);
            t.setTexParameteri(gl3, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
            t.setTexParameteri(gl3, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
//      t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_S, GL3.GL_CLAMP_TO_EDGE);
            t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_S, GL3.GL_REPEAT);
//      t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_T, GL3.GL_CLAMP_TO_EDGE);
            t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_T, GL3.GL_REPEAT);
        } catch (Exception e) {
            System.out.println("Error loading texture " + filename);
        }
        return t;
    }

    // only deals with rgb jpg files
    public static int[] loadRepeatTexture(GL3 gl, String filename) {
        return loadTexture(gl, filename, GL.GL_REPEAT, GL.GL_REPEAT,
                GL.GL_LINEAR, GL.GL_LINEAR);
    }


    public static int[] loadTexture(GL3 gl, String filename,
                                    int wrappingS, int wrappingT, int filterS, int filterT) {
        int[] textureId = new int[1];
        try {
            File f = new File(filename);
            JPEGImage img = JPEGImage.read(new FileInputStream(f));
            gl.glGenTextures(1, textureId, 0);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textureId[0]);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, wrappingS);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, wrappingT);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, filterS);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, filterT);
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, img.getWidth(), img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getData());
            gl.glGenerateMipmap(GL.GL_TEXTURE_2D);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
            gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        } catch (Exception e) {
            System.out.println("Error loading texture " + filename);
        }
        return textureId;
    }

    public Texture loadSkyBox(GL3 gl3, String[] filename) {
        Texture t = null;
        try {
            for (int i = 0; i < 6; i++) {
                File f = new File(filename[i]);
                t = (Texture) TextureIO.newTexture(f, true);
                TextureData data = (TextureData) TextureIO.newTextureData(gl3.getGLProfile(), f, true, TextureIO.JPG);
                // InputStream inputStream = new FileInputStream(f);
                // JPEGImage data1 = JPEGImage.read(inputStream);
                t.bind(gl3);

                gl3.glTexImage2D(GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL.GL_RGB,
                        t.getImageWidth(), t.getImageHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, data.getBuffer());
            }
        } catch (Exception e) {
            System.out.println("Error loading skybox " + filename[0] + e);
        }

        gl3.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
        gl3.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
        gl3.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_WRAP_S, GL3.GL_CLAMP_TO_EDGE);
        gl3.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_WRAP_T, GL3.GL_CLAMP_TO_EDGE);
        gl3.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_WRAP_R, GL3.GL_CLAMP_TO_EDGE);
        System.out.println(111);
        return t;
    }

//    public void addSkybox(GL3 gl, String name, String[] filename) {
//        Texture texture = loadSkyBox(gl, filename);
//        textures.put(name, texture);
//    }
}