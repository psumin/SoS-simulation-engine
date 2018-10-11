package core;

import misc.Position;
import misc.Time;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class MsgRouter extends SoSObject {
    // For the Simulation
    // All delay
    private int ALL_DELAY = 0;

    // TO: Org delay
    private int TO_ORG_DELAY = 0;

    // FROM: Org delay
    private int FROM_ORG_DELAY = 0;

    // FF <-> Organization delay
    private int FF_TO_ORG_DELAY = 0;
    private int ORG_TO_FF_DELAY = 0;

    // Ambulance <-> Organization delay
    private int AMB_TO_ORG_DELAY = 0;
    private int ORG_TO_AMB_DELAY = 0;

    // SafeZone <-> Organization delay
    private int SZ_TO_ORG_DELAY = 0;
    private int ORG_TO_SZ_DELAY = 0;

    // FF <-> FF message delay at the communication range
    private int FF_RANGECAST_DELAY = 0;

    private class DelayedMsg {
        public Msg source;
        public int delayedTime;

        public DelayedMsg() {

        }
        public DelayedMsg(Msg src, int delayedTime) {
            this.source = src;
            this.delayedTime = delayedTime;
        }
    }

    private final Queue<DelayedMsg> delayedMsgs = new LinkedList<>();


    World world;
    Map worldMap;

    private Workbook workbook;
    private Sheet sheet;
    private int rowNum = 0;

    // For the log. Save at the Excel
    public MsgRouter(World world, Workbook workbook) {
        CellStyle centerAlignmentStyle = workbook.createCellStyle();
        centerAlignmentStyle.setAlignment(HorizontalAlignment.CENTER);

        this.world = world;
        worldMap = world.getMap();

        this.workbook = workbook;
        this.sheet = workbook.createSheet("communications");;

        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("frame count");

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));

        row.createCell(1).setCellValue("send messages");
        row.getCell(1).setCellStyle(centerAlignmentStyle);

        row.createCell(4).setCellValue("recv messages");
        row.getCell(4).setCellStyle(centerAlignmentStyle);

        row = sheet.createRow(rowNum++);
        row.createCell(1).setCellValue("from");
        row.getCell(1).setCellStyle(centerAlignmentStyle);
        row.createCell(2).setCellValue("to");
        row.getCell(2).setCellStyle(centerAlignmentStyle);
        row.createCell(3).setCellValue("title");
        row.getCell(3).setCellStyle(centerAlignmentStyle);
        row.createCell(4).setCellValue("from");
        row.getCell(4).setCellStyle(centerAlignmentStyle);
        row.createCell(5).setCellValue("to");
        row.getCell(5).setCellStyle(centerAlignmentStyle);
        row.createCell(6).setCellValue("title");
        row.getCell(6).setCellStyle(centerAlignmentStyle);
    }

    @Override
    public void onUpdate() {
        delayedMsgs.add(null);
        while(true) {
            DelayedMsg delayedMsg = delayedMsgs.poll();
            if(delayedMsg == null) break;

            if(Time.getFrameCount() == delayedMsg.delayedTime) {
                _route(delayedMsg.source);
            } else {
                delayedMsgs.add(delayedMsg);
            }
        }
    }

    CellStyle recvColorStyle = workbook.createCellStyle();
    private void _route(Msg msg) {
        // recv log
        recvColorStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        recvColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(Time.getFrameCount());
        row.createCell(4).setCellValue(msg.from);
        row.getCell(4).setCellStyle(recvColorStyle);
        row.createCell(5).setCellValue(msg.to);
        row.getCell(5).setCellStyle(recvColorStyle);
        if(msg.title == "reserve") {
            row.createCell(6).setCellValue(msg.title + " " + msg.to);
            row.getCell(6).setCellStyle(recvColorStyle);
        } else {
            row.createCell(6).setCellValue(msg.title);
            row.getCell(6).setCellStyle(recvColorStyle);
        }

        SoSObject target = world.findObject(msg.to);
        if(target != null) {
            target.recvMsg(msg);
        }
    }

    CellStyle sendColorStyle = workbook.createCellStyle();
    public void route(Msg msg) {
        // send log
        sendColorStyle = workbook.createCellStyle();
        sendColorStyle.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
        sendColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(Time.getFrameCount());
        row.createCell(1).setCellValue(msg.from);
        row.getCell(1).setCellStyle(sendColorStyle);
        row.createCell(2).setCellValue(msg.to);
        row.getCell(2).setCellStyle(sendColorStyle);
        if(msg.title == "reserve") {
            row.createCell(3).setCellValue(msg.title + " " + msg.to);
            row.getCell(3).setCellStyle(sendColorStyle);
        } else {
            row.createCell(3).setCellValue(msg.title);
            row.getCell(3).setCellStyle(sendColorStyle);
        }

        SoSObject target = world.findObject(msg.to);

        // FF <-> Org
        if(FF_TO_ORG_DELAY > 0 && msg.from.startsWith(World.fireFighterPrefix) && msg.to.startsWith("Org")) {
            delayedMsgs.add(new DelayedMsg(msg, FF_TO_ORG_DELAY + Time.getFrameCount()));
            return;
        }
        if(ORG_TO_FF_DELAY > 0 && msg.from.startsWith("Org") && msg.to.startsWith(World.fireFighterPrefix)) {
            delayedMsgs.add(new DelayedMsg(msg, ORG_TO_FF_DELAY + Time.getFrameCount()));
            return;
        }

        // Ambulance <-> Org
        if(AMB_TO_ORG_DELAY > 0 && msg.from.startsWith("Amb") && msg.to.startsWith("Org")) {
            delayedMsgs.add(new DelayedMsg(msg, AMB_TO_ORG_DELAY + Time.getFrameCount()));
            return;
        }
        if(ORG_TO_AMB_DELAY > 0 && msg.from.startsWith("Org") && msg.to.startsWith("Amb")) {
            delayedMsgs.add(new DelayedMsg(msg, ORG_TO_AMB_DELAY + Time.getFrameCount()));
            return;
        }

        // SafeZone <-> Org
        if(SZ_TO_ORG_DELAY > 0 && msg.from.startsWith("SafeZone") && msg.to.startsWith("Org")) {
            delayedMsgs.add(new DelayedMsg(msg, SZ_TO_ORG_DELAY + Time.getFrameCount()));
            return;
        }
        if(ORG_TO_SZ_DELAY > 0 && msg.from.startsWith("Org") && msg.to.startsWith("SafeZone")) {
            delayedMsgs.add(new DelayedMsg(msg, ORG_TO_SZ_DELAY + Time.getFrameCount()));
            return;
        }

        if(TO_ORG_DELAY > 0 && msg.to.startsWith("Org")) {
            delayedMsgs.add(new DelayedMsg(msg, TO_ORG_DELAY + Time.getFrameCount()));
            return;
        }

        if(FROM_ORG_DELAY > 0 && msg.from.startsWith("Org")) {
            delayedMsgs.add(new DelayedMsg(msg, FROM_ORG_DELAY + Time.getFrameCount()));
            return;
        }

        if(FF_RANGECAST_DELAY > 0 && msg.from.startsWith(World.fireFighterPrefix) && msg.to.startsWith(World.fireFighterPrefix)) {
            delayedMsgs.add(new DelayedMsg(msg, FF_RANGECAST_DELAY + Time.getFrameCount()));
            return;
        }

        // All Delay
        if(ALL_DELAY > 0) {
            delayedMsgs.add(new DelayedMsg(msg, ALL_DELAY + Time.getFrameCount()));
            return;
        }

        _route(msg);
    }

    // Broadcast the message only for the firefighter. (share the local map and the knowledge about the patients)
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
            tile.fireFighters.forEach(fireFighter -> {
                if(fireFighter != sender) {

                    Msg copiedMsg = new Msg()
                            .setFrom(msg.from)
                            .setTo(fireFighter.name)
                            .setTitle(msg.title)
                            .setData(msg.data);

                    route(copiedMsg);
                    //
                    //fireFighter.recvMsg(msg);
                }
            });
        }
    }
}
