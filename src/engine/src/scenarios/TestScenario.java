package scenarios;

import maps.Map;
import misc.Size;

public class TestScenario extends SoSScenario {

    Map map;

    @Override
    public void start() {
        map = new Map(new Size(70, 70));
    }

    @Override
    public void clear() {
        map = null;
    }
}
