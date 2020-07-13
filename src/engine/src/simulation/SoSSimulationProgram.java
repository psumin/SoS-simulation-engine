package simulation;

import core.RuntimeMonitoring;
import core.World;
import log.Log;
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
// simulation.SoSSimulationProgram 클래스에서 참조하고 있는 클래스: Time, SoSObject, SoSScenario

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class SoSSimulationProgram implements KeyListener {
    String filePath;
    int super_counter = 1;
    final int MAX_SIMULATION_COUNT = 1;                          // 시뮬레이션 반복 횟수
    final int MAX_FRAME_COUNT = 1000;                                // 각 시뮬레이션마다 최대 frame의 수

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

    public void setRunning() {
        this.running = true;
    }

    public Boolean getRunning() {
        return this.running;
    }

    public void setSuper_counter() {
        this.super_counter++;
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

    long desiredFPS = 60;
    long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;

    boolean running = true;

    boolean stop = false;

    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet statisticsSheet;
    XSSFSheet inputScenarioSheet;
    CellStyle headerStyle;

    public Log run(){
//        Scanner scan = new Scanner();
//        RuntimeMonitoring runtimeMonitoring = new RuntimeMonitoring();
//        String className = "core.World";
        long beginLoopTime;
        long endLoopTime;
        long currentUpdateTime = System.nanoTime();
        long lastUpdateTime;
        long deltaLoop;

        Log log = new Log();
        if(isFirstSimulation) {
            long nano = System.currentTimeMillis();
            String date = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss").format(nano);
            filePath = "log/new_log/bad1/" + date + ".xlsx";
            statisticsSheet = workbook.createSheet("statistics");
//            inputScenarioSheet = workbook.createSheet("inputScenarios");
        }



        headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Sheet sheet = statisticsSheet;
        Row row = sheet.createRow(0);
        ExcelHelper.getCell(row, 0).setCellValue("Treatment rate");
        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);

        init();                                                         // World 초기화

//        programStartTime = System.nanoTime();
        while(running) {
            if (isFirstSimulation) {                                    // 첫 번째 시뮬레이션에만 적용
                beginLoopTime = System.nanoTime();
                render();                                               // 첫 번째 시뮬레이션에서만 GUI 를 rendering 한다.

                lastUpdateTime = currentUpdateTime;
                currentUpdateTime = System.nanoTime();
                if (!pause) {
                    update((int) ((currentUpdateTime - lastUpdateTime) / (1000 * 1000)), log);
//                    runtimeMonitoring.classLoader(className, 0.5);
                } else {                                                // 키보드 입력을 통한 pause 는 첫 번째 시뮬레이션에서만!
                    frame.setVisible(true);                            // pause 상태에서는 GUI 를 숨긴다.
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
                update(1, log);

            }




        }

//        programEndTime = System.nanoTime();
//        System.out.println("=== Program running time: " + (programEndTime - programStartTime) / (float)1000_000_000 + " sec");          // 전체 프로그램 실행 시간
//        System.out.println("Rescued People: " + world.rescuedPatientCount);

        return log;
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
        if(super_counter == 1)
            world = new World(MAX_FRAME_COUNT, true);
        else
            world = new World(MAX_FRAME_COUNT, false);

    }

    /**
     * Rewrite this method for your game
     */
    // deltaTime 단위: 밀리초
    int time = 0;
    int simulation_count = 1;


    // Upodate 실행하는 함수
    protected void update(int deltaTime, Log log){

//        System.out.println("Simulation repeated: " + simulation_count + " Frame count: " + timeImpl.getFrameCount());

        time += deltaTime;
        if(time >= Time.fromSecond(0.0f)) {
            timeImpl.update(deltaTime);
            world.update();
            log.addSnapshot(timeImpl.getFrameCount(), " RescuedRate: " + String.valueOf(world.getRescuedRate())
                    + " TreatmentRate: " +  String.valueOf(world.getTreatmentRate()) +
                    " CurrentFF: " + world.getFFNumber() + " CurrentAmb: " + world.getAmbNumber() + " " + world.printCSSnapshot());
//            System.out.println(timeImpl.getFrameCount());
            if(world.isFinished()) {                        //Maximum frame 지나면 true로 들어올 수 있음
//                System.out.println("isFinished is true!!!!!!!!!!!!!!!!!!!!!!!!!");
//                System.out.println(timeImpl.getFrameCount());
//                System.out.println("getRescuedRate: " + world.getRescuedRate());
//                log.addSnapshot(time, "RescuedRate: " + String.valueOf(world.getSavedRate()));
//                log.addSnapshot(timeImpl.getFrameCount() + 1, " RescuedRate: " + String.valueOf(world.getRescuedRate())  + " TreatmentRate: " +  String.valueOf(world.getTreatmentRate()) +
//                    " CurrentFF: " + world.getFFNumber() + " CurrentAmb: " + world.getAmbNumber() + " " + world.printCSSnapshot());
//                System.out.println("log add 후에 모습: !!!!");
//                log.printSnapshot();
//                log.addSnapshot(time, "RescuedPatient: " + String.valueOf(world.rescuedPatientCount));
                Sheet sheet = statisticsSheet;
                Row row = sheet.createRow(simulation_count);
//                ExcelHelper.getCell(row, 0).setCellValue("" + world.getRescuedRate());
                ExcelHelper.getCell(row, 0).setCellValue("" + world.getTreatmentRate());
                if(isFirstSimulation) {                                                 // 첫 번째 시뮬레이션일 때 초기 정보 저장
//                    sheet = inputScenarioSheet;
//                    int rowNum = 0;
//
//                    row = sheet.createRow(rowNum++);
//                    ExcelHelper.getCell(row, 0).setCellValue("# of Patient");
//                    ExcelHelper.getCell(row, 1).setCellValue("# of FireFighter");
//                    ExcelHelper.getCell(row, 2).setCellValue("# of Hospital");
//                    ExcelHelper.getCell(row, 3).setCellValue("# of Ambulance");
//                    ExcelHelper.getCell(row, 4).setCellValue("# of Bridgehead");
//                    ExcelHelper.getCell(row, 5).setCellValue("Max frame count");
////                    ExcelHelper.getCell(row, 6).setCellValue("Max simulation time");
////                    for(int i = 0; i < 7; ++i) {
//                    for(int i = 0; i < 6; ++i) {
//                        ExcelHelper.getCell(row, i).setCellStyle(headerStyle);
//                    }
//
//                    row = sheet.createRow(rowNum++);
//                    ExcelHelper.getCell(row, 0).setCellValue(World.maxPatient);
//                    ExcelHelper.getCell(row, 1).setCellValue(World.maxFireFighter);
//                    ExcelHelper.getCell(row, 2).setCellValue(World.maxHospital);
//                    ExcelHelper.getCell(row, 3).setCellValue(World.maxAmbulance);
//                    ExcelHelper.getCell(row, 4).setCellValue(World.maxBridgehead);
//                    ExcelHelper.getCell(row, 5).setCellValue(MAX_FRAME_COUNT);
////                    ExcelHelper.getCell(row, 6).setCellValue(MAX_SIMULATION_COUNT);
//
//
//                                                                                                       // 사용자에 의해서 추가되는 시나리오가 있으면 log에 추가된다. ==> new_log
//                    row = sheet.createRow(rowNum++);
//                    for(DataStructure.AddCS inputData: World.addCS) {                                  // 첫 번째 시뮬레이션일 때 적용한 시나리오 저장 (router, msg 제외)
//                        row = sheet.createRow(rowNum++);
//                        int colNum = 0;
//                        ExcelHelper.getCell(row, colNum).setCellValue("command: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.command);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("frame: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.frame);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("How many?: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.count);
//                    }
//
//                    row = sheet.createRow(rowNum++);
//                    for(DataStructure.Message inputData: World.message) {                                  // 첫 번째 시뮬레이션일 때 적용한 시나리오 저장 (router, msg 관련)
//                        row = sheet.createRow(rowNum++);
//                        int colNum = 0;
//                        ExcelHelper.getCell(row, colNum).setCellValue("command: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.command);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("start frame: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.startFrame);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("end frame: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.finishFrame);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("sender: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.sender);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("receiver: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.receiver);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("delay duration: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.duration);
//                    }
//
//                    row = sheet.createRow(rowNum++);
//                    for(DataStructure.Range inputData: World.range) {                                  // 첫 번째 시뮬레이션일 때 적용한 시나리오 저장 (router, msg 관련)
//                        row = sheet.createRow(rowNum++);
//                        int colNum = 0;
//                        ExcelHelper.getCell(row, colNum).setCellValue("command: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.command);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("frame: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.frame);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("left: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.left);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("top: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.top);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("right: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.right);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("bottom: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.bottom);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("ratio: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue((Float) inputData.value);
//                    }
//
//                    row = sheet.createRow(rowNum++);
//                    for(DataStructure.ChangeAll inputData: World.changeAll) {                                  // 첫 번째 시뮬레이션일 때 적용한 시나리오 저장 (router, msg 관련)
//                        row = sheet.createRow(rowNum++);
//                        int colNum = 0;
//                        ExcelHelper.getCell(row, colNum).setCellValue("command: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.command);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("frame: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.frame);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("value: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue((int) inputData.value);
//                    }
//
//                    row = sheet.createRow(rowNum++);
//                    for(DataStructure.ChangeOne inputData: World.changeOne) {                                  // 첫 번째 시뮬레이션일 때 적용한 시나리오 저장 (router, msg 관련)
//                        row = sheet.createRow(rowNum++);
//                        int colNum = 0;
//                        ExcelHelper.getCell(row, colNum).setCellValue("command: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.command);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("frame: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.frame);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("number: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.number);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("value: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue((int) inputData.value);
//                    }
//
//                    row = sheet.createRow(rowNum++);
//                    for(DataStructure.RemoveCS inputData: World.removeCS) {                                  // 첫 번째 시뮬레이션일 때 적용한 시나리오 저장 (router, msg 관련)
//                        row = sheet.createRow(rowNum++);
//                        int colNum = 0;
//                        ExcelHelper.getCell(row, colNum).setCellValue("command: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.command);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("frame: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.frame);
//
//                        ExcelHelper.getCell(row, colNum).setCellValue("number: ");
//                        ExcelHelper.getCell(row, colNum++).setCellStyle(headerStyle);
//                        ExcelHelper.getCell(row, colNum++).setCellValue(inputData.number);
//                    }


                }

                // 시뮬레이션의 반복 실행 횟수가 최대가 될 때까지 반복한다.
//                if(simulation_count < MAX_SIMULATION_COUNT) {
                simulation_count++;                                         // 시뮬레이션 실행 횟수는 증가
                timeImpl.reset();                                           // frame 의 시작은 0으로 초기화.
                isFirstSimulation = false;                                  // 첫 번째 시뮬레이션이 아니므로 boolean 값을 false 로 바꿔준다
//                    world = new World(MAX_FRAME_COUNT, false);      // world 를 다시 생성한다.
//                }
//                else{
                clear();
                running = false;
                frame.dispose();
//                }
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
//        long nano = System.currentTimeMillis();
//        String date = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss").format(nano);
//        String filePath = "log/new_log/bad1/" + date + ".xlsx";
//
//        ExcelHelper.autoSizeAllColumn(workbook);
//        ExcelHelper.save(workbook, filePath);
    }

    /*public static void main(String [] args){

        simulation.SoSSimulationProgram simulationEngine = new simulation.SoSSimulationProgram();
        for (int i = 0; i < 5; i++) {
            simulationEngine.running = true;
            simulationEngine.run();
            simulationEngine.super_counter++;
        }
//        new Thread(simulationEngine).start();
    }*/
    /**
     * Insertion parts
     */
    private void expertMode() {
        System.out.println("현재 시뮬레이션 Frame: " + timeImpl.getFrameCount());
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
                    case "ambulance":
                        world.interAddAmb(input.nextInt(), input.nextInt());
                        break;

                    case "firefighter":
                        world.interAddFF(input.nextInt(), input.nextInt());
                        break;
                }
                break;

            case "message":
                command = input.next().toLowerCase();
                // message delay 100 500 FF FF 50
                switch (command) {
                    case "delay":
                        world.interMsgDelay(input.nextInt(), input.nextInt(), input.next(), input.next(), input.nextInt());
                        break;

                    case "loss":
                        world.interMsgLoss(input.nextInt(), input.nextInt(), input.next(), input.next());
                        break;
                }
                break;

            case "speed":
                command = input.next().toLowerCase();
                // speed ambulance all 300 10
                // speed ambulance one 300 5 10
                switch (command) {
                    case "ambulance":
                        command = input.next().toLowerCase();
                        switch (command) {
                            case "all":
                                world.interSpeedAllAmb(input.nextInt(), input.nextInt());
                                break;

                            case "one":
                                world.interSpeedOneAmb(input.nextInt(), input.nextInt(), input.nextInt());
                                break;
                        }
                        break;

                    case "firefighter":
                        command = input.next().toLowerCase();
                        switch (command) {
                            // speed firefighter range 300 8  8  26  26 3.0
                            case "Range":
                                world.interSpeedRange(input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt(), input.nextFloat());
                                break;
                            // speed firefighter all 300 10
                            case "all":
                                world.interSpeedAllFF(input.nextInt(), input.nextInt());
                                break;
                            // speed firefighter one 300 4 10
                            case "one":
                                world.interSpeedOneFF(input.nextInt(), input.nextInt(), input.nextInt());
                                break;
                        }
                        break;
                }
                break;
            case "sight":
                command = input.next().toLowerCase();
                switch (command) {
                    case "range":
                        world.interSightRange(input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt(), input.nextFloat());
                        break;
                    case "all":
                        world.interSightAllFF(input.nextInt(), input.nextInt());
                        break;
                    case "one":
                        world.interSightOneFF(input.nextInt(), input.nextInt(), input.nextInt());
                        break;
                }
                break;

            case "communication":
                command = input.next().toLowerCase();
                switch (command) {
                    case "range":
                        world.interCommunicationRange(input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt(), input.nextFloat());
                        break;
                    case "all":
                        world.interCommunicationRangeAllFF(input.nextInt(), input.nextInt());
                        break;
                    case "one":
                        world.interCommunicationRangeOneFF(input.nextInt(), input.nextInt(), input.nextInt());
                        break;
                }
                break;

            // injury 100 5
            case "injury":
                world.interInjury(input.nextInt(), input.nextInt());
                break;

            case "remove":
                command = input.next().toLowerCase();
                switch (command) {
                    // remove ambulance 100 5
                    case "ambulance":
                        world.interRemoveAmb(input.nextInt(), input.nextInt());
                        break;
                    case "firefighter":
                        world.interRemoveAmb(input.nextInt(), input.nextInt());
                        break;
                }
                break;
        }
    }

    private void beginnerMode() {
        System.out.println("현재 시뮬레이션 Frame: " + timeImpl.getFrameCount());
        String menu = "===== Menu\n" +
                "  1. Speed\n" +
                "  2. Range\n" +
                "  3. Message\n" +
                "  4. Add\n" +
                "  5. Remove\n" +
                "  6. Injury\n" +
                "  0. Resume\n" +
                "===== Input command: ";
        int command = getCommandOnBeginnerMode(menu);
        switch (command) {
            case 1:
                menu = "==== Speed menu\n" +
                        "  1. Ambulance\n" +
                        "  2. FireFighter\n" +
                        "===== Input command: ";
                command = getCommandOnBeginnerMode(menu);
                switch (command) {
                    case 1:
                        menu = "==== Ambulance menu\n" +
                                "  1. One\n" +
                                "  2. All\n" +
                                "===== Input command: ";
                        command = getCommandOnBeginnerMode(menu);
                        switch (command) {
                            case 1: {
                                int frame = getCommandOnBeginnerMode("frame count: ");
                                int number = getCommandOnBeginnerMode("ambulance number: ");
                                Object value = getCommandOnBeginnerMode("speed: ");
                                world.interSpeedOneAmb(frame, number, value);
                                break;
                            }
                            case 2: {
                                int frame = getCommandOnBeginnerMode("frame count: ");
                                Object value = getCommandOnBeginnerMode("speed: ");
                                world.interSpeedAllAmb(frame, value);
                                break;
                            }
                        }
                        break;



                    case 2: {
                        menu = "==== FireFighter menu\n" +
                                "  1. One\n" +
                                "  2. All\n" +
                                "  3. Range\n" +
                                "===== Input command: ";
                        command = getCommandOnBeginnerMode(menu);
                        switch (command) {
                            case 1: {
                                int frame = getCommandOnBeginnerMode("frame count: ");
                                int number = getCommandOnBeginnerMode("firefighter number: ");
                                Object value = getCommandOnBeginnerMode("speed: ");
                                world.interSpeedOneFF(frame, number, value);
                                break;
                            }
                            case 2: {
                                int frame = getCommandOnBeginnerMode("frame count: ");
                                Object value = getCommandOnBeginnerMode("speed: ");
                                world.interSpeedAllFF(frame, value);
                                break;
                            }

                            case 3: {
                                int frame = getCommandOnBeginnerMode("frame count: ");
                                int left = getCommandOnBeginnerMode("range of left: ");
                                int top = getCommandOnBeginnerMode("range of top: ");
                                int right = getCommandOnBeginnerMode("range of right: ");
                                int bottom = getCommandOnBeginnerMode("range of bottom: ");
                                Object value = getCommandOnBeginnerModeF("speed delay ratio (float: 0.0 ~ 11.0): ");
                                world.interSpeedRange(frame, left, top, right, bottom, value);
                                break;
                            }
                        }
                        break;
                    }
                }
                break;

            case 2:
                menu = "==== Range\n" +
                        "  1. Sight\n" +
                        "  2. Communication\n" +
                        "===== Input command: ";

                command = getCommandOnBeginnerMode(menu);
                switch (command) {
                    // Sight
                    case 1: {
                        menu = "==== Sight\n" +
                                "  1. One\n" +
                                "  2. All\n" +
                                "  3. Range\n" +
                                "===== Input command: ";
                        command = getCommandOnBeginnerMode(menu);
                        switch (command) {
                            case 1: {
                                int frame = getCommandOnBeginnerMode("frame count: ");
                                int number = getCommandOnBeginnerMode("firefighter number: ");
                                Object value = getCommandOnBeginnerMode("sight range: ");
                                world.interSightOneFF(frame, number, value);
                                break;
                            }
                            case 2: {
                                int frame = getCommandOnBeginnerMode("frame count: ");
                                Object value = getCommandOnBeginnerMode("sight range: ");
                                world.interSightAllFF(frame, value);
                                break;
                            }
                            case 3: {
                                int frame = getCommandOnBeginnerMode("frame count: ");
                                int left = getCommandOnBeginnerMode("range of left: ");
                                int top = getCommandOnBeginnerMode("range of top: ");
                                int right = getCommandOnBeginnerMode("range of right: ");
                                int bottom = getCommandOnBeginnerMode("range of bottom: ");
                                Object value = getCommandOnBeginnerModeF("sight range increasing ratio (float): ");
                                world.interSightRange(frame, left, top, right, bottom, value);
                                break;
                            }
                        }
                        break;
                    }
                    case 2: {
                        // Communication
                        menu = "==== Communication\n" +
                                "  1. One\n" +
                                "  2. All\n" +
                                "  3. Range\n" +
                                "===== Input command: ";
                        command = getCommandOnBeginnerMode(menu);
                        switch (command) {
                            case 1: {
                                int frame = getCommandOnBeginnerMode("frame count: ");
                                int number = getCommandOnBeginnerMode("firefighter number: ");
                                Object value = getCommandOnBeginnerMode("communication range: ");
                                world.interCommunicationRangeOneFF(frame, number, value);
                                break;
                            }
                            case 2: {
                                int frame = getCommandOnBeginnerMode("frame count: ");
                                Object value = getCommandOnBeginnerMode("communication range: ");
                                world.interCommunicationRangeAllFF(frame, value);
                                break;
                            }
                            case 3: {
                                int frame = getCommandOnBeginnerMode("frame count: ");
                                int left = getCommandOnBeginnerMode("range of left: ");
                                int top = getCommandOnBeginnerMode("range of top: ");
                                int right = getCommandOnBeginnerMode("range of right: ");
                                int bottom = getCommandOnBeginnerMode("range of bottom: ");
                                Object value = getCommandOnBeginnerModeF("communication range increasing ratio (float): ");
                                world.interCommunicationRange(frame, left, top, right, bottom, value);
                                break;
                            }
                        }
                        break;
                    }
                }
                break;

            case 3:
                menu = "==== Message menu\n" +
                        "  1. Delay\n" +
                        "  2. Loss\n" +
                        "===== Input command: ";
                command = getCommandOnBeginnerMode(menu);
                switch (command) {
                    case 1: {
                        int startFrame = getCommandOnBeginnerMode("start frame count: ");
                        int endFrame = getCommandOnBeginnerMode("end frame count: ");
                        String sender = getCommandOnBeginnerModeS("sender: ");
                        String receiver = getCommandOnBeginnerModeS("receiver: ");
                        int delay = getCommandOnBeginnerMode("duration of delay: ");
                        world.interMsgDelay(startFrame, endFrame, sender, receiver, delay);
                        break;
                    }
                    case 2: {
                        int startFrame = getCommandOnBeginnerMode("start frame count: ");
                        int endFrame = getCommandOnBeginnerMode("end frame count: ");
                        String sender = getCommandOnBeginnerModeS("sender: ");
                        String receiver = getCommandOnBeginnerModeS("receiver: ");
                        world.interMsgLoss(startFrame, endFrame, sender, receiver);
                        break;
                    }
                }
                break;

            case 4:
                menu = "==== Add\n" +
                        "  1. FireFighter\n" +
                        "  2. Ambulance\n" +
                        "===== Input command: ";
                command = getCommandOnBeginnerMode(menu);
                switch (command) {
                    case 1: {
                        // FireFighter
                        int frame = getCommandOnBeginnerMode("frame count: ");
                        int number = getCommandOnBeginnerMode("number of firefighters: ");
                        world.interAddFF(frame, number);
                        break;
                    }

                    case 2: {
                        // Ambulance
                        int frame = getCommandOnBeginnerMode("frame count: ");
                        int number = getCommandOnBeginnerMode("number of ambulances: ");
                        world.interAddAmb(frame, number);
                        break;
                    }
                }
                break;

            case 5:
                menu = "==== Remove menu\n" +
                        "  1. FireFighter\n" +
                        "  2. Ambulance\n" +
                        "===== Input command: ";
                command = getCommandOnBeginnerMode(menu);
                switch (command) {
                    case 1: {
                        int frame = getCommandOnBeginnerMode("frame count: ");
                        int number = getCommandOnBeginnerMode("firefighter number: ");
                        world.interRemoveFF(frame, number);
                        break;
                    }
                    case 2: {
                        int frame = getCommandOnBeginnerMode("frame count: ");
                        int number = getCommandOnBeginnerMode("ambulance number: ");
                        world.interRemoveAmb(frame, number);
                        break;
                    }
                }
                break;

            case 6: {
                int frame = getCommandOnBeginnerMode("frame count: ");
                int number = getCommandOnBeginnerMode("firefighter number: ");
                world.interInjury(frame, number);
                break;
            }
            case 0: {
                frame.setVisible(true);
                canvas.requestFocus();
                pause = false;
                break;
            }
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

    float getCommandOnBeginnerModeF(String menu) {
        System.out.print(menu);
        Scanner input = new Scanner(System.in);
        while(true) {
            if (input.hasNextFloat()) {
                break;
            } else {
                System.out.println("Please enter float");
                System.out.println(input.next());
                System.out.print(menu);
            }
        }
        return input.nextFloat();
    }

    String getCommandOnBeginnerModeS(String menu) {
        System.out.print(menu);
        Scanner input = new Scanner(System.in);
//        String command = input.next();
        while(true) {
            if (input.hasNext()) {
                break;
            } else {
                System.out.println("Please enter CS");
                System.out.println(input.next());
                System.out.print(menu);
            }
        }
        return input.next();
    }
}