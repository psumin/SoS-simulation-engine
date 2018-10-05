package agents;

import core.Msg;
import core.World;
import misc.Time;

import java.util.ArrayList;

import static core.World.fireFighterPrefix;

public class Organization extends CS {

    private final ArrayList<Ambulance> freeStateAmbulances = new ArrayList<>();
    private final ArrayList<SafeZone> targetSafeZones = new ArrayList<>();

    public Organization(World world, String name) {
        super(world, name);
    }

    @Override
    public void onUpdate() {
    }

    private void ambulanceFreeStateStart(Msg msg) {
        if(targetSafeZones.isEmpty()) {
            freeStateAmbulances.add((Ambulance) msg.data);
        } else if(!targetSafeZones.isEmpty()) {
            Ambulance ambulance = (Ambulance)msg.data;
            SafeZone safeZone = targetSafeZones.get(0);
            targetSafeZones.remove(0);

            router.route(new Msg()
                    .setFrom(name)
                    .setTo(ambulance.name)
                    .setTitle("move to safezone")
                    .setData(safeZone));
        }
    }

    private void patientArrivedAtSafeZone(Msg msg) {
        if(freeStateAmbulances.isEmpty()) {
            targetSafeZones.add((SafeZone)msg.data);
        } else {
            Ambulance ambulance = freeStateAmbulances.get(0);
            freeStateAmbulances.remove(0);

//            SafeZone safeZone = targetSafeZones.get(0);
//            targetSafeZones.remove(0);

            router.route(new Msg()
                    .setFrom(name)
                    .setTo(ambulance.name)
                    .setTitle("move to safezone")
                    .setData(msg.data));
        }
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
        } else if(msg.from.startsWith("SafeZone")) {
            if(msg.title == "patient arrived") {
                patientArrivedAtSafeZone(msg);
            }
        } else if(msg.from.startsWith("Ambulance")) {
            if(msg.title == "free state start") {
                ambulanceFreeStateStart(msg);
            }
        }
    }
}
