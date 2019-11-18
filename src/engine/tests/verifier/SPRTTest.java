package verifier;

import javafx.util.Pair;
import property.*;
import simulation.SoSSimulationProgram;

import static org.junit.Assert.*;

public class SPRTTest {
    
    @org.junit.Test
    public void ExistenceTest() {
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0);
        MCIPropertyChecker existenceChecker = new MCIPropertyChecker();
        SPRT verifier = new SPRT(existenceChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;
        
        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        
        double theta = 0.01;
        verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, theta);    //or T = 3

        assertEquals(2, (int)verificationResult.getKey().getKey());
        assertTrue(verificationResult.getKey().getValue());
        assertEquals("theta: 0.01 numSamples: 2 numTrue: 2 result: true", verificationResult.getValue());
        assertNotNull(verificationResult);
    }
    
    @org.junit.Test
    public void AbsenceTest() {
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0.2);
        property.setThresholdValue(0); // RescueRate - TreatmentRate
        MCIAbsenceChecker absenceChecker = new MCIAbsenceChecker();
        SPRT verifier = new SPRT(absenceChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;
        
        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        
        double theta = 0.01;
        verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, theta);    //or T = 3
        
        assertEquals(2, (int)verificationResult.getKey().getKey());
        assertTrue(verificationResult.getKey().getValue());
        assertEquals("theta: 0.01 numSamples: 2 numTrue: 2 result: true", verificationResult.getValue());
        assertNotNull(verificationResult);
    }
    
    @org.junit.Test
    public void BoundedExistenceTest() {
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0);
        
        property.setDuration(20); // Bounded Frame 20
        property.setState("Free"); // State가 Free인게 아님을 확인하기 위해
        MCIBoundedExistenceChecker boundedExistenceChecker = new MCIBoundedExistenceChecker();
        SPRT verifier = new SPRT(boundedExistenceChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;
        
        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        
        double theta = 0.01;
        verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, theta);    //or T = 3
        
        assertEquals(2, (int)verificationResult.getKey().getKey());
        assertTrue(verificationResult.getKey().getValue());
        assertEquals("theta: 0.01 numSamples: 2 numTrue: 2 result: true", verificationResult.getValue());
        assertNotNull(verificationResult);
    }
    
    @org.junit.Test
    public void UniversalityTest() {
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0);
        property.setThresholdValue(1.0); // RescueRate
    
        MCIUniversalityChecker universalityChecker = new MCIUniversalityChecker();
        SPRT verifier = new SPRT(universalityChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;
        
        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        
        double theta = 0.01;
        verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, theta);    //or T = 3
        
        assertEquals(2, (int)verificationResult.getKey().getKey());
        assertTrue(verificationResult.getKey().getValue());
        assertEquals("theta: 0.01 numSamples: 2 numTrue: 2 result: true", verificationResult.getValue());
        assertNotNull(verificationResult);
    }
    
    @org.junit.Test
    public void TransientSPTest() {
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0);
        property.setStateProbabilityValues(0.6, 60, 81);
        
        MCITransientSPChecker transientSPChecker = new MCITransientSPChecker();
        SPRT verifier = new SPRT(transientSPChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;
        
        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        
        double theta = 0.01;
        verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, theta);    //or T = 3
        
        assertEquals(2, (int)verificationResult.getKey().getKey());
        assertTrue(verificationResult.getKey().getValue());
        assertEquals("theta: 0.01 numSamples: 2 numTrue: 2 result: true", verificationResult.getValue());
        assertNotNull(verificationResult);
    }
    
    @org.junit.Test
    public void SteadySPTest() {
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0);
        property.setStateProbabilityValues(0.15, 0, 81);
        
        MCISteadySPChecker steadySPChecker = new MCISteadySPChecker();
        SPRT verifier = new SPRT(steadySPChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;
        
        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        
        double theta = 0.01;
        verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, theta);    //or T = 3
        
        assertEquals(2, (int)verificationResult.getKey().getKey());
        assertTrue(verificationResult.getKey().getValue());
        assertEquals("theta: 0.01 numSamples: 2 numTrue: 2 result: true", verificationResult.getValue());
        assertNotNull(verificationResult);
    }
    
    @org.junit.Test
    public void MinimumDurationTest() {
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0);
        property.setThresholdValue(10); // FF가 10명 이상 활동하고 있어야 한다.
        property.setDuration(65); // 최소 65 Frame 이상
        
        MCIMinimumDurationChecker minimumDurationChecker = new MCIMinimumDurationChecker();
        SPRT verifier = new SPRT(minimumDurationChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;
        
        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        
        double theta = 0.01;
        verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, theta);    //or T = 3
        
        assertEquals(2, (int)verificationResult.getKey().getKey());
        assertTrue(verificationResult.getKey().getValue());
        assertEquals("theta: 0.01 numSamples: 2 numTrue: 2 result: true", verificationResult.getValue());
        assertNotNull(verificationResult);
    }
    
    @org.junit.Test
    public void MaximumDurationTest() {
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0);
        property.setThresholdValue(0); // Rescurate
        property.setDuration(70); // 최대 60 Frame 이하
    
        MCIMaximumDurationChecker maximumDurationChecker = new MCIMaximumDurationChecker();
        SPRT verifier = new SPRT(maximumDurationChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;
        
        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        
        double theta = 0.01;
        verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, theta);    //or T = 3
        
        assertEquals(2, (int)verificationResult.getKey().getKey());
        assertTrue(verificationResult.getKey().getValue());
        assertEquals("theta: 0.01 numSamples: 2 numTrue: 2 result: true", verificationResult.getValue());
        assertNotNull(verificationResult);
    }
    
    @org.junit.Test
    public void PrecedenceTest() {
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0);
        property.setPrevState("MoveToPatient");
        property.setState("FirstAid");
        
        MCIPrecedenceChecker precedenceChecker = new MCIPrecedenceChecker();
        SPRT verifier = new SPRT(precedenceChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;
        
        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        
        double theta = 0.01;
        verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, theta);    //or T = 3
        
        assertEquals(2, (int)verificationResult.getKey().getKey());
        assertTrue(verificationResult.getKey().getValue());
        assertEquals("theta: 0.01 numSamples: 2 numTrue: 2 result: true", verificationResult.getValue());
        assertNotNull(verificationResult);
    }
    
    @org.junit.Test
    public void ResponseTest() {
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0);
        property.setPrevState("FirstAid");
        property.setState("TransferToBridgehead");
    
        MCIResponseChecker responseChecker = new MCIResponseChecker();
        SPRT verifier = new SPRT(responseChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;
        
        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        
        double theta = 0.01;
        verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, theta);    //or T = 3
        System.out.println(verificationResult.getKey().getKey());
        System.out.println(verificationResult.getValue());
        
        assertEquals(2, (int)verificationResult.getKey().getKey());
        assertTrue(verificationResult.getKey().getValue());
        assertEquals("theta: 0.01 numSamples: 2 numTrue: 2 result: true", verificationResult.getValue());
        assertNotNull(verificationResult);
    }
    
    @org.junit.Test
    public void RecurrenceTest() {
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0);
        property.setPrevState("MoveToPatient");
        property.setThresholdValue(80);
        
        MCIRecurrenceChecker recurrenceChecker = new MCIRecurrenceChecker();
        SPRT verifier = new SPRT(recurrenceChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;
        
        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        
        double theta = 0.01;
        verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, theta);    //or T = 3
        System.out.println(verificationResult.getKey().getKey());
        System.out.println(verificationResult.getValue());
        
        assertEquals(2, (int)verificationResult.getKey().getKey());
        assertTrue(verificationResult.getKey().getValue());
        assertEquals("theta: 0.01 numSamples: 2 numTrue: 2 result: true", verificationResult.getValue());
        assertNotNull(verificationResult);
    }
    
    @org.junit.Test
    public void UntilTest() {
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0);
        property.setPrevState("Free");
    
        MCIUntilChecker untilChecker = new MCIUntilChecker();
        SPRT verifier = new SPRT(untilChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;
        
        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        
        double theta = 0.01;
        verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, theta);    //or T = 3
        
        assertEquals(2, (int)verificationResult.getKey().getKey());
        assertTrue(verificationResult.getKey().getValue());
        assertEquals("theta: 0.01 numSamples: 2 numTrue: 2 result: true", verificationResult.getValue());
        assertNotNull(verificationResult);
    }
}