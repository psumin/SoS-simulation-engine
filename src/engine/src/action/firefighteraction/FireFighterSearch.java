package action.firefighteraction;

import action.Action;
import agents.CS;
import agents.FireFighter;
import agents.Patient;
import core.Tile;
import misc.Position;

import java.util.ArrayList;
import java.util.Queue;

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
        //------------------------------------- observe environment
        fireFighter.observe();
        //ArrayList<Patient> foundPatient = fireFighter.observe();
        //patientsMemory.addAll(foundPatient);

        //------------------------------------- 타겟 환자 선택
        Patient targetPatient = fireFighter.selectTargetPatient(patientsMemory);
        patientsMemory.remove(targetPatient);

        if(targetPatient != null) {
            fireFighter.changeAction(new FireFighterMoveToPatient(fireFighter, targetPatient));
        } else {
            //------------------------------------- 타겟 환자가 존재하지 않을 때, 방문하지 않은 타일로 이동
            // select unvisited tile
            if(unvisitedTile == null) {
                unvisitedTile = selectUnvisitedTile();
            }
            // move to unvisited tile (1 tile per 3 frame)
            if(unvisitedTile != null) {
                fireFighter.moveTo(unvisitedTile.position);
                fireFighter.markVisitedTiles();
            }

            if(fireFighter.isArrivedAt(unvisitedTile.position)) {
                unvisitedTile = null;
            }
        }

        //-------------------------------------
        // TODO: 소방관 사이의 통신
        // if exist other firefighters in sight range
        //      send msg( patient memory ) to other firefighters
    }



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
