
import core.World;
import misc.Time;

import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
// Add parts of key

import java.awt.image.BufferStrategy;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import javax.swing.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import misc.ExcelHelper;

// 참고: http://www.java-gaming.org/topics/basic-game/21919/view.html
// SoSSimulationProgram 클래스에서 참조하고 있는 클래스: Time, SoSObject, SoSScenario

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class SoSSimulationProgram implements Runnable, KeyListener {

    final int MAX_SIMULATION_COUNT = 100;                          // 시뮬레이션 반복 횟수
    final int MAX_FRAME_COUNT = 200;                                // 각 시뮬레이션마다 최대 frame의 수

    final int SIMULATION_WIDTH = 910;                               // 시뮬레이션 GUI의 너비
    final int SIMULATION_HEIGHT = 910;                              // 시뮬레이션 GUI의 높이
//    final int CONSOLE_WIDTH = 200;

    JFrame frame;
    Canvas canvas;
    BufferStrategy bufferStrategy;
    boolean isExpert = false;
    boolean isFirstSimulation = true;

    long programStartTime;                                          // 프로그램 시작 시간
    long programEndTime;                                            // 프로그램 종료 시간


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
//        canvas.addMouseListener(new MouseControl());                      // 현재 마우스는 사용 안하므로 필요없음
        canvas.addKeyListener(this);                                     // Interactive simulation 을 위해서 key listener 가 필요함. Key listener 를 통해서 프로그램을 정지시키고 stimulus 를 삽입한다.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);               // 종료를 위한건데, 없어도 동작하네?
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        canvas.requestFocus();                                              // 처음에 프로그램을 시작시키고 focus를 GUI (Canvas)에 적용한다.


        // 현재 필요없는 기능
//        frame.addWindowListener(new WindowAdapter() {
//            public void windowClosed(WindowEvent e) {
//                //System.out.println("dialog window closed event received");
//            }
//
//            public void windowClosing(WindowEvent e) {
//                running = false;
//                clear();
//            }
//        });

    }


    boolean pause = false;

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_S) {                          // 첫번째 시뮬레이션에서 "S" key 가 눌리면 pause 의 value 를 true 로!
            if(isFirstSimulation) {
                //System.out.println("Stop button pressed!");
                pause = true;
            }
        }
//        if (e.getKeyCode() == KeyEvent.VK_G) {
//            pause = false;
//        }
//        //restart
//        if (e.getKeyCode() == KeyEvent.VK_I) {
//            pause = false;
//            init();
//        }
    }
    public void keyReleased(KeyEvent e)
    {}
    public void keyTyped(KeyEvent e)
    {}

    // 현재 마우스 event는 사용하지 않음
