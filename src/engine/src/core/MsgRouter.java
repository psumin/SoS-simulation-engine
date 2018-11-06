package core;

import misc.Position;
import misc.Time;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import stimulus.MessageStimulus.Delay;

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

    private final ArrayList<Delay> conditions = new ArrayList<>();

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

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private int rowNum = 0;

    private Row currentRow;
    CellStyle centerAlignmentStyle;

    // For the log. Save at the Excel
    public MsgRouter(World world, XSSFWorkbook workbook) {
        centerAlignmentStyle = workbook.createCellStyle();
        centerAlignmentStyle.setAlignment(HorizontalAlignment.CENTER);

        this.world = world;
        worldMap = world.getMap();

        this.workbook = workbook;
        sheet = workbook.createSheet("communications");;
        //sheet.trackAllColumnsForAutoSizing();

        currentRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
        Row row = currentRow;
        row.createCell(0).setCellValue("frame count");

//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));
//
//        row.createCell(1).setCellValue("send messages");
//        row.getCell(1).setCellStyle(centerAlignmentStyle);
//
//        row.createCell(4).setCellValue("recv messages");
//        row.getCell(4).setCellStyle(centerAlignmentStyle);

        for(int i = 0; i < 100; ++i) {
            row.createCell(row.getPhysicalNumberOfCells()).setCellValue("No.");
            row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(centerAlignmentStyle);
            row.createCell(row.getPhysicalNumberOfCells()).setCellValue("from");
            row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(centerAlignmentStyle);
            row.createCell(row.getPhysicalNumberOfCells()).setCellValue("to");
            row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(centerAlignmentStyle);
            row.createCell(row.getPhysicalNumberOfCells()).setCellValue("title");

            row.createCell(row.getPhysicalNumberOfCells());
        }
    }

    boolean isFirstUpdate = true;
    @Override
    public void onUpdate() {
        currentRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
        currentRow.createCell(0).setCellValue(Time.getFrameCount());

        ArrayList<Delay> recoveries = new ArrayList<>();
        for(Delay condition: conditions) {
            if(condition.frame < Time.getFrameCount() && condition.delay == 0) {
                recoveries.add(condition);
            }
        }

        for(Delay recovery: recoveries) {
            ArrayList<Delay> removes = new ArrayList<>();
            for(Delay condition: conditions) {
                if(condition.frame > recovery.frame)
                    continue;

                if(condition.sender.equals(recovery.sender) && condition.receiver.equals(recovery.receiver)) {
                    removes.add(condition);
                    removes.add(recovery);
                }
            }

            conditions.removeAll(removes);
        }

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

    CellStyle recvColorStyle;
    private void _route(Msg msg) {
        // recv log
        if(recvColorStyle == null) {
            recvColorStyle = workbook.createCellStyle();
            recvColorStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            recvColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        //Row row = sheet.createRow(rowNum++);
        Row row = currentRow;
        //row.createCell(0).setCellValue(Time.getFrameCount());
        row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.id);
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(recvColorStyle);
        row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.from);
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(recvColorStyle);
        row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.to);
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(recvColorStyle);
        if(msg.title == "reserve") {
            row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.title + " " + msg.to);
            row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(recvColorStyle);
        } else {
            row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.title);
            row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(recvColorStyle);
        }
        row.createCell(row.getPhysicalNumberOfCells());

        SoSObject target = world.findObject(msg.to);
        if(target != null) {
            target.recvMsg(msg);
        }
    }

    private boolean containDigit(String str) {
        return str.matches(".*\\d+.*");
    }

    CellStyle sendColorStyle;
    public void route(Msg msg) {
        // send log
        if(sendColorStyle == null) {
            sendColorStyle = workbook.createCellStyle();
            sendColorStyle.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
            sendColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }


        Row row = currentRow;
        //row.createCell(0).setCellValue(Time.getFrameCount());
        row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.id);
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(sendColorStyle);
        row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.from);
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(sendColorStyle);
        row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.to);
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(sendColorStyle);
        if(msg.title == "reserve") {
            row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.title + " " + msg.to);
            row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(sendColorStyle);
        } else {
            row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.title);
            row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(sendColorStyle);
        }
        row.createCell(row.getPhysicalNumberOfCells());

        SoSObject target = world.findObject(msg.to);

        for(Delay condition: conditions) {
            if(condition.frame > Time.getFrameCount()) {
                continue;
            }

            boolean senderIsAll = condition.sender.equalsIgnoreCase("ALL");
            boolean receiverIsAll = condition.receiver.equalsIgnoreCase("ALL");

            boolean senderHasDigit = containDigit(condition.sender);
            boolean receiverHasDigit = containDigit(condition.receiver);

            if(senderIsAll && receiverIsAll) {
                // ALL && ALL
                delayedMsgs.add(new DelayedMsg(msg, condition.delay + Time.getFrameCount()));
                return;
            } else if(senderIsAll) {
                // ALL && Not ALL
                if(receiverHasDigit) {
                    // ALL && FF1
                    if(msg.to.equals(condition.receiver)) {
                        delayedMsgs.add(new DelayedMsg(msg, condition.delay + Time.getFrameCount()));
                        return;
                    }
                } else {
                    // ALL && FF
                    if(msg.to.startsWith(condition.receiver)) {
                        delayedMsgs.add(new DelayedMsg(msg, condition.delay + Time.getFrameCount()));
                        return;
                    }
                }

            } else if(receiverIsAll) {
                // Not ALL && ALL

                if(senderHasDigit) {
                    // FF1 && ALL
                    if(msg.from.equals(condition.sender)) {
                        delayedMsgs.add(new DelayedMsg(msg, condition.delay + Time.getFrameCount()));
                        return;
                    }
                } else {
                    // FF && ALL
                    if(msg.from.startsWith(condition.sender)) {
                        delayedMsgs.add(new DelayedMsg(msg, condition.delay + Time.getFrameCount()));
                        return;
                    }
                }
            } else {
                // Not ALL && Not ALL

                if(senderHasDigit && receiverHasDigit) {
                    // FF1 && FF2
                    if(msg.from.equals(condition.sender) && msg.to.equals(condition.receiver)) {
                        delayedMsgs.add(new DelayedMsg(msg, condition.delay + Time.getFrameCount()));
                        return;
                    }
                } else if(senderHasDigit) {
                    // FF1 && FF
                    if(msg.from.equals(condition.sender) && msg.to.startsWith(condition.receiver)) {
                        delayedMsgs.add(new DelayedMsg(msg, condition.delay + Time.getFrameCount()));
                        return;
                    }
                } else if(receiverHasDigit) {
                    // FF && FF1
                    if(msg.from.startsWith(condition.sender) && msg.to.startsWith(condition.receiver)) {
                        delayedMsgs.add(new DelayedMsg(msg, condition.delay + Time.getFrameCount()));
                        return;
                    }
                } else {
                    // FF && FF
                    if(msg.from.startsWith(condition.sender) && msg.to.startsWith(condition.receiver)) {
                        delayedMsgs.add(new DelayedMsg(msg, condition.delay + Time.getFrameCount()));
                        return;
                    }
                }
            }
        }

