package core;

import action.ambulanceaction.AmbulanceFree;
import action.ambulanceaction.AmbulanceMoveTobridgehead;
import action.ambulanceaction.AmbulanceSearch;
import action.ambulanceaction.AmbulanceTransferToHospital;
import action.firefighteraction.*;

import agents.*;
import misc.Position;

import misc.Range;
import misc.Time;
import stimulus.*;
import stimulus.EntityStimulus.RemoveEntity;
import stimulus.MessageStimulus.Delay;
import stimulus.MessageStimulus.Loss;
import stimulus.StateStimulus.Injury;
import stimulus.ValueStimulus.CommunicationRange;
import stimulus.ValueStimulus.SightRange;
import stimulus.ValueStimulus.Speed;
import stimulus.EntityStimulus.AddEntity;

import java.awt.*;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class World extends SoSObject{

//    // Structure of stimuli
//    public static class InputData {
//        public String command;
//        public int frame;
//        public int count;
//
//        public InputData(String command, int frame, int count) {
//            this.command = command;
//            this.frame = frame;
//            this.count = count;
//        }
//    }
//
//    public static class InputRanges {
//        public String command;
//        public int frame;
//        public int left;
//        public int top;
//        public int right;
//        public int bottom;
//        public Object value;
//
//        public InputRanges(String command, int frame, int left, int top, int right, int bottom, Object value) {
//            this.command = command;
//            this. frame = frame;
//            this.left = left;
//            this.top = top;
//            this.right = right;
//            this.bottom = bottom;
//            this.value = value;
//        }
//    }
//
//    // Structure o message stimuli
//    public static class InputMessages {
//        public String command;
//        public int startFrame;
//        public int finishFrame;
//        public String sender;
//        public String receiver;
//        public int duration;
//
//        public InputMessages(String command, int startFrame, int finishFrame, String sender, String receiver, int duration) {
//            this.command = command;
//            this.startFrame = startFrame;
//            this.finishFrame = finishFrame;
//            this.sender = sender;
//            this.receiver = receiver;
//            this.duration = duration;
//        }
//    }


    // Use arraylist to apply stimuli structure (InputData, InputData)
    public static final ArrayList<DataStructure.AddCS> addCS = new ArrayList<>();
    public static final ArrayList<DataStructure.Message> message = new ArrayList<>();
    public static final ArrayList<DataStructure.Range> range = new ArrayList<>();
    public static final ArrayList<DataStructure.ChangeAll> changeAll = new ArrayList<>();
    public static final ArrayList<DataStructure.ChangeOne> changeOne = new ArrayList<>();
    public static final ArrayList<DataStructure.RemoveCS> removeCS = new ArrayList<>();

    private final ArrayList<Stimulus> stimuli = new ArrayList<>();
//    private final ArrayList<MsgRouter> router = new ArrayList<>();
    private final ArrayList<String> firefighterNames = new ArrayList<>();
    private final ArrayList<String> AmbulanceNames = new ArrayList<>();

    // Variables for logs
    int patientCounter = 0;
    int fireFighterCounter = 0;
    int ambulanceCounter = 0;
    int currentFirefighterCounter = 0;


    // Initial Values
//    public static final int maxPatient = 294;
    public static final int maxPatient = 200;                // 223
    //    public static final int maxPatient = 100;
//    public static final int maxPatient = 65;
    public static final int maxFireFighter = 12;            // 4
    public static final int maxHospital = 4;
    public static final int maxAmbulance = 4;              // 16
    public static final int maxBridgehead = 4;

    public Map map;
    public MsgRouter router;
    public ArrayList<Patient> patients = new ArrayList<>(maxPatient);               // Patient를 위한 arraylist
    public ArrayList<FireFighter> fireFighters = new ArrayList<>(maxFireFighter);   // FireFighter를 위한 arraylist
    public ArrayList<Hospital> hospitals = new ArrayList<>(maxHospital);            // Hospital를 위한 arraylist
    public ArrayList<Bridgehead> bridgeheads = new ArrayList<>(maxBridgehead);      // Bridgehead를 위한 arraylist
    public ArrayList<Ambulance> ambulances = new ArrayList<>(maxAmbulance);         // Ambulance를 위한 arraylist

    public static final String fireFighterPrefix = "FF";                            // FireFighter의 이름은 "FF"로 시작
//    public static final String ambulancePrefix = "Amb";

//    XSSFWorkbook workbook = new XSSFWorkbook();
//    XSSFSheet statisticsSheet;

//    CellStyle headerStyle;

    // log 작성에 필요한 변수들. 현재는 사용하지 않음
//    long startTime;                                                                 // 프로그램 시작 시간
//    long endTime = 0;                                                               // 프로그램 종료 시간
//    long endFrame = 0;                                                              // 프로그램 종료의 frame 수

    int maxFrame = 0;                                                               // 시뮬레이션 한 번의 최대 frame 수

    public int transferCounter = 0;                                                 // Hospital에서 치료를 마친 환자 수
    public int rescuedPatientCount = 0;                                             // Bridgehead까지 이송 시킨 환자 수

    boolean saveInputData = false;
    public World(int maxFrame, boolean saveInputData) {
        this.maxFrame = maxFrame;
        this.saveInputData = saveInputData;
//        startTime = System.currentTimeMillis();

//        statisticsSheet = workbook.createSheet("statistics");
        //statisticsSheet.trackAllColumnsForAutoSizing();

//        headerStyle = workbook.createCellStyle();
//        headerStyle.setAlignment(HorizontalAlignment.CENTER);
//        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Create map
        map = new Map();
        addChild(map);
        //map.canUpdate(false);

        // Create router
        router = new MsgRouter(this);
        addChild(router);

        createObjects();

        // static stimulus injection technique
//        writeScenario();          // old version
//        writeScenario1();         // baseline
//        writeScenario2();         // interactive simulation test

        for(int i = 0; i < maxFireFighter; ++i) {
            firefighterNames.add(fireFighterPrefix + (i + 1));
        }

//        ArrayList<String> AmbulanceNames = new ArrayList<>();
        for(int i = 0; i < maxAmbulance; ++i) {
            AmbulanceNames.add("Ambulance" + (i + 1));
        }

        // 첫 번째 시뮬레이션에서 저장한 정보를 그 다음 시뮬레이션에서 그대로 이용

        if(!addCS.isEmpty()) {
            for (DataStructure.AddCS data: addCS) {
                switch (data.command) {
                    case "addFireFighter":
                        this.interAddFF(data.frame, data.count);
                        break;
                    case "addAmbulance":
                        this.interAddAmb(data.frame, data.count);
                        break;
                }
            }
        }
        if(!message.isEmpty()) {
            for(DataStructure.Message data: message) {
                switch(data.command) {
                    case "messageDelay" :
                        this.interMsgDelay(data.startFrame, data.finishFrame, data.sender, data.receiver, data.duration);
                        break;
                    case "messageLoss" :
                        this.interMsgDelay(data.startFrame, data.finishFrame, data.sender, data.receiver, data.duration);
                        break;
                }
            }
        }

        if(!range.isEmpty()) {
            for(DataStructure.Range data: range) {
                switch(data.command) {
                    case "speedRange" :
                        this.interSpeedRange(data.frame, data.left, data.top, data.right, data.bottom, data.value);
                        break;
                    case "interSightRange" :
                        this.interSightRange(data.frame, data.left, data.top, data.right, data.bottom, data.value);
                        break;
                    case "interCommunicationRange" :
                        this.interSightRange(data.frame, data.left, data.top, data.right, data.bottom, data.value);
                        break;
                }
            }
        }

        if(!changeAll.isEmpty()) {
            for(DataStructure.ChangeAll data: changeAll) {
                switch(data.command) {
                    case "speedAllAmb" :
                        this.interSpeedAllAmb(data.frame, data.value);
                        break;
                    case "speedAllFF" :
                        this.interSpeedAllFF(data.frame, data.value);
                        break;
                    case "sightAllFF" :
                        this.interSightAllFF(data.frame, data.value);
                        break;
                    case "communicationAllFF" :
                        this.interCommunicationRangeAllFF(data.frame, data.value);
                        break;
                }
            }
        }

        if(!changeOne.isEmpty()) {
            for(DataStructure.ChangeOne data: changeOne) {
                switch(data.command) {
                    case "interSpeedOneAmb" :
                        this.interSpeedOneAmb(data.frame, data.number, data.value);
                        break;
                    case "interSpeedOneFF" :
                        this.interSpeedOneAmb(data.frame, data.number, data.value);
                        break;
                    case "interSightOneFF" :
                        this.interSpeedOneAmb(data.frame, data.number, data.value);
                        break;
                    case "interCommunicationRangeOneFF" :
                        this.interSpeedOneAmb(data.frame, data.number, data.value);
                        break;
                }
            }
        }

        if(!removeCS.isEmpty()) {
            for(DataStructure.RemoveCS data: removeCS) {
                switch(data.command) {
                    case "removeFF" :
                        this.interRemoveFF(data.frame, data.number);
                        break;
                    case "removeAmb" :
                        this.interRemoveAmb(data.frame, data.number);
                        break;
                    case "injuryFF" :
                        this.interInjury(data.frame, data.number);
                        break;
                }
            }
        }


    }

    // Create Objects for visualization
    private void createObjects() {
        createBridgehead();
        createOrganization();
        createHospitals();
        createPatients();
        createAmbulances();
        createFireFighters();
    }

    // Create the patient at random position
    private void createPatients() {
        for (int i = 0; i < maxPatient; i++) {
            Patient patient = new Patient(this, "Patient" + ++patientCounter);
            patients.add(patient);
            patient.setStatus(Patient.Status.random());
            Position randomPosition = null;

            while(true) {
                //randomPosition = GlobalRandom.nextPosition(Map.mapSize.width, Map.mapSize.height);
                randomPosition = GlobalRandom.nextPosition(Map.mapSize.width / 8 + 2, 7 * Map.mapSize.width / 8 -1,
                        Map.mapSize.height / 8 + 2, 7 * Map.mapSize.width / 8 - 1);
                boolean isBridgehead = false;
                for(Bridgehead zone: bridgeheads) {
                    if(zone.isBridgehead(randomPosition)) {
                        isBridgehead = true;
                        break;
                    }
                }
                if(isBridgehead == false) break;
            }
            patient.setPosition(randomPosition);
            this.addChild(patient);
        }
    }

    // Create Firefighters at the edge position
    private void createFireFighters() {
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
        currentFirefighterCounter = fireFighterCounter;                         // count number of firefighters
    }

    // Create Hospitals at the edge position
    private void createHospitals() {
        for(int i = 0; i < maxHospital; ++i) {
            Hospital hospital = new Hospital(this, "Hospital" + (i + 1));
            hospitals.add(hospital);
            addChild(hospital);

//            if(i == 0){
//                hospital.setCapacity(2);
//            } else {
            hospital.setCapacity(10);
//            }
        }
        hospitals.get(0).setPosition(0, 0);
        hospitals.get(1).setPosition(Map.mapSize.width - 1, 0);
        hospitals.get(2).setPosition(0, Map.mapSize.height - 1);
        hospitals.get(3).setPosition(Map.mapSize.width - 1, Map.mapSize.height - 1);
//        hospitals.get(4).setPosition(0, (Map.mapSize.height - 1) / 2);
//        hospitals.get(5).setPosition(Map.mapSize.width - 1, (Map.mapSize.height - 1) / 2);
    }

    // Create the Bridgeheads at the quarter position of the map
    private void createBridgehead() {
        for(int i = 0; i < maxBridgehead; ++i) {
            Bridgehead bridgehead = new Bridgehead(this, "Bridgehead" + (i + 1));
            bridgeheads.add(bridgehead);
            addChild(bridgehead);
        }

//        bridgeheads.get(0).setPosition(new Position(Map.mapSize.width / 4, Map.mapSize.height / 4));
//        bridgeheads.get(1).setPosition(new Position(3 * Map.mapSize.width / 4, Map.mapSize.height / 4));
//        bridgeheads.get(2).setPosition(new Position(3 * Map.mapSize.width / 4, 3 * Map.mapSize.height / 4));
//        bridgeheads.get(3).setPosition(new Position(Map.mapSize.width / 4, 3 * Map.mapSize.height / 4));
        bridgeheads.get(0).setPosition(new Position(Map.mapSize.width / 8, Map.mapSize.height / 8));
        bridgeheads.get(1).setPosition(new Position(7 * Map.mapSize.width / 8, Map.mapSize.height / 8));
        bridgeheads.get(2).setPosition(new Position(7 * Map.mapSize.width / 8, 7 * Map.mapSize.height / 8));
        bridgeheads.get(3).setPosition(new Position(Map.mapSize.width / 8, 7 * Map.mapSize.height / 8));

    }

    int ambulancePositionIndex = 0;

    // Create Ambulances at the edge position
    private void createAmbulances() {
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

    // Create Organization.  Not visible
    private void createOrganization() {
        Organization organization = new Organization(this, "Organization");
        addChild(organization);
    }

    int frameCount = 0;
    public int savedPatientCount = 0;

    // Update at every frame
    @Override
    public void onUpdate() {

        // end condition ==> current frame count is equal or bigger than the max frame count.
        if(frameCount >= maxFrame) {
            canUpdate(false);
            return;
        }

        // end condition ==> every patients are saved
        //if(getPatientCount() == 0 && map.getUnvisitedTileCount() == 0) {
        if(patients.size() == savedPatientCount && map.getUnvisitedTileCount() == 0) {
            canUpdate(false);
//            endTime = System.currentTimeMillis();
//            endFrame = frameCount;
//            printPatientLog(true);
//            printFireFighterLog(true);
            return;
        } else {                                                        // It is not an end condition. continue.
//            printPatientLog(false);
//            printFireFighterLog(false);
//            printAmbulanceLog(false);
            frameCount++;
//            System.out.println("FrameCount: " + frameCount);
        }

        // Execute the stimulus and then remove it. To prevent the duplicate execution.
        ArrayList<Stimulus> mustRemove = new ArrayList<>();
        for(Stimulus stimulus : stimuli) {
            if(stimulus.frame == Time.getFrameCount()) {
                stimulus.execute();
                mustRemove.add(stimulus);
            }
        }
        stimuli.removeAll(mustRemove);

    }



    @Override
    public void onRender(Graphics2D g) {
        Rectangle rect = g.getDeviceConfiguration().getBounds();
        g.setColor(new Color(100, 100, 100));                   // 타일 간의 경계를 표현함. (사각형의 테두리 색깔)
        g.fillRect(rect.x, rect.y, rect.width, rect.height);

        // GUI에 frame을 보여주기 위한 것이었으나 현재는 사용하지 않음.
//        g.setColor(Color.red);
//        g.setFont(new Font("default", Font.BOLD, 16));
//        String strFrameCount = "frameCount: " + frameCount;
//        g.drawChars(strFrameCount.toCharArray(), 0, strFrameCount.length(), 600, 700);
    }

    public Map getMap() {
        return map;
    }


    // patient의 수를 가져오기 위한 함수. 현재는 사용 안함.
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

        router.clear();

//        long nano = System.currentTimeMillis();
//        String date = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss").format(nano);
//        String filePath = "log/RQ1/" + date + ".xlsx";
//
//        ExcelHelper.autoSizeAllColumn(workbook);
//        ExcelHelper.save(workbook, filePath);
    }


    public boolean contains(SoSObject object) {
        return children.contains(object);
    }

    int positionIndex = 0;


    public void addPatient(Position position) {
        Patient patient = new Patient(this, "Patient" + ++patientCounter);
        patients.add(patient);
        patient.setStatus(Patient.Status.random());                                 // serious, wounded.  현재는 wounded만 사용
        if(position == null) {
            Position randomPosition = null;

            while (true) {
                //randomPosition = GlobalRandom.nextPosition(Map.mapSize.width, Map.mapSize.height);
                randomPosition = GlobalRandom.nextPosition(Map.mapSize.width / 8, 7 * Map.mapSize.width / 8,
                        Map.mapSize.height / 8, 7 * Map.mapSize.width / 8);
                boolean isBridgehead = false;
                for (Bridgehead zone : bridgeheads) {
                    if (zone.isBridgehead(randomPosition)) {
                        isBridgehead = true;
                        break;
                    }
                }
                if (isBridgehead == false) break;
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

        //ArrayList<String> firefighterNames = new ArrayList<>();
        for(int i = 0; i < maxFireFighter; ++i) {
            firefighterNames.add(fireFighterPrefix + (i + 1));
        }

//        ArrayList<String> AmbulanceNames = new ArrayList<>();
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


        stimuli.add(new Speed(this, 600, new Range(8, 8, 26, 26), 3.0f));      // smoke 시작 4층 위로
        stimuli.add(new Speed(this, 900, new Range(8, 8, 26, 26), 6.0f));
        stimuli.add(new Speed(this, 2130, new Range(8, 8, 26, 26), 9.0f));      // 4층 위로 fire

        stimuli.add(new Speed(this, 2415, new Range(8, 8, 26, 26), 6.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
        stimuli.add(new Speed(this, 2415, new Range(10, 10, 24, 24), 9.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)

        stimuli.add(new Speed(this, 2655, new Range(10, 10, 24, 24), 6.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
        stimuli.add(new Speed(this, 2655, new Range(12, 12, 22, 22), 9.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)

//        stimuli.add(new Speed(this, 3030, new Range(13, 13, 23, 23), 2.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//        stimuli.add(new Speed(this, 6060, new Range(22, 22, 42, 42), 2.0f));



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
//        stimuli.add(new SightRange(this, 600, firefighterNames, 1));                    // 특정 frame count 이후 전체 FF의 sight range 변화
//        stimuli.add(new SightRange(this, 1000, firefighterNames, 1));
        //stimuli.add(new SightRange(this, 600, new Range(8, 8, 26, 26), 0.3f));      // smoke 시작 4층 위로

        stimuli.add(new SightRange(this, 2130, new Range(8, 8, 26, 26), 0.3f));      // 4층 위로 fire

        stimuli.add(new SightRange(this, 2415, new Range(8, 8, 26, 26), 0.3f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
        stimuli.add(new SightRange(this, 2415, new Range(10, 10, 24, 24), 0.1f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)

        stimuli.add(new SightRange(this, 2655, new Range(10, 10, 24, 24), 0.3f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
        stimuli.add(new SightRange(this, 2655, new Range(12, 12, 22, 22), 0.1f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)



//        stimuli.add(new SightRange(this, 800, firefighterNames, 5));                    // 특정 frame count 이후 전체 FF의 sight range 변화
//        stimuli.add(new SightRange(this, 1600, firefighterNames, 3));                    // 특정 frame count 이후 전체 FF의 sight range 변화
//        stimuli.add(new SightRange(this, 2400, firefighterNames, 1));                    // 특정 frame count 이후 전체 FF의 sight range 변화
//
//
//        stimuli.add(new SightRange(this, 1200, new Range(13, 13, 51, 51), 0.5f));
//        stimuli.add(new SightRange(this, 4830, new Range(14, 14, 50, 50), 0.2f));
//
//
////        stimuli.add(new SightRange(this, 10, new Range(0, 0, 64, 64), 3.0f));           // 특정 frame count 이후 특정 구역의 sight range 변화
////
////        // TODO: communicationRange (FF 관련)
//        stimuli.add(new CommunicationRange(this, 100, "FF1", 7));                               // 특정 frame count 이후 FF1의 communication range 변화
//        stimuli.add(new CommunicationRange(this, 800, firefighterNames, 7));                    // 특정 frame count 이후 전체 FF의 communication range 변화
////        stimuli.add(new CommunicationRange(this, 1600, firefighterNames, 5));                    // 특정 frame count 이후 전체 FF의 communication range 변화
//        stimuli.add(new CommunicationRange(this, 1200, firefighterNames, 7));
//        stimuli.add(new CommunicationRange(this, 2000, firefighterNames, 5));                    // 특정 frame count 이후 전체 FF의 communication range 변화
//        stimuli.add(new CommunicationRange(this, 10, new Range(0, 0, 10, 10), 5.0f));           // 특정 frame count 이후 특정 구역의 communication range 변화
//
        stimuli.add(new CommunicationRange(this, 600, firefighterNames, 3));                    // 특정 frame count 이후 전체 FF의 communication range 변화
        stimuli.add(new CommunicationRange(this, 1000, firefighterNames, 3));
        stimuli.add(new CommunicationRange(this, 2130, firefighterNames, 1));
//
//        // TODO: FireFighter => Patient
//        stimuli.add(new Injury(this, 100, "FF1"));
//        stimuli.add(new Injury(this, 100, "FF2"));
//        stimuli.add(new Injury(this, 100, "FF3"));
//        stimuli.add(new Injury(this, 100, "FF4"));
//        stimuli.add(new Injury(this, 100, "FF5"));
//        stimuli.add(new Injury(this, 100, "FF6"));
//
//        // TODO: remove FireFighter1
//        stimuli.add(new RemoveEntity(this, 100, "FF1", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 110, "FF2", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 120, "FF3", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 130, "FF4", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 140, "FF5", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 150, "FF6", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 160, "FF7", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 170, "FF8", this::removeCS));
//
//        // TODO: remove Ambulance1
//        stimuli.add(new RemoveEntity(this, 100, "Ambulance1", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 120, "Ambulance2", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 130, "Ambulance3", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 140, "Ambulance4", this::removeCS));
//
//        // TODO: add FireFighter
        for(int i = 0; i < 1; i++) {
            stimuli.add(new AddEntity(this, 275, this::addFireFighter));
            stimuli.add(new AddEntity(this, 275, this::addFireFighter));
        }
        for(int i = 0; i < 1; i++) {
            stimuli.add(new AddEntity(this, 315, this::addFireFighter));
            stimuli.add(new AddEntity(this, 315, this::addFireFighter));
        }
        for(int i = 0; i < 1; i++) {
            stimuli.add(new AddEntity(this, 390, this::addFireFighter));
            stimuli.add(new AddEntity(this, 390, this::addFireFighter));
        }
        for(int i = 0; i < 1; i++) {
            stimuli.add(new AddEntity(this, 435, this::addFireFighter));
            stimuli.add(new AddEntity(this, 435, this::addFireFighter));
            stimuli.add(new AddEntity(this, 435, this::addFireFighter));
            stimuli.add(new AddEntity(this, 435, this::addFireFighter));
            stimuli.add(new AddEntity(this, 435, this::addFireFighter));
        }

        for(int i = 0; i < 1; i++) {
            stimuli.add(new AddEntity(this, 465, this::addFireFighter));
            stimuli.add(new AddEntity(this, 465, this::addFireFighter));
            stimuli.add(new AddEntity(this, 465, this::addFireFighter));
            stimuli.add(new AddEntity(this, 465, this::addFireFighter));
            stimuli.add(new AddEntity(this, 465, this::addFireFighter));
        }

        for(int i = 0; i < 1; i++) {
            stimuli.add(new AddEntity(this, 495, this::addFireFighter));
            stimuli.add(new AddEntity(this, 495, this::addFireFighter));
            stimuli.add(new AddEntity(this, 495, this::addFireFighter));
            stimuli.add(new AddEntity(this, 495, this::addFireFighter));
            stimuli.add(new AddEntity(this, 495, this::addFireFighter));
        }

        for(int i = 0; i < 1; i++) {
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
        }

        // TODO: add Ambulance
        stimuli.add(new AddEntity(this, 200, this::addAmbulance));
        stimuli.add(new AddEntity(this, 210, this::addAmbulance));
        stimuli.add(new AddEntity(this, 220, this::addAmbulance));
        stimuli.add(new AddEntity(this, 230, this::addAmbulance));

//        // TODO: Msg Delay
//        // CS && CS
//        router.add(new Delay(1, 3000, "FF", "FF", 3000));   // 1frame 부터 20프레임까지 적용
//        router.add(new Delay(10, 100, "FF", "FF", 20));
//        router.add(new Delay(10, 200, "All", "ALL", 20));
//
//        router.add(new Delay(1, 20,"Ambulance", "Org", 20));
//
//        // Entity && CS
//        router.add(new Delay(1, 20, "FF1", "FF", 20));
//        router.add(new Delay(1, 20, "Ambulance1", "Org", 20));
//
//        // CS && Entity
//        router.add(new Delay(1, 20, "FF", "FF5", 20));
//        router.add(new Delay(1, 20, "Org", "Ambulance1", 20));

//        // Entity && Entity
//        router.add(new Delay(1, 20, "FF1", "FF5", 20));

        // TODO: Msg Loss
        // CS && CS
//        router.add(new Loss(1, 3000, "FF", "FF"));
//        router.add(new Loss(1, 20, "FF", "FF"));
//        router.add(new Loss(10, 200, "All", "All"));
//
//        router.add(new Loss(1, 20, "Ambulance", "Org"));
//
//        // Entity && CS
//        router.add(new Loss(1, 20, "FF1", "FF"));
//        router.add(new Loss(1, 20, "Ambulance1", "Org"));
//
//        // CS && Entity
//        router.add(new Loss(1, 20, "FF", "FF5"));
//        router.add(new Loss(1, 20, "Org", "Ambulance1"));
//
//        // Entity && Entity
//        router.add(new Loss(1, 20, "FF1", "FF5"));

    }       // initial version


    private void writeScenario1() {

        for(int i = 0; i < maxFireFighter; ++i) {
            firefighterNames.add(fireFighterPrefix + (i + 1));
        }

        for(int i = 0; i < maxAmbulance; ++i) {
            AmbulanceNames.add("Ambulance" + (i + 1));
        }

        // TODO: speed
        stimuli.add(new Speed(this, 600, new Range(8, 8, 26, 26), 3.0f));      // smoke 시작 4층 위로
        stimuli.add(new Speed(this, 900, new Range(8, 8, 26, 26), 6.0f));
        stimuli.add(new Speed(this, 2130, new Range(8, 8, 26, 26), 9.0f));      // 4층 위로 fire

        stimuli.add(new Speed(this, 2415, new Range(8, 8, 26, 26), 6.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
        stimuli.add(new Speed(this, 2415, new Range(10, 10, 24, 24), 9.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)

        stimuli.add(new Speed(this, 2655, new Range(10, 10, 24, 24), 6.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
        stimuli.add(new Speed(this, 2655, new Range(12, 12, 22, 22), 9.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)

//        stimuli.add(new Speed(this, 100, AmbulanceNames, 10));                              // 특정 frame count 이후 Ambulance 전체 move speed 변경
//        stimuli.add(new Speed(this, 100, "Ambulance1", 7));
//
//        // TODO: sightRange
//        stimuli.add(new SightRange(this, 100, "FF1", 5));                               // 특정 frame count 이후 FF1의 sight range 변화
//        stimuli.add(new SightRange(this, 600, firefighterNames, 1));                    // 특정 frame count 이후 전체 FF의 sight range 변화
        //stimuli.add(new SightRange(this, 600, new Range(8, 8, 26, 26), 0.3f));      // smoke 시작 4층 위로

        stimuli.add(new SightRange(this, 600, new Range(8, 8, 26, 26), 0.6f));
        stimuli.add(new SightRange(this, 2130, new Range(8, 8, 26, 26), 0.3f));      // 4층 위로 fire

        stimuli.add(new SightRange(this, 2415, new Range(8, 8, 26, 26), 0.3f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
        stimuli.add(new SightRange(this, 2415, new Range(10, 10, 24, 24), 0.1f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)

        stimuli.add(new SightRange(this, 2655, new Range(10, 10, 24, 24), 0.3f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
        stimuli.add(new SightRange(this, 2655, new Range(12, 12, 22, 22), 0.1f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)


////        // TODO: communicationRange (FF 관련)     range는 그냥 default로 유지됐다고 가정
//        stimuli.add(new CommunicationRange(this, 100, "FF1", 7));                               // 특정 frame count 이후 FF1의 communication range 변화
//        stimuli.add(new CommunicationRange(this, 800, firefighterNames, 7));                    // 특정 frame count 이후 전체 FF의 communication range 변화
//        stimuli.add(new CommunicationRange(this, 10, new Range(0, 0, 10, 10), 5.0f));           // 특정 frame count 이후 특정 구역의 communication range 변화



//        stimuli.add(new CommunicationRange(this, 600, firefighterNames, 3));                    // 특정 frame count 이후 전체 FF의 communication range 변화
//        stimuli.add(new CommunicationRange(this, 1000, firefighterNames, 3));
//        stimuli.add(new CommunicationRange(this, 2130, firefighterNames, 1));


//        // TODO: FireFighter => Patient
//        stimuli.add(new Injury(this, 100, "FF1"));
//        stimuli.add(new Injury(this, 100, "FF2"));
//        stimuli.add(new Injury(this, 100, "FF3"));
//        stimuli.add(new Injury(this, 100, "FF4"));
//        stimuli.add(new Injury(this, 100, "FF5"));
//        stimuli.add(new Injury(this, 100, "FF6"));
//
//        // TODO: remove FireFighter
//        stimuli.add(new RemoveEntity(this, 600, "FF1", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF2", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF3", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF4", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF5", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF6", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF7", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF8", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF9", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF10", this::removeCS));



//
//        // TODO: remove Ambulance
//        stimuli.add(new RemoveEntity(this, 100, "Ambulance1", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 120, "Ambulance2", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 130, "Ambulance3", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 140, "Ambulance4", this::removeCS));
//
//        // TODO: add FireFighter
        for(int i = 0; i < 2; i++) {
            stimuli.add(new AddEntity(this, 275, this::addFireFighter));
            stimuli.add(new AddEntity(this, 315, this::addFireFighter));
            stimuli.add(new AddEntity(this, 390, this::addFireFighter));
        }
        for(int i = 0; i < 5; i++) {
            stimuli.add(new AddEntity(this, 435, this::addFireFighter));
            stimuli.add(new AddEntity(this, 465, this::addFireFighter));
            stimuli.add(new AddEntity(this, 495, this::addFireFighter));
        }
        for(int i = 0; i < 15; i++) {
            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
        }
//        for(int i = 0; i < 10; i++) {
//            stimuli.add(new AddEntity(this, 600, this::addFireFighter));
//        }

        // TODO: add Ambulance
//        for(int i = 0; i < 2; i++) {
//            stimuli.add(new AddEntity(this, 300, this::addAmbulance));
//            stimuli.add(new AddEntity(this, 400, this::addAmbulance));
//            stimuli.add(new AddEntity(this, 800, this::addAmbulance));
//            stimuli.add(new AddEntity(this, 1000, this::addAmbulance));
//        }
//        // TODO: Msg Delay
//        // CS && CS
//        router.add(new Delay(900, 2130, "FF", "FF", 75));
        router.add(new Delay(600, 2130, "FF", "FF", 75));       // 5분간 delay
        router.add(new Delay(2130, 2655, "FF", "FF", 150));     // 10분간 delay


//        router.add(new Delay(600, 2655, "FF", "FF", 100));       // 5분간 delay


//        router.add(new Loss(900, 2130, "FF", "FF"));
//        router.add(new Loss(2130, 2655, "FF", "FF"));
//        router.add(new Delay(10, 100, "FF", "FF", 20));
//        router.add(new Delay(10, 200, "All", "ALL", 20));
//        router.add(new Delay(1, 20,"Ambulance", "Org", 20));
//
//        // Entity && CS
//        router.add(new Delay(1, 20, "FF1", "FF", 20));
//        router.add(new Delay(1, 20, "Ambulance1", "Org", 20));
//
//        // CS && Entity
//        router.add(new Delay(1, 20, "FF", "FF5", 20));
//        router.add(new Delay(1, 20, "Org", "Ambulance1", 20));

//        // Entity && Entity
//        router.add(new Delay(1, 20, "FF1", "FF5", 20));

        // TODO: Msg Loss 없었다고 가정하자.
        // CS && CS
//        router.add(new Loss(2130, 3000, "FF", "FF"));
//        router.add(new Loss(2130, 3000, "ALL", "ALL"));
//        router.add(new Loss(1, 3000, "FF", "FF"));
//        router.add(new Loss(1, 20, "FF", "FF"));
//        router.add(new Loss(10, 200, "All", "All"));
//        router.add(new Loss(1, 20, "Ambulance", "Org"));
//
//        // Entity && CS
//        router.add(new Loss(1, 20, "FF1", "FF"));
//        router.add(new Loss(1, 20, "Ambulance1", "Org"));
//
//        // CS && Entity
//        router.add(new Loss(1, 20, "FF", "FF5"));
//        router.add(new Loss(1, 20, "Org", "Ambulance1"));
//
//        // Entity && Entity
//        router.add(new Loss(1, 20, "FF1", "FF5"));

    }       // baseline (message delay 만 존재(All - All)


    private void writeScenario2() {

        for(int i = 0; i < maxFireFighter; ++i) {
            firefighterNames.add(fireFighterPrefix + (i + 1));
        }

        for(int i = 0; i < maxAmbulance; ++i) {
            AmbulanceNames.add("Ambulance" + (i + 1));
        }

//        // TODO: speed
//        stimuli.add(new Speed(this, 600, new Range(8, 8, 26, 26), 3.0f));      // smoke 시작 4층 위로
//        stimuli.add(new Speed(this, 900, new Range(8, 8, 26, 26), 6.0f));
//        stimuli.add(new Speed(this, 2130, new Range(8, 8, 26, 26), 9.0f));      // 4층 위로 fire
//
//        stimuli.add(new Speed(this, 2415, new Range(8, 8, 26, 26), 6.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//        stimuli.add(new Speed(this, 2415, new Range(10, 10, 24, 24), 9.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//
//        stimuli.add(new Speed(this, 2655, new Range(10, 10, 24, 24), 6.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//        stimuli.add(new Speed(this, 2655, new Range(12, 12, 22, 22), 9.0f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)

//        stimuli.add(new Speed(this, 100, AmbulanceNames, 10));                              // 특정 frame count 이후 Ambulance 전체 move speed 변경
//        stimuli.add(new Speed(this, 100, "Ambulance1", 7));
//
//        // TODO: sightRange
//        stimuli.add(new SightRange(this, 100, "FF1", 5));                               // 특정 frame count 이후 FF1의 sight range 변화
//        stimuli.add(new SightRange(this, 600, firefighterNames, 1));                    // 특정 frame count 이후 전체 FF의 sight range 변화
        //stimuli.add(new SightRange(this, 600, new Range(8, 8, 26, 26), 0.3f));      // smoke 시작 4층 위로

//        stimuli.add(new SightRange(this, 600, new Range(8, 8, 26, 26), 0.6f));
//        stimuli.add(new SightRange(this, 2130, new Range(8, 8, 26, 26), 0.3f));      // 4층 위로 fire
//
//        stimuli.add(new SightRange(this, 2415, new Range(8, 8, 26, 26), 0.3f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//        stimuli.add(new SightRange(this, 2415, new Range(10, 10, 24, 24), 0.1f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//
//        stimuli.add(new SightRange(this, 2655, new Range(10, 10, 24, 24), 0.3f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)
//        stimuli.add(new SightRange(this, 2655, new Range(12, 12, 22, 22), 0.1f));      // 100 frame 부터 16, 16, 48, 48 위치에서 이속 감소 (3배 감소)


////        // TODO: communicationRange (FF 관련)     range는 그냥 default로 유지됐다고 가정
//        stimuli.add(new CommunicationRange(this, 100, "FF1", 7));                               // 특정 frame count 이후 FF1의 communication range 변화
//        stimuli.add(new CommunicationRange(this, 800, firefighterNames, 7));                    // 특정 frame count 이후 전체 FF의 communication range 변화
//        stimuli.add(new CommunicationRange(this, 10, new Range(0, 0, 10, 10), 5.0f));           // 특정 frame count 이후 특정 구역의 communication range 변화



//        stimuli.add(new CommunicationRange(this, 600, firefighterNames, 3));                    // 특정 frame count 이후 전체 FF의 communication range 변화
//        stimuli.add(new CommunicationRange(this, 1000, firefighterNames, 3));
//        stimuli.add(new CommunicationRange(this, 2130, firefighterNames, 1));


//        // TODO: FireFighter => Patient
//        stimuli.add(new Injury(this, 100, "FF1"));
//        stimuli.add(new Injury(this, 100, "FF2"));
//        stimuli.add(new Injury(this, 100, "FF3"));
//        stimuli.add(new Injury(this, 100, "FF4"));
//        stimuli.add(new Injury(this, 100, "FF5"));
//        stimuli.add(new Injury(this, 100, "FF6"));
//
//        // TODO: remove FireFighter
//        stimuli.add(new RemoveEntity(this, 600, "FF1", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF2", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF3", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF4", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF5", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF6", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF7", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF8", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF9", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 600, "FF10", this::removeCS));



//
//        // TODO: remove Ambulance
//        stimuli.add(new RemoveEntity(this, 100, "Ambulance1", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 120, "Ambulance2", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 130, "Ambulance3", this::removeCS));
//        stimuli.add(new RemoveEntity(this, 140, "Ambulance4", this::removeCS));
//
//        // TODO: add FireFighter
        for(int i = 0; i < 40; i++) {
            stimuli.add(new AddEntity(this, 100, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 315, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 390, this::addFireFighter));
        }
//        for(int i = 0; i < 5; i++) {
//            stimuli.add(new AddEntity(this, 435, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 465, this::addFireFighter));
//            stimuli.add(new AddEntity(this, 495, this::addFireFighter));
//        }
//        for(int i = 0; i < 15; i++) {
//            stimuli.add(new AddEntity(this, 990, this::addFireFighter));
//        }
//        for(int i = 0; i < 10; i++) {
//            stimuli.add(new AddEntity(this, 600, this::addFireFighter));
//        }

        // TODO: add Ambulance
//        for(int i = 0; i < 2; i++) {
//            stimuli.add(new AddEntity(this, 300, this::addAmbulance));
//            stimuli.add(new AddEntity(this, 400, this::addAmbulance));
//            stimuli.add(new AddEntity(this, 800, this::addAmbulance));
//            stimuli.add(new AddEntity(this, 1000, this::addAmbulance));
//        }
//        // TODO: Msg Delay
//        // CS && CS
//        router.add(new Delay(900, 2130, "FF", "FF", 75));
//        router.add(new Delay(600, 2130, "FF", "FF", 75));       // 5분간 delay
//        router.add(new Delay(2130, 2655, "FF", "FF", 150));     // 10분간 delay


//        router.add(new Delay(600, 2655, "FF", "FF", 100));       // 5분간 delay


//        router.add(new Loss(900, 2130, "FF", "FF"));
//        router.add(new Loss(2130, 2655, "FF", "FF"));
//        router.add(new Delay(10, 100, "FF", "FF", 20));
//        router.add(new Delay(10, 200, "All", "ALL", 20));
//        router.add(new Delay(1, 20,"Ambulance", "Org", 20));
//
//        // Entity && CS
//        router.add(new Delay(1, 20, "FF1", "FF", 20));
//        router.add(new Delay(1, 20, "Ambulance1", "Org", 20));
//
//        // CS && Entity
//        router.add(new Delay(1, 20, "FF", "FF5", 20));
//        router.add(new Delay(1, 20, "Org", "Ambulance1", 20));

//        // Entity && Entity
//        router.add(new Delay(1, 20, "FF1", "FF5", 20));

        // TODO: Msg Loss 없었다고 가정하자.
        // CS && CS
//        router.add(new Loss(2130, 3000, "FF", "FF"));
//        router.add(new Loss(2130, 3000, "ALL", "ALL"));
//        router.add(new Loss(1, 3000, "FF", "FF"));
//        router.add(new Loss(1, 20, "FF", "FF"));
//        router.add(new Loss(10, 200, "All", "All"));
//        router.add(new Loss(1, 20, "Ambulance", "Org"));
//
//        // Entity && CS
//        router.add(new Loss(1, 20, "FF1", "FF"));
//        router.add(new Loss(1, 20, "Ambulance1", "Org"));
//
//        // CS && Entity
//        router.add(new Loss(1, 20, "FF", "FF5"));
//        router.add(new Loss(1, 20, "Org", "Ambulance1"));
//
//        // Entity && Entity
//        router.add(new Loss(1, 20, "FF1", "FF5"));

    }       // baseline (message delay 만 존재(All - All)

    // remove CS stimulus를 적용하기 위한 함수
    void removeCS(String csName) {
        SoSObject obj = findObject(csName);
        if(obj == null) return;


        CS cs = (CS)obj;
        cs.visible(false);
        cs.canUpdate(false);
        cs.currentAction.name = "Removed";

        // Remove 하려는 CS가 Firefighter인 경우
        // 현재의 action 상태에 따라 다르게 처리한다.
        if(cs instanceof FireFighter) {
            FireFighter ff = (FireFighter) cs;
            if (cs.currentAction instanceof FireFighterFirstAid) {
                FireFighterFirstAid action = (FireFighterFirstAid) cs.currentAction;
                action.targetPatient.assignedFireFighter = null;
                action.targetPatient.isSaved = false;
                map.add(action.targetPatient);
                action.targetPatient.position.set(cs.position);
                ff.patientsMemory.remove(action.targetPatient);
                ff.patientsMemory.add(action.targetPatient);
//                fireFighters.remove(ff);
                firefighterNames.remove(ff);
                fireFighterCounter--;
            } else if (cs.currentAction instanceof FireFighterSelectTransferDestination) {
                FireFighterSelectTransferDestination action = (FireFighterSelectTransferDestination) cs.currentAction;
                action.targetPatient.isSaved = false;
                action.targetPatient.assignedFireFighter = null;
                addChild(action.targetPatient);
                map.add(action.targetPatient);
                action.targetPatient.position.set(cs.position);
                ff.patientsMemory.remove(action.targetPatient);
                ff.patientsMemory.add(action.targetPatient);
//                fireFighters.remove(ff);
                firefighterNames.remove(ff);
                fireFighterCounter--;
            } else if (cs.currentAction instanceof FireFighterTransferToBridgehead) {
                FireFighterTransferToBridgehead action = (FireFighterTransferToBridgehead) cs.currentAction;
                action.targetPatient.isSaved = false;
                action.targetPatient.assignedFireFighter = null;
                addChild(action.targetPatient);
                map.add(action.targetPatient);
                action.targetPatient.position.set(cs.position);
                ff.patientsMemory.remove(action.targetPatient);
                ff.patientsMemory.add(action.targetPatient);
//                fireFighters.remove(ff);
                firefighterNames.remove(ff);
                fireFighterCounter--;
            } else if (cs.currentAction instanceof FireFighterTransferToHospital) {
                FireFighterTransferToHospital action = (FireFighterTransferToHospital) cs.currentAction;
                action.targetPatient.assignedFireFighter = null;
                action.targetPatient.isSaved = false;
                addChild(action.targetPatient);
                map.add(action.targetPatient);
                action.targetPatient.position.set(cs.position);
                ff.patientsMemory.remove(action.targetPatient);
                ff.patientsMemory.add(action.targetPatient);
//                fireFighters.remove(ff);
                firefighterNames.remove(ff);
                fireFighterCounter--;
            } else if (cs.currentAction instanceof FireFighterMoveToPatient) {
                FireFighterMoveToPatient action = (FireFighterMoveToPatient) cs.currentAction;
                action.targetPatient.assignedFireFighter = null;
                map.add(action.targetPatient);
                ff.patientsMemory.remove(action.targetPatient);
                ff.patientsMemory.add(action.targetPatient);
//                fireFighters.remove(ff);
                firefighterNames.remove(ff);
                fireFighterCounter--;
            } else if (cs.currentAction instanceof FireFighterSearch) {
                FireFighterSearch action = (FireFighterSearch) cs.currentAction;
//                action.targetPatient.assignedFireFighter = null;
//                map.add(action.targetPatient);
//                ff.patientsMemory.remove(action.targetPatient);
//                ff.patientsMemory.add(action.targetPatient);
//                fireFighters.remove(ff);
                firefighterNames.remove(ff);
                fireFighterCounter--;
            }
        }
        // Remove 하려는 CS가 Ambulance인 경우
        // 현재의 action 상태에 따라 다르게 처리한다.
        else if(cs instanceof Ambulance) {
            Ambulance ambulance = (Ambulance) cs;

            if(cs.currentAction instanceof AmbulanceFree) {
                AmbulanceFree action = (AmbulanceFree) cs.currentAction;
                AmbulanceNames.remove(ambulance);
                ambulanceCounter--;
            } else if(cs.currentAction instanceof AmbulanceMoveTobridgehead) {
                AmbulanceMoveTobridgehead action = (AmbulanceMoveTobridgehead) cs.currentAction;
                AmbulanceNames.remove(ambulance);
                ambulanceCounter--;
            } else if(cs.currentAction instanceof AmbulanceSearch) {
                AmbulanceSearch action = (AmbulanceSearch) cs.currentAction;
                AmbulanceNames.remove(ambulance);
                ambulanceCounter--;
            } else if(cs.currentAction instanceof AmbulanceTransferToHospital) {
                AmbulanceTransferToHospital action = (AmbulanceTransferToHospital) cs.currentAction;

                action.patient.assignedFireFighter = null;
                action.patient.isSaved = false;
//                ambulance.moveTo(action.hospital.position);
//                ambulance.setPosition(action.hospital.position);
//                action.hospital.hospitalize(action.patient);
//                transferCounter++;
                addChild(action.patient);
                action.patient.position.set(cs.position);
                map.add(action.patient);
//                action.patient.position.set(cs.position);

                AmbulanceNames.remove(ambulance);
                ambulanceCounter--;

            }
        }

        removeChild(obj);               // child에서도 remove함.
    }

    // Firefighter를 추가하는 stimulus를 적용하기 위한 함수
    void addFireFighter() {
        Position[] positions = new Position[]{
                new Position(0, 0),
                new Position(Map.mapSize.width - 1, 0),
                new Position(Map.mapSize.width - 1, Map.mapSize.height - 1),
                new Position(0, Map.mapSize.height - 1)
        };
        int current = 0;
        FireFighter ff = new FireFighter(this, fireFighterPrefix + ++currentFirefighterCounter);
        fireFighterCounter++;
        current = currentFirefighterCounter;
        fireFighters.add(ff);
        firefighterNames.add(fireFighterPrefix + current);

        ff.setPosition(positions[positionIndex++]);
        if (positionIndex >= 4)
            positionIndex = 0;
        addChild(ff);
    }



    // Ambulance를 추가하는 stimulus를 적용하기 위한 함수
    private void addAmbulance() {
        Position[] positions = new Position[]{
                new Position(0, 0),
                new Position(Map.mapSize.width - 1, 0),
                new Position(Map.mapSize.width - 1, Map.mapSize.height - 1),
                new Position(0, Map.mapSize.height - 1)
        };
        int current = 0;
        Ambulance ambulance = new Ambulance(this, "Ambulance" + ++ambulanceCounter);
        current = ambulanceCounter;
        ambulances.add(ambulance);
        AmbulanceNames.add("Ambulance" + current);
        ambulance.setPosition(positions[positionIndex++]);
        if (positionIndex >= 4)
            positionIndex = 0;
        addChild(ambulance);
    }

    // Interactive simulation에서 firefighter를 추가하는 것을 처리하는 함수
    public void interAddFF(int frame, int count) {
        if(saveInputData) {
            addCS.add(new DataStructure.AddCS("addFireFighter", frame, count));
        }
        for(int i = 0; i < count; ++i) {
            Stimulus stimulus = new AddEntity(this, frame, this::addFireFighter);
            stimuli.add(stimulus);
        }
    }

    // Interactive simulation에서 ambulance를 추가하는 것을 처리하는 함수
    public void interAddAmb(int frame, int count) {
        if(saveInputData) {
            addCS.add(new DataStructure.AddCS("addAmbulance", frame, count));
        }
        for(int i = 0; i < count; ++i) {
            Stimulus stimulus = new AddEntity(this, frame, this::addAmbulance);
            stimuli.add(stimulus);
        }
    }

    public void interMsgDelay(int startFrame, int finishFrame, String sender, String receiver, int duration) {
        if(saveInputData) {
            message.add(new DataStructure.Message("messageDelay", startFrame, finishFrame, sender, receiver, duration));
        }
        router.add(new Delay(startFrame, finishFrame, sender, receiver, duration));

    }

    public void interMsgLoss(int startFrame, int finishFrame, String sender, String receiver) {
        if(saveInputData) {
            message.add(new DataStructure.Message("messageLoss", startFrame, finishFrame, sender, receiver,0));
        }
        router.add(new Loss(startFrame, finishFrame, sender, receiver));
    }

    //Speed
    public void interSpeedRange(int frame, int left, int top, int right, int bottom, Object value) {
        if(saveInputData) {
            range.add(new DataStructure.Range("speedRange", frame, left, top, right, bottom, value));
        }
        Stimulus stimulus = new Speed(this, frame, new Range(left, top, right, bottom), value);
        stimuli.add(stimulus);
    }

    public void interSpeedAllAmb(int frame, Object value) {
        if(saveInputData) {
            changeAll.add(new DataStructure.ChangeAll("speedAllAmb", frame, value));
        }
        Stimulus stimulus = new Speed(this, frame, AmbulanceNames, value);
        stimuli.add(stimulus);
    }

    public void interSpeedAllFF(int frame, Object value) {
        if(saveInputData) {
            changeAll.add(new DataStructure.ChangeAll("speedAllFF", frame, value));
        }
        Stimulus stimulus = new Speed(this, frame, firefighterNames, value);
        stimuli.add(stimulus);

    }

    public void interSpeedOneAmb(int frame, int number, Object value) {
        if(saveInputData) {
            changeOne.add(new DataStructure.ChangeOne("speedOneAmb", frame, number, value));
        }
        Stimulus stimulus = new Speed(this, frame, "Ambulance" + number, value);
        stimuli.add(stimulus);

    }

    public void interSpeedOneFF(int frame, int number, Object value) {
        if(saveInputData) {
            changeOne.add(new DataStructure.ChangeOne("speedOneFF", frame, number, value));
        }
        Stimulus stimulus = new Speed(this, frame, "FF" + number, value);
        stimuli.add(stimulus);
    }

    //Sight
    public void interSightRange(int frame, int left, int top, int right, int bottom, Object value) {
        if(saveInputData) {
            range.add(new DataStructure.Range("sightRange", frame, left, top, right, bottom, value));
        }
        Stimulus stimulus = new SightRange(this, frame, new Range(left, top, right, bottom), value);
        stimuli.add(stimulus);
    }

    public void interSightAllFF(int frame, Object value) {
        if(saveInputData) {
            changeAll.add(new DataStructure.ChangeAll("sightAllFF", frame, value));
        }
        Stimulus stimulus = new SightRange(this, frame, firefighterNames, value);
        stimuli.add(stimulus);
    }

    public void interSightOneFF(int frame, int number, Object value) {
        if(saveInputData) {
            changeOne.add(new DataStructure.ChangeOne("sightOneFF", frame, number, value));
        }
        Stimulus stimulus = new SightRange(this, frame, "FF" + number, value);
        stimuli.add(stimulus);
    }

    //Remove
    public void interRemoveFF(int frame, int number) {
        if(saveInputData) {
            removeCS.add(new DataStructure.RemoveCS("removeFF", frame, number));
        }
        Stimulus stimulus = new RemoveEntity(this, frame, "FF" + number, this::removeCS);
        stimuli.add(stimulus);
    }

    public void interRemoveAmb(int frame, int number) {
        if(saveInputData) {
            removeCS.add(new DataStructure.RemoveCS("removeAmb", frame, number));
        }
        Stimulus stimulus = new RemoveEntity(this, frame, "Ambulance" + number, this::removeCS);
        stimuli.add(stimulus);
    }

    //Communication Range
    public void interCommunicationRange(int frame, int left, int top, int right, int bottom, Object value) {
        if(saveInputData) {
            range.add(new DataStructure.Range("communicationRange", frame, left, top, right, bottom, value));
        }
        Stimulus stimulus = new CommunicationRange(this, frame, new Range(left, top, right, bottom), value);
        stimuli.add(stimulus);
    }

    public void interCommunicationRangeAllFF(int frame, Object value) {
        if(saveInputData) {
            changeAll.add(new DataStructure.ChangeAll("communicationAllFF", frame, value));
        }
        Stimulus stimulus = new CommunicationRange(this, frame, firefighterNames, value);
        stimuli.add(stimulus);
    }

    public void interCommunicationRangeOneFF(int frame, int number, Object value) {
        if(saveInputData) {
            changeOne.add(new DataStructure.ChangeOne("communicationOneFF", frame, number, value));
        }
        Stimulus stimulus = new CommunicationRange(this, frame, "FF" + number, value);
        stimuli.add(stimulus);

    }

    //Injury
    public void interInjury(int frame, int number) {
        if(saveInputData) {
            removeCS.add(new DataStructure.RemoveCS("injuryFF", frame, number));
        }
        Stimulus stimulus = new Injury(this, frame, "FF" + number);
        stimuli.add(stimulus);
    }



    public boolean isFinished() {
        return !_canUpdate;
    }
    public float getSavedRate() {
        return savedPatientCount / (float)maxPatient;
    }
}