package utils;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MyMouseInput extends MouseMotionAdapter {
    private Point lastpoint;
    private Camera camera;

    public MyMouseInput(Camera camera) {
        this.camera = camera;
    }

    /**
     * mouse is used to control camera position
     *
     * @param e instance of MouseEvent
     */
    public void mouseDragged(MouseEvent e) {
        Point ms = e.getPoint();
        float sensitivity = 0.001f;
        float dx = (float) (ms.x - lastpoint.x) * sensitivity;
        float dy = (float) (ms.y - lastpoint.y) * sensitivity;
        //System.out.println("dy,dy: "+dx+","+dy);
        if (e.getModifiers() == MouseEvent.BUTTON1_MASK)
            camera.updateYawPitch(dx, -dy);
        lastpoint = ms;
    }

    /**
     * mouse is used to control camera position
     *
     * @param e instance of MouseEvent
     */
    public void mouseMoved(MouseEvent e) {
        lastpoint = e.getPoint();
    }
}
