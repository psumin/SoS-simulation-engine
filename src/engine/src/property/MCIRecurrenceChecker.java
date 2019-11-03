package property;

import log.Log;
import log.Snapshot;
import property.pattern.RecurrenceChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MCIRecurrenceChecker extends RecurrenceChecker {

    public MCIRecurrenceChecker() {

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
        ArrayList<ArrayList<Integer>> stateList = new ArrayList<>();

        ArrayList<Integer> tempA = new ArrayList<>();
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
                        if(fflog.contains(prev)) {
                            tempA.add(counter, i);
                        }
                        counter++;
                    }
                }
            }
            stateList.add(tempA);
            tempA.clear();
        }
//        System.out.println(prevList);

        int sub = -1;
        ArrayList<Integer> startingIndex = new ArrayList<>(Collections.nCopies(12,0));
        for(int i = 1; i < stateList.size(); i++) { // 12 == # of FFs
            for(int j = 0; j < 12; j++) {
                sub = stateList.get(i).get(j) - stateList.get(startingIndex.get(j)).get(j);
                if(sub != 1) {
                    if(sub > verificationProperty.getValue()) { // 구조 주기의 차이가 value보다 크면 return false
                        return false;
                    }
                    startingIndex.set(j,i);
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
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {return false; }
}
