package simvasos.scenario.mciresponse;

import simvasos.scenario.faultscenario.FaultWorld;
import simvasos.scenario.faultscenario.Statistics;
import simvasos.simulation.Simulator;
import simvasos.simulation.analysis.Snapshot;
import simvasos.simulation.component.Scenario;
import simvasos.scenario.mciresponse.MCIResponseScenario.SoSType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MCIResponseRunner {



    public static void main(String[] args) throws InterruptedException {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String testSession = "ABCPlus_AllTypes";

        // 테스트 커밋
        // 수정 --> Tick의 총 횟수
        //int endTick = 7500; // 8000
        int endTick = 8000;

        int minTrial = 1;

        // 수정 --> 각 케이스별로 돌리는 횟수
        //int maxTrial = 100;
        int maxTrial = 1;

        try {
            File simulationLogFile = new File(String.format("traces/" + testSession + "/" + testSession + "_simulation_logs.csv"));

            BufferedWriter simulationLogWriter = new BufferedWriter(new FileWriter(simulationLogFile, true));

            File statisticsFile = new File("traces/ABCPlus_AllTypes/statistics.csv");
            BufferedWriter statisticsWriter = new BufferedWriter(new FileWriter(statisticsFile, false));
            Statistics statistics = new Statistics();

//            simulationLogWriter.write("nPatient,nFireFighter,SoSType,Duration,MessageCount");
//            simulationLogWriter.newLine();

            // nPatient, nFireFighter

            // 수정 (원본)
            //SoSType[] targetTypeArray = {SoSType.Virtual, SoSType.Collaborative, SoSType.Acknowledged, SoSType.Directed};
            SoSType[] targetTypeArray = {SoSType.Collaborative, SoSType.Acknowledged};
            //SoSType[] targetTypeArray = {SoSType.Collaborative};

            // 수정 (원본)
//            int[] nPatientArray = {50, 100, 150, 200, 250};
//            int[] nFireFighterArray = {2, 5, 10, 25, 50};

            int[] nPatientArray = {50, 100, 150, 200, 250};
            int[] nFireFighterArray = {5, 20, 50};

            //int[] nPatientArray = {15};
            //int[] nFireFighterArray = {2};

            ArrayList<Integer> delays = new ArrayList<Integer>();
            //int[] delays = {0, 50, 100, 150, 200, 500, 1000};
            for(int i = 0; i < 5; ++i)
            {
                delays.add(i * 50);
            }
            //int[] delays = {0, 50, 300};
            //int[] delays = {0};

            ArrayList<Snapshot> trace;
            long startTime;
            long duration;      // 매 케이스 별로 시스템 실행 시간
            long durationSum;   // 각각의 케이스별 시스템 실행 시간의 총합
            int messageCnt;
            int messageCntSum;


            for (int nPatient : nPatientArray) {
                for (int nFireFighter : nFireFighterArray) {
                    for (int i = minTrial - 1; i < maxTrial; i++) {
//                        durationSum = 0;
//                        messageCntSum = 0;
                        for (SoSType sostype : targetTypeArray) {
                            for(int delay : delays) {
                                System.out.println("Patient: " + nPatient + ", Firefighter: " + nFireFighter + ", SoS: " + sostype+ ", Delay time: " + delay);
                                System.out.println(datetimeFormat.format(new Date()));

                                FaultWorld world = new FaultWorld(sostype, nPatient);
                                world.setDelay(delay);
                                Scenario scenario = new MCIResponseScenario(sostype, nFireFighter, 0, 0, world);

                                durationSum = 0;
                                messageCntSum = 0;
                                world.setSeed(new Random().nextLong());
                                //for (int i = minTrial - 1; i <= maxTrial; i++) {        // 왜인지는 모르겠지만 maxtrial보다 한번 더 돌리네...
                                //world.setSeed(new Random().nextLong());
                                ((MCIResponseWorld) world).setSoSType(sostype);

                                startTime = System.currentTimeMillis();

                                Simulator.setPatient(nPatient);
                                trace = Simulator.execute(world, endTick);      // 여기서 들어가서 fault를 넣어야 할듯. 아니면 message쪽까지 가서 해야하나?

//                                if (i == minTrial - 1)                          // 왜인지 모르지만 첫번째 실행은 건너뛴다...
//                                    continue;

                                duration = (System.currentTimeMillis() - startTime);
                                durationSum += duration;
                                messageCnt = (int) world.getCurrentSnapshot().getProperties().get(1).value;
                                messageCntSum += messageCnt;

                                simulationLogWriter.write(nPatient + "," + nFireFighter + "," + sostype.toString() + "," + duration + "," + messageCnt);
                                simulationLogWriter.newLine();

                                writeTrace(trace, String.format("traces/%s/%04d_%03d_%s_%04d.txt", testSession, nPatient, nFireFighter, sostype, i));
                                statistics.add(i, sostype.toString(), nPatient, nFireFighter, delay, trace);

                                simulationLogWriter.flush();

                                System.out.println("Average duration: " + durationSum);
                                System.out.println("Average messageCnt: " + messageCntSum);
                            }
//                            if (i == minTrial - 1)                          // 왜인지 모르지만 첫번째 실행은 건너뛴다...
//                                continue;


                        }
                    }
                }
            }

            statistics.write(statisticsWriter);

            statisticsWriter.close();
            simulationLogWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Session complete: " + datetimeFormat.format(new Date()));
    }

    private static void writeTrace(List<Snapshot> trace, String filename) {
        try {
            File file = new File(filename);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            for (Snapshot snapshot : trace) {
                bw.write((snapshot.getProperties().get(0).value).toString());
                bw.newLine();
            }

            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
