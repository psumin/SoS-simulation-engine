package core;

import agents.FireFighter;
import misc.Position;
import misc.Time;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.util.ArrayList;

public class MsgRouter extends SoSObject {

    World world;
    Map worldMap;

    private Workbook workbook;
    private Sheet sheet;
    private int rowNum = 0;

    public MsgRouter(World world, Workbook workbook) {
        this.world = world;
        worldMap = world.getMap();

        this.workbook = workbook;
        this.sheet = workbook.createSheet("communications");;

        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("frame count");
        row.createCell(1).setCellValue("from");
        row.createCell(2).setCellValue("to");
        row.createCell(3).setCellValue("title");
    }

    @Override
    public void onUpdate() {

    }

    public void route(Msg msg) {

        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(Time.getFrameCount());
        row.createCell(1).setCellValue(msg.from);
        row.createCell(2).setCellValue(msg.to);
        if(msg.title == "reserve") {
            row.createCell(3).setCellValue(msg.title + " " + msg.to);
        } else {
            row.createCell(3).setCellValue(msg.title);
        }

        SoSObject target = world.findObject(msg.to);
        if(target != null) {
            target.recvMsg(msg);
        }
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

        for(Tile tile: tiles) {
            for(SoSObject obj : tile.getObjects()) {
                if(obj instanceof  FireFighter) {
                    if(obj != sender) {
                        if(Time.getFrameCount() > 10) {
                            int a = 10;
                        }
                        obj.recvMsg(msg);
                    }
                }
            }
        }
    }
}
