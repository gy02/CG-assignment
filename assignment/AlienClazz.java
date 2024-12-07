import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.Texture;
import utils.*;
import gmath.*;

/**
 * I declare that this class is my own work
 *
 * @Date:2023/11/12
 * @Author:Yuan Gao
 */
public class AlienClazz {
    Mesh mesh;
    Shader shader;
    Material material;
    Mat4 modelMatrix;
    ModelMultipleLights sphere;
    private SGNode alienRoot;
    Mat4 m;

    //create alien
    public AlienClazz(GL3 gl, Camera camera, Light[] lights, TransformNode translateAll, TransformNode rotateAll, TransformNode rotateHead, Texture diffuse, Texture specular) {

        mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        shader = new Shader(gl, "shader/vs_standard.txt", "shader/fs_for_alien_2t.txt");
        material = new Material(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(0.5f, 0.5f, 0.5f), new Vec3(0.1f, 0.1f, 0.1f), 32.0f);
        modelMatrix = null;
        sphere = new ModelMultipleLights("alien", mesh, modelMatrix, shader, material, lights, camera, diffuse, specular);

        alienRoot = new NameNode("two-branch structure");
        //alien's body(lower branch)
        float bodyHeight = 4f;
        NameNode bodyBranch = new NameNode("body branch");
        m = Mat4Transform.scale(bodyHeight, bodyHeight, bodyHeight);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode makeBodyBranch = new TransformNode("alien body", m);
        ModelNode body = new ModelNode("body", sphere);

        //alien's head(upper branch)
        float headHeight = 3f;
        TransformNode translateToHead = new TransformNode("translateToHead", Mat4Transform.translate(0, bodyHeight, 0));
        NameNode headBranch = new NameNode("head branch");
        m = Mat4Transform.scale(headHeight, headHeight, headHeight);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode makeHeadBranch = new TransformNode("alien head", m);
        ModelNode head = new ModelNode("head", sphere);

        //alien's antenna(2 spheres)
        //first sphere--spike
        float antennaSpikeHeight = 0.8f;
        TransformNode translateToSpike = new TransformNode("translateToHead", Mat4Transform.translate(0, headHeight, 0));
        NameNode antennaSpikeBranch = new NameNode("antennaSpike branch");
        m = Mat4Transform.scale(0.1f, antennaSpikeHeight, 0.1f);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode makeSpikeBranch = new TransformNode("alien antenna spike", m);
        ModelNode antennaSpike = new ModelNode("antennaSpike", sphere);
        //second sphere--top
        float antennaTopHeight = 0.3f;
        TransformNode translateToAntennaTop = new TransformNode("translateToAntennaTop", Mat4Transform.translate(0, antennaSpikeHeight, 0));
        NameNode antennaTopBranch = new NameNode("antenna branch");
        m = Mat4Transform.scale(antennaTopHeight, antennaTopHeight, antennaTopHeight);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode makeTopBranch = new TransformNode("alien antenna top", m);
        ModelNode antennaTop = new ModelNode("antennaTop", sphere);

        //alien's arms
        float armLength = 2f;
        float armWidth = 0.4f;
        float rotateArm = 45;
        Texture armTexture = TextureLibrary.loadTexture(gl, "textures/arm.jpg");
        sphere = new ModelMultipleLights("alien", mesh, modelMatrix, shader, material, lights, camera, armTexture);
        //left arm
        TransformNode translateToLeftArm = new TransformNode("translateToLeftArm", Mat4Transform.translate(bodyHeight / 2, bodyHeight / 2, 0));
        NameNode leftArmBranch = new NameNode("left arm branch");
        //rotate first?
        m = Mat4Transform.rotateAroundZ(-rotateArm);
        m = Mat4.multiply(m, Mat4Transform.scale(armWidth, armLength, armWidth));
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode makeLeftArm = new TransformNode("alien left arm", m);
        ModelNode leftArm = new ModelNode("left arm", sphere);
        //right arm
        TransformNode translateToRightArm = new TransformNode("translateToRightArm", Mat4Transform.translate(-bodyHeight / 2, bodyHeight / 2, 0));
        NameNode rightArmBranch = new NameNode("right arm branch");
        m = Mat4Transform.rotateAroundZ(rotateArm);
        m = Mat4.multiply(m, Mat4Transform.scale(armWidth, armLength, armWidth));
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode makeRightArm = new TransformNode("alien Right arm", m);
        ModelNode rightArm = new ModelNode("right arm", sphere);

        //alien's eyes
        float eyeDiameter = 0.5f;
        float eyeHeight = 1.8f;
        float eyeWidth = 0.8f;
        TransformNode translateToEye = new TransformNode("translateToEye", Mat4Transform.translate(0, eyeHeight, 0));
        NameNode eyeBranch = new NameNode("eye branch");
        //change eye color by different texture
        Texture eyeTexture = TextureLibrary.loadTexture(gl, "textures/eye.jpg");
        sphere = new ModelMultipleLights("alien", mesh, modelMatrix, shader, material, lights, camera, eyeTexture);
        //left eye
        m = Mat4Transform.scale(eyeDiameter + 0.1f, eyeDiameter, eyeDiameter);
        m = Mat4.multiply(m, Mat4Transform.translate(eyeWidth, 0.5f, 2.5f));
        TransformNode makeLeftEyeBranch = new TransformNode("alien left eye", m);
        ModelNode leftEye = new ModelNode("left eye", sphere);
        //right eye
        m = Mat4Transform.scale(eyeDiameter + 0.1f, eyeDiameter, eyeDiameter);
        m = Mat4.multiply(m, Mat4Transform.translate(-eyeWidth, 0.5f, 2.5f));
        TransformNode makeRightEyeBranch = new TransformNode("alien right eye", m);
        ModelNode rightEye = new ModelNode("right eye", sphere);

        //alien's ears
        float earWidth = 0.2f;
        float earHeight = 1.5f;
        Texture earTexture = TextureLibrary.loadTexture(gl, "textures/ear.jpg");
        sphere = new ModelMultipleLights("alien", mesh, modelMatrix, shader, material, lights, camera, earTexture);
        //left ear
        TransformNode translateToLeftEar = new TransformNode("translateToLeftEar", Mat4Transform.translate(headHeight / 2, headHeight / 2, 0));
        NameNode leftEarBranch = new NameNode("left ear branch");
        m = Mat4Transform.scale(earWidth, earHeight, earWidth);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode makeLeftEarBranch = new TransformNode("alien left ear", m);
        ModelNode leftEar = new ModelNode("left ear", sphere);
        //right ear
        TransformNode translateToRightEar = new TransformNode("translateToRightEar", Mat4Transform.translate(-headHeight / 2, headHeight / 2, 0));
        NameNode rightEarBranch = new NameNode("right ear branch");
        m = Mat4Transform.scale(earWidth, earHeight, earWidth);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode makeRightEarBranch = new TransformNode("alien right ear", m);
        ModelNode rightEar = new ModelNode("right ear", sphere);

        alienRoot.addChild(translateAll);
        translateAll.addChild(rotateAll);
        rotateAll.addChild(bodyBranch);
        bodyBranch.addChild(makeBodyBranch);
        makeBodyBranch.addChild(body);
        bodyBranch.addChild(translateToHead);
        translateToHead.addChild(rotateHead);
        rotateHead.addChild(headBranch);
        headBranch.addChild(makeHeadBranch);
        makeHeadBranch.addChild(head);
        headBranch.addChild(translateToLeftEar);
        translateToLeftEar.addChild(leftEarBranch);
        leftEarBranch.addChild(makeLeftEarBranch);
        makeLeftEarBranch.addChild(leftEar);
        headBranch.addChild(translateToRightEar);
        translateToRightEar.addChild(rightEarBranch);
        rightEarBranch.addChild(makeRightEarBranch);
        makeRightEarBranch.addChild(rightEar);
        headBranch.addChild(translateToSpike);
        translateToSpike.addChild(antennaSpikeBranch);
        antennaSpikeBranch.addChild(makeSpikeBranch);
        makeSpikeBranch.addChild(antennaSpike);
        antennaSpikeBranch.addChild(translateToAntennaTop);
        translateToAntennaTop.addChild(antennaTopBranch);
        antennaTopBranch.addChild(makeTopBranch);
        makeTopBranch.addChild(antennaTop);
        headBranch.addChild(translateToEye);
        translateToEye.addChild(eyeBranch);
        eyeBranch.addChild(makeLeftEyeBranch);
        makeLeftEyeBranch.addChild(leftEye);
        eyeBranch.addChild(makeRightEyeBranch);
        makeRightEyeBranch.addChild(rightEye);
        bodyBranch.addChild(translateToLeftArm);
        translateToLeftArm.addChild(leftArmBranch);
        leftArmBranch.addChild(makeLeftArm);
        makeLeftArm.addChild(leftArm);
        bodyBranch.addChild(translateToRightArm);
        translateToRightArm.addChild(rightArmBranch);
        rightArmBranch.addChild(makeRightArm);
        makeRightArm.addChild(rightArm);
    }

    public SGNode getRoot() {
        return alienRoot;
    }
}
