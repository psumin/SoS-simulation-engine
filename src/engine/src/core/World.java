package core;

import agents.*;
import misc.Position;

import java.awt.*;
import java.util.ArrayList;

public class World extends SoSObject{

    public static final int maxPatient = 70;
    public static final int maxFireFighter = 12;
    public static final int maxHospital = 4;
    public static final int maxAmbulance = 4;
    public static final int maxSafeZone = 4;

    Map map;
    public MsgRouter router;
    public ArrayList<FireFighter> fireFighters = new ArrayList<FireFighter>(maxFireFighter);
    public ArrayList<Hospital> hospitals = new ArrayList<Hospital>(maxHospital);
    public ArrayList<SafeZone> safeZones = new ArrayList<>(maxSafeZone);
    public ArrayList<Ambulance> ambulances = new ArrayList<>(maxAmbulance);

    int frameCount = 0;

    public World() {
        map = new Map();
        addChild(map);

        // 맵 생성 후에 라우터 생성해야함
        // 안그러면 널 에러
        router = new MsgRouter(this);

        createHospitals();
        createSafeZones();
        createPatients();
        createFireFighters();
        createAmbulances();
    }

    private void createPatients() {
        for (int i = 0; i < maxPatient; i++) {
            Patient patient = new Patient(this, "Patient");
            patient.setStatus(Patient.Status.random());
            Position randomPosition = null;

            while(true) {
                randomPosition = GlobalRandom.nextPosition(Map.mapSize.width, Map.mapSize.height);
                boolean isSafeZone = false;
                for(SafeZone zone: safeZones) {
                    if(zone.isSafeZone(randomPosition)) {
                        isSafeZone = true;
                        break;
                    }
                }
                if(isSafeZone == false) break;
            }
            patient.position.set(randomPosition.x, randomPosition.y);
            addChild(patient);
            map.addObject(randomPosition.x, randomPosition.y, patient);
        }
    }

    private void createFireFighters() {
        Position[] positions = new Position[] {
                new Position(0, 0),
                new Position(Map.mapSize.width - 1, 0),
                new Position(Map.mapSize.width - 1, Map.mapSize.height - 1),
                new Position(0, Map.mapSize.height - 1)
        };
        int factor = maxFireFighter / 4;
        for (int i = 0; i < maxFireFighter; i++) {
            FireFighter ff = new FireFighter(this, "FF" + (i + 1));
            ff.setPosition(0, 0);
            fireFighters.add(ff);

            ff.setPosition(positions[i / factor]);
            addChild(ff);
        }
    }

    private void createHospitals() {
        for(int i = 0; i < maxHospital; ++i) {
            Hospital hospital = new Hospital(this, "Hospital" + (i + 1));
            hospitals.add(hospital);
            addChild(hospital);

            if(i == 0){
                hospital.setCapacity(2);
            } else {
                hospital.setCapacity(100);
            }
        }

        hospitals.get(0).setPosition(0, 0);
        hospitals.get(1).setPosition(Map.mapSize.width - 1, 0);
        hospitals.get(2).setPosition(0, Map.mapSize.height - 1);
        hospitals.get(3).setPosition(Map.mapSize.width - 1, Map.mapSize.height - 1);
    }

    private void createSafeZones() {
        for(int i = 0; i < maxSafeZone; ++i) {
            SafeZone safeZone = new SafeZone(this, "SafeZone" + (i + 1));
            safeZones.add(safeZone);
            addChild(safeZone);
        }

        safeZones.get(0).setPosition(new Position(Map.mapSize.width / 4, Map.mapSize.height / 4));
        safeZones.get(1).setPosition(new Position(3 * Map.mapSize.width / 4, Map.mapSize.height / 4));
        safeZones.get(2).setPosition(new Position(3 * Map.mapSize.width / 4, 3 * Map.mapSize.height / 4));
        safeZones.get(3).setPosition(new Position(Map.mapSize.width / 4, 3 * Map.mapSize.height / 4));
    }

    private void createAmbulances() {
        for(int i = 0; i < maxAmbulance; ++i) {
            Ambulance ambulance = new Ambulance(this, "Ambulance" + (i + 1));
            ambulances.add(ambulance);
            addChild(ambulance);
        }
    }

    @Override
    public void onUpdate() {
        if(getPatientCount() == 0 && map.getUnvisitedTileCount() == 0) {
            canUpdate(false);
            return;
        }
        frameCount++;
        System.out.println("FrameCount: " + frameCount);
    }

    @Override
    public void onRender(Graphics2D g) {
        Rectangle rect = g.getDeviceConfiguration().getBounds();
        g.setColor(new Color(100, 100, 100));
        g.fillRect(rect.x, rect.y, rect.width, rect.height);

        g.setColor(Color.red);
        g.setFont(new Font("default", Font.BOLD, 16));
        String strFrameCount = "frameCount: " + frameCount;
        g.drawChars(strFrameCount.toCharArray(), 0, strFrameCount.length(), 600, 700);
    }

    public Map getMap() {
        return map;
    }


    public int getPatientCount() {
        int count = 0;
        for(SoSObject child: children) {
            if(child instanceof Patient) {
                count++;
            }
        }
        return count;
    }
}
