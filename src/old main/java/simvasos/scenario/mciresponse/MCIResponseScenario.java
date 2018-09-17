package simvasos.scenario.mciresponse;

import kiise2016.SoS;
import kr.ac.kaist.se.simulator.BaseConstituent;
import simvasos.scenario.faultscenario.FaultWorld;
import simvasos.scenario.mciresponse.entity.*;
import simvasos.simulation.component.Scenario;
import simvasos.simulation.component.World;
import simvasos.simulation.util.*;

public class MCIResponseScenario extends Scenario {
    public enum SoSType {Virtual, Collaborative, Acknowledged, Directed}

    public MCIResponseScenario(SoSType type, int nFireFighter, int nAmbulance, int nHospital, World world) {
        //this.world = new MCIResponseWorld(type, nPatient);
        // 수민 - 2018.09.12 수정
        //this.world = new FaultWorld(type, nPatient);
        this.world = world;

        for (int i = 1; i <= nFireFighter; i++)
            this.world.addAgent(new FireFighter(this.world, "FireFighter" + i));
        for (int i = 1; i <= nAmbulance; i++)       // 현재 사용 안함, 맵 중앙 위치
            this.world.addAgent(new Ambulance(this.world, "Ambulance" + i, new Location(MCIResponseWorld.MAP_SIZE.getLeft() / 2, MCIResponseWorld.MAP_SIZE.getRight() / 2)));
        for (int i = 1; i <= nHospital; i++)        // 현재 사용 안함, 맵 중앙 위치
            this.world.addAgent(new Hospital(this.world, "Hospital" + i, new Location(MCIResponseWorld.MAP_SIZE.getLeft() / 2, MCIResponseWorld.MAP_SIZE.getRight() / 2), 100));

        if (type != SoSType.Virtual)
        //if (type != SoSType.Virtual && type != SoSType.Collaborative)     // Collaborative SoS도 Control tower 필요없음
            this.world.addAgent(new ControlTower(this.world, "ControlTower"));

        this.checker = new PulloutChecker(2);
    }
}
