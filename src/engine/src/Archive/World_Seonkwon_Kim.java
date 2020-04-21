//package core;
//
//import action.ambulanceaction.AmbulanceFree;
//import action.ambulanceaction.AmbulanceMoveToBridgehead;
//import action.ambulanceaction.AmbulanceSearch;
//import action.ambulanceaction.AmbulanceTransferToHospital;
//import action.firefighteraction.*;
//
//import agents.*;
//import misc.ExcelHelper;
//import misc.Position;
//
//import misc.Range;
//import misc.Time;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import stimulus.*;
//import stimulus.EntityStimulus.RemoveEntity;
//import stimulus.MessageStimulus.Delay;
//import stimulus.MessageStimulus.Loss;
//import stimulus.StateStimulus.Injury;
//import stimulus.ValueStimulus.CommunicationRange;
//import stimulus.ValueStimulus.SightRange;
//import stimulus.ValueStimulus.Speed;
//import stimulus.EntityStimulus.AddEntity;
//
//import java.awt.*;
//import java.awt.Color;
//import java.awt.Font;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//
///**
// * Project: NewSimulator
// * Created by IntelliJ IDEA
// * Author: Sumin Park <smpark@se.kaist.ac.kr>
// * Github: https://github.com/sumin0407/NewSimulator.git
// */
//
//public class World extends SoSObject{
//
//    private final ArrayList<Stimulus> stimuli = new ArrayList<>();
//    private final ArrayList<String> firefighterNames = new ArrayList<>();
//    private final ArrayList<String> AmbulanceNames = new ArrayList<>();
//    int patientCounter = 0;
//    int fireFighterCounter = 0;
//    int ambulanceCounter = 0;
//    int currentFirefighterCounter = 0;
//
//
//    // Initial Values
////    public static final int maxPatient = 294;
//    public static final int maxPatient = 223;
//    //    public static final int maxPatient = 100;
////    public static final int maxPatient = 65;
//    public static final int maxFireFighter = 40;
//    public static final int maxHospital = 4;
//    public static final int maxAmbulance = 16;
//    public static final int maxBridgehead = 4;
//
//    public Map map;
//    public MsgRouter router;
//    public ArrayList<Patient> patients = new ArrayList<>(maxPatient);
//    public ArrayList<FireFighter> fireFighters = new ArrayList<>(maxFireFighter);
//    public ArrayList<Hospital> hospitals = new ArrayList<>(maxHospital);
//    public ArrayList<Bridgehead> bridgeheads = new ArrayList<>(maxBridgehead);
//    public ArrayList<Ambulance> ambulances = new ArrayList<>(maxAmbulance);
//
//    public static final String fireFighterPrefix = "FF";
//    //public static final String ambulancePrefix = "AM";
//
//    XSSFWorkbook workbook = new XSSFWorkbook();
//    XSSFSheet statisticsSheet;
//    XSSFSheet hospitalSheet;
//    XSSFSheet patientSheet;
//    XSSFSheet ambulanceSheet;
//    XSSFSheet fireFighterSheet;
//
//
//    CellStyle headerStyle;
//
//    long startTime;
//    long endTime = 0;
//    long endFrame = 0;
//
//    public int transferCounter = 0;
//    public int rescuedPatientCount = 0;
//
//    public World() {
//        startTime = System.currentTimeMillis();
//
//        statisticsSheet = workbook.createSheet("statistics");
//        //statisticsSheet.trackAllColumnsForAutoSizing();
//
//        hospitalSheet = workbook.createSheet("hospitals");
//        //hospitalSheet.trackAllColumnsForAutoSizing();
//
//        patientSheet = workbook.createSheet("patients");
//        //patientSheet.trackAllColumnsForAutoSizing();
//
//        ambulanceSheet = workbook.createSheet("ambulances");
//        //ambulanceSheet.trackAllColumnsForAutoSizing();
//
//        fireFighterSheet = workbook.createSheet("fire fighters");
//        //fireFighterSheet.trackAllColumnsForAutoSizing();
//
//        headerStyle = workbook.createCellStyle();
//        headerStyle.setAlignment(HorizontalAlignment.CENTER);
//        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//        map = new Map();
//        addChild(map);
//        //map.canUpdate(false);
//
//        // Create map first and then create router
//        router = new MsgRouter(this, workbook);
//        addChild(router);
//
//        createObjects();
////        writeScenario();          // old version
////        writeScenario1();         // baseline
////        writeScenario2();         // 2배의 소방관을 투입
////        writeScenario3();         // 3배의 소방관을 투입
////        writeScenario4();         // 4배의 소방관을 투입
////        writeScenario5();         // 2배 빠른 소방관 투입
////        writeScenario6();         // 3배 빠른 소방관 투입
////        writeScenario7();         // 4배 빠른 소방관 투입
//
////        writeScenario8();         // delay 가 아닌 loss 인 경우
////        writeScenario9();         // delay 가 없었을 경우
////        writeScenario10();        // sight range 가 감소 안한 경우
////        writeScenario11();        // speed 가 감소 안한 경우
////        writeScenario12();        // speed, sight range 가 둘다 감소 안한 경우
////        writeScenario13();         // // smoke, fire 로 인한 communication range 감소
//
////        writeScenario14();         // RQ3, RQ4
//
//
//    }
//
//    // Create Objects for visualization
//    private void createObjects() {
//        createBridgehead();
//        createOrganization();
//        createHospitals();
//        createPatients();
//        createAmbulances();
//        createFireFighters();
//    }
//
//    private void createPatients() {                 // Create the patient at random position
//        for (int i = 0; i < maxPatient; i++) {
//            Patient patient = new Patient(this, "Patient" + ++patientCounter);
//            patients.add(patient);
//            patient.setStatus(Patient.Status.random());
//            Position randomPosition = null;
//
//            while(true) {
//                //randomPosition = GlobalRandom.nextPosition(Map.mapSize.width, Map.mapSize.height);
//                randomPosition = GlobalRandom.nextPosition(Map.mapSize.width / 8 + 2, 7 * Map.mapSize.width / 8 -1,
//                        Map.mapSize.height / 8 + 2, 7 * Map.mapSize.width / 8 - 1);
//                boolean isBridgehead = false;
//                for(Bridgehead zone: bridgeheads) {
//                    if(zone.isBridgehead(randomPosition)) {
//                        isBridgehead = true;
//                        break;
//                    }
//                }
//                if(isBridgehead == false) break;
//            }
//            patient.setPosition(randomPosition);
//            this.addChild(patient);
//        }
//    }
//
//    private void createFireFighters() {                 // Create Firefighters at the edge position
//        Position[] positions = new Position[] {
//                new Position(0, 0),
//                new Position(Map.mapSize.width - 1, 0),
//                new Position(Map.mapSize.width - 1, Map.mapSize.height - 1),
//                new Position(0, Map.mapSize.height - 1)
//        };
//        for (int i = 0; i < maxFireFighter; i++) {
//            FireFighter ff = new FireFighter(this, fireFighterPrefix + ++fireFighterCounter);
//            ff.setPosition(0, 0);
//            fireFighters.add(ff);
//
//            ff.setPosition(positions[positionIndex++]);
//            if(positionIndex >= 4)
//                positionIndex = 0;
////            if(i == 0) {
////                addChild(ff.individualMap);
////            }
//            addChild(ff);
//        }
//        currentFirefighterCounter = fireFighterCounter;
//    }
//
//    private void createHospitals() {                 // Create Hospital at the edge position
//        Row row = hospitalSheet.createRow(0);
//        row.createCell(0).setCellValue("frame count");
//        row.createCell(1).setCellValue("hospital name");
//        row.createCell(2).setCellValue("treatment patient count");
//        row.createCell(3).setCellValue("wait patient count");
////        row.createCell(4).setCellValue("hospitalize");
////        row.createCell(5).setCellValue("leave");
//        for(int i = 0; i < maxHospital; ++i) {
//            Hospital hospital = new Hospital(this, "Hospital" + (i + 1), hospitalSheet);
//            hospitals.add(hospital);
//            addChild(hospital);
//
////            if(i == 0){
////                hospital.setCapacity(2);
////            } else {
//            hospital.setCapacity(10);
////            }
//        }
//        hospitals.get(0).setPosition(0, 0);
//        hospitals.get(1).setPosition(Map.mapSize.width - 1, 0);
//        hospitals.get(2).setPosition(0, Map.mapSize.height - 1);
//        hospitals.get(3).setPosition(Map.mapSize.width - 1, Map.mapSize.height - 1);
////        hospitals.get(4).setPosition(0, (Map.mapSize.height - 1) / 2);
////        hospitals.get(5).setPosition(Map.mapSize.width - 1, (Map.mapSize.height - 1) / 2);
//    }
//
//    private void createBridgehead() {                // Create the Bridgehead at the quarter position of the map
//        for(int i = 0; i < maxBridgehead; ++i) {
//            Bridgehead bridgehead = new Bridgehead(this, "Bridgehead" + (i + 1));
//            bridgeheads.add(bridgehead);
//            addChild(bridgehead);
//        }
//
////        bridgeheads.get(0).setPosition(new Position(Map.mapSize.width / 4, Map.mapSize.height / 4));
////        bridgeheads.get(1).setPosition(new Position(3 * Map.mapSize.width / 4, Map.mapSize.height / 4));
////        bridgeheads.get(2).setPosition(new Position(3 * Map.mapSize.width / 4, 3 * Map.mapSize.height / 4));
////        bridgeheads.get(3).setPosition(new Position(Map.mapSize.width / 4, 3 * Map.mapSize.height / 4));
//        bridgeheads.get(0).setPosition(new Position(Map.mapSize.width / 8, Map.mapSize.height / 8));
//        bridgeheads.get(1).setPosition(new Position(7 * Map.mapSize.width / 8, Map.mapSize.height / 8));
//        bridgeheads.get(2).setPosition(new Position(7 * Map.mapSize.width / 8, 7 * Map.mapSize.height / 8));
//        bridgeheads.get(3).setPosition(new Position(Map.mapSize.width / 8, 7 * Map.mapSize.height / 8));
//
//    }
//
//    int ambulancePositionIndex = 0;
//    private void createAmbulances() {                    // Create Ambulance at the edge position
//        Position[] positions = new Position[] {
//                new Position(0, 0),
//                new Position(Map.mapSize.width - 1, 0),
//                new Position(Map.mapSize.width - 1, Map.mapSize.height - 1),
//                new Position(0, Map.mapSize.height - 1)
//        };
//        for(int i = 0; i < maxAmbulance; ++i) {
//            Ambulance ambulance = new Ambulance(this, "Ambulance" + ++ambulanceCounter);
//            ambulances.add(ambulance);
//            addChild(ambulance);
//            ambulance.setPosition(positions[ambulancePositionIndex++]);
//            if(ambulancePositionIndex >= 4) {
//                ambulancePositionIndex = 0;
//            }
//        }
//    }
//
//    private void createOrganization() {                      // Create Organization
//        Organization organization = new Organization(this, "Organization");
//        addChild(organization);
//    }
//
//    int frameCount = 0;
//    public int savedPatientCount = 0;
//    @Override
//    public void onUpdate() {
//
//        //if(getPatientCount() == 0 && map.getUnvisitedTileCount() == 0) {
//        if(patients.size() == savedPatientCount && map.getUnvisitedTileCount() == 0) {
//            canUpdate(false);
//
//            endTime = System.currentTimeMillis();
//            endFrame = frameCount;
////            printPatientLog(true);
////            printFireFighterLog(true);
//            return;
//        } else {
//            printPatientLog(false);
//            printFireFighterLog(false);
//            printAmbulanceLog(false);
//            frameCount++;
//            System.out.println("FrameCount: " + frameCount);
//        }
//
//        ArrayList<Stimulus> mustRemove = new ArrayList<>();
//        for(Stimulus stimulus : stimuli) {
//            if(stimulus.frame == Time.getFrameCount()) {
//                stimulus.execute();
//                mustRemove.add(stimulus);
//            }
//        }
//        stimuli.removeAll(mustRemove);
//
//    }
//
//
//    private void printPatientLog(boolean isFinish) {
//
//        if(frameCount == 0) {
//            Row row = patientSheet.createRow(patientSheet.getPhysicalNumberOfRows());
//            Cell frameCountCell = row.createCell(0);
//            Cell savedPatientCell = row.createCell(1);
//            Cell rescuedPatientCell = row.createCell(2);
//
//            frameCountCell.setCellValue("frame count");
//            savedPatientCell.setCellValue("number of treated patients");
//            rescuedPatientCell.setCellValue("number of rescued patients");
//        }
//
//        Row row = patientSheet.createRow(patientSheet.getPhysicalNumberOfRows());
//        Cell frameCountCell = row.createCell(0);
//        Cell savedPatientCell = row.createCell(1);
//        Cell rescuedPatientCell = row.createCell(2);
//
//        frameCountCell.setCellValue(frameCount);
//        savedPatientCell.setCellValue(savedPatientCount);
//        rescuedPatientCell.setCellValue(rescuedPatientCount);
//    }
//
//
//    private void printFireFighterLog(boolean isFinish) {
//
//        ExcelHelper.getCell(fireFighterSheet, 0, 0).setCellValue("frame Count");
//        ExcelHelper.getCell(fireFighterSheet, 0, 1).setCellValue("number of Firefighter");
//        ExcelHelper.getCell(fireFighterSheet, 0, fireFighters.size() + 2).setCellValue("Total distance");
//        for(int i = 0; i < fireFighters.size(); ++i) {
//            ExcelHelper.getCell(fireFighterSheet, 0, i + 2).setCellValue("FF" + (i + 1));
////            ExcelHelper.getCell(fireFighterSheet, 0, i * 2 + 3).setCellValue("FF" + (i + 1) + " Status");
//        }
//
//        Row row = fireFighterSheet.createRow(fireFighterSheet.getPhysicalNumberOfRows());
//        Cell frameCountCell = row.createCell(0);
//        Cell firefighterCountCell = row.createCell(1);
//        frameCountCell.setCellValue(frameCount);
//        firefighterCountCell.setCellValue(fireFighterCounter);
//        Cell[] positionCells = new Cell[fireFighters.size()];
//
//        if(isFinish) {
//            int totalFirefighterDistance = 0;
//            row = fireFighterSheet.createRow(fireFighterSheet.getPhysicalNumberOfRows());
//            frameCountCell = row.createCell(0);
//            frameCountCell.setCellValue("distance");
//            positionCells = new Cell[fireFighters.size() + 1];
//
//            for(int i = 0; i <= fireFighters.size(); ++i) {
//                Cell currentCell = row.createCell(i + 2);
//                positionCells[i] = currentCell;
//                if (i < fireFighters.size()) {
//                    positionCells[i].setCellValue(fireFighters.get(i).totalDistance);
//                    totalFirefighterDistance += fireFighters.get(i).totalDistance;
//                } else {
//                    positionCells[i].setCellValue(totalFirefighterDistance);
//                }
//            }
//        }
//    }
//
//
//    private void printAmbulanceLog(boolean isFinish) {
//
//        ExcelHelper.getCell(ambulanceSheet, 0, 0).setCellValue("frame count");
//        ExcelHelper.getCell(ambulanceSheet, 0, 1).setCellValue("number of Ambulances");
//        ExcelHelper.getCell(ambulanceSheet, 0, 2).setCellValue("total patient transfer");
//        for(int i = 0; i < ambulances.size(); ++i) {
//            ExcelHelper.getCell(ambulanceSheet, 0, i + 3).setCellValue("Amb" + (i + 1));
////            ExcelHelper.getCell(ambulanceSheet, 0, i * 2 + 3).setCellValue("Amb" + (i + 1) + " Status");
//        }
//        ExcelHelper.getCell(ambulanceSheet, 0, ambulances.size() + 2).setCellValue("Total distance");
//
//        Row row = ambulanceSheet.createRow(ambulanceSheet.getPhysicalNumberOfRows());
//        Cell frameCountCell = row.createCell(0);
//        Cell ambulanceCountCell = row.createCell(1);
//        Cell transferCountCell = row.createCell(2);
//        frameCountCell.setCellValue(frameCount);
//        ambulanceCountCell.setCellValue(ambulanceCounter);
//        transferCountCell.setCellValue(transferCounter);
//        Cell[] positionCells;
//
//        if(isFinish) {
//            int totalAmbulanceDistance = 0;
//            row = ambulanceSheet.createRow(ambulanceSheet.getPhysicalNumberOfRows());
//            frameCountCell = row.createCell(0);
//            frameCountCell.setCellValue("distance");
//            positionCells = new Cell[ambulances.size() + 1];
//
//            for(int i = 0; i <= ambulances.size(); ++i) {
//                Cell currentCell = row.createCell(i + 3);
//                positionCells[i] = currentCell;
//                if(i < ambulances.size()) {
//                    positionCells[i].setCellValue(ambulances.get(i).totalDistance);
//                    totalAmbulanceDistance += ambulances.get(i).totalDistance;
//                } else {
//                    positionCells[i].setCellValue(totalAmbulanceDistance);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onRender(Graphics2D g) {
//        Rectangle rect = g.getDeviceConfiguration().getBounds();
//        g.setColor(new Color(100, 100, 100));
//        g.fillRect(rect.x, rect.y, rect.width, rect.height);
//
//        g.setColor(Color.red);
//        g.setFont(new Font("default", Font.BOLD, 16));
//        String strFrameCount = "frameCount: " + frameCount;
//        g.drawChars(strFrameCount.toCharArray(), 0, strFrameCount.length(), 600, 700);
//    }
//
//    public Map getMap() {
//        return map;
//    }
//
//
//    public int getPatientCount() {
//        int count = 0;
//        for(SoSObject child: children) {
//            if(child instanceof Patient) {
//                count++;
//            }
//        }
//        return count;
//    }
//
//    @Override
//    public void clear() {
//
//        Sheet sheet = statisticsSheet;
//        Row row = sheet.createRow(0);
//
//        // Total runtime
//        ExcelHelper.getCell(row, 0).setCellValue("Runtime");
//        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
//        if(endTime == 0) {
//            endTime = System.currentTimeMillis();
//        }
//        ExcelHelper.getCell(row, 1).setCellValue(((endTime - startTime) / 1000) + " s");
//        row = ExcelHelper.nextRow(row);
//
//        // end Frame
//        ExcelHelper.getCell(row, 0).setCellValue("Run frame");
//        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
//        if(endFrame == 0) {
//            endFrame = frameCount;
//        }
//        ExcelHelper.getCell(row, 1).setCellValue(endFrame);
//        row = ExcelHelper.nextRow(row);
//
//        // initial patient count
//        ExcelHelper.getCell(row, 0).setCellValue("initial patient count");
//        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
//        ExcelHelper.getCell(row, 1).setCellValue(maxPatient);
//        row = ExcelHelper.nextRow(row);
//
//        // initial fire fighter count
//        ExcelHelper.getCell(row, 0).setCellValue("initial fire fighter count");
//        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
//        ExcelHelper.getCell(row, 1).setCellValue(maxFireFighter);
//        row = ExcelHelper.nextRow(row);
//
//        // initial ambulance count
//        ExcelHelper.getCell(row, 0).setCellValue("initial ambulance count");
//        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
//        ExcelHelper.getCell(row, 1).setCellValue(maxAmbulance);
//        row = ExcelHelper.nextRow(row);
//
//        // initial hospital count
//        ExcelHelper.getCell(row, 0).setCellValue("initial hospital count");
//        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
//        ExcelHelper.getCell(row, 1).setCellValue(maxHospital);
//        row = ExcelHelper.nextRow(row);
//
//        // initial Bridgehead count
//        ExcelHelper.getCell(row, 0).setCellValue("initial bridgehead count");
//        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
//        ExcelHelper.getCell(row, 1).setCellValue(maxBridgehead);
//        row = ExcelHelper.nextRow(row);
//
//
//
//
//        printPatientLog(true);
//        printFireFighterLog(true);
//        printAmbulanceLog(true);
//
//        router.clear();
//
//        long nano = System.currentTimeMillis();
//        String date = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss").format(nano);
//        String filePath = "log/" + date + ".xlsx";
//
//        ExcelHelper.autoSizeAllColumn(workbook);
//        ExcelHelper.save(workbook, filePath);
//    }
//
//
//    public boolean contains(SoSObject object) {
//        return children.contains(object);
//    }
//
//    int positionIndex = 0;
//
//
//    public void addPatient(Position position) {
//        Patient patient = new Patient(this, "Patient" + ++patientCounter);
//        patients.add(patient);
//        patient.setStatus(Patient.Status.random());
//        if(position == null) {
//            Position randomPosition = null;
//
//            while (true) {
//                //randomPosition = GlobalRandom.nextPosition(Map.mapSize.width, Map.mapSize.height);
//                randomPosition = GlobalRandom.nextPosition(Map.mapSize.width / 8, 7 * Map.mapSize.width / 8,
//                        Map.mapSize.height / 8, 7 * Map.mapSize.width / 8);
//                boolean isBridgehead = false;
//                for (Bridgehead zone : bridgeheads) {
//                    if (zone.isBridgehead(randomPosition)) {
//                        isBridgehead = true;
//                        break;
//                    }
//                }
//                if (isBridgehead == false) break;
//            }
//
//            patient.setPosition(randomPosition);
//        } else {
//            patient.setPosition(position);
//        }
//        this.addChild(patient);
//
//        ArrayList<Patient> data = new ArrayList<>();
//        data.add(patient);
//
//
//        for(FireFighter fireFighter: fireFighters) {
//            Msg msg = new Msg()
//                    .setFrom(fireFighterPrefix)
//                    .setTo(fireFighter.name)
//                    .setTitle("patientsMemory")
//                    .setData(data);
//            router.route(msg);
//        }
//    }
//
//
//    private void writeScenario() {
//
//        //ArrayList<String> firefighterNames = new ArrayList<>();
//        for(int i = 0; i < maxFireFighter; ++i) {
//            firefighterNames.add(fireFighterPrefix + (i + 1));
//        }
//
////        ArrayList<String> AmbulanceNames = new ArrayList<>();
//        for(int i = 0; i < maxAmbulance; ++i) {
//            AmbulanceNames.add("Ambulance" + (i + 1));
//        }
//
////
//        // Stimulus types
////        // TODO: Late Rescue Start
////        stimuli.add(new Speed(this, 1, "FF1", 100));
////        stimuli.add(new Speed(this, 1, "FF5", 100));
////        stimuli.add(new Speed(this, 1, firefighterNames, 100));                 // 1 frame 부터 100 frame까지 동작 안함 ==> 이동속도가 100 frame당 한 칸이므로
////        stimuli.add(new Speed(this, 100, firefighterNames, 3));                 // 100 프레임 부터 회복 --> 그런데 만약 위에서 1 frame부터 200 frame까지 이속 감소를 시키면... 회복은 200부터 적용되는듯?
////
////        stimuli.add(new Speed(this, 100, AmbulanceNames, 300));                 // 100 frame 부터 400 frame까지 동작 안함
////        stimuli.add(new Speed(this, 400, AmbulanceNames, 0));
////
////        // TODO: moveDelay
////        stimuli.add(new Speed(this, 100, "FF1", 3));                           // 100 frame 부터 FF1의 이동 속도 30 frame 당 1칸 이동 ==> 이속 감소
////        stimuli.add(new Speed(this, 100, firefighterNames, 3));                // 100 frame 부터 전체 FF의 이동소도 감소
////        stimuli.add(new Speed(this, 10, new Range(29, 29, 35, 35), 10.0f));
//
//
//        stimuli.add(new Speed(this, 600, new Range(8, 8, 26, 26), 3.0f));      // smoke 시작 4층 위로
//        stimuli.add(new Speed(this, 900, new Range(8, 8, 26, 26), 6.0f));
//        stimuli.add(new Speed(this, 2130, new Range(8, 8, 26, 26), 9.0f));      // 4층 위로 fire
//
//        stimuli.add(new Speed(this, 2415, new Range(8, 8, 26, 26), 6.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//        stimuli.add(new Speed(this, 2415, new Range(10, 10, 24, 24), 9.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//
//        stimuli.add(new Speed(this, 2655, new Range(10, 10, 24, 24), 6.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//        stimuli.add(new Speed(this, 2655, new Range(12, 12, 22, 22), 9.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//
////        stimuli.add(new Speed(this, 3030, new Range(13, 13, 23, 23), 2.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
////        stimuli.add(new Speed(this, 6060, new Range(22, 22, 42, 42), 2.0f));
//
//
//
////        stimuli.add(new Speed(this, 10, new Range(16, 16, 48, 48), 2.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
////        stimuli.add(new Speed(this, 10, new Range(24, 24, 40, 40), 3.0f));
////        stimuli.add(new Speed(this, 10, new Range(29, 29, 35, 35), 5.0f));
////
////
////        stimuli.add(new Speed(this, 100, AmbulanceNames, 10));                              // 특정 frame count 이후 Ambulance 전체 move speed 변경
////        stimuli.add(new Speed(this, 100, "Ambulance1", 7));
////
////        // TODO: sightRange
////        stimuli.add(new SightRange(this, 100, "FF1", 5));                               // 특정 frame count 이후 FF1의 sight range 변화
////        stimuli.add(new SightRange(this, 600, firefighterNames, 1));                    // 특정 frame count 이후 전체 FF의 sight range 변화
////        stimuli.add(new SightRange(this, 1000, firefighterNames, 1));
//        //stimuli.add(new SightRange(this, 600, new Range(8, 8, 26, 26), 0.3f));      // smoke 시작 4층 위로
//
//        stimuli.add(new SightRange(this, 2130, new Range(8, 8, 26, 26), 0.3f));      // 4층 위로 fire
//
//        stimuli.add(new SightRange(this, 2415, new Range(8, 8, 26, 26), 0.3f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//        stimuli.add(new SightRange(this, 2415, new Range(10, 10, 24, 24), 0.1f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//
//        stimuli.add(new SightRange(this, 2655, new Range(10, 10, 24, 24), 0.3f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//        stimuli.add(new SightRange(this, 2655, new Range(12, 12, 22, 22), 0.1f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//
//
//
////        stimuli.add(new SightRange(this, 800, firefighterNames, 5));                    // 특정 frame count 이후 전체 FF의 sight range 변화
////        stimuli.add(new SightRange(this, 1600, firefighterNames, 3));                    // 특정 frame count 이후 전체 FF의 sight range 변화
////        stimuli.add(new SightRange(this, 2400, firefighterNames, 1));                    // 특정 frame count 이후 전체 FF의 sight range 변화
////
////
////        stimuli.add(new SightRange(this, 1200, new Range(13, 13, 51, 51), 0.5f));
////        stimuli.add(new SightRange(this, 4830, new Range(14, 14, 50, 50), 0.2f));
////
////
//////        stimuli.add(new SightRange(this, 10, new Range(0, 0, 64, 64), 3.0f));           // 특정 frame count 이후 특정 구역의 sight range 변화
//////
//////        // TODO: communicationRange (FF 관련)
////        stimuli.add(new CommunicationRange(this, 100, "FF1", 7));                               // 특정 frame count 이후 FF1의 communication range 변화
////        stimuli.add(new CommunicationRange(this, 800, firefighterNames, 7));                    // 특정 frame count 이후 전체 FF의 communication range 변화
//////        stimuli.add(new CommunicationRange(this, 1600, firefighterNames, 5));                    // 특정 frame count 이후 전체 FF의 communication range 변화
////        stimuli.add(new CommunicationRange(this, 1200, firefighterNames, 7));
////        stimuli.add(new CommunicationRange(this, 2000, firefighterNames, 5));                    // 특정 frame count 이후 전체 FF의 communication range 변화
////        stimuli.add(new CommunicationRange(this, 10, new Range(0, 0, 10, 10), 5.0f));           // 특정 frame count 이후 특정 구역의 communication range 변화
////
//        stimuli.add(new CommunicationRange(this, 600, firefighterNames, 3));                    // 특정 frame count 이후 전체 FF의 communication range 변화
//        stimuli.add(new CommunicationRange(this, 1000, firefighterNames, 3));
//        stimuli.add(new CommunicationRange(this, 2130, firefighterNames, 1));
////
////        // TODO: FireFighter => Patient
////        stimuli.add(new Injury(this, 100, "FF1"));
////        stimuli.add(new Injury(this, 100, "FF2"));
////        stimuli.add(new Injury(this, 100, "FF3"));
////        stimuli.add(new Injury(this, 100, "FF4"));
////        stimuli.add(new Injury(this, 100, "FF5"));
////        stimuli.add(new Injury(this, 100, "FF6"));
////
////        // TODO: remove FireFighter1
////        stimuli.add(new RemoveEntity(this, 100, "FF1", this::removeCS));
////        stimuli.add(new RemoveEntity(this, 110, "FF2", this::removeCS));
////        stimuli.add(new RemoveEntity(this, 120, "FF3", this::removeCS));
////        stimuli.add(new RemoveEntity(this, 130, "FF4", this::removeCS));
////        stimuli.add(new RemoveEntity(this, 140, "FF5", this::removeCS));
////        stimuli.add(new RemoveEntity(this, 150, "FF6", this::removeCS));
////        stimuli.add(new RemoveEntity(this, 160, "FF7", this::removeCS));
////        stimuli.add(new RemoveEntity(this, 170, "FF8", this::removeCS));
////
////        // TODO: remove Ambulance1
////        stimuli.add(new RemoveEntity(this, 100, "Ambulance1", this::removeCS));
////        stimuli.add(new RemoveEntity(this, 120, "Ambulance2", this::removeCS));
////        stimuli.add(new RemoveEntity(this, 130, "Ambulance3", this::removeCS));
////        stimuli.add(new RemoveEntity(this, 140, "Ambulance4", this::removeCS));
////
////        // TODO: add FireFighter
//        for(int i = 0; i < 1; i++) {
//            stimuli.add(new AddEntity(this, 275, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 275, this::addFireFighter));
//        }
//        for(int i = 0; i < 1; i++) {
//            stimuli.add(new AddEntity(this, 315, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 315, this::addFireFighter));
//        }
//        for(int i = 0; i < 1; i++) {
//            stimuli.add(new AddEntity(this, 390, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 390, this::addFireFighter));
//        }
//        for(int i = 0; i < 1; i++) {
//            stimuli.add(new AddEntity(this, 435, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 435, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 435, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 435, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 435, this::addFireFighter));
//        }
//
//        for(int i = 0; i < 1; i++) {
//            stimuli.add(new AddEntity(this, 465, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 465, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 465, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 465, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 465, this::addFireFighter));
//        }
//
//        for(int i = 0; i < 1; i++) {
//            stimuli.add(new AddEntity(this, 495, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 495, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 495, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 495, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 495, this::addFireFighter));
//        }
//
//        for(int i = 0; i < 1; i++) {
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//        }
//
//        // TODO: add Ambulance
//        stimuli.add(new AddEntity(this, 200, this::addAmbulance));
//        stimuli.add(new AddEntity(this, 210, this::addAmbulance));
//        stimuli.add(new AddEntity(this, 220, this::addAmbulance));
//        stimuli.add(new AddEntity(this, 230, this::addAmbulance));
//
////        // TODO: Msg Delay
////        // CS && CS
//        router.add(new Delay(1, 3000, "FF", "FF", 3000));   // 1frame 부터 20프레임까지 적용
////        router.add(new Delay(10, 100, "FF", "FF", 20));
////        router.add(new Delay(10, 200, "All", "ALL", 20));
////
////        router.add(new Delay(1, 20,"Ambulance", "Org", 20));
////
////        // Entity && CS
////        router.add(new Delay(1, 20, "FF1", "FF", 20));
////        router.add(new Delay(1, 20, "Ambulance1", "Org", 20));
////
////        // CS && Entity
////        router.add(new Delay(1, 20, "FF", "FF5", 20));
////        router.add(new Delay(1, 20, "Org", "Ambulance1", 20));
//
////        // Entity && Entity
////        router.add(new Delay(1, 20, "FF1", "FF5", 20));
//
//        // TODO: Msg Loss
//        // CS && CS
////        router.add(new Loss(1, 3000, "FF", "FF"));
////        router.add(new Loss(1, 20, "FF", "FF"));
////        router.add(new Loss(10, 200, "All", "All"));
////
////        router.add(new Loss(1, 20, "Ambulance", "Org"));
////
////        // Entity && CS
////        router.add(new Loss(1, 20, "FF1", "FF"));
////        router.add(new Loss(1, 20, "Ambulance1", "Org"));
////
////        // CS && Entity
////        router.add(new Loss(1, 20, "FF", "FF5"));
////        router.add(new Loss(1, 20, "Org", "Ambulance1"));
////
////        // Entity && Entity
////        router.add(new Loss(1, 20, "FF1", "FF5"));
//
//    }       // initial version
//
//
//
//    // n배의 수, n배 빠르게 적용했을때도 delay 나 loss 나 그런게 없을 경우에 대해서도 해야하나?
//
//    // Communication range를 줄이면??
//
//    // remove를 넣으면      ==> random하게? 어떤 방식으로 어떤 entity를 선택? 몇명이나?
//
//    //injury를 넣으면?      ==> random하게? 어떤 방식으로 어떤 entity를 선택? 몇명이나?
//
//    void removeCS(String csName) {
//        SoSObject obj = findObject(csName);
//        if(obj == null) return;
//
//
//        CS cs = (CS)obj;
//        cs.visible(false);
//        cs.canUpdate(false);
//        cs.currentAction.name = "Removed";
//
//        if(cs instanceof FireFighter) {
//            FireFighter ff = (FireFighter) cs;
//            if (cs.currentAction instanceof FireFighterFirstAid) {
//                FireFighterFirstAid action = (FireFighterFirstAid) cs.currentAction;
//                action.targetPatient.assignedFireFighter = null;
//                action.targetPatient.isSaved = false;
//                map.add(action.targetPatient);
//                action.targetPatient.position.set(cs.position);
//                ff.patientsMemory.remove(action.targetPatient);
//                ff.patientsMemory.add(action.targetPatient);
////                fireFighters.remove(ff);
//                firefighterNames.remove(ff);
//                fireFighterCounter--;
//            } else if (cs.currentAction instanceof FireFighterSelectTransferDestination) {
//                FireFighterSelectTransferDestination action = (FireFighterSelectTransferDestination) cs.currentAction;
//                action.targetPatient.isSaved = false;
//                action.targetPatient.assignedFireFighter = null;
//                addChild(action.targetPatient);
//                map.add(action.targetPatient);
//                action.targetPatient.position.set(cs.position);
//                ff.patientsMemory.remove(action.targetPatient);
//                ff.patientsMemory.add(action.targetPatient);
////                fireFighters.remove(ff);
//                firefighterNames.remove(ff);
//                fireFighterCounter--;
//            } else if (cs.currentAction instanceof FireFighterTransferToBridgehead) {
//                FireFighterTransferToBridgehead action = (FireFighterTransferToBridgehead) cs.currentAction;
//                action.targetPatient.isSaved = false;
//                action.targetPatient.assignedFireFighter = null;
//                addChild(action.targetPatient);
//                map.add(action.targetPatient);
//                action.targetPatient.position.set(cs.position);
//                ff.patientsMemory.remove(action.targetPatient);
//                ff.patientsMemory.add(action.targetPatient);
////                fireFighters.remove(ff);
//                firefighterNames.remove(ff);
//                fireFighterCounter--;
//            } else if (cs.currentAction instanceof FireFighterTransferToHospital) {
//                FireFighterTransferToHospital action = (FireFighterTransferToHospital) cs.currentAction;
//                action.targetPatient.assignedFireFighter = null;
//                action.targetPatient.isSaved = false;
//                addChild(action.targetPatient);
//                map.add(action.targetPatient);
//                action.targetPatient.position.set(cs.position);
//                ff.patientsMemory.remove(action.targetPatient);
//                ff.patientsMemory.add(action.targetPatient);
////                fireFighters.remove(ff);
//                firefighterNames.remove(ff);
//                fireFighterCounter--;
//            } else if (cs.currentAction instanceof FireFighterMoveToPatient) {
//                FireFighterMoveToPatient action = (FireFighterMoveToPatient) cs.currentAction;
//                action.targetPatient.assignedFireFighter = null;
//                map.add(action.targetPatient);
//                ff.patientsMemory.remove(action.targetPatient);
//                ff.patientsMemory.add(action.targetPatient);
////                fireFighters.remove(ff);
//                firefighterNames.remove(ff);
//                fireFighterCounter--;
//            } else if (cs.currentAction instanceof FireFighterSearch) {
//                FireFighterSearch action = (FireFighterSearch) cs.currentAction;
////                action.targetPatient.assignedFireFighter = null;
////                map.add(action.targetPatient);
////                ff.patientsMemory.remove(action.targetPatient);
////                ff.patientsMemory.add(action.targetPatient);
////                fireFighters.remove(ff);
//                firefighterNames.remove(ff);
//                fireFighterCounter--;
//            }
//        }
//        else if(cs instanceof Ambulance) {
//            Ambulance ambulance = (Ambulance) cs;
//
//            if(cs.currentAction instanceof AmbulanceFree) {
//                AmbulanceFree action = (AmbulanceFree) cs.currentAction;
//                AmbulanceNames.remove(ambulance);
//                ambulanceCounter--;
//            } else if(cs.currentAction instanceof AmbulanceMoveToBridgehead) {
//                AmbulanceMoveToBridgehead action = (AmbulanceMoveToBridgehead) cs.currentAction;
//                AmbulanceNames.remove(ambulance);
//                ambulanceCounter--;
//            } else if(cs.currentAction instanceof AmbulanceSearch) {
//                AmbulanceSearch action = (AmbulanceSearch) cs.currentAction;
//                AmbulanceNames.remove(ambulance);
//                ambulanceCounter--;
//            } else if(cs.currentAction instanceof AmbulanceTransferToHospital) {
//                AmbulanceTransferToHospital action = (AmbulanceTransferToHospital) cs.currentAction;
//
//                action.patient.assignedFireFighter = null;
//                action.patient.isSaved = false;
////                ambulance.moveTo(action.hospital.position);
////                ambulance.setPosition(action.hospital.position);
////                action.hospital.hospitalize(action.patient);
////                transferCounter++;
//                addChild(action.patient);
//                action.patient.position.set(cs.position);
//                map.add(action.patient);
////                action.patient.position.set(cs.position);
//
//                AmbulanceNames.remove(ambulance);
//                ambulanceCounter--;
//
//            }
//        }
//
//        removeChild(obj);
//    }
//
//    void addFireFighter() {
//        Position[] positions = new Position[]{
//                new Position(0, 0),
//                new Position(Map.mapSize.width - 1, 0),
//                new Position(Map.mapSize.width - 1, Map.mapSize.height - 1),
//                new Position(0, Map.mapSize.height - 1)
//        };
//        int current = 0;
//        FireFighter ff = new FireFighter(this, fireFighterPrefix + ++currentFirefighterCounter);
//        fireFighterCounter++;
//        current = currentFirefighterCounter;
//        fireFighters.add(ff);
//        firefighterNames.add(fireFighterPrefix + current);
//
//        ff.setPosition(positions[positionIndex++]);
//        if (positionIndex >= 4)
//            positionIndex = 0;
//        addChild(ff);
//    }
//
//    private void addAmbulance() {
//        Position[] positions = new Position[]{
//                new Position(0, 0),
//                new Position(Map.mapSize.width - 1, 0),
//                new Position(Map.mapSize.width - 1, Map.mapSize.height - 1),
//                new Position(0, Map.mapSize.height - 1)
//        };
//        int current = 0;
//        Ambulance ambulance = new Ambulance(this, "Ambulance" + ++ambulanceCounter);
//        current = ambulanceCounter;
//        ambulances.add(ambulance);
//        AmbulanceNames.add("Ambulance" + current);
//        ambulance.setPosition(positions[positionIndex++]);
//        if (positionIndex >= 4)
//            positionIndex = 0;
//        addChild(ambulance);
//    }
//
//    public void interAddFF(int frame, int count) {
//        for(int i = 0; i < count; ++i) {
//            stimuli.add(new AddEntity(this, frame, this::addFireFighter));
//        }
//    }
//
//    public void interAddAmb(int frame, int count) {
//        for(int i = 0; i < count; ++i) {
//            stimuli.add(new AddEntity(this, frame, this::addAmbulance));
//        }
//    }
//
//    // Msg Delay
//    // CS & CS
//    public void interAddDelay(int frame, int endFrame, int delay) {
//
//        router.add(new Delay(frame, endFrame, "FF", "FF", delay));
//
//    }
//
//    //Speed
//    public void interSpeedRange(int frame, int left, int top, int right, int bottom, Object value) {
//
//        stimuli.add(new Speed(this, frame, new Range(left, top, right, bottom), value));
//
//    }
//
//    public void interSpeedAllAmb(int frame, Object value) {
//
//        stimuli.add(new Speed(this, frame, AmbulanceNames, value));
//
//    }
//
//    public void interSpeedAllFF(int frame, Object value) {
//
//        stimuli.add(new Speed(this, frame, firefighterNames, value));
//
//    }
//
//    public void interSpeedOneAmb(int frame, int number, Object value) {
//
//        stimuli.add(new Speed(this, frame, "Ambulance" + number, value));
//
//    }
//
//    public void interSpeedOneFF(int frame, int number, Object value) {
//
//        stimuli.add(new Speed(this, frame, "FF" + number, value));
//
//    }
//
//    //Sight
//    public void interSightRange(int frame, int left, int top, int right, int bottom, Object value) {
//
//        stimuli.add(new SightRange(this, frame, new Range(left, top, right, bottom), value));
//
//    }
//
//    public void interSightAllFF(int frame, Object value) {
//
//        stimuli.add(new SightRange(this, frame, firefighterNames, value));
//
//    }
//
//    public void interSightOneFF(int frame, int number, Object value) {
//
//        stimuli.add(new SightRange(this, frame, "FF" + number, value));
//
//    }
//
//    //Remove
//    public void interRemoveFF(int frame, int number) {
//
//        stimuli.add(new RemoveEntity(this, frame, "FF" + number, this::removeCS));
//
//    }
//
//    public void interRemoveAmb(int frame, int number) {
//
//        stimuli.add(new RemoveEntity(this, frame, "Ambulance" + number, this::removeCS));
//
//    }
//
//    //Communication Range
//    public void interCommunicationRange(int frame, int left, int top, int right, int bottom, Object value) {
//
//        stimuli.add(new CommunicationRange(this, frame, new Range(left, top, right, bottom), value));
//
//    }
//
//    public void interCommunicationRangeAllFF(int frame, Object value) {
//
//        stimuli.add(new CommunicationRange(this, frame, firefighterNames, value));
//
//    }
//
//    public void interCommunicationRangeOneFF(int frame, int number, Object value) {
//
//        stimuli.add(new CommunicationRange(this, frame, "FF" + number, value));
//
//    }
//
//    //Injury
//    public void interInjury(int frame, int number) {
//
//        stimuli.add(new Injury(this, frame, "FF" + number));
//
//    }
//
//    //Injury
//    public void interAddLoss(int frame, int endframe) {
//
//        router.add(new Loss(frame,endframe, "FF", "FF"));
//
//    }
//
//}