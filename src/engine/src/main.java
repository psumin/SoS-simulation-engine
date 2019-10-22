import javafx.util.Pair;
import log.Log;
import property.MCIProperty;
import property.MCIPropertyChecker;
import simulation.SoSSimulationProgram;
import verifier.SPRT;

public class main {

    Log log = new Log();
    public static void main(String [] args){
        long programStartTime;                                          // 프로그램 시작 시간
        long programEndTime;                                            // 프로그램 종료 시간
        long thetaStartTime;                                            // 한 사이클 시작 시간
        long thetaEndTime;                                              // 한 사이클 종료 시간
        boolean ruuning = true;
        //verification
        SPRT verifier;
        //ComfortZoneChecker comfortZoneChecker = new ComfortZoneChecker();
        MCIProperty rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.03);

        MCIPropertyChecker checker = new MCIPropertyChecker();
        //verifier = new SPRT(comfortZoneChecker);
        verifier = new SPRT(checker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;


        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        simulationEngine.setRunning();
//        System.out.println("Get Running: "+ simulationEngine.getRunning());
        programStartTime = System.nanoTime();           // 첫번째 시뮬레이션까지 포함할려면 여기에 정의
        simulationEngine.run();
        simulationEngine.setSuper_counter();

        double satisfactionProb = 0;
        Boolean satisfaction = true;

        // Test algorithm to improve Statistical Model Checking

        for (int i = 1; i < 100; i++) {
//            programStartTime = System.nanoTime();           // 첫번째 시뮬레이션을 제외하려면 여기에 정의

//            System.out.println("inside for loop:" + simulationEngine.getRunning());
            double theta = i * 0.01;
            //Existence, Absence, Universality
            //verificationResult = verifier.verifyWithSimulationGUI(smartHomeSimulation, null, 2000, theta);

            thetaStartTime = System.nanoTime();
            verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, rescuedProperty, 2000, theta);    //or T = 3
            thetaEndTime = System.nanoTime();
            System.out.println(i /(float)100 + " theta verification running time: " + (thetaEndTime - thetaStartTime) / (float)1000_000_000 + " sec");          // 한 theta 실행 시간

            System.out.println(verificationResult.getValue());
            if (satisfaction == true && !verificationResult.getKey().getValue()) {
                satisfactionProb = theta;
                satisfaction = false;
            }

        }
        if (satisfaction == true) {
            satisfactionProb = 1;
        }
        System.out.println("Verification property satisfaction probability: " + satisfactionProb);
        programEndTime = System.nanoTime();
        System.out.println("=== Total Program running time: " + (programEndTime - programStartTime) / (float)1000_000_000 + " sec");          // 전체 프로그램 실행 시간

//        new Thread(simulationEngine).start();
    }
}