//
//        // FF <-> Org
//        if(FF_TO_ORG_DELAY > 0 && msg.from.startsWith(World.fireFighterPrefix) && msg.to.startsWith("Org")) {
//            delayedMsgs.add(new DelayedMsg(msg, FF_TO_ORG_DELAY + Time.getFrameCount()));
//            return;
//        }
//        if(ORG_TO_FF_DELAY > 0 && msg.from.startsWith("Org") && msg.to.startsWith(World.fireFighterPrefix)) {
//            delayedMsgs.add(new DelayedMsg(msg, ORG_TO_FF_DELAY + Time.getFrameCount()));
//            return;
//        }
//
//        // Ambulance <-> Org
//        if(AMB_TO_ORG_DELAY > 0 && msg.from.startsWith("Amb") && msg.to.startsWith("Org")) {
//            delayedMsgs.add(new DelayedMsg(msg, AMB_TO_ORG_DELAY + Time.getFrameCount()));
//            return;
//        }
//        if(ORG_TO_AMB_DELAY > 0 && msg.from.startsWith("Org") && msg.to.startsWith("Amb")) {
//            delayedMsgs.add(new DelayedMsg(msg, ORG_TO_AMB_DELAY + Time.getFrameCount()));
//            return;
//        }
//
//        // SafeZone <-> Org
//        if(SZ_TO_ORG_DELAY > 0 && msg.from.startsWith("SafeZone") && msg.to.startsWith("Org")) {
//            delayedMsgs.add(new DelayedMsg(msg, SZ_TO_ORG_DELAY + Time.getFrameCount()));
//            return;
//        }
//        if(ORG_TO_SZ_DELAY > 0 && msg.from.startsWith("Org") && msg.to.startsWith("SafeZone")) {
//            delayedMsgs.add(new DelayedMsg(msg, ORG_TO_SZ_DELAY + Time.getFrameCount()));
//            return;
//        }
//
//        if(TO_ORG_DELAY > 0 && msg.to.startsWith("Org")) {
//            delayedMsgs.add(new DelayedMsg(msg, TO_ORG_DELAY + Time.getFrameCount()));
//            return;
//        }
//
//        if(FROM_ORG_DELAY > 0 && msg.from.startsWith("Org")) {
//            delayedMsgs.add(new DelayedMsg(msg, FROM_ORG_DELAY + Time.getFrameCount()));
//            return;
//        }
//
//        if(FF_RANGECAST_DELAY > 0 && msg.from.startsWith(World.fireFighterPrefix) && msg.to.startsWith(World.fireFighterPrefix)) {
//            delayedMsgs.add(new DelayedMsg(msg, FF_RANGECAST_DELAY + Time.getFrameCount()));
//            return;
//        }
//
//        // All Delay
//        if(ALL_DELAY > 0) {
//            delayedMsgs.add(new DelayedMsg(msg, ALL_DELAY + Time.getFrameCount()));
//            return;
//        }

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
                if(fireFighter.currentAction.name.startsWith("Removed") || fireFighter.currentAction.name.startsWith("Dead")) {

                } else if(fireFighter != sender) {

                    Msg copiedMsg = new Msg()
                            .setFrom(msg.from)
                            .setTo(fireFighter.name)
                            .setTitle(msg.title)
                            .setData(msg.data);
                    copiedMsg.id--;
                    copiedMsg.idCounter--;

                    route(copiedMsg);
                    //
                    //fireFighter.recvMsg(msg);
                }
            });
        }
    }

    public void add(Delay condition) {
        conditions.add(condition);
    }
}
