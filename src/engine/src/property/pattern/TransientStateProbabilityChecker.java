package property.pattern;

import log.Log;
import log.Snapshot;
import property.Property;
import property.PropertyChecker;

import java.util.HashMap;

/**
 * The type Transient state probability checker.
 * checks ...
 */
public abstract class TransientStateProbabilityChecker extends PropertyChecker {
    @Override
    protected abstract boolean evaluateState(Snapshot state, Property verificationProperty);

    @Override
    public boolean check(Log log, Property verificationProperty) {
        HashMap<Integer, Snapshot> snapshots = log.getSnapshotMap();
        int logSize = snapshots.size(); // 0 ... 10 => size: 11, endTime: 10
        int satisfiedCount = 0;
        double prob = verificationProperty.getProb();
        int t = verificationProperty.getT();
        int T = verificationProperty.getTT();
        
        for (int i = t; i < logSize; i++) {
            if (evaluateState(snapshots.get(i), verificationProperty)) {
                satisfiedCount++;
            }
        }

        if ((double)satisfiedCount/(double)(T-t) >= prob){
            return true;
        }
        return false;
    }
}
