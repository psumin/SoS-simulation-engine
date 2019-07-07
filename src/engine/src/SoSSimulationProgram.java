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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

// Add parts of key

// 참고: http://www.java-gaming.org/topics/basic-game/21919/view.html
// SoSSimulationProgram 클래스에서 참조하고 있는 클래스: Time, SoSObject, SoSScenario

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class SoSSimulationProgram implements Runnable, KeyListener {

    final int SIMULATION_WIDTH = 910;
    final int SIMULATION_HEIGHT = 910;
    final int CONSOLE_WIDTH = 500;

    JFrame frame;
    Canvas canvas;
    BufferStrategy bufferStrategy;

    boolean isExpert = false;

    SimulationConsole console;

    public SoSSimulationProgram(){
        frame = new JFrame("SimulationEngine");

        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(SIMULATION_WIDTH + CONSOLE_WIDTH, SIMULATION_HEIGHT));
        //panel.setPreferredSize(new Dimension(SIMULATION_WIDTH , SIMULATION_HEIGHT));
        panel.setLayout(null);
//        panel.setLayout(new FlowLayout());

        panel.setLayout(null);

        console = new SimulationConsole();
        console.setPreferredSize(new Dimension(CONSOLE_WIDTH, SIMULATION_HEIGHT));
        console.setBounds(SIMULATION_WIDTH, 0, CONSOLE_WIDTH, SIMULATION_HEIGHT);
        panel.add(console);

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
            System.out.println("Stop button pressed!");
            pause = true;
            console.requestFocus();
        }
//        if (e.getKeyCode() == KeyEvent.VK_G) {
//            pause = false;
//        }
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
            } else { // pause
                if(isExpert) {
                    expertMode();
                } else {
                    beginnerMode();
                }
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
    protected void update(int deltaTime){



        time += deltaTime;
        if(time >= Time.fromSecond(0.0f)) {
            timeImpl.update(deltaTime);
            world.update();
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

    private void expertMode() {
        System.out.print("Input command here: ");
        Scanner input = new Scanner(System.in);
        String command = input.next().toLowerCase();
        switch (command) {
            case "resume":
                pause = false;
                canvas.requestFocus();
                break;
            case "add":
                command = input.next().toLowerCase();
                // add firefighter 275 5
                switch (command) {
                    case "firefighter":
                        world.onAddFireFighter(input.nextInt(), input.nextInt());
                        break;
                    case "ambulance":
                        world.onAddAmbulance(input.nextInt(), input.nextInt());
                        break;
                }

                break;
        }
    }

    private void beginnerMode() {
        String menu = "===== Menu\n" +
                "  1. Add\n" +
                "  2. Set\n" +
                "  3. Remove\n" +
                "  0. Resume\n" +
                "===== Input command: ";

        int command = getCommandOnBeginnerMode(menu);
        switch (command) {
            case 1:
                menu = "==== Add menu\n" +
                        "  1. Ambulance\n" +
                        "  2. FireFighter\n" +
                        "===== Input command: ";
                command = getCommandOnBeginnerMode(menu);
                switch (command) {
                    case 1: {
                        int frame = getCommandOnBeginnerMode("frame count: ");
                        int number = getCommandOnBeginnerMode("number of ambulance: ");
                        world.onAddAmbulance(frame, number);
                        break;
                    }
                    case 2: {
                        int frame = getCommandOnBeginnerMode("frame count: ");
                        int number = getCommandOnBeginnerMode("number of firefighter: ");
                        world.onAddFireFighter(frame, number);
                        break;
                    }
                }
                break;
            case 2:
                break;
            case 3:
                break;
            case 0:
                canvas.requestFocus();
                pause = false;
                break;
        }
    }

    int getCommandOnBeginnerMode(String menu) {
        System.out.print(menu);
        Scanner input = new Scanner(System.in);
        while(true) {
            if (input.hasNextInt()) {
                break;
            } else {
                System.out.println("Please enter number");
                System.out.println(input.next());
                System.out.print(menu);
            }
        }
        return input.nextInt();
    }


    public static class SimulationConsole extends TextArea {

        SimulationInputStream inputStream = new SimulationInputStream();
        public SimulationConsole() {
            this.setRows(24);
            this.setColumns(80);
            setBackground(Color.BLACK);
            setForeground(Color.LIGHT_GRAY);
            setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    append(String.valueOf((char) b));
                }
            }));

            this.addKeyListener(inputStream);
            System.setIn(inputStream);
        }
    }

    public static class SimulationInputStream extends InputStream implements KeyListener {

        ArrayBlockingQueue<Integer> queue;

        public SimulationInputStream(){
            queue=new ArrayBlockingQueue<Integer>(1024);
        }

        @Override
        public int read() throws IOException {
            Integer i=null;
            try {
                i = queue.take();
            } catch (InterruptedException ex) {
                Logger.getLogger(Console.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
            if(i!=null)
                return i;
            return -1;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (b == null) {
                throw new NullPointerException();
            } else if (off < 0 || len < 0 || len > b.length - off) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }
            int c = read();
            if (c == -1) {
                return -1;
            }
            b[off] = (byte)c;

            int i = 1;
            try {
                for (; i < len && available() > 0 ; i++) {
                    c = read();
                    if (c == -1) {
                        break;
                    }
                    b[off + i] = (byte)c;
                }
            } catch (IOException ee) {
            }
            return i;

        }



        @Override
        public int available(){
            return queue.size();
        }

        @Override
        public void keyTyped(KeyEvent e) {
            int c = e.getKeyChar();
            try {
                if(c == '\n') {
                    queue.put((int)'\r');
                }
                queue.put(c);
            } catch (InterruptedException ex) {
                Logger.getLogger(Console.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

    }
}