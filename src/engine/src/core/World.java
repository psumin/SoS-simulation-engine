package core;

import action.firefighteraction.FireFighterDead;
import agents.*;
import misc.Position;

import misc.Range;
import misc.Time;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import stimulus.*;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class World extends SoSObject{

    private final ArrayList<Scenario> scenarios = new ArrayList<>();

    int patientCounter = 0;
    int fireFighterCounter = 0;
    int ambulanceCounter = 0;

    // Initial Values
    public static final int maxPatient = 300;
    public static final int maxFireFighter = 8;
    public static final int maxHospital = 4;
    public static final int maxAmbulance = 12;
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

    Workbook workbook = new SXSSFWorkbook();
    Sheet statisticsSheet = workbook.createSheet("statistics");
    Sheet hospitalSheet = workbook.createSheet("hospitals");
    long startTime;

    public World() {
        startTime = System.currentTimeMillis();

//        workbook = new XSSFWorkbook();
//        patientSheet = workbook.createSheet("patients");

        map = new Map();
        addChild(map);
        map.canUpdate(false);

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
                randomPosition = GlobalRandom.nextPosition(Map.mapSize.width, Map.mapSize.height);
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
            //ff.setPosition(0, 0);
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
        row.createCell(4).setCellValue("hospitalize");;
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
    }

    private void createSafeZones() {                // Create the Safe Zone at the quarter position of the map
        for(int i = 0; i < maxSafeZone; ++i) {
            SafeZone safeZone = new SafeZone(this, "SafeZone" + (i + 1));
            safeZones.add(safeZone);
            addChild(safeZone);
        }

        safeZones.get(0).setPosition(new Position(Map.mapSize.width / 4, Map.mapSize.height / 4));
        safeZones.get(1).setPosition(new Position(3 * Map.mapSize.width / 4, Map.mapSize.height / 4));
        safeZones.get(2).setPosition(new Position(3 * Map.mapSize.width / 4, 3 * Map.mapSize.height / 4));
        safeZones.get(3).setPosition(new Position(Map.mapSize.width / 4, 3 * Map.mapSize.height / 4));
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

        ArrayList<Scenario> mustRemove = new ArrayList<>();
        for(Scenario scenario: scenarios) {
            if(scenario.frame == Time.getFrameCount()) {
                scenario.execute();
                mustRemove.add(scenario);
            }
        }
        scenarios.removeAll(mustRemove);

    }


    Sheet patientSheet = workbook.createSheet("patients");
    private void printPatientLog(boolean isFinish) {

        if(frameCount == 0) {
            Row row = patientSheet.createRow(patientSheet.getPhysicalNumberOfRows());
            Cell frameCountCell = row.createCell(0);
            Cell savedPatientCell = row.createCell(1);

            frameCountCell.setCellValue("frame count");
            savedPatientCell.setCellValue("number of rescued patients");
        }

        Row row = patientSheet.createRow(patientSheet.getPhysicalNumberOfRows());
        Cell frameCountCell = row.createCell(0);
        Cell savedPatientCell = row.createCell(1);

        frameCountCell.setCellValue(frameCount);
        savedPatientCell.setCellValue(savedPatientCount);
    }

    Sheet fireFighterSheet = workbook.createSheet("fire fighters");
    private void printFireFighterLog(boolean isFinish) {

        // i * 2
        // i * 2 + 1

        if(frameCount == 0) {
            Row row = fireFighterSheet.createRow(fireFighterSheet.getPhysicalNumberOfRows());
            Cell frameCountCell = row.createCell(0);
            frameCountCell.setCellValue("frame count");
            Cell[] positionCells = new Cell[fireFighters.size()];

            for(int i = 0; i < fireFighters.size(); ++i) {
                Cell currentCell = row.createCell(i * 2 + 1);
                String position = fireFighters.get(i).position.toString();
                currentCell.setCellValue("FF" + (i + 1) + " pos");

                currentCell = row.createCell(  i * 2 + 2);
                currentCell.setCellValue("FF" + (i + 1) + " Status");
            }
        }

        Row row = fireFighterSheet.createRow(fireFighterSheet.getPhysicalNumberOfRows());
        Cell frameCountCell = row.createCell(0);
        frameCountCell.setCellValue(frameCount);
        Cell[] positionCells = new Cell[fireFighters.size()];

        for(int i = 0; i < fireFighters.size(); ++i) {
            Cell currentCell = row.createCell(i * 2 + 1);

            String position = fireFighters.get(i).position.toString();
            currentCell.setCellValue(position);

            currentCell = row.createCell(  i * 2 + 2);
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

    Sheet ambulanceSheet = workbook.createSheet("Ambulances");
    private void printAmbulanceLog(boolean isFinish) {
        if(frameCount == 0) {
            Row row = ambulanceSheet.createRow(ambulanceSheet.getPhysicalNumberOfRows());
            Cell frameCountCell = row.createCell(0);
            frameCountCell.setCellValue("frame count");
            Cell[] positionCells = new Cell[maxAmbulance];

            for(int i = 0; i < maxAmbulance; ++i) {
                Cell currentCell = row.createCell(i * 2 + 1);
                String position = ambulances.get(i).position.toString();
                currentCell.setCellValue("Amb" + (i + 1) + " pos");

                currentCell = row.createCell(  i * 2 + 2);
                currentCell.setCellValue("Amb" + (i + 1) + " Status");
            }
        }

        Row row = ambulanceSheet.createRow(ambulanceSheet.getPhysicalNumberOfRows());
        Cell frameCountCell = row.createCell(0);
        frameCountCell.setCellValue(frameCount);
        Cell[] positionCells = new Cell[maxAmbulance];

        for(int i = 0; i < maxAmbulance; ++i) {
            Cell currentCell = row.createCell(i * 2 + 1);

            String position = ambulances.get(i).position.toString();
            currentCell.setCellValue(position);

            currentCell = row.createCell(  i * 2 + 2);
            currentCell.setCellValue(ambulances.get(i).currentAction.name);
            //currentCell.setCellValue(fireFighters.get(i).getState().toString());
        }

        if(isFinish) {
            row = ambulanceSheet.createRow(ambulanceSheet.getPhysicalNumberOfRows());
            frameCountCell = row.createCell(0);
            frameCountCell.setCellValue("total distance");
            positionCells = new Cell[maxAmbulance];

            for(int i = 0; i < maxAmbulance; ++i) {
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

        long endTime = System.currentTimeMillis();

        Row row = statisticsSheet.createRow(0);
        Cell header = row.createCell(0);
        Cell body = row.createCell(1);
        header.setCellValue("Total RunTime");
        body.setCellValue(((endTime - startTime) / 1000) + " s");


        printPatientLog(true);
        printFireFighterLog(true);
        printAmbulanceLog(true);

        long nano = System.currentTimeMillis();
        String date = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss").format(nano);
        try (OutputStream fileOut = new FileOutputStream("log/" + date +".xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean contains(SoSObject object) {
        return children.contains(object);
    }

    int positionIndex = 0;
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

    public void addPatient(Position position) {
        Patient patient = new Patient(this, "Patient" + ++patientCounter);
        patients.add(patient);
        patient.setStatus(Patient.Status.random());
        if(position == null) {
            Position randomPosition = null;

            while (true) {
                randomPosition = GlobalRandom.nextPosition(Map.mapSize.width, Map.mapSize.height);
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

    private void addAmbulance() {
        Ambulance ambulance = new Ambulance(this, "Ambulance" + ++ambulanceCounter);
        ambulances.add(ambulance);
        addChild(ambulance);
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

        // Stimulus types

        // TODO: Late Rescue Start
//        scenarios.add(new MoveDelayScenario(this, 1, "FF1", 100));
//        scenarios.add(new MoveDelayScenario(this, 1, "FF5", 100));
//        scenarios.add(new MoveDelayScenario(this, 1, firefighterNames, 100));                 // 1 frame 부터 100 frame까지 동작 안함 ==> 이동속도가 100 frame당 한 칸이므로
//        scenarios.add(new MoveDelayScenario(this, 100, firefighterNames, 3));                 // 100 프레임 부터 회복 --> 그런데 만약 위에서 1 frame부터 200 frame까지 이속 감소를 시키면... 회복은 200부터 적용되는듯?
//
//        scenarios.add(new MoveDelayScenario(this, 100, AmbulanceNames, 300));                 // 100 frame 부터 400 frame까지 동작 안함
//        scenarios.add(new MoveDelayScenario(this, 400, AmbulanceNames, 0));
//
//        // TODO: moveDelay
//        scenarios.add(new MoveDelayScenario(this, 100, "FF1", 3));                           // 100 frame 부터 FF1의 이동 속도 30 frame 당 1칸 이동 ==> 이속 감소
//        scenarios.add(new MoveDelayScenario(this, 100, firefighterNames, 3));                // 100 frame 부터 전체 FF의 이동소도 감소
//        scenarios.add(new MoveDelayScenario(this, 100, new Range(0, 0, 10, 10), 10.0f));      // 100 frame 부터 0, 0, 10, 10 위치에서 이속 감소 (10배 감소)
//        scenarios.add(new MoveDelayScenario(this, 100, AmbulanceNames, 10));                              // 특정 frame count 이후 Ambulance 전체 move speed 변경
//        scenarios.add(new MoveDelayScenario(this, 100, "Ambulance1", 7));
//
//        // TODO: sightRange
//        scenarios.add(new SightRangeScenario(this, 100, "FF1", 5));                               // 특정 frame count 이후 FF1의 sight range 변화
//        scenarios.add(new SightRangeScenario(this, 100, firefighterNames, 9));                    // 특정 frame count 이후 전체 FF의 sight range 변화
////        scenarios.add(new SightRangeScenario(this, 10, new Range(0, 0, 10, 10), 5.0f));           // 특정 frame count 이후 특정 구역의 sight range 변화
//
//        // TODO: communicationRange (FF 관련)
//        scenarios.add(new CommunicationRangeScenario(this, 100, "FF1", 7));                               // 특정 frame count 이후 FF1의 communication range 변화
//        scenarios.add(new CommunicationRangeScenario(this, 100, firefighterNames, 7));                    // 특정 frame count 이후 전체 FF의 communication range 변화
////        scenarios.add(new CommunicationRangeScenario(this, 10, new Range(0, 0, 10, 10), 5.0f));           // 특정 frame count 이후 특정 구역의 communication range 변화
//
//        // TODO: communication (1 to 1 casting), FF 제외?  FF가 org로 보내는 message는 포함
//        scenarios.add(new CommunicationDelayScenario(this, 10, "ALL_DELAY", 300));            //
//        scenarios.add(new CommunicationDelayScenario(this, 500,  "ALL_DELAY", 0));
//
//        scenarios.add(new CommunicationDelayScenario(this, 10, "TO_ORG_DELAY", 130));
//        scenarios.add(new CommunicationDelayScenario(this, 200,  "TO_ORG_DELAY", 0));
//
//        scenarios.add(new CommunicationDelayScenario(this, 10, "FROM_ORG_DELAY", 130));
//        scenarios.add(new CommunicationDelayScenario(this, 200,  "FROM_ORG_DELAY", 0));
//
//        scenarios.add(new CommunicationDelayScenario(this, 10, "FF_TO_ORG_DELAY", 130));
//        scenarios.add(new CommunicationDelayScenario(this, 200,  "FF_TO_ORG_DELAY", 0));
//
//        scenarios.add(new CommunicationDelayScenario(this, 10, "ORG_TO_FF_DELAY", 130));
//        scenarios.add(new CommunicationDelayScenario(this, 200,  "ORG_TO_FF_DELAY", 0));
//
//        scenarios.add(new CommunicationDelayScenario(this, 10, "AMB_TO_ORG_DELAY", 300));
//        scenarios.add(new CommunicationDelayScenario(this, 500,  "AMB_TO_ORG_DELAY", 0));
//
//        scenarios.add(new CommunicationDelayScenario(this, 10, "ORG_TO_AMB_DELAY", 130));
//        scenarios.add(new CommunicationDelayScenario(this, 200,  "ORG_TO_AMB_DELAY", 0));
//
//        scenarios.add(new CommunicationDelayScenario(this, 10, "SZ_TO_ORG_DELAY", 130));
//        scenarios.add(new CommunicationDelayScenario(this, 200,  "SZ_TO_ORG_DELAY", 0));
//
//        scenarios.add(new CommunicationDelayScenario(this, 10, "ORG_TO_SZ_DELAY", 130));
//        scenarios.add(new CommunicationDelayScenario(this, 200,  "ORG_TO_SZ_DELAY", 0));
//
//        scenarios.add(new CommunicationDelayScenario(this, 10, "FF_RANGECAST_DELAY", 130));
//        scenarios.add(new CommunicationDelayScenario(this, 200,  "FF_RANGECAST_DELAY", 0));
//
//
//        // TODO: FireFighter => Patient
//        scenarios.add(new FireFighterToPatientScenario(this, 100, "FF1"));
//
//        // TODO: remove FireFighter1
//        scenarios.add(new LambdaScenario(this, 100, "FF1", this::removeCS));
//
        // TODO: remove Ambulance1
//        scenarios.add(new LambdaScenario(this, 1000, "Ambulance1", this::removeCS));
//        scenarios.add(new LambdaScenario(this, 1100, "Ambulance2", this::removeCS));
//        scenarios.add(new LambdaScenario(this, 1200, "Ambulance8", this::removeCS));
//        scenarios.add(new LambdaScenario(this, 1300, "Ambulance6", this::removeCS));
//
//        // TODO: add FireFighter
//        scenarios.add(new LambdaScenario(this, 105, this::addFireFighter));
//
        // TODO: add Ambulance
//        scenarios.add(new LambdaScenario(this, 100, this::addAmbulance));
//        scenarios.add(new LambdaScenario(this, 200, this::addAmbulance));
//        scenarios.add(new LambdaScenario(this, 300, this::addAmbulance));
//        scenarios.add(new LambdaScenario(this, 400, this::addAmbulance));

        scenarios.add(new FireFighterToPatientScenario(this, 100, "FF1"));
        scenarios.add(new FireFighterToPatientScenario(this, 100, "FF2"));
        scenarios.add(new FireFighterToPatientScenario(this, 100, "FF3"));
        scenarios.add(new FireFighterToPatientScenario(this, 100, "FF4"));
        scenarios.add(new FireFighterToPatientScenario(this, 100, "FF5"));
        scenarios.add(new FireFighterToPatientScenario(this, 100, "FF6"));
    }

    void removeCS(String csName) {
        SoSObject obj = findObject(csName);
        if(obj == null) return;

        if(obj instanceof FireFighter) {
            map.remove((FireFighter)obj);
            //fireFighters.remove(obj);
            FireFighter fireFighter = (FireFighter)obj;
            fireFighter.changeAction(new FireFighterDead(fireFighter));
        } else if(obj instanceof Ambulance) {
            ambulances.remove(obj);
        }
        removeChild(obj);
    }
}
