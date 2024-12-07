import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.Texture;
import utils.*;
import gmath.*;

public class ALIENS_GLEventListener implements GLEventListener {
    private static final boolean DISPLAY_SHADERS = false;

    public ALIENS_GLEventListener(Camera camera) {
        this.camera = camera;
        //center view
        this.camera.setPosition(new Vec3(0f, 5f, 18f));
    }

    // ***************************************************
    /*
     * METHODS DEFINED BY GLEventListener
     */

    /**
     * I declare that this code is my own work
     *
     * @Date:2023/12/1
     * @Author:Yuan Gao
     */
    public void lightOff() {
        this.lights[0].lightOff();
        this.lights[1].lightOff();
    }

    public void lightOn() {
        this.lights[0].lightOn();
        this.lights[1].lightOn();
    }

    private boolean isBulbOn = true;

    public void bulbOn() {
        bulbLight.bulbOn();
        isBulbOn = true;
    }

    public void bulbOff() {
        bulbLight.bulbOff();
        isBulbOn = false;
    }

    public void rockBody() {
        isRockBody = !isRockBody;
        if (isRockBody) {
            if (pauseBodyTime > 0) {
                System.out.println(1);
                startBodyTime += getSeconds() - pauseBodyTime;//time that has elapsed during the pause period
                pauseBodyTime = 0;
            } else {//first Rock
                startBodyTime = getSeconds();
            }
        } else {//Record the timestamp when paused
            pauseBodyTime = getSeconds();
        }
    }

    public void rockHead() {
        isRockHead = !isRockHead;
        if (isRockHead) {
            if (pauseHeadTime > 0) {
                System.out.println(1);
                startHeadTime += getSeconds() - pauseHeadTime;
                pauseHeadTime = 0;
            } else {
                startHeadTime = getSeconds();
            }
        } else {
            pauseHeadTime = getSeconds();
        }
    }

    @Override
    /* Initialisation */
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LESS);
        gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
        gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
        gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
        initialise(gl);
        startTime = getSeconds();
        startBodyTime = getSeconds();
        startHeadTime = getSeconds();
    }

    /* Called to indicate the drawing surface has been moved and/or resized  */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glViewport(x, y, width, height);
        float aspect = (float) width / (float) height;
        camera.setPerspectiveMatrix(Mat4Transform.perspective(90, aspect));
    }

    /* Draw */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        render(gl);
    }

    /* Clean up memory, if necessary */
    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        lights[0].dispose(gl);
        lights[1].dispose(gl);
        tt1.dispose(gl);
        tt2.dispose(gl);