//    private class MouseControl extends MouseAdapter{
//        public void mouseClicked(MouseEvent e) {
//            int a = 10;
//        }
//    }

    long desiredFPS = 120;
    long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;

    boolean running = true;

    boolean stop = false;

    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet statisticsSheet;
    XSSFSheet inputScenarioSheet;
    CellStyle headerStyle;

    public void run(){
//        Scanner scan = new Scanner();
        long beginLoopTime;
        long endLoopTime;
        long currentUpdateTime = System.nanoTime();
        long lastUpdateTime;
        long deltaLoop;

        statisticsSheet = workbook.createSheet("statistics");
        inputScenarioSheet = workbook.createSheet("inputScenarios");

        headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Sheet sheet = statisticsSheet;
        Row row = sheet.createRow(0);
        ExcelHelper.getCell(row, 0).setCellValue("saved rate");
        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);

        init();                                                         // World 초기화

        programStartTime = System.nanoTime();
        while(running) {

            if (isFirstSimulation) {                                    // 첫 번째 시뮬레이션에만 적용
                beginLoopTime = System.nanoTime();
                render();                                               // 첫 번째 시뮬레이션에서만 GUI 를 rendering 한다.

                lastUpdateTime = currentUpdateTime;
                currentUpdateTime = System.nanoTime();
                if (!pause) {
                    update((int) ((currentUpdateTime - lastUpdateTime) / (1000 * 1000)));
                } else {                                                // 키보드 입력을 통한 pause 는 첫 번째 시뮬레이션에서만!
                    frame.setVisible(false);                            // pause 상태에서는 GUI 를 숨긴다.
                    if (isExpert) {                                     // Expert 모드와 Beginner 모드가 존재함
                        expertMode();                                   // String 으로 stimulus 입력 가능
                    } else {
                        beginnerMode();                                 // 메뉴에 따라서 stimulus 입력 가능
                    }
                }

                endLoopTime = System.nanoTime();
                deltaLoop = endLoopTime - beginLoopTime;

                if (deltaLoop > desiredDeltaLoop) {
                    //Do nothing. We are already late.
                } else {
                    try {
                        Thread.sleep((desiredDeltaLoop - deltaLoop) / (1000 * 1000));
                    } catch (InterruptedException e) {
                        //Do nothing
                    }
                }
            } else {                                                    // 첫 번째 시뮬레이션이 아니면 그냥 계속해서 업데이트 진행. stop 없음
                update(1);
            }
        }

        programEndTime = System.nanoTime();
        System.out.println("=== Program running time: " + (programEndTime - programStartTime) / (float)1000_000_000 + " sec");          // 전체 프로그램 실행 시간
    }

    // Rendering
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

        public void reset() {
            frameCount = 0;
        }

        public void update(int deltaTime) {
            this.deltaTime = deltaTime;
            time += deltaTime;
            frameCount++;
        }
    }
    TimeImpl timeImpl = new TimeImpl();

    // World 초기화
    World world;
    protected void init() {
        world = new World(MAX_FRAME_COUNT, true);

    }

    /**
     * Rewrite this method for your game
     */
    // deltaTime 단위: 밀리초
    int time = 0;
    int simulation_count = 1;


    // Upodate 실행하는 함수
    protected void update(int deltaTime){

        System.out.println("Simulation repeated: " + simulation_count + " Frame count: " + timeImpl.getFrameCount());


        time += deltaTime;
        if(time >= Time.fromSecond(0.0f)) {
            timeImpl.update(deltaTime);
            world.update();
            if(world.isFinished()) {
                Sheet sheet = statisticsSheet;
                Row row = sheet.createRow(simulation_count);
                ExcelHelper.getCell(row, 0).setCellValue("" + world.getSavedRate());
                if(isFirstSimulation) {                                                 // 첫 번째 시뮬레이션일 때 초기 정보 저장
                    sheet = inputScenarioSheet;
                    int rowNum = 0;

                    row = sheet.createRow(rowNum++);
                    ExcelHelper.getCell(row, 0).setCellValue("# of Patient");
                    ExcelHelper.getCell(row, 1).setCellValue("# of FireFighter");
                    ExcelHelper.getCell(row, 2).setCellValue("# of Hospital");
                    ExcelHelper.getCell(row, 3).setCellValue("# of Ambulance");
                    ExcelHelper.getCell(row, 4).setCellValue("# of Bridgehead");
                    ExcelHelper.getCell(row, 5).setCellValue("Max frame count");
                    ExcelHelper.getCell(row, 6).setCellValue("Max simulation time");
                    for(int i = 0; i < 7; ++i) {
                        ExcelHelper.getCell(row, i).setCellStyle(headerStyle);
                    }

                    row = sheet.createRow(rowNum++);
                    ExcelHelper.getCell(row, 0).setCellValue(World.maxPatient);
                    ExcelHelper.getCell(row, 1).setCellValue(World.maxFireFighter);
                    ExcelHelper.getCell(row, 2).setCellValue(World.maxHospital);
                    ExcelHelper.getCell(row, 3).setCellValue(World.maxAmbulance);
                    ExcelHelper.getCell(row, 4).setCellValue(World.maxBridgehead);
                    ExcelHelper.getCell(row, 5).setCellValue(MAX_FRAME_COUNT);
                    ExcelHelper.getCell(row, 6).setCellValue(MAX_SIMULATION_COUNT);

                    row = sheet.createRow(rowNum++);
                    for(World.InputData inputData: World.inputDatum) {                                  // 첫 번째 시뮬레이션일 때 적용한 시나리오 저장 (router, msg 제외)
                        row = sheet.createRow(rowNum++);
                        int colNum = 0;
                        ExcelHelper.getCell(row, colNum).setCellValue("command: ");
                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.command);

                        ExcelHelper.getCell(row, colNum).setCellValue("frame: ");
                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.frame);

                        ExcelHelper.getCell(row, colNum).setCellValue("count: ");
                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.count);
                    }

                    row = sheet.createRow(rowNum++);
                    for(World.InputMessages inputMsg: World.inputMsg) {                                  // 첫 번째 시뮬레이션일 때 적용한 시나리오 저장 (router, msg 관련)
                        row = sheet.createRow(rowNum++);
                        int colNum = 0;
                        ExcelHelper.getCell(row, colNum).setCellValue("command: ");
                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
                        ExcelHelper.getCell(row, colNum++).setCellValue(inputMsg.command);

                        ExcelHelper.getCell(row, colNum).setCellValue("start frame: ");
                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
                        ExcelHelper.getCell(row, colNum++).setCellValue(inputMsg.startFrame);

                        ExcelHelper.getCell(row, colNum).setCellValue("finish frame: ");
                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
                        ExcelHelper.getCell(row, colNum++).setCellValue(inputMsg.finishFrame);

                        ExcelHelper.getCell(row, colNum).setCellValue("sender: ");
                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
                        ExcelHelper.getCell(row, colNum++).setCellValue(inputMsg.sender);

                        ExcelHelper.getCell(row, colNum).setCellValue("receiver: ");
                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
                        ExcelHelper.getCell(row, colNum++).setCellValue(inputMsg.receiver);

                        ExcelHelper.getCell(row, colNum).setCellValue("duration: ");
                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
                        ExcelHelper.getCell(row, colNum++).setCellValue(inputMsg.duration);
                    }
                }

                // 시뮬레이션의 반복 실행 횟수가 최대가 될 때까지 반복한다.
                if(simulation_count < MAX_SIMULATION_COUNT) {
                    simulation_count++;                                         // 시뮬레이션 실행 횟수는 증가
                    timeImpl.reset();                                           // frame 의 시작은 0으로 초기화.
                    isFirstSimulation = false;                                  // 첫 번째 시뮬레이션이 아니므로 boolean 값을 false 로 바꿔준다
                    world = new World(MAX_FRAME_COUNT, false);      // world 를 다시 생성한다.
                } else {
                    clear();
                    running = false;
                    frame.dispose();
                }
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
        long nano = System.currentTimeMillis();
        String date = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss").format(nano);
        String filePath = "new_log/" + date + ".xlsx";

        ExcelHelper.autoSizeAllColumn(workbook);
        ExcelHelper.save(workbook, filePath);
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