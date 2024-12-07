package utils;

public final class TwoTriangles {

    // ***************************************************
    /* THE DATA
     */
    // anticlockwise/counterclockwise ordering
    public static final float[] vertices = {      // position, colour, tex coords
    -0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f,  // top left
    -0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f,  // bottom left
     0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f,  // bottom right
     0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f   // top right

            //floor is 16:9
//            -8f, 0.0f, -4.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,  // top left
//            -8f, 0.0f, 4.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,  // bottom left
//            8f, 0.0f, 4.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,  // bottom right
//            8f, 0.0f, -4.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f   // top right
    };

    public static final int[] indices = {         // Note that we start from 0!
            0, 1, 2,
            0, 2, 3
    };

}