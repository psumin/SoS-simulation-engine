package core;

import agents.FireFighter;
import agents.Patient;
import misc.Position;
import misc.Size;
import misc.Time;

import java.awt.*;
import java.util.ArrayList;

public class World extends SoSObject{

    public static final int maxPatient = 100;
    public static final int maxFireFighter = 5;

    Map map;
    public MsgRouter router;

    int frameCount = 0;

    public World() {
        map = new Map();
        addChild(map);

        // 맵 생성 후에 라우터 생성해야함
        // 안그러면 널 에러
        router = new MsgRouter(this);

        createFireFighters();
        createPatients();
    }

    private void createPatients() {
        for (int i = 0; i < maxPatient; i++) {
            Patient patient = new Patient(this, "Patient");
            patient.setStatus(Patient.Status.random());
            Position randomPosition = GlobalRandom.nextPosition(Map.mapSize.width, Map.mapSize.height);
            patient.position.set(randomPosition.x, randomPosition.y);
            addChild(patient);
            map.addObject(randomPosition.x, randomPosition.y, patient);
        }
    }

    private void createFireFighters() {
        for (int i = 0; i < maxFireFighter; i++) {
            FireFighter ff = new FireFighter(this, "FireFighter" + (i + 1));
//            if(i == 0) {
//                addChild(ff.individualMap);
//            }
            addChild(ff);
            ff.setPosition(0, 0);
        }
    }

    @Override
    public void onUpdate() {
        if(map.getUnvisitedTileCount() == 0) {
            this.canUpdate(false);
        } else {
            frameCount++;
        }
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
}
