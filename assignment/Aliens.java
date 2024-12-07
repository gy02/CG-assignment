import utils.*;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Aliens extends JFrame implements ActionListener {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
    private GLCanvas canvas;
    private ALIENS_GLEventListener glEventListener;
    private final FPSAnimator animator;
    private Camera camera;

    public Aliens(String textForTitleBar) {
        super(textForTitleBar);
        GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
        canvas = new GLCanvas(glcapabilities);
        camera = new Camera(Camera.DEFAULT_POSITION, Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
        glEventListener = new ALIENS_GLEventListener(camera);
        canvas.addGLEventListener(glEventListener);
        canvas.addMouseMotionListener(new MyMouseInput(camera));
        canvas.addKeyListener(new MyKeyboardInput(camera));
        getContentPane().add(canvas, BorderLayout.CENTER);

        //TODO:add button
        JMenuBar menuBar=new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);
        menuBar.add(fileMenu);

        JPanel p = new JPanel();
        //general lights
        JButton b = new JButton("lights on");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("lights off");
        b.addActionListener(this);
        p.add(b);
        //bulb
        b = new JButton("bulb on");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("bulb off");
        b.addActionListener(this);
        p.add(b);
        //action of aliens
        b = new JButton("rock body");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("rock head");
        b.addActionListener(this);
        p.add(b);
        this.add(p, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                animator.stop();
                remove(canvas);
                dispose();
                System.exit(0);
            }
        });
        animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("lights on")) {
            glEventListener.lightOn();
        }
        else if (e.getActionCommand().equalsIgnoreCase("lights off")) {
            glEventListener.lightOff();
        }
        else if (e.getActionCommand().equalsIgnoreCase("bulb on")) {
            glEventListener.bulbOn();
        }
        else if (e.getActionCommand().equalsIgnoreCase("bulb off")) {
            glEventListener.bulbOff();
        }
        else if (e.getActionCommand().equalsIgnoreCase("rock body")) {
            glEventListener.rockBody();
        }
        else if (e.getActionCommand().equalsIgnoreCase("rock head")) {
            glEventListener.rockHead();
        }
    }

    public static void main(String[] args) {
        Aliens aliens=new Aliens("2 aliens");
        aliens.getContentPane().setPreferredSize(dimension);
        aliens.pack();
        aliens.setVisible(true);
    }
}
