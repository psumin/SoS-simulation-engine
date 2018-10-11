package action.firefighteraction;

import agents.FireFighter;
import agents.Patient;
import core.Tile;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class FireFighterSearch extends FireFighterAction {

    private Queue<Tile> unvisitedTiles;
    private ArrayList<Patient> patientsMemory;
    private Tile unvisitedTile;

    public FireFighterSearch(FireFighter target) {
        super(target);

        name = "Search";
        unvisitedTiles = fireFighter.unvisitedTiles;
        patientsMemory = fireFighter.patientsMemory;
        fireFighter.markVisitedTiles();
    }

    @Override
    public void onUpdate() {
        // Observe environment
        fireFighter.observe();
        fireFighter.markVisitedTiles();
        //ArrayList<Patient> foundPatient = fireFighter.observe();
        //patientsMemory.addAll(foundPatient);

        Patient targetPatient = null;
        // Select target patient
        targetPatient = fireFighter.selectTargetPatient(patientsMemory);
        //patientsMemory.remove(targetPatient);
        while(targetPatient != null) {
            if(targetPatient.isSaved) {
                patientsMemory.remove(targetPatient);
                targetPatient = fireFighter.selectTargetPatient(patientsMemory);
            } else {
                break;
            }
        }

        if(targetPatient != null) {
            fireFighter.changeAction(new FireFighterMoveToPatient(fireFighter, targetPatient));
        } else {
            // If there is no target patient, move to the unvisited tile
            // Select unvisited tile
            if(unvisitedTile == null) {
                unvisitedTile = selectUnvisitedTile();
            }
            // Move to unvisited tile
            if(unvisitedTile != null) {
                fireFighter.moveTo(unvisitedTile.position);
                //fireFighter.markVisitedTiles();
            }

            if(unvisitedTile != null && fireFighter.isArrivedAt(unvisitedTile.position)) {
                unvisitedTile = null;
            }
        }

        //-------------------------------------
        // TODO: 소방관 사이의 통신
        // Implemented at Firefighter.java
    }


    // Select the unvisited tile randomly
    private Tile selectUnvisitedTile() {
        while(unvisitedTiles.isEmpty() == false) {
            Tile tile = unvisitedTiles.poll();
            if(tile.isVisited() == false) {
                return tile;
            }
        }
        return null;
    }
}
