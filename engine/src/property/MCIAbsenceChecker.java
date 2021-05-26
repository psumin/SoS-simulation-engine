package property;

import log.Log;
import log.Snapshot;
import property.pattern.AbsenceChecker;

import java.util.StringTokenizer;

public class MCIAbsenceChecker extends AbsenceChecker {
    
    public MCIAbsenceChecker() {
        super();
    }
    
    @Override
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
        double rescueRate = -1;
        double treatRate = -1;
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        while(st.hasMoreTokens()) {
            if(st.nextToken().equals("RescuedRate:")) {
                rescueRate = Double.parseDouble(st.nextToken());
            } else if (st.nextToken().equals("TreatmentRate:")) {
                treatRate = Double.parseDouble(st.nextToken());
            }
        }
        
        if(rescueRate - treatRate >= verificationProperty.getThresholdValue()){
            return false;
        }
        else{
            return true;
        }
    }
    
    @Override
    public boolean check(Log log, Property verificationProperty, int until) {
        return false;
    }
    
    @Override
    public boolean check(Log log, Property verificationProperty, double prob, int T) {
        return false;
    }
    
    @Override
    public boolean check(Log log, Property verificationProperty, double prob, int t, int T) {
        return false;
    }
    
    @Override
    public boolean check(Log log, Property verificationProperty, int t, int T) {
        return false;
    }
    
    @Override
    protected boolean evaluateState(Log log, Property verificationProperty) {return false; }
}
