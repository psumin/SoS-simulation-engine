
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

    final int MAX_SIMULATION_COUNT = 100;
    final int MAX_FRAME_COUNT = 200;

    final int SIMULATION_WIDTH = 910;
    final int SIMULATION_HEIGHT = 910;
//    final int CONSOLE_WIDTH = 200;

    JFrame frame;
    Canvas canvas;
    BufferStrategy bufferStrategy;
    boolean isExpert = false;
    boolean isFirstWorld = true;

    long programStartTime;
    long programEndTime;


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
            if(isFirstWorld) {
                //            System.out.println("Stop buttom pressed!");
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


    private class MouseControl extends MouseAdapter{
        public void mouseClicked(MouseEvent e) {
            int a = 10;
        }
    }

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

        init();

        programStartTime = System.nanoTime();
        while(running) {

            if (isFirstWorld) {
                beginLoopTime = System.nanoTime();
                render();

                lastUpdateTime = currentUpdateTime;
                currentUpdateTime = System.nanoTime();
                if (!pause) {
                    update((int) ((currentUpdateTime - lastUpdateTime) / (1000 * 1000)));
                } else { // pause
                    frame.setVisible(false);
                    if (isExpert) {
                        expertMode();
                    } else {
                        beginnerMode();
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
            } else {
                update(1);
            }
        }

        programEndTime = System.nanoTime();
        System.out.println("=== Program running time: " + (programEndTime - programStartTime) / (float)1000_000_000 + " sec");
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
                if(isFirstWorld) {
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
                    for(World.InputData inputData: World.inputDatas) {
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
                }

                if(simulation_count < MAX_SIMULATION_COUNT) {
                    simulation_count++;
                    timeImpl.reset();
                    isFirstWorld = false;






                    world = new World(MAX_FRAME_COUNT, false);
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
        String filePath = "log/RQ1/" + date + ".xlsx";

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