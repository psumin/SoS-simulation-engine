import misc.Time;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

// 참고: http://www.java-gaming.org/topics/basic-game/21919/view.html
public class SoSSimulationEngine implements Runnable {

    final int SIMULATION_WIDTH = 800;
    final int SIMULATION_HEIGHT = 800;
    final int CONSOLE_WIDTH = 200;

    JFrame frame;
    Canvas canvas;
    BufferStrategy bufferStrategy;

    public SoSSimulationEngine(){
        frame = new JFrame("SimulationEngine");

        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(SIMULATION_WIDTH + CONSOLE_WIDTH, SIMULATION_HEIGHT));
        //panel.setLayout(null);
        panel.setLayout(new FlowLayout());

        // 시뮬레이션 콘솔 GUI
        Button button = new Button("Console GUI Here");
        button.setPreferredSize(new Dimension(CONSOLE_WIDTH, SIMULATION_HEIGHT));
        panel.add(button);
        // 시뮬레이션 콘솔 GUI

        // 시뮬레이션 화면
        canvas = new Canvas();
        canvas.setBounds(0, 0, SIMULATION_WIDTH, SIMULATION_HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);
        // 시뮬레이션 화면

        // 마우스 이벤트... 알아보기
        canvas.addMouseListener(new MouseControl());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        canvas.requestFocus();
    }


    private class MouseControl extends MouseAdapter{

    }

    long desiredFPS = 60;
    long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;

    boolean running = true;

    public void run(){

        long beginLoopTime;
        long endLoopTime;
        long currentUpdateTime = System.nanoTime();
        long lastUpdateTime;
        long deltaLoop;

        while(running){
            beginLoopTime = System.nanoTime();

            // 보통은 update 이후 render를 하는데, 여기서는 순서가 반대임
            // 이게 맞는 것 같기도 함
            // 문제 생길 것 같진 않지만
            // 나중에 문제가 생기게 된다면 그때 수정
            render();

            lastUpdateTime = currentUpdateTime;
            currentUpdateTime = System.nanoTime();
            update((int) ((currentUpdateTime - lastUpdateTime)/(1000*1000)));

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

    /**
     * Rewrite this method for your game
     */
    // deltaTime 단위: 밀리초
    protected void update(int deltaTime){
        timeImpl.update(deltaTime);
    }

    /**
     * Rewrite this method for your game
     */
    protected void render(Graphics2D g){
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, SIMULATION_WIDTH, SIMULATION_HEIGHT);
    }

    public static void main(String [] args){

        SoSSimulationEngine simulationEngine = new SoSSimulationEngine();
        new Thread(simulationEngine).start();
    }
}
