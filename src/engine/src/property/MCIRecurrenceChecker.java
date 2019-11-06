package property;

import log.Log;
import log.Snapshot;
import property.pattern.RecurrenceChecker;

import java.util.*;

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
        boolean flagFF = false;
        HashMap<Integer, Snapshot> snapshots = log.getSnapshotMap();
        int logSize = snapshots.size(); // 0 ... 10 => size: 11, endTime: 10
        ArrayList<ArrayList<Integer>> stateList = new ArrayList<>();

        ArrayList<Integer> tempA;
        for(int i = 1; i < logSize; i++) {
            temp = snapshots.get(i).getSnapshotString();

            StringTokenizer st = new StringTokenizer(temp, " ");
            counter = 0;
            tempA = new ArrayList<>(Collections.nCopies(12,0));
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
                            tempA.set(counter, i);
                            flagFF = true;
                        }
                        counter++;
                    }
                    if(flagFF) {
                        stateList.add((ArrayList<Integer>)tempA.clone());
//                        System.out.println(tempA);
                        flagFF = false;
                        tempA.clear();
                    }
                }
            }
        }
        System.out.println("FFs Rescue Activity Time Table");
        for(int i = 0; i < stateList.size(); i++) {
            System.out.println(stateList.get(i));
        }

        int sub = -1;
        ArrayList<Boolean> periodFlags = new ArrayList<>(Collections.nCopies(12,false));
        ArrayList<Integer> startingValues= new ArrayList<>(Collections.nCopies(12,0));
        for(int i = 0; i < stateList.size(); i++) { // 12 == # of FFs
            for(int j = 0; j < 12; j++) {
                if(stateList.get(i).get(j) == 0) { // 주기가 바뀌는 경우
                    if(startingValues.get(j) != 0 && !periodFlags.get(j)) periodFlags.set(j,true);
                    continue;
                }
                else if (startingValues.get(j) == 0) { // 처음에 해당 FF 자리에 0이 아닌 값이 나오는 경우
                    startingValues.set(j,stateList.get(i).get(j));
                    continue;
                }
                
                if(periodFlags.get(j)) {
                    sub = stateList.get(i).get(j) - startingValues.get(j);
                    System.out.println(j + "th FF: " + sub);
                    if (sub > verificationProperty.getThresholdValue()) { // 구조 주기의 차이가 value보다 크면 return false
                        return false;
                    }
                    startingValues.set(j,stateList.get(i).get(j));
                    periodFlags.set(j, false);
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
