package property.pattern;

import log.Log;
import log.Snapshot;
import property.Property;
import property.PropertyChecker;

import java.util.HashMap;

public abstract class PrecedenceChecker extends PropertyChecker {
    @Override
    protected abstract boolean evaluateState(Snapshot state, Property verificationProperty);
    
    @Override
    public boolean check(Log log, Property verificationProperty) {
        if (evaluateState(log, verificationProperty)) return true;
        else return false;
    }
}
