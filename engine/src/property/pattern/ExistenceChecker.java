package property.pattern;

import log.Log;
import log.Snapshot;
import property.Property;
import property.PropertyChecker;

import java.util.HashMap;

/**
 * The type Existence checker.
 * checks whether a property is satisfied at least once.
 */
public abstract class ExistenceChecker extends PropertyChecker {
    @Override
    protected abstract boolean evaluateState(Snapshot snapshot, Property verificationProperty);

    @Override
    public boolean check(Log log, Property verificationProperty) {
        HashMap<Integer, Snapshot> snapshots = log.getSnapshotMap();
        int logSize = snapshots.size(); // 0 ... 10 => size: 11, endTime: 10

        for (int i = logSize; i <= logSize; i++) {                                // 0번째는 아무정보가 없음 null   , 1번부터 시작
            if (evaluateState(snapshots.get(i), verificationProperty)) {
                return true;
            }
        }
        return false;
    }
}
