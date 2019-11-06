package property;

import log.Log;
import log.Snapshot;
import property.pattern.ExistenceChecker;

import java.util.StringTokenizer;

public class MCIPropertyChecker extends ExistenceChecker {
    
    public MCIPropertyChecker() {
        
        super();
    }
    
    @Override
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        while(st.hasMoreTokens()) {
            if(st.nextToken().equals("TreatmentRate:"))
                break;
        }
        
        double TreatmentRate = Double.parseDouble(st.nextToken());
//        System.out.println(TreatmentRate);
        
        if(TreatmentRate >= verificationProperty.getValue()){
            return true;
        }
        else{
            return false;
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