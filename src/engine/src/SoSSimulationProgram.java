//import core.World;
//import misc.Time;
//
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.awt.image.BufferStrategy;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//// 참고: http://www.java-gaming.org/topics/basic-game/21919/view.html
//// SoSSimulationProgram 클래스에서 참조하고 있는 클래스: Time, SoSObject, SoSScenario
//
///**
// * Project: NewSimulator
// * Created by IntelliJ IDEA
// * Author: Sumin Park <smpark@se.kaist.ac.kr>
// * Github: https://github.com/sumin0407/NewSimulator.git
// */
//
//public class SoSSimulationProgram implements Runnable {
//
//    final int SIMULATION_WIDTH = 910;
//    final int SIMULATION_HEIGHT = 910;
////    final int CONSOLE_WIDTH = 200;
//
//    JFrame frame;
//    Canvas canvas;
//    BufferStrategy bufferStrategy;
//
//    public SoSSimulationProgram(){
//        frame = new JFrame("SimulationEngine");
//
//        JPanel panel = (JPanel) frame.getContentPane();
////        panel.setPreferredSize(new Dimension(SIMULATION_WIDTH + CONSOLE_WIDTH, SIMULATION_HEIGHT));
//        panel.setPreferredSize(new Dimension(SIMULATION_WIDTH , SIMULATION_HEIGHT));
//        //panel.setLayout(null);
//        panel.setLayout(new FlowLayout());
//
//        // 시뮬레이션 콘솔 GUI
////        Button button = new Button("Console GUI Here");
////        button.setPreferredSize(new Dimension(CONSOLE_WIDTH, SIMULATION_HEIGHT));
////        panel.add(button);
//        // 시뮬레이션 콘솔 GUI
//
//        // 시뮬레이션 화면
//        canvas = new Canvas();
//        canvas.setBounds(0, 0, SIMULATION_WIDTH, SIMULATION_HEIGHT);
//        canvas.setIgnoreRepaint(true);
//
//        panel.add(canvas);
//        // 시뮬레이션 화면
//
//        // 마우스 이벤트... 알아보기
//        canvas.addMouseListener(new MouseControl());
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setResizable(false);
//        frame.setVisible(true);
//
//        canvas.createBufferStrategy(2);
//        bufferStrategy = canvas.getBufferStrategy();
//
//        canvas.requestFocus();
//
//        frame.addWindowListener(new WindowAdapter() {
//            public void windowClosed(WindowEvent e) {
//                //System.out.println("jdialog window closed event received");
//            }
//
//            public void windowClosing(WindowEvent e) {
//                running = false;
//                clear();
//            }
//        });
//    }
//
//
//    private class MouseControl extends MouseAdapter{
//        public void mouseClicked(MouseEvent e) {
//            int a = 10;
//        }
//    }
//
//    long desiredFPS = 120;
//    long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;
//
//    boolean running = true;
//
//    public void run(){
//
//        long beginLoopTime;
//        long endLoopTime;
//        long currentUpdateTime = System.nanoTime();
//        long lastUpdateTime;
//        long deltaLoop;
//
//        init();
//        while(running){
//            beginLoopTime = System.nanoTime();
//            render();
//
//            lastUpdateTime = currentUpdateTime;
//            currentUpdateTime = System.nanoTime();
//            update((int) ((currentUpdateTime - lastUpdateTime)/(1000*1000)));
//
//            endLoopTime = System.nanoTime();
//            deltaLoop = endLoopTime - beginLoopTime;
//
//            if(deltaLoop > desiredDeltaLoop){
//                //Do nothing. We are already late.
//            }else{
//                try{
//                    Thread.sleep((desiredDeltaLoop - deltaLoop)/(1000*1000));
//                }catch(InterruptedException e){
//                    //Do nothing
//                }
//            }
//        }
//    }
//
//    private void render() {
//        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
//        g.clearRect(0, 0, SIMULATION_WIDTH, SIMULATION_HEIGHT);
//        render(g);
//        g.dispose();
//        bufferStrategy.show();
//    }
//
//
//
//    // misc.Time class implementation
//    private static final class TimeImpl extends Time {
//        public TimeImpl() {
//            instance = this;
//            deltaTime = 0;
//            time = 0;
//            frameCount = 0;
//        }
//
//        public void update(int deltaTime) {
//            this.deltaTime = deltaTime;
//            time += deltaTime;
//            frameCount++;
//        }
//    }
//    TimeImpl timeImpl = new TimeImpl();
//
//    World world;
//    protected void init() {
//        world = new World();
//    }
//
//    /**
//     * Rewrite this method for your game
//     */
//    // deltaTime 단위: 밀리초
//    int time = 0;
//    protected void update(int deltaTime){
//
//
//        time += deltaTime;
//        if(time >= Time.fromSecond(0.0f)) {
//            timeImpl.update(deltaTime);
//            world.update();
//            time = 0;
//        }
//    }
//
//    /**
//     * Rewrite this method for your game
//     */
//    protected void render(Graphics2D g){
//        g.setColor(new Color(255, 255, 255));
//        g.fillRect(0, 0, SIMULATION_WIDTH, SIMULATION_HEIGHT);
//        world.render(g);
//    }
//
//    protected void clear() {
//        world.clear();
//    }
//
//    public static void main(String [] args){
//
//            SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
//            new Thread(simulationEngine).start();
//    }
//}








