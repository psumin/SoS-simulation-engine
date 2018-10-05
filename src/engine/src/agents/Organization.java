package agents;

import core.Msg;
import core.World;

import java.util.ArrayList;

import static core.World.fireFighterPrefix;

public class Organization extends CS {

    private final ArrayList<Ambulance> idleAmbulances = new ArrayList<>();
    private final ArrayList<SafeZone> targetSafeZones = new ArrayList<>();

    public Organization(World world, String name) {
        super(world, name);
    }

    @Override
    public void recvMsg(Msg msg) {
        if(msg.from.startsWith(fireFighterPrefix)) {
            if(msg.title == "nearest hospital") {
                FireFighter fireFighter = (FireFighter)msg.data;
                Hospital nearestHospital = (Hospital)fireFighter.nearestObject(new ArrayList<>(world.hospitals));
                router.route(new Msg()
                        .setFrom(name)
                        .setTo(fireFighter.name)
                        .setTitle("nearest hospital")
                        .setData(nearestHospital));
            }
        }
    }
}
