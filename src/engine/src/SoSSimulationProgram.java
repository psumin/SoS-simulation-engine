import core.World;
import misc.Time;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.Scanner;

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
    final int MAX_SIMULATION_COUNT = 2;
 
    final int SIMULATION_WIDTH = 910;
    final int SIMULATION_HEIGHT = 910;
//    final int CONSOLE_WIDTH = 200;

    JFrame frame;
    Canvas canvas;
    BufferStrategy bufferStrategy;
    boolean isExpert = false;


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
            System.out.println("Stop button pressed!");
            pause = true;

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
                frame.setVisible(false);
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

    private void expertMode() {
        System.out.print("Input command here: ");
        Scanner input = new Scanner(System.in);
        String command = input.next().toLowerCase();
        switch (command) {
            case "resume":
                pause = false;
                frame.setVisible(true);
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
                frame.setVisible(true);
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

}
