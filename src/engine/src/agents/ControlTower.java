package agents;

import core.Map;
import core.Msg;
import core.MsgRouter;
import core.World;
import misc.Position;
import misc.Range;

import java.util.ArrayList;

public class ControlTower extends CS {

    public Map worldMap;
    public Map ctMap;
    MsgRouter router;

    ArrayList<Patient> patients = new ArrayList<Patient>();
    ArrayList<FireFighter> fireFighters;

    public ControlTower(World world, String name) {
        super(world, name);

        worldMap = world.getMap();
        ctMap = new Map();
        fireFighters = world.fireFighters;
        router = world.router;
    }

    boolean firstUpdate = true;
    @Override
    public void onUpdate() {
        if(firstUpdate) {
            // 소방관들에게 어디어디를 탐색하라고 명령
            if(fireFighters.size() <= Map.mapSize.height) {
                int n = Map.mapSize.height / fireFighters.size();
                Position begin = new Position(0, 0);
                Position end = new Position(Map.mapSize.width - 1, n - 1);
                for(int i = 0; i < fireFighters.size() - 1; ++i) {
                    router.route(new Msg()
                        .setFrom("ControlTower")
                        .setTo(fireFighters.get(i).name)
                        .setTitle("search range")
                        .setData(new Range(begin.x, begin.y, end.x, end.y)));
                    begin.y += n;
                    end.y += n;
                }
                router.route(new Msg()
                        .setFrom("ControlTower")
                        .setTo(fireFighters.get(fireFighters.size() - 1).name)
                        .setTitle("search range")
                        .setData(new Range(begin.x, begin.y, end.x, Map.mapSize.height - 1)));
            }

            firstUpdate = false;
        }

        if(patients.isEmpty() == false) {
            // 환자 정보가 있을 때
            // 소방관들에게 누구누구를 구하라고 명령
        }
    }

    @Override
    public void recvMsg(Msg msg) {
    }
}
