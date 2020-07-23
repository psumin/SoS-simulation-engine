package property;

import log.Log;
import log.Snapshot;
import property.pattern.UntilChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MCIUntilChecker extends UntilChecker {
    
    public MCIUntilChecker() {
        super();
    }
    
    @Override
    protected boolean evaluateState(Log log, Property verificationProperty) {
        String prev = verificationProperty.getPrevState();
        String temp = "";
        int numFF = 0;
        int counter = 0;
        HashMap<Integer, Snapshot> snapshots = log.getSnapshotMap();
        int logSize = snapshots.size(); // 0 ... 10 => size: 11, endTime: 10
        ArrayList<Integer> prevList = new ArrayList<>(Collections.nCopies(12,-1));
        
        for(int i = 1; i < logSize; i++) {
        temp = snapshots.get(i).getSnapshotString();
        
        StringTokenizer st = new StringTokenizer(temp, " ");
        counter = 0;
        while(st.hasMoreTokens()) {
            String tokens = st.nextToken();
            if(tokens.equals("Amb:")) break;
            if(tokens.equals("CurrentFF:")) {
                int tmpFF = Integer.parseInt(st.nextToken());
                if(tmpFF > numFF) numFF = tmpFF;
            }
            if(tokens.equals("FF:")) {
                while(counter < numFF) {
                    String fflog = st.nextToken();
                    if(fflog.contains(prev)) { // 마지막으로 prev state가 관찰될때
                        if(prevList.get(counter) != -1) // prev&latterList가 모두 꽉 차 있을때 Index 하나 더 증가
                            prevList.set(counter, i);
                        }
                    counter++;
                    }
                }
            }
        }
        
        System.out.println("FF Free State Check Table");
        System.out.println(prevList);

        for(int i = 0; i < 12; i++) {
            if(prevList.get(i) != -1) { return false; }
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
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {return false; }
}