//        sphere.dispose(gl);
    }


    // ***************************************************
    /* INTERACTION
     *
     *
     */

    public void incXPosition() {
        xPosition += 0.5f;
        if (xPosition > 5f) xPosition = 5f;
        updateX();
    }

    public void decXPosition() {
        xPosition -= 0.5f;
        if (xPosition < -5f) xPosition = -5f;
        updateX();
    }

    private void updateX() {
        translateAll.setTransform(Mat4Transform.translate(xPosition, 0, 0));
        translateAll.update(); // IMPORTANT–the scene graph has changed
    }


    // ***************************************************
    /* THE SCENE
     * Now define all the methods to handle the scene.
     * This will be added to in later examples.
     */

    private Camera camera;
    private Mat4 perspective;
    //    private Model tt1,tt2, sphere,lightSphere,bulb;
    private ModelMultipleLights tt1, tt2, sphere, lightSphere, bulb;
    private Shader bulbShader, backShader, floorShader, shader;
    private Skybox skyBox;
    //    private Light light,light1;
    private Light[] lights = new Light[3];
    private Bulb bulbLight;
    private SGNode alien1Root, alien2Root, lightRoot;

    private TransformNode translateAll, rotateAll1, rotateAll2;
    private TransformNode translateLight, rotateLight;
    private TransformNode rotateHead1, rotateHead2;
    private float xPosition = 3f;
    private float rotateAllAngleStart = 20, rotateAllAngle = rotateAllAngleStart;
    //    private float rotateUpperAngleStart = -60, rotateUpperAngle = rotateUpperAngleStart;
    private float rotateLightAngleStart = -120, rotateLightAngle = rotateLightAngleStart;
    private float rotateHeadAngle = 0;
    //    private float rotateUpper2Angle = rotateUpperAngleStart;
    private AlienClazz alien1, alien2;


    private void initialise(GL3 gl) {
        createRandomNumbers();

        Texture floorTexture = TextureLibrary.loadTexture(gl, "textures/floor.jpg");
        Texture snowTexture = TextureLibrary.loadTexture(gl, "textures/snow.png");
        Texture backTexture = TextureLibrary.loadTexture(gl, "textures/back.jpg");
        Texture jadeTexture = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
        Texture jadeSpecular = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");
        Texture venusTexture = TextureLibrary.loadTexture(gl, "textures/venus.jpg");
        Texture venusSpecular = TextureLibrary.loadTexture(gl, "textures/venus_specular.jpg");


        /**
         *I declare that this code is my own work
         *@Date:2023/11/30
         *@Author:Yuan Gao
         */
        lights[0] = new Light(gl);
        lights[0].setCamera(camera);
        lights[1] = new Light(gl);
        lights[1].setCamera(camera);
        bulbLight = new Bulb(gl);
        bulbLight.setCamera(camera);
        lights[2] = bulbLight;


        //left alien
        rotateAll1 = new TransformNode("rotateLeftAlien", Mat4Transform.rotateAroundZ(0));
        rotateHead1 = new TransformNode("rotateLeftHead", Mat4Transform.rotateAroundZ(rotateHeadAngle));
        translateAll = new TransformNode("translateLeftAlien", Mat4Transform.translate(-xPosition, 0, 0));
        alien1 = new AlienClazz(gl, camera, lights, translateAll, rotateAll1, rotateHead1, jadeTexture, jadeSpecular);
        alien1Root = alien1.getRoot();
        //right alien
        rotateAll2 = new TransformNode("rotateRightALien", Mat4Transform.rotateAroundZ(0));
        rotateHead2 = new TransformNode("rotateRightHead", Mat4Transform.rotateAroundZ(rotateHeadAngle));
        translateAll = new TransformNode("translateRightAlien", Mat4Transform.translate(xPosition, 0, 0));
        alien2 = new AlienClazz(gl, camera, lights, translateAll, rotateAll2, rotateHead2, venusTexture, venusSpecular);
        alien2Root = alien2.getRoot();

        //the backdrop
        Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        floorShader = new Shader(gl, "shader/vs_standard.txt", "shader/fs_standard_m_1t.txt");
        backShader = new Shader(gl, "shader/vs_standard.txt", "shader/fs_standard_m_2t.txt");

//        skyBox=new Shader(gl,"shader/vs_light.txt","shader/fs_skybox");

        Material material = new Material(new Vec3(1f, 1f, 1f), new Vec3(1f, 1f, 1f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
        Mat4 modelMatrix = Mat4Transform.scale(20, 1, 20);
        tt1 = new ModelMultipleLights("floor", mesh, modelMatrix, floorShader, material, lights, camera, floorTexture);
        tt2 = new ModelMultipleLights("back", mesh, modelMatrix, backShader, material, lights, camera, backTexture, snowTexture);

        //create the spotlight
        float lightOffset = -8f;
        lightRoot = new NameNode("spotlight branch structure");
        translateLight = new TransformNode("translateX", Mat4Transform.translate(lightOffset, 0, 0));
        mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        shader = new Shader(gl, "shader/vs_standard.txt", "shader/fs_standard_m_0t.txt");
//        material = new Material(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(1.0f, 1f, 1f), new Vec3(0f, 0f, 0f), 32.0f);
        material = new Material(new Vec3(0.1f, 0.1f, 0.1f), new Vec3(1.0f, 1f, 1f), new Vec3(0f, 0f, 0f), 32.0f);
        modelMatrix = null;
        lightSphere = new ModelMultipleLights("spotLight", mesh, modelMatrix, shader, material, lights, camera);
        bulbShader = new Shader(gl, "shader/vs_standard.txt", "shader/fs_bulb.txt");
        bulbShader.use(gl);
        bulbShader.setInt(gl, "bulbOn", 1);
        bulb = new ModelMultipleLights("spotLight", mesh, modelMatrix, bulbShader, material, lights, camera);

        //create the light post
        float lightHeight = 10f;
        NameNode lightPostBranch = new NameNode("light post branch");
        Mat4 m = Mat4Transform.scale(0.5f, lightHeight, 0.5f);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode makeLightPostBranch = new TransformNode("light post", m);
        ModelNode lightPost = new ModelNode("post", lightSphere);
        //create the light shade & bulb
        //create the shade
        float shadeHeight = 3f;
        rotateLight = new TransformNode("rotateLight", Mat4Transform.rotateAroundZ(rotateLightAngle));
        TransformNode translateToShade = new TransformNode("translateToShade", Mat4Transform.translate(0, lightHeight, 0));
        NameNode lightShadeBranch = new NameNode("light shade branch");
        m = Mat4Transform.scale(0.8f, shadeHeight, 0.8f);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0, 0));
        TransformNode makeLightShadeBranch = new TransformNode("light shade", m);
        ModelNode lightShade = new ModelNode("shade", lightSphere);
        //create the bulb
        TransformNode translateToBulb = new TransformNode("translateToBulb", Mat4Transform.translate(0, shadeHeight / 2, 0));
        NameNode bulbBranch = new NameNode("bulb branch");
        m = Mat4Transform.scale(0.5f, 1, 0.5f);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0f, 0));
        TransformNode makeLightBulbBranch = new TransformNode("light bulb", m);
        ModelNode lightBulb = new ModelNode("bulb", bulb);

        //----------------------------------------------------------------------
        /**
         *I declare that this code is my own work
         *@Date:2023/11/15
         *@Author:Yuan Gao
         */
        lightRoot.addChild(translateLight);
        translateLight.addChild(lightPostBranch);
        lightPostBranch.addChild(makeLightPostBranch);
        makeLightPostBranch.addChild(lightPost);
        lightPostBranch.addChild(translateToShade);
        translateToShade.addChild(rotateLight);
        rotateLight.addChild(lightShadeBranch);

        lightShadeBranch.addChild(makeLightShadeBranch);
        makeLightShadeBranch.addChild(lightShade);
        lightShadeBranch.addChild(translateToBulb);
        translateToBulb.addChild(bulbBranch);
        bulbBranch.addChild(makeLightBulbBranch);
        makeLightBulbBranch.addChild(lightBulb);


        // IMPORTANT–must be done every time any part of the scene graph changes
        alien1Root.update();
        alien2Root.update();
        lightRoot.update();
    }

    // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
    private Mat4 getMforFloor() {
        float size = 16f;
        Mat4 modelMatrix = new Mat4(1);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(size, 1f, size), modelMatrix);
        return modelMatrix;
    }

    // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
    private Mat4 getMforBack() {
        float size = 16f;
        Mat4 modelMatrix = new Mat4(1);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(size, 1f, size), modelMatrix);
        modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
        modelMatrix = Mat4.multiply(Mat4Transform.translate(0, size * 0.5f, -size * 0.5f), modelMatrix);
        return modelMatrix;
    }

    /**
     * I declare that this code is my own work
     *
     * @Date:2023/11/15
     * @Author:Yuan Gao
     */
    private void render(GL3 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        lights[0].setPosition(-8, 8, 8);
        lights[0].render(gl);
        lights[1].setPosition(8, 8, 8);
        lights[1].render(gl);
        lights[2].setPosition(getBulbPosition());
        lights[2].render(gl);

        tt1.setModelMatrix(getMforFloor());
        tt1.render(gl);
        tt2.setModelMatrix(getMforBack());
        tt2.render(gl);

        if (isRockBody)
            updateBody();
        if (isRockHead)
            updateHead();

        updateBulb();
        snow(gl);
        turnBulb(gl);

        alien1Root.draw(gl);
        alien2Root.draw(gl);
        lightRoot.draw(gl);

//        skyBox.render(gl);
    }

    /**
     * I declare that this code is my own work
     *
     * @Date:2023/11/30
     * @Author:Yuan Gao
     */
    private void turnBulb(GL3 gl) {
        if (isBulbOn) {
            bulbShader.use(gl);
            bulbShader.setInt(gl, "bulbOn", 1);
        } else {
            bulbShader.use(gl);
            bulbShader.setInt(gl, "bulbOn", 0);
        }
    }

    /**
     * I declare that this code is my own work
     *
     * @Date:2023/11/29
     * @Author:Yuan Gao
     */
    private void snow(GL3 gl) {
        double elapsedTime = getSeconds() - startTime;
        double t = elapsedTime * 0.1;
//        float offsetX = 0;
        float offsetX = (float) Math.sin(t);
        float offsetY = (float) (t - Math.floor(t));
        backShader.setFloat(gl, "offset", offsetX, offsetY);
    }

    private boolean isRockBody = false, isRockHead = false;

    /**
     * I declare that this code is my own work
     *
     * @Date:2023/11/28
     * @Author:Yuan Gao
     */
    private void updateBody() {
        double elapsedTime = getSeconds() - startBodyTime;
        rotateAllAngle = rotateAllAngleStart * (float) Math.sin(elapsedTime);

        rotateAll1.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
        rotateAll2.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));

        alien1Root.update(); // IMPORTANT–the scene graph has changed
        alien2Root.update();
    }

    /**
     * I declare that this code is my own work
     *
     * @Date:2023/11/28
     * @Author:Yuan Gao
     */
    private void updateHead() {
        double elapsedTime = getSeconds() - startHeadTime;
        rotateAllAngle = rotateAllAngleStart * (float) Math.sin(elapsedTime);

        rotateHead1.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
        rotateHead2.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));

        alien1Root.update(); // IMPORTANT–the scene graph has changed
        alien2Root.update();
    }

    /**
     * I declare that this code is my own work
     *
     * @Date:2023/11/29
     * @Author:Yuan Gao
     */
    private void updateBulb() {
        double elapsedTime = getSeconds() - startTime;

        float rotateX = -20.0f * (float) (Math.sin(elapsedTime));
        float rotateZ = -20.0f * (float) (Math.cos(elapsedTime));

        Mat4 multiply = Mat4.multiply(Mat4Transform.rotateAroundX(rotateX), Mat4Transform.rotateAroundZ(rotateZ));
        multiply = Mat4.multiply(multiply, Mat4Transform.rotateAroundZ(rotateLightAngle));
        rotateLight.setTransform(multiply);

        lightRoot.update();
    }

    // The light's postion is continually being changed, so needs to be calculated for each frame.
    private Vec3 getLightPosition() {
        double elapsedTime = getSeconds() - startTime;
        float x = 5.0f * (float) (Math.sin(Math.toRadians(elapsedTime * 50)));
        float y = 2.7f;
        float z = 5.0f * (float) (Math.cos(Math.toRadians(elapsedTime * 50)));
        return new Vec3(x, y, z);
        //return new Vec3(5f,3.4f,5f);
    }

    /**
     * I declare that this code is my own work
     * *Apply the bulb's branching transform (translational/rotation) to the bulb light to fit the two
     *
     * @Date:2023/11/26
     * @Author:Yuan Gao
     */
    private Vec3 getBulbPosition() {
        double elapsedTime = getSeconds() - startTime;

        float x = 0;
//        float y = 2.0f * (float) (Math.sin(Math.toRadians(elapsedTime * 50)));
        float y = 4.0f * (float) (Math.sin(elapsedTime));
//        float z = 2.0f * (float) (Math.cos(Math.toRadians(elapsedTime * 50)));
        float z = 4.0f * (float) (Math.cos(elapsedTime));

        //Transform to the specified position
        Vec4 vec4 = new Vec4(x, y, z, 1);
        Mat4 translate = Mat4Transform.translate(-8f, 0, 0);
        translate = Mat4.multiply(translate, Mat4Transform.translate(0, 10f, 0));
        translate = Mat4.multiply(translate, Mat4Transform.translate(1.061f, 0, 0));
        Vec4 result = Mat4.multiply(translate, vec4);
        return result.toVec3();
    }
    // ***************************************************
    /* TIME
     */

    private double startTime, startBodyTime, startHeadTime;
    private double pauseBodyTime, pauseHeadTime;

    private double getSeconds() {
        return System.currentTimeMillis() / 1000.0;
    }

    // ***************************************************
    /* An array of random numbers
     */

    private int NUM_RANDOMS = 1000;
    private float[] randoms;

    private void createRandomNumbers() {
        randoms = new float[NUM_RANDOMS];
        for (int i = 0; i < NUM_RANDOMS; ++i) {
            randoms[i] = (float) Math.random();
        }
    }
}
