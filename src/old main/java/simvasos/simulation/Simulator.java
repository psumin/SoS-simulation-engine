package simvasos.simulation;

import simvasos.scenario.mciresponse.MCIResponseRunner;
import simvasos.simulation.component.Action;
import simvasos.simulation.component.Agent;
import simvasos.simulation.analysis.Snapshot;
import simvasos.simulation.component.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.TimeUnit;
/**
 * Created by mgjin on 2017-06-21.
 */
public class Simulator {

    static int nPatient;
    public static void setPatient(int patientCount) {
        nPatient = patientCount;
    }

    public static ArrayList<Snapshot> execute(World world, int endOfTime) throws InterruptedException {
        ArrayList<Snapshot> simulationLog = new ArrayList<Snapshot>();

        boolean stoppingCondition = false;
        ArrayList<Action> actions = new ArrayList();
        ArrayList<Action> immediateActions = new ArrayList();

        world.reset();
        simulationLog.add(world.getCurrentSnapshot()); // Initial snapshot
//        long startTime = System.currentTimeMillis();
        while (!stoppingCondition) {
            actions.clear();
            do {
//                long duration = System.currentTimeMillis() - startTime;
//                //System.out.println("duration : " + duration);
//                if(duration <= 10)
//                    Thread.sleep(300);
//                    //System.out.println("hello world!!!");

                immediateActions.clear();
                actions.clear();
                for (Agent agent : world.getAgents()) {
                    Action action = agent.step();

                    if (action.isImmediate()) {
                        immediateActions.add(action);
                    } else {
                        actions.add(action);
                    }
                }

                Collections.shuffle(immediateActions, world.random);
                progress(immediateActions);
            } while (immediateActions.size() > 0);

            ArrayList<Action> exoActions = world.generateExogenousActions();
            actions.addAll(exoActions);
            actions = new ArrayList<Action>(new LinkedHashSet<Action>(actions)); // Remove duplicates

            Collections.shuffle(actions, world.random);
            progress(actions);
            world.progress(1);
            simulationLog.add(world.getCurrentSnapshot());
            // Verdict - evaluateProperties();
            if(((int)(simulationLog.get(simulationLog.size() - 1).getProperties().get(0).value)) == nPatient)
                stoppingCondition = true;
            if (world.getTime() >= endOfTime)       // 이게 tick 검사 부분
                stoppingCondition = true;
        }

        return simulationLog;
    }

    private static void progress(ArrayList<Action> actions) {
        for (Action action : actions) {
            action.execute();
        }
    }
}
