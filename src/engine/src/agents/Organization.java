package agents;

import core.Msg;
import core.SoSObject;
import core.World;
import misc.Time;

import java.util.*;

import static core.World.fireFighterPrefix;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Organization extends CS {

    private final int seriousPatientWeight = 10;

    private final ArrayList<Ambulance> freeStateAmbulances = new ArrayList<>();
    private final ArrayList<Msg> msgsFromSafeZone = new ArrayList<>();
    //private final ArrayList<SafeZone> targetSafeZones = new ArrayList<>();

    public Organization(World world, String name) {
        super(world, name);
    }

    // Serious patient priority
    private void ambulanceFreeStateStart(Msg msg) {
        if(msgsFromSafeZone.isEmpty()) {
            freeStateAmbulances.add((Ambulance) msg.data);
        } else {
            Ambulance ambulance = (Ambulance)msg.data;

            Msg seriousMsg = null;
            for(Msg msgFromSafeZone: msgsFromSafeZone) {
                if(msgFromSafeZone.title == "serious patient arrived") {
                    seriousMsg = msgFromSafeZone;
                    break;
                }
            }

            SafeZone safeZone = null;
            if(seriousMsg != null) {                    // Serious patient first
                msgsFromSafeZone.remove(seriousMsg);
                safeZone = (SafeZone)seriousMsg.data;
            } else {
                Msg first = msgsFromSafeZone.get(0);    // Wounded patient next
                msgsFromSafeZone.remove(0);
                safeZone = (SafeZone)first.data;
            }

            router.route(new Msg()
                    .setFrom(name)
                    .setTo(ambulance.name)
                    .setTitle("move to safezone")
                    .setData(safeZone));
        }
    }

    private void patientArrivedAtSafeZone(Msg msg) {
        if(freeStateAmbulances.isEmpty()) {
            msgsFromSafeZone.add(msg);
        } else {
            SafeZone safeZone = (SafeZone)msg.data;

            ArrayList<Ambulance> mustRemove = new ArrayList<Ambulance>();
            for(Ambulance amb: freeStateAmbulances) {
                if(amb.currentAction.name.startsWith("Removed")) {
                    mustRemove.add(amb);
                }
            }
            freeStateAmbulances.removeAll(mustRemove);


            Ambulance ambulance = (Ambulance)safeZone.nearestObject(new ArrayList<SoSObject>(freeStateAmbulances));
            freeStateAmbulances.remove(ambulance);

            //Ambulance ambulance = freeStateAmbulances.get(0);
            //freeStateAmbulances.remove(0);

            router.route(new Msg()
                    .setFrom(name)
                    .setTo(ambulance.name)
                    .setTitle("move to safezone")
                    .setData(safeZone));
        }
    }

    @Override
    public void recvMsg(Msg msg) {
        if(msg.from.startsWith(fireFighterPrefix)) {
            if(msg.title == "nearest hospital") {                                                                       // Message from the Fire fighter when the firefighter's target patient is Serious patient
                FireFighter fireFighter = (FireFighter)msg.data;
                Hospital nearestHospital = (Hospital)fireFighter.nearestObject(new ArrayList<>(world.hospitals));       // Check the nearest hospital from the Fire fighter's position
                router.route(new Msg()
                        .setFrom(name)
                        .setTo(fireFighter.name)
                        .setTitle("nearest hospital")
                        .setData(nearestHospital));
            }
        } else if(msg.from.startsWith("SafeZone")) {                                                                    // Message from Safe Zone. Information about the arrived patient
            patientArrivedAtSafeZone(msg);
//            if(msg.title == "serious patient arrived") {
//                patientArrivedAtSafeZone(msg);
//            } else if(msg.title == "wounded patient arrived") {
//                patientArrivedAtSafeZone(msg);
//            }
        } else if(msg.from.startsWith("Ambulance")) {                                                                   // Message from the Ambulance, when Free state
            if(msg.title == "free state start") {
                ambulanceFreeStateStart(msg);
            }
        }
    }
}