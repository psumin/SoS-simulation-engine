import javafx.util.Pair;
import log.Log;
import property.MCIProperty;
import property.MCIPropertyChecker;
import simulation.SoSSimulationProgram;
import verifier.SPRT;

public class main {

    Log log = new Log();
    public static void main(String [] args){
        boolean ruuning = true;
        //verification
        SPRT verifier;
        //ComfortZoneChecker comfortZoneChecker = new ComfortZoneChecker();
        MCIProperty rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.80);

        MCIPropertyChecker checker = new MCIPropertyChecker();
        //verifier = new SPRT(comfortZoneChecker);
        verifier = new SPRT(checker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;

        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        simulationEngine.setRunning();
        System.out.println("Get Running: "+ simulationEngine.getRunning());
        simulationEngine.run();
        simulationEngine.setSuper_counter();

        double satisfactionProb = 0;
        Boolean satisfaction = true;
        for (int i = 1; i < 100; i++) {
            System.out.println("inside for loop:" + simulationEngine.getRunning());
            double theta = i * 0.01;
            //Existence, Absence, Universality
            //verificationResult = verifier.verifyWithSimulationGUI(smartHomeSimulation, null, 2000, theta);
            verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, rescuedProperty, 2000, theta);    //or T = 3
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

//        new Thread(simulationEngine).start();
    }
}
