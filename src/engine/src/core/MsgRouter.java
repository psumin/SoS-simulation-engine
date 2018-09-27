package core;

import agents.FireFighter;
import misc.Position;

import java.util.ArrayList;

public class MsgRouter extends SoSObject {

    World world;
    Map worldMap;

    public MsgRouter(World world) {
        this.world = world;
        worldMap = world.getMap();
    }

    @Override
    public void onUpdate() {

    }

    public void route(Msg msg) {
    }

    // 그 범위 내의 소방관에게만 브로드캐스팅
    public void broadcast(SoSObject sender,  Msg msg, Position center, int range) {
        ArrayList<Tile> tiles = new ArrayList<>();

        int left = center.x - range / 2;
        int right = center.x + range / 2;
        int top = center.y - range / 2;
        int bottom = center.y + range / 2;

        for(int y = top; y <= bottom; ++y) {
            for(int x = left; x <= right; ++x) {
                Tile tile = worldMap.getTile(x, y);
                if(tile != null) {
                    tiles.add(tile);
                }
            }
        }

        tiles.forEach(tile -> {
            tile.getObjects().forEach(obj -> {
                if(obj instanceof FireFighter) {
                    if(obj != sender) {
                        obj.recvMsg(msg);
                    }
                }
            });
        });
    }
}
