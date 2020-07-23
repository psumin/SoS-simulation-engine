import javafx.util.Pair;
import log.Log;
import property.*;
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

/*
        //verification
        SPRT verifier;
        //ComfortZoneChecker comfortZoneChecker = new ComfortZoneChecker();

        // Existence
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0.02);
        // Absence
//        property.setThresholdValue(0); // RescueRate - TreatmentRate
        // Universality
//        property.setThresholdValue(1.0); // RescueRate
        // TransientStateProbability
//        property.setStateProbabilityValues(0.6, 60, 81);
        // SteadyStateProbability
//        property.setStateProbabilityValues(0.15, 0, 81);
        // MinimumDuration
//        property.setThresholdValue(10); // FF가 10명 이상 활동하고 있어야 한다.
//        property.setDuration(65); // 최소 65 Frame 이상
        // MaximumDuration
//        property.setThresholdValue(0); // Rescurate
//        property.setDuration(60); // 최대 60 Frame 이하
        // Bounded Existence
//        property.setDuration(15); // Bounded Frame 20
//        property.setState("Free"); // State가 Free인게 아님을 확인하기 위해
        // Precedence
//        property.setPrevState("MoveToPatient");
//        property.setState("FirstAid");
        // Response
//        property.setPrevState("FirstAid");
//        property.setState("TransferToBridgehead");
        // Recurrence
//        property.setPrevState("MoveToPatient");
//        property.setThresholdValue(51);
        // Until
        property.setPrevState("Free");

        MCIPropertyChecker existenceChecker = new MCIPropertyChecker();
        MCIAbsenceChecker absenceChecker = new MCIAbsenceChecker();
        MCIUniversalityChecker universalityChecker = new MCIUniversalityChecker();
        MCITransientSPChecker transientSPChecker = new MCITransientSPChecker();
        MCISteadySPChecker steadySPChecker = new MCISteadySPChecker();
        MCIMinimumDurationChecker minimumDurationChecker = new MCIMinimumDurationChecker();
        MCIMaximumDurationChecker maximumDurationChecker = new MCIMaximumDurationChecker();
        MCIBoundedExistenceChecker boundedExistenceChecker = new MCIBoundedExistenceChecker();
        MCIPrecedenceChecker precedenceChecker = new MCIPrecedenceChecker();
        MCIResponseChecker responseChecker = new MCIResponseChecker();
        MCIRecurrenceChecker recurrenceChecker = new MCIRecurrenceChecker();
        MCIUntilChecker untilChecker = new MCIUntilChecker();

//        verifier = new SPRT(comfortZoneChecker);

//        verifier = new SPRT(existenceChecker);
//        verifier = new SPRT(absenceChecker);
//        verifier = new SPRT(universalityChecker);
//        verifier = new SPRT(transientSPChecker);
//        verifier = new SPRT(steadySPChecker);
//        verifier = new SPRT(minimumDurationChecker);
//        verifier = new SPRT(maximumDurationChecker);
//        verifier = new SPRT(boundedExistenceChecker);
//        verifier = new SPRT(precedenceChecker);
//        verifier = new SPRT(responseChecker);
//        verifier = new SPRT(recurrenceChecker);
        verifier = new SPRT(untilChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;

*/

        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        simulationEngine.setRunning();
//        System.out.println("Get Running: "+ simulationEngine.getRunning());
        programStartTime = System.nanoTime();           // 첫번째 시뮬레이션까지 포함할려면 여기에 정의
        simulationEngine.run();
        simulationEngine.setSuper_counter();

        double satisfactionProb = 0;
        Boolean satisfaction = true;

/*

        for (int i = 1; i < 100; i++) {
//            programStartTime = System.nanoTime();           // 첫번째 시뮬레이션을 제외하려면 여기에 정의

//            System.out.println("inside for loop:" + simulationEngine.getRunning());
            double theta = i * 0.01;

            thetaStartTime = System.nanoTime();
            verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, theta);    //or T = 3
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

*/
    }
}
