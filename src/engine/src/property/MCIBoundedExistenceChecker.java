package property;

import log.Log;
import log.Snapshot;
import property.pattern.BoundedExistenceChecker;

import java.util.StringTokenizer;

public class MCIBoundedExistenceChecker extends BoundedExistenceChecker {
    public MCIBoundedExistenceChecker() {
        
        super();
    }
    
    @Override
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
        int ambNum = 0;
        String tmp = "";
        
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        while(st.hasMoreTokens()) {
            if(st.nextToken().equals("CurrentAmb:")) {
                ambNum = Integer.parseInt(st.nextToken());
            }
            else if (st.nextToken().equals("Amb:")) {
                for(int i = 0; i < ambNum; i++) {
                    tmp = st.nextToken();
                    StringTokenizer st2 = new StringTokenizer(tmp, "/");
                    st2.nextToken();
                    
                    if(st2.nextToken().equals(verificationProperty.getState())) { // Ambulance의 State가 Free인지 아닌지 확인
                        return false;
                    }
                }
            }
        }
        return true;
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
