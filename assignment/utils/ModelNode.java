package utils;

import com.jogamp.opengl.GL3;

public class ModelNode extends SGNode {

//    protected Model model;
    protected ModelMultipleLights model;

    public ModelNode(String name, ModelMultipleLights m) {
        super(name);
        model = m;
    }

    public void draw(GL3 gl) {
        model.render(gl, worldTransform);
        for (int i = 0; i < children.size(); i++) {
            children.get(i).draw(gl);
        }
    }

}