package property.pattern;

import log.Log;
import log.Snapshot;
import property.Property;
import property.PropertyChecker;
import java.util.HashMap;

public abstract class BoundedExistenceChecker extends PropertyChecker {
    @Override
    protected abstract boolean evaluateState(Snapshot snapshot, Property verificationProperty);
    
    @Override
    public boolean check(Log log, Property verificationProperty) {
        HashMap<Integer, Snapshot> snapshots = log.getSnapshotMap();
        
        if (evaluateState(snapshots.get(verificationProperty.getDuration()), verificationProperty)) {
                return true;
        }
        return false;
    }
}
