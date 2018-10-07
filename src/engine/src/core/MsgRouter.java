package core;

import agents.FireFighter;
import misc.Position;
import misc.Time;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MsgRouter extends SoSObject {

    // 전체 딜레이
    private int ALL_DELAY = 0;

    // TO: Org 딜레이
    private int TO_ORG_DELAY = 0;

    // FROM: Org 딜레이
    private int FROM_ORG_DELAY = 0;

    // FF <-> Organization
    private int FF_TO_ORG_DELAY = 0;
    private int ORG_TO_FF_DELAY = 0;

    // Ambulance <-> Organization
    private int AMB_TO_ORG_DELAY = 0;
    private int ORG_TO_AMB_DELAY = 0;

    // SafeZone <-> Organization
    private int SZ_TO_ORG_DELAY = 0;
    private int ORG_TO_SZ_DELAY = 0;

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

    private void _route(Msg msg) {
        // recv log
        CellStyle colorStyle = workbook.createCellStyle();

        colorStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        colorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(Time.getFrameCount());
        row.createCell(4).setCellValue(msg.from);
        row.getCell(4).setCellStyle(colorStyle);
        row.createCell(5).setCellValue(msg.to);
        row.getCell(5).setCellStyle(colorStyle);
        if(msg.title == "reserve") {
            row.createCell(6).setCellValue(msg.title + " " + msg.to);
            row.getCell(6).setCellStyle(colorStyle);
        } else {
            row.createCell(6).setCellValue(msg.title);
            row.getCell(6).setCellStyle(colorStyle);
        }

        SoSObject target = world.findObject(msg.to);
        if(target != null) {
            target.recvMsg(msg);
        }
    }

    public void route(Msg msg) {
        // send log
        CellStyle colorStyle = workbook.createCellStyle();
        colorStyle.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
        colorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(Time.getFrameCount());
        row.createCell(1).setCellValue(msg.from);
        row.getCell(1).setCellStyle(colorStyle);
        row.createCell(2).setCellValue(msg.to);
        row.getCell(2).setCellStyle(colorStyle);
        if(msg.title == "reserve") {
            row.createCell(3).setCellValue(msg.title + " " + msg.to);
            row.getCell(3).setCellStyle(colorStyle);
        } else {
            row.createCell(3).setCellValue(msg.title);
            row.getCell(3).setCellStyle(colorStyle);
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

        // All Delay
        if(ALL_DELAY > 0) {
            delayedMsgs.add(new DelayedMsg(msg, ALL_DELAY + Time.getFrameCount()));
            return;
        }

        _route(msg);
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
            tile.fireFighters.forEach(fireFighter -> {
                if(fireFighter != sender) {
                    fireFighter.recvMsg(msg);
                }
            });
        }
    }
}
