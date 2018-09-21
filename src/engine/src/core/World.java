package core;

import agents.FireFighter;
import agents.Patient;
import misc.Size;

import java.awt.*;
import java.util.Random;

public class World {

    Map map;
    Size mapSize = new Size(20, 20);

    // 테스트
    int maxFireFighter = 10;
    int maxPatient = 10;

    public void init() {
        map = new Map();
        map.init(mapSize);

        Map.setGlobal(map);


        SoSObject.initAll();

//        // 소방관 생성 예시
//        FireFighter ff = new FireFighter();
//        ff.init();
//        ff.setPosition(1, 1);
//
//        // 환자 생성 예시
//        Patient p = new Patient();
//        p.init();
//        p.setPosition(0, 0);
        generateFireFighters();
        generatePaitents();
    }

    public void update() {
        SoSObject.updateAll();
    }

    public void render(Graphics2D g) {
        map.render(g);
        SoSObject.renderAll(g);
    }
    public void clear() {
        SoSObject.clearAll();
        map.clear();
    }

    private void generateFireFighters(){
        for(int i = 0; i < maxFireFighter; ++i) {
            FireFighter fireFighter = new FireFighter();
            fireFighter.init();
            fireFighter.setPosition(0, 0);
        }
    }

    private void generatePaitents() {
        Random random = new Random();
        for(int i = 0; i < maxPatient; ++i) {
            Patient patient = new Patient();
            patient.init();
            patient.setPosition(random.nextInt(mapSize.width), random.nextInt(mapSize.height));
        }
    }
}