import core.World;
import misc.Time;

import java.awt.*;
import java.awt.event.*;
// Add parts of key

import java.awt.image.BufferStrategy;
import javax.swing.*;

// 참고: http://www.java-gaming.org/topics/basic-game/21919/view.html
// SoSSimulationProgram 클래스에서 참조하고 있는 클래스: Time, SoSObject, SoSScenario

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class SoSSimulationProgram implements Runnable, KeyListener {

    final int MAX_SIMULATION_COUNT = 2;

    final int SIMULATION_WIDTH = 910;
    final int SIMULATION_HEIGHT = 910;
//    final int CONSOLE_WIDTH = 200;

    JFrame frame;
    Canvas canvas;
    BufferStrategy bufferStrategy;


    public SoSSimulationProgram(){
        frame = new JFrame("SimulationEngine");

        JPanel panel = (JPanel) frame.getContentPane();
//        panel.setPreferredSize(new Dimension(SIMULATION_WIDTH + CONSOLE_WIDTH, SIMULATION_HEIGHT));
        panel.setPreferredSize(new Dimension(SIMULATION_WIDTH , SIMULATION_HEIGHT));
        //panel.setLayout(null);
        panel.setLayout(new FlowLayout());

        // 시뮬레이션 콘솔 GUI
//        Button button = new Button("Console GUI Here");
//        button.setPreferredSize(new Dimension(CONSOLE_WIDTH, SIMULATION_HEIGHT));
//        panel.add(button);
        // 시뮬레이션 콘솔 GUI

        // 시뮬레이션 화면
        canvas = new Canvas();
        canvas.setBounds(0, 0, SIMULATION_WIDTH, SIMULATION_HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);
        // 시뮬레이션 화면

        // 마우스 이벤트... 알아보기
        canvas.addMouseListener(new MouseControl());
        canvas.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        canvas.requestFocus();


        frame.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                //System.out.println("jdialog window closed event received");
            }

            public void windowClosing(WindowEvent e) {
                running = false;
                clear();
            }
        });

    }


    boolean pause = false;

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            System.out.println("Stop buttom pressed!");
            pause = true;

        }
        if (e.getKeyCode() == KeyEvent.VK_G) {
            pause = false;
        }
        //restart
        if (e.getKeyCode() == KeyEvent.VK_I) {
            pause = false;
            init();
        }
    }
    public void keyReleased(KeyEvent e)
    {}
    public void keyTyped(KeyEvent e)
    {}


    private class MouseControl extends MouseAdapter{
        public void mouseClicked(MouseEvent e) {
            int a = 10;
        }
    }

    long desiredFPS = 120;
    long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;

    boolean running = true;

    boolean stop = false;

    public void run(){
//        Scanner scan = new Scanner();
        long beginLoopTime;
        long endLoopTime;
        long currentUpdateTime = System.nanoTime();
        long lastUpdateTime;
        long deltaLoop;

        init();

        while(running){

            beginLoopTime = System.nanoTime();
            render();

            lastUpdateTime = currentUpdateTime;
            currentUpdateTime = System.nanoTime();
            if(!pause) {
                update((int) ((currentUpdateTime - lastUpdateTime) / (1000 * 1000)));
            }
            endLoopTime = System.nanoTime();
            deltaLoop = endLoopTime - beginLoopTime;

            if(deltaLoop > desiredDeltaLoop){
                //Do nothing. We are already late.
            }else{
                try{
                    Thread.sleep((desiredDeltaLoop - deltaLoop)/(1000*1000));
                }catch(InterruptedException e){
                    //Do nothing
                }
            }
        }
    }

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, SIMULATION_WIDTH, SIMULATION_HEIGHT);
        render(g);
        g.dispose();
        bufferStrategy.show();
    }



    // misc.Time class implementation
    private static final class TimeImpl extends Time {
        public TimeImpl() {
            instance = this;
            deltaTime = 0;
            time = 0;
            frameCount = 0;
        }

        public void update(int deltaTime) {
            this.deltaTime = deltaTime;
            time += deltaTime;
            frameCount++;
        }
    }
    TimeImpl timeImpl = new TimeImpl();

    World world;
    protected void init() {
        world = new World();

    }

    /**
     * Rewrite this method for your game
     */
    // deltaTime 단위: 밀리초
    int time = 0;
    int simulation_count = 1;
    protected void update(int deltaTime){



        time += deltaTime;
        if(time >= Time.fromSecond(0.0f)) {
            timeImpl.update(deltaTime);
            world.update();
            if(world.isFinished() && simulation_count < MAX_SIMULATION_COUNT) {
                simulation_count++;
                world = new World();
            }
            time = 0;
        }
    }

    /**
     * Rewrite this method for your game
     */
    protected void render(Graphics2D g){
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, SIMULATION_WIDTH, SIMULATION_HEIGHT);
        world.render(g);
    }

    protected void clear() {
        world.clear();
    }

    public static void main(String [] args){

        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        new Thread(simulationEngine).start();
    }
}