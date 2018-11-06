package core;

import agents.*;
import misc.ExcelHelper;
import misc.Position;

import misc.Range;
import misc.Time;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import stimulus.*;
import stimulus.ChangeStateStimulus.Injured;
import stimulus.ChangeValueStimulus.CommunicationRange;
import stimulus.ChangeValueStimulus.SightRange;
import stimulus.ChangeValueStimulus.Speed;
import stimulus.MessageStimulus.Delay;
import stimulus.NumberOfEntityStimulus.AddEntity;
import stimulus.NumberOfEntityStimulus.RemoveEntity;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class World extends SoSObject{

    private final ArrayList<Stimulus> stimuli = new ArrayList<>();

    int patientCounter = 0;
    int fireFighterCounter = 0;
    int ambulanceCounter = 0;

    // Initial Values
    public static final int maxPatient = 294;
//    public static final int maxPatient = 65;
    public static final int maxFireFighter = 16;
    public static final int maxHospital = 4;
    public static final int maxAmbulance = 4;
    public static final int maxSafeZone = 4;

    public Map map;
    public MsgRouter router;
    public ArrayList<Patient> patients = new ArrayList<>(maxPatient);
    public ArrayList<FireFighter> fireFighters = new ArrayList<>(maxFireFighter);
    public ArrayList<Hospital> hospitals = new ArrayList<>(maxHospital);
    public ArrayList<SafeZone> safeZones = new ArrayList<>(maxSafeZone);
    public ArrayList<Ambulance> ambulances = new ArrayList<>(maxAmbulance);

    public static final String fireFighterPrefix = "FF";
    //public static final String ambulancePrefix = "AM";

    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet statisticsSheet;
    XSSFSheet hospitalSheet;
    XSSFSheet patientSheet;
    XSSFSheet ambulanceSheet;
    XSSFSheet fireFighterSheet;


    CellStyle headerStyle;

    long startTime;
    long endTime = 0;
    long endFrame = 0;

    public int rescuedPatientCount = 0;

    public World() {
        startTime = System.currentTimeMillis();

//        workbook = new XSSFWorkbook();
//        patientSheet = workbook.createSheet("patients");

        statisticsSheet = workbook.createSheet("statistics");
        //statisticsSheet.trackAllColumnsForAutoSizing();

        hospitalSheet = workbook.createSheet("hospitals");
        //hospitalSheet.trackAllColumnsForAutoSizing();

        patientSheet = workbook.createSheet("patients");
        //patientSheet.trackAllColumnsForAutoSizing();

        ambulanceSheet = workbook.createSheet("ambulances");
        //ambulanceSheet.trackAllColumnsForAutoSizing();

        fireFighterSheet = workbook.createSheet("fire fighters");
        //fireFighterSheet.trackAllColumnsForAutoSizing();

        headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        map = new Map();
        addChild(map);
        //map.canUpdate(false);

        // Create map first and then create router
        router = new MsgRouter(this, workbook);
        addChild(router);

        createObjects();
        writeScenario();
    }

    // Create Objects for visualization
    private void createObjects() {
        createSafeZones();
        createOrganization();
        createHospitals();
        createPatients();
        createAmbulances();
        createFireFighters();
    }

    private void createPatients() {                 // Create the patient at random position
        for (int i = 0; i < maxPatient; i++) {
            Patient patient = new Patient(this, "Patient" + ++patientCounter);
            patients.add(patient);
            patient.setStatus(Patient.Status.random());
            Position randomPosition = null;

            while(true) {
                //randomPosition = GlobalRandom.nextPosition(Map.mapSize.width, Map.mapSize.height);
                randomPosition = GlobalRandom.nextPosition(Map.mapSize.width / 8 + 2, 7 * Map.mapSize.width / 8 -1,
                        Map.mapSize.height / 8 + 2, 7 * Map.mapSize.width / 8 - 1);
                boolean isSafeZone = false;
                for(SafeZone zone: safeZones) {
                    if(zone.isSafeZone(randomPosition)) {
                        isSafeZone = true;
                        break;
                    }
                }
                if(isSafeZone == false) break;
            }
            patient.setPosition(randomPosition);
            this.addChild(patient);
        }
    }

    private void createFireFighters() {                 // Create Firefighters at the edge position
        Position[] positions = new Position[] {
                new Position(0, 0),
                new Position(Map.mapSize.width - 1, 0),
                new Position(Map.mapSize.width - 1, Map.mapSize.height - 1),
                new Position(0, Map.mapSize.height - 1)
        };
        for (int i = 0; i < maxFireFighter; i++) {
            FireFighter ff = new FireFighter(this, fireFighterPrefix + ++fireFighterCounter);
            ff.setPosition(0, 0);
            fireFighters.add(ff);

            ff.setPosition(positions[positionIndex++]);
            if(positionIndex >= 4)
                positionIndex = 0;
//            if(i == 0) {
//                addChild(ff.individualMap);
//            }
            addChild(ff);
        }
    }

    private void createHospitals() {                 // Create Hospital at the edge position
        Row row = hospitalSheet.createRow(0);
        row.createCell(0).setCellValue("frame count");
        row.createCell(1).setCellValue("hospital name");
        row.createCell(2).setCellValue("treatment patient count");
        row.createCell(3).setCellValue("wait patient count");
        row.createCell(4).setCellValue("hospitalize");
        row.createCell(5).setCellValue("leave");
        for(int i = 0; i < maxHospital; ++i) {
            Hospital hospital = new Hospital(this, "Hospital" + (i + 1), hospitalSheet);
            hospitals.add(hospital);
            addChild(hospital);

            if(i == 0){
                hospital.setCapacity(2);
            } else {
                hospital.setCapacity(10);
            }
        }
        hospitals.get(0).setPosition(0, 0);
        hospitals.get(1).setPosition(Map.mapSize.width - 1, 0);
        hospitals.get(2).setPosition(0, Map.mapSize.height - 1);
        hospitals.get(3).setPosition(Map.mapSize.width - 1, Map.mapSize.height - 1);
//        hospitals.get(4).setPosition(0, (Map.mapSize.height - 1) / 2);
//        hospitals.get(5).setPosition(Map.mapSize.width - 1, (Map.mapSize.height - 1) / 2);
    }

    private void createSafeZones() {                // Create the Safe Zone at the quarter position of the map
        for(int i = 0; i < maxSafeZone; ++i) {
            SafeZone safeZone = new SafeZone(this, "SafeZone" + (i + 1));
            safeZones.add(safeZone);
            addChild(safeZone);
        }

//        safeZones.get(0).setPosition(new Position(Map.mapSize.width / 4, Map.mapSize.height / 4));
//        safeZones.get(1).setPosition(new Position(3 * Map.mapSize.width / 4, Map.mapSize.height / 4));
//        safeZones.get(2).setPosition(new Position(3 * Map.mapSize.width / 4, 3 * Map.mapSize.height / 4));
//        safeZones.get(3).setPosition(new Position(Map.mapSize.width / 4, 3 * Map.mapSize.height / 4));
        safeZones.get(0).setPosition(new Position(Map.mapSize.width / 8, Map.mapSize.height / 8));
        safeZones.get(1).setPosition(new Position(7 * Map.mapSize.width / 8, Map.mapSize.height / 8));
        safeZones.get(2).setPosition(new Position(7 * Map.mapSize.width / 8, 7 * Map.mapSize.height / 8));
        safeZones.get(3).setPosition(new Position(Map.mapSize.width / 8, 7 * Map.mapSize.height / 8));

    }

    int ambulancePositionIndex = 0;
    private void createAmbulances() {                    // Create Ambulance at the edge position
        Position[] positions = new Position[] {
                new Position(0, 0),
                new Position(Map.mapSize.width - 1, 0),
                new Position(Map.mapSize.width - 1, Map.mapSize.height - 1),
                new Position(0, Map.mapSize.height - 1)
        };
        for(int i = 0; i < maxAmbulance; ++i) {
            Ambulance ambulance = new Ambulance(this, "Ambulance" + ++ambulanceCounter);
            ambulances.add(ambulance);
            addChild(ambulance);
            ambulance.setPosition(positions[ambulancePositionIndex++]);
            if(ambulancePositionIndex >= 4) {
                ambulancePositionIndex = 0;
            }
        }
    }

    private void createOrganization() {                      // Create Organization
        Organization organization = new Organization(this, "Organization");
        addChild(organization);
    }

    int frameCount = 0;
    public int savedPatientCount = 0;
    @Override
    public void onUpdate() {

        //if(getPatientCount() == 0 && map.getUnvisitedTileCount() == 0) {
        if(patients.size() == savedPatientCount && map.getUnvisitedTileCount() == 0) {
            canUpdate(false);

            endTime = System.currentTimeMillis();
            endFrame = frameCount;
//            printPatientLog(true);
//            printFireFighterLog(true);
            return;
        } else {
            printPatientLog(false);
            printFireFighterLog(false);
            printAmbulanceLog(false);
            frameCount++;
            System.out.println("FrameCount: " + frameCount);
        }

        ArrayList<Stimulus> mustRemove = new ArrayList<>();
        for(Stimulus stimulus : stimuli) {
            if(stimulus.frame == Time.getFrameCount()) {
                stimulus.execute();
                mustRemove.add(stimulus);
            }
        }
        stimuli.removeAll(mustRemove);

    }



    private void printPatientLog(boolean isFinish) {

        if(frameCount == 0) {
            Row row = patientSheet.createRow(patientSheet.getPhysicalNumberOfRows());
            Cell frameCountCell = row.createCell(0);
            Cell savedPatientCell = row.createCell(1);
            Cell rescuedPatientCell = row.createCell(2);

            frameCountCell.setCellValue("frame count");
            savedPatientCell.setCellValue("number of treated patients");
            rescuedPatientCell.setCellValue("number of rescued patients");
        }

        Row row = patientSheet.createRow(patientSheet.getPhysicalNumberOfRows());
        Cell frameCountCell = row.createCell(0);
        Cell savedPatientCell = row.createCell(1);
        Cell rescuedPatientCell = row.createCell(2);

        frameCountCell.setCellValue(frameCount);
        savedPatientCell.setCellValue(savedPatientCount);
        rescuedPatientCell.setCellValue(rescuedPatientCount);
    }


    private void printFireFighterLog(boolean isFinish) {

        ExcelHelper.getCell(fireFighterSheet, 0, 0).setCellValue("frame Count");
        ExcelHelper.getCell(fireFighterSheet, 0, 1).setCellValue("number of Firefighter");
        for(int i = 0; i < fireFighters.size(); ++i) {
            ExcelHelper.getCell(fireFighterSheet, 0, i * 2 + 2).setCellValue("FF" + (i + 1) + " pos");
            ExcelHelper.getCell(fireFighterSheet, 0, i * 2 + 3).setCellValue("FF" + (i + 1) + " Status");
        }

        Row row = fireFighterSheet.createRow(fireFighterSheet.getPhysicalNumberOfRows());
        Cell frameCountCell = row.createCell(0);
        Cell firefighterCountCell = row.createCell(1);
        frameCountCell.setCellValue(frameCount);
        firefighterCountCell.setCellValue(fireFighterCounter);
        Cell[] positionCells = new Cell[fireFighters.size()];

        for(int i = 0; i < fireFighters.size(); ++i) {
            Cell currentCell = row.createCell(i * 2 + 2);

            String position = fireFighters.get(i).position.toString();
            currentCell.setCellValue(position);

            currentCell = row.createCell(i * 2 + 3);
            currentCell.setCellValue(fireFighters.get(i).currentAction.name);
            //currentCell.setCellValue(fireFighters.get(i).getState().toString());
        }

        if(isFinish) {
            row = fireFighterSheet.createRow(fireFighterSheet.getPhysicalNumberOfRows());
            frameCountCell = row.createCell(0);
            frameCountCell.setCellValue("total distance");
            positionCells = new Cell[fireFighters.size()];

            for(int i = 0; i < fireFighters.size(); ++i) {
                Cell currentCell = row.createCell(i * 2 + 1);
                positionCells[i] = currentCell;

                positionCells[i].setCellValue(fireFighters.get(i).totalDistance);
            }
        }
    }


    private void printAmbulanceLog(boolean isFinish) {

        ExcelHelper.getCell(ambulanceSheet, 0, 0).setCellValue("frame count");
        ExcelHelper.getCell(ambulanceSheet, 0, 1).setCellValue("number of Ambulances");
        for(int i = 0; i < ambulances.size(); ++i) {
            ExcelHelper.getCell(ambulanceSheet, 0, i * 2 + 2).setCellValue("Amb" + (i + 1) + " pos");
            ExcelHelper.getCell(ambulanceSheet, 0, i * 2 + 3).setCellValue("Amb" + (i + 1) + " Status");
        }

        Row row = ambulanceSheet.createRow(ambulanceSheet.getPhysicalNumberOfRows());
        Cell frameCountCell = row.createCell(0);
        Cell ambulanceCountCell = row.createCell(1);
        frameCountCell.setCellValue(frameCount);
        ambulanceCountCell.setCellValue(ambulanceCounter);
        Cell[] positionCells;

        for(int i = 0; i < ambulances.size(); ++i) {
            Cell currentCell = row.createCell(i * 2 + 2);

            String position = ambulances.get(i).position.toString();
            currentCell.setCellValue(position);

            currentCell = row.createCell(i * 2 + 3);
            currentCell.setCellValue(ambulances.get(i).currentAction.name);
            //currentCell.setCellValue(fireFighters.get(i).getState().toString());
        }

        if(isFinish) {
            row = ambulanceSheet.createRow(ambulanceSheet.getPhysicalNumberOfRows());
            frameCountCell = row.createCell(0);
            frameCountCell.setCellValue("total distance");
            positionCells = new Cell[ambulances.size()];

            for(int i = 0; i < ambulances.size(); ++i) {
                Cell currentCell = row.createCell(i * 2 + 1);
                positionCells[i] = currentCell;

                positionCells[i].setCellValue(ambulances.get(i).totalDistance);
            }
        }
    }

    @Override
    public void onRender(Graphics2D g) {
        Rectangle rect = g.getDeviceConfiguration().getBounds();
        g.setColor(new Color(100, 100, 100));
        g.fillRect(rect.x, rect.y, rect.width, rect.height);

        g.setColor(Color.red);
        g.setFont(new Font("default", Font.BOLD, 16));
        String strFrameCount = "frameCount: " + frameCount;
        g.drawChars(strFrameCount.toCharArray(), 0, strFrameCount.length(), 600, 700);
    }

    public Map getMap() {
        return map;
    }


    public int getPatientCount() {
        int count = 0;
        for(SoSObject child: children) {
            if(child instanceof Patient) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void clear() {

        Sheet sheet = statisticsSheet;
        Row row = sheet.createRow(0);

        // Total runtime
        ExcelHelper.getCell(row, 0).setCellValue("Runtime");
        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
        if(endTime == 0) {
            endTime = System.currentTimeMillis();
        }
        ExcelHelper.getCell(row, 1).setCellValue(((endTime - startTime) / 1000) + " s");
        row = ExcelHelper.nextRow(row);

        // end Frame
        ExcelHelper.getCell(row, 0).setCellValue("Run frame");
        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
        if(endFrame == 0) {
            endFrame = frameCount;
        }
        ExcelHelper.getCell(row, 1).setCellValue(endFrame);
        row = ExcelHelper.nextRow(row);

        // initial patient count
        ExcelHelper.getCell(row, 0).setCellValue("initial patient count");
        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
        ExcelHelper.getCell(row, 1).setCellValue(maxPatient);
        row = ExcelHelper.nextRow(row);

        // initial fire fighter count
        ExcelHelper.getCell(row, 0).setCellValue("initial fire fighter count");
        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
        ExcelHelper.getCell(row, 1).setCellValue(maxFireFighter);
        row = ExcelHelper.nextRow(row);

        // initial ambulance count
        ExcelHelper.getCell(row, 0).setCellValue("initial ambulance count");
        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
        ExcelHelper.getCell(row, 1).setCellValue(maxAmbulance);
        row = ExcelHelper.nextRow(row);

        // initial hospital count
        ExcelHelper.getCell(row, 0).setCellValue("initial hospital count");
        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
        ExcelHelper.getCell(row, 1).setCellValue(maxHospital);
        row = ExcelHelper.nextRow(row);

        // initial safezone count
        ExcelHelper.getCell(row, 0).setCellValue("initial safezone count");
        ExcelHelper.getCell(row, 0).setCellStyle(headerStyle);
        ExcelHelper.getCell(row, 1).setCellValue(maxSafeZone);
        row = ExcelHelper.nextRow(row);




        printPatientLog(true);
        printFireFighterLog(true);
        printAmbulanceLog(true);

        long nano = System.currentTimeMillis();
        String date = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss").format(nano);
        String filePath = "log/" + date + ".xlsx";

        ExcelHelper.autoSizeAllColumn(workbook);
        ExcelHelper.save(workbook, filePath);
    }


    public boolean contains(SoSObject object) {
        return children.contains(object);
    }

    int positionIndex = 0;


    public void addPatient(Position position) {
        Patient patient = new Patient(this, "Patient" + ++patientCounter);
        patients.add(patient);
        patient.setStatus(Patient.Status.random());
        if(position == null) {
            Position randomPosition = null;

            while (true) {
                //randomPosition = GlobalRandom.nextPosition(Map.mapSize.width, Map.mapSize.height);
                randomPosition = GlobalRandom.nextPosition(Map.mapSize.width / 8, 7 * Map.mapSize.width / 8,
                        Map.mapSize.height / 8, 7 * Map.mapSize.width / 8);
                boolean isSafeZone = false;
                for (SafeZone zone : safeZones) {
                    if (zone.isSafeZone(randomPosition)) {
                        isSafeZone = true;
                        break;
                    }
                }
                if (isSafeZone == false) break;
            }

            patient.setPosition(randomPosition);
        } else {
            patient.setPosition(position);
        }
        this.addChild(patient);

        ArrayList<Patient> data = new ArrayList<>();
        data.add(patient);


        for(FireFighter fireFighter: fireFighters) {
            Msg msg = new Msg()
                    .setFrom(fireFighterPrefix)
                    .setTo(fireFighter.name)
                    .setTitle("patientsMemory")
                    .setData(data);
            router.route(msg);
        }
    }



    private void writeScenario() {

        ArrayList<String> firefighterNames = new ArrayList<>();
        for(int i = 0; i < maxFireFighter; ++i) {
            firefighterNames.add(fireFighterPrefix + (i + 1));
        }

        ArrayList<String> AmbulanceNames = new ArrayList<>();
        for(int i = 0; i < maxAmbulance; ++i) {
            AmbulanceNames.add("Ambulance" + (i + 1));
        }

//
        // Stimulus types
//        // TODO: Late Rescue Start
//        stimuli.add(new Speed(this, 1, "FF1", 100));
//        stimuli.add(new Speed(this, 1, "FF5", 100));
//        stimuli.add(new Speed(this, 1, firefighterNames, 100));                 // 1 frame 부터 100 frame까지 동작 안함 ==> 이동속도가 100 frame당 한 칸이므로
//        stimuli.add(new Speed(this, 100, firefighterNames, 3));                 // 100 프레임 부터 회복 --> 그런데 만약 위에서 1 frame부터 200 frame까지 이속 감소를 시키면... 회복은 200부터 적용되는듯?
//
//        stimuli.add(new Speed(this, 100, AmbulanceNames, 300));                 // 100 frame 부터 400 frame까지 동작 안함
//        stimuli.add(new Speed(this, 400, AmbulanceNames, 0));
//
//        // TODO: moveDelay
//        stimuli.add(new Speed(this, 100, "FF1", 3));                           // 100 frame 부터 FF1의 이동 속도 30 frame 당 1칸 이동 ==> 이속 감소
//        stimuli.add(new Speed(this, 100, firefighterNames, 3));                // 100 frame 부터 전체 FF의 이동소도 감소
//        stimuli.add(new Speed(this, 10, new Range(29, 29, 35, 35), 10.0f));
//



        stimuli.add(new Speed(this, 1200, new Range(13, 13, 51, 51), 2.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
        stimuli.add(new Speed(this, 4260, new Range(14, 14, 50, 50), 4.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)

        stimuli.add(new Speed(this, 4830, new Range(14, 14, 50, 50), 2.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
        stimuli.add(new Speed(this, 4830, new Range(18, 18, 46, 46), 4.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)

        stimuli.add(new Speed(this, 5310, new Range(18, 18, 46, 46), 2.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
        stimuli.add(new Speed(this, 5310, new Range(22, 22, 42, 42), 4.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)

        stimuli.add(new Speed(this, 6060, new Range(22, 22, 42, 42), 2.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)




//        stimuli.add(new Speed(this, 10, new Range(16, 16, 48, 48), 2.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//        stimuli.add(new Speed(this, 10, new Range(24, 24, 40, 40), 3.0f));
//        stimuli.add(new Speed(this, 10, new Range(29, 29, 35, 35), 5.0f));
//
//
//        stimuli.add(new Speed(this, 100, AmbulanceNames, 10));                              // 특정 frame count 이후 Ambulance 전체 move speed 변경
//        stimuli.add(new Speed(this, 100, "Ambulance1", 7));
//
//        // TODO: sightRange
//        stimuli.add(new SightRange(this, 100, "FF1", 5));                               // 특정 frame count 이후 FF1의 sight range 변화
//        stimuli.add(new SightRange(this, 10, firefighterNames, 0));                    // 특정 frame count 이후 전체 FF의 sight range 변화
//        stimuli.add(new SightRange(this, 800, firefighterNames, 5));                    // 특정 frame count 이후 전체 FF의 sight range 변화
//        stimuli.add(new SightRange(this, 1600, firefighterNames, 3));                    // 특정 frame count 이후 전체 FF의 sight range 변화
//        stimuli.add(new SightRange(this, 2400, firefighterNames, 1));                    // 특정 frame count 이후 전체 FF의 sight range 변화
//
//
//
//
//        stimuli.add(new SightRange(this, 1200, new Range(13, 13, 51, 51), 0.5f));
//        stimuli.add(new SightRange(this, 4830, new Range(14, 14, 50, 50), 0.2f));
//
//
//
//
////        stimuli.add(new SightRange(this, 10, new Range(0, 0, 64, 64), 3.0f));           // 특정 frame count 이후 특정 구역의 sight range 변화
////
////        // TODO: communicationRange (FF 관련)
////        stimuli.add(new CommunicationRange(this, 100, "FF1", 7));                               // 특정 frame count 이후 FF1의 communication range 변화
////        stimuli.add(new CommunicationRange(this, 10, firefighterNames, 0));                    // 특정 frame count 이후 전체 FF의 communication range 변화
////        stimuli.add(new CommunicationRange(this, 800, firefighterNames, 7));                    // 특정 frame count 이후 전체 FF의 communication range 변화
////        stimuli.add(new CommunicationRange(this, 1600, firefighterNames, 5));                    // 특정 frame count 이후 전체 FF의 communication range 변화
//
//
//

        stimuli.add(new CommunicationRange(this, 2000, firefighterNames, 1));
//        stimuli.add(new CommunicationRange(this, 1200, firefighterNames, 7));
//        stimuli.add(new CommunicationRange(this, 2000, firefighterNames, 5));                    // 특정 frame count 이후 전체 FF의 communication range 변화
//
//
//
//        stimuli.add(new CommunicationRange(this, 2400, firefighterNames, 3));                    // 특정 frame count 이후 전체 FF의 communication range 변화
//        stimuli.add(new CommunicationRange(this, 10, new Range(0, 0, 10, 10), 5.0f));           // 특정 frame count 이후 특정 구역의 communication range 변화
//
//        // TODO: communication (1 to 1 casting), FF 제외?  FF가 org로 보내는 message는 포함
//        stimuli.add(new Delay(this, 1980, "ALL_DELAY", 5000));
//        stimuli.add(new Delay(this, 7000,  "ALL_DELAY", 0));
//
//        stimuli.add(new Delay(this, 10, "TO_ORG_DELAY", 130));
//        stimuli.add(new Delay(this, 200,  "TO_ORG_DELAY", 0));
//
//        stimuli.add(new Delay(this, 10, "FROM_ORG_DELAY", 130));
//        stimuli.add(new Delay(this, 200,  "FROM_ORG_DELAY", 0));
//
//        stimuli.add(new Delay(this, 10, "FF_TO_ORG_DELAY", 130));
//        stimuli.add(new Delay(this, 200,  "FF_TO_ORG_DELAY", 0));
//
//        stimuli.add(new Delay(this, 10, "ORG_TO_FF_DELAY", 130));
//        stimuli.add(new Delay(this, 200,  "ORG_TO_FF_DELAY", 0));
//
//        stimuli.add(new Delay(this, 10, "AMB_TO_ORG_DELAY", 300));
//        stimuli.add(new Delay(this, 500,  "AMB_TO_ORG_DELAY", 0));
//
//        stimuli.add(new Delay(this, 10, "ORG_TO_AMB_DELAY", 130));
//        stimuli.add(new Delay(this, 200,  "ORG_TO_AMB_DELAY", 0));
//
//        stimuli.add(new Delay(this, 10, "SZ_TO_ORG_DELAY", 130));
//        stimuli.add(new Delay(this, 200,  "SZ_TO_ORG_DELAY", 0));
//
//        stimuli.add(new Delay(this, 10, "ORG_TO_SZ_DELAY", 130));
//        stimuli.add(new Delay(this, 200,  "ORG_TO_SZ_DELAY", 0));
//
//        stimuli.add(new Delay(this, 10, "FF_RANGECAST_DELAY", 130));
//        stimuli.add(new Delay(this, 200,  "FF_RANGECAST_DELAY", 0));
//        stimuli.add(new Delay(this, 1980, "FF_RANGECAST_DELAY", 5000));
//        stimuli.add(new Delay(this, 7000,  "FF_RANGECAST_DELAY", 0));
//
//
//        // TODO: FireFighter => Patient
//        stimuli.add(new Injured(this, 100, "FF1"));
//        stimuli.add(new Injured(this, 100, "FF2"));
//        stimuli.add(new Injured(this, 100, "FF3"));
//        stimuli.add(new Injured(this, 100, "FF4"));
//        stimuli.add(new Injured(this, 100, "FF5"));
//        stimuli.add(new Injured(this, 100, "FF6"));
//
//        // TODO: remove FireFighter1
//
//        stimuli.add(new RemoveEntity(this, 100, "FF1", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 110, "FF2", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 120, "FF3", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 130, "FF4", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 140, "FF5", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 150, "FF6", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 160, "FF7", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 170, "FF8", this::removeCS));
//
//        // TODO: remove Ambulance1    ==> error 발생!! log 문제인듯
//        stimuli.add(new RemoveEntity(this, 100, "Ambulance1", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 120, "Ambulance2", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 130, "Ambulance3", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 140, "Ambulance4", this::removeCS));
//
//        // TODO: add FireFighter
        stimuli.add(new AddEntity(this, 450, this::addFireFighter));
        stimuli.add(new AddEntity(this, 450, this::addFireFighter));

        stimuli.add(new AddEntity(this, 630, this::addFireFighter));
        stimuli.add(new AddEntity(this, 630, this::addFireFighter));

        stimuli.add(new AddEntity(this, 780, this::addFireFighter));
        stimuli.add(new AddEntity(this, 780, this::addFireFighter));

        stimuli.add(new AddEntity(this, 870, this::addFireFighter));
        stimuli.add(new AddEntity(this, 870, this::addFireFighter));
        stimuli.add(new AddEntity(this, 870, this::addFireFighter));
        stimuli.add(new AddEntity(this, 870, this::addFireFighter));
        stimuli.add(new AddEntity(this, 870, this::addFireFighter));

        stimuli.add(new AddEntity(this, 930, this::addFireFighter));
        stimuli.add(new AddEntity(this, 930, this::addFireFighter));
        stimuli.add(new AddEntity(this, 930, this::addFireFighter));
        stimuli.add(new AddEntity(this, 930, this::addFireFighter));
        stimuli.add(new AddEntity(this, 930, this::addFireFighter));

        stimuli.add(new AddEntity(this, 990, this::addFireFighter));
        stimuli.add(new AddEntity(this, 990, this::addFireFighter));
        stimuli.add(new AddEntity(this, 990, this::addFireFighter));
        stimuli.add(new AddEntity(this, 990, this::addFireFighter));
        stimuli.add(new AddEntity(this, 990, this::addFireFighter));

//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));
//        stimuli.add(new AddEntity(this, 1980, this::addFireFighter));



         // TODO: add Ambulance
        stimuli.add(new AddEntity(this, 200, this::addAmbulance));
        stimuli.add(new AddEntity(this, 210, this::addAmbulance));
        stimuli.add(new AddEntity(this, 220, this::addAmbulance));
        stimuli.add(new AddEntity(this, 230, this::addAmbulance));

        // CS && CS
        router.add(new Delay(1, "FF", "FF", 20));
        router.add(new Delay(25, "FF", "FF", 0));       // 해제
        router.add(new Delay(26, "FF", "FF", 100));     // 해제 했을 경우 다음 프레임부터 적용하도록

        //router.add(new Delay(1, "Ambulance", "Org", 20));

        // Entity && CS
        //router.add(new Delay(1, "FF1", "FF", 20));
//        router.add(new Delay(1, "Ambulance1", "Org", 20));

        // CS && Entity
        //router.add(new Delay(1, "FF", "FF5", 20));
        //router.add(new Delay(1, "Org", "Ambulance1", 20));

        // Entity && Entity
        //router.add(new Delay(1, "FF1", "FF5", 20));



    }

    void removeCS(String csName) {
        SoSObject obj = findObject(csName);
        if(obj == null) return;


        CS cs = (CS)obj;
        cs.visible(false);
        cs.canUpdate(false);
        cs.currentAction.name = "Removed";
        removeChild(obj);
    }

    void addFireFighter() {
        Position[] positions = new Position[]{
                new Position(0, 0),
                new Position(Map.mapSize.width - 1, 0),
                new Position(Map.mapSize.width - 1, Map.mapSize.height - 1),
                new Position(0, Map.mapSize.height - 1)
        };

        FireFighter ff = new FireFighter(this, fireFighterPrefix + ++fireFighterCounter);
        fireFighters.add(ff);

        ff.setPosition(positions[positionIndex++]);
        if (positionIndex >= 4)
            positionIndex = 0;
        addChild(ff);
    }

    private void addAmbulance() {
        Position[] positions = new Position[]{
                new Position(0, 0),
                new Position(Map.mapSize.width - 1, 0),
                new Position(Map.mapSize.width - 1, Map.mapSize.height - 1),
                new Position(0, Map.mapSize.height - 1)
        };
        Ambulance ambulance = new Ambulance(this, "Ambulance" + ++ambulanceCounter);
        ambulances.add(ambulance);
        ambulance.setPosition(positions[positionIndex++]);
        if (positionIndex >= 4)
            positionIndex = 0;
        addChild(ambulance);
    }
}
