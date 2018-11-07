package core;

import misc.ExcelHelper;
import misc.Position;
import misc.Time;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import stimulus.MessageStimulus.Delay;
import stimulus.MessageStimulus.Loss;

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

    private final ArrayList<Delay> delayConditions = new ArrayList<>();
    private final ArrayList<Loss> lossConditions = new ArrayList<>();

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

    // Bridgehead <-> Organization delay
    private int SZ_TO_ORG_DELAY = 0;
    private int ORG_TO_SZ_DELAY = 0;

    // FF <-> FF message delay at the communication range
    private int FF_RANGECAST_DELAY = 0;

    private int FF_TO_FF_SEND = 0;
    private int FF_TO_FF_RECV = 0;

    private int FF_TO_ORG_SEND = 0;
    private int FF_TO_ORG_RECV = 0;

    private int ORG_TO_FF_SEND = 0;
    private int ORG_TO_FF_RECV = 0;

    private int AMB_TO_ORG_SEND = 0;
    private int AMB_TO_ORG_RECV = 0;

    private int ORG_TO_AMB_SEND = 0;
    private int ORG_TO_AMB_RECV = 0;

    private int BRIDGE_TO_ORG_SEND = 0;
    private int BRIDGE_TO_ORG_RECV = 0;

    private int ORG_TO_BRIDGE_SEND = 0;
    private int ORG_TO_BRIDGE_RECV = 0;


    private int TOTAL_FF_TO_FF_SEND = 0;
    private int TOTAL_FF_TO_FF_RECV = 0;

    private int TOTAL_FF_TO_ORG_SEND = 0;
    private int TOTAL_FF_TO_ORG_RECV = 0;

    private int TOTAL_ORG_TO_FF_SEND = 0;
    private int TOTAL_ORG_TO_FF_RECV = 0;

    private int TOTAL_AMB_TO_ORG_SEND = 0;
    private int TOTAL_AMB_TO_ORG_RECV = 0;

    private int TOTAL_ORG_TO_AMB_SEND = 0;
    private int TOTAL_ORG_TO_AMB_RECV = 0;

    private int TOTAL_BRIDGE_TO_ORG_SEND = 0;
    private int TOTAL_BRIDGE_TO_ORG_RECV = 0;

    private int TOTAL_ORG_TO_BRIDGE_SEND = 0;
    private int TOTAL_ORG_TO_BRIDGE_RECV = 0;




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
        sheet = workbook.createSheet("communications");
        //sheet.trackAllColumnsForAutoSizing();

        currentRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
        Row row = currentRow;

        row.createCell(row.getPhysicalNumberOfCells()).setCellValue("Type");
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(centerAlignmentStyle);


        row.createCell(row.getPhysicalNumberOfCells()).setCellValue("FF->FF");
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(centerAlignmentStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, row.getPhysicalNumberOfCells() - 1, row.getPhysicalNumberOfCells()));
        row.createCell(row.getPhysicalNumberOfCells());
        row.createCell(row.getPhysicalNumberOfCells());

        row.createCell(row.getPhysicalNumberOfCells()).setCellValue("FF->Org");
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(centerAlignmentStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, row.getPhysicalNumberOfCells() - 1, row.getPhysicalNumberOfCells()));
        row.createCell(row.getPhysicalNumberOfCells());
        row.createCell(row.getPhysicalNumberOfCells());

        row.createCell(row.getPhysicalNumberOfCells()).setCellValue("Org->FF");
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(centerAlignmentStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, row.getPhysicalNumberOfCells() - 1, row.getPhysicalNumberOfCells()));
        row.createCell(row.getPhysicalNumberOfCells());
        row.createCell(row.getPhysicalNumberOfCells());

        row.createCell(row.getPhysicalNumberOfCells()).setCellValue("Amb->Org");
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(centerAlignmentStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, row.getPhysicalNumberOfCells() - 1, row.getPhysicalNumberOfCells()));
        row.createCell(row.getPhysicalNumberOfCells());
        row.createCell(row.getPhysicalNumberOfCells());

        row.createCell(row.getPhysicalNumberOfCells()).setCellValue("Org->Amb");
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(centerAlignmentStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, row.getPhysicalNumberOfCells() - 1, row.getPhysicalNumberOfCells()));
        row.createCell(row.getPhysicalNumberOfCells());
        row.createCell(row.getPhysicalNumberOfCells());

        row.createCell(row.getPhysicalNumberOfCells()).setCellValue("Bridge->Org");
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(centerAlignmentStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, row.getPhysicalNumberOfCells() - 1, row.getPhysicalNumberOfCells()));
        row.createCell(row.getPhysicalNumberOfCells());
        row.createCell(row.getPhysicalNumberOfCells());

        row.createCell(row.getPhysicalNumberOfCells()).setCellValue("Org->Bridge");
        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(centerAlignmentStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, row.getPhysicalNumberOfCells() - 1, row.getPhysicalNumberOfCells()));
        row.createCell(row.getPhysicalNumberOfCells());
        row.createCell(row.getPhysicalNumberOfCells());

        currentRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
        row = currentRow;
        row.createCell(0).setCellValue("frame count");

        for (int i = 0; i < 7; ++i) {
            row.createCell(row.getPhysicalNumberOfCells()).setCellValue("Send");
            row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(centerAlignmentStyle);

            row.createCell(row.getPhysicalNumberOfCells()).setCellValue("Received");
            row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(centerAlignmentStyle);

            row.createCell(row.getPhysicalNumberOfCells());
        }
    }

    boolean isFirstUpdate = true;
    @Override
    public void onUpdate() {
        currentRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
        currentRow.createCell(0).setCellValue(Time.getFrameCount());

        ExcelHelper.getCell(currentRow, 1).setCellValue(FF_TO_FF_SEND / 2);
        ExcelHelper.getCell(currentRow, 2).setCellValue(FF_TO_FF_RECV / 2);

        ExcelHelper.getCell(currentRow, 4).setCellValue(FF_TO_ORG_SEND);
        ExcelHelper.getCell(currentRow, 5).setCellValue(FF_TO_ORG_RECV);

        ExcelHelper.getCell(currentRow, 7).setCellValue(ORG_TO_FF_SEND);
        ExcelHelper.getCell(currentRow, 8).setCellValue(ORG_TO_FF_RECV);

        ExcelHelper.getCell(currentRow, 10).setCellValue(AMB_TO_ORG_SEND);
        ExcelHelper.getCell(currentRow, 11).setCellValue(AMB_TO_ORG_RECV);

        ExcelHelper.getCell(currentRow, 13).setCellValue(ORG_TO_AMB_SEND);
        ExcelHelper.getCell(currentRow, 14).setCellValue(ORG_TO_AMB_RECV);

        ExcelHelper.getCell(currentRow, 16).setCellValue(BRIDGE_TO_ORG_SEND);
        ExcelHelper.getCell(currentRow, 17).setCellValue(BRIDGE_TO_ORG_RECV);

        ExcelHelper.getCell(currentRow, 19).setCellValue(ORG_TO_BRIDGE_SEND);
        ExcelHelper.getCell(currentRow, 20).setCellValue(ORG_TO_BRIDGE_RECV);

        FF_TO_FF_SEND = 0;
        FF_TO_FF_RECV = 0;
        FF_TO_ORG_SEND = 0;
        FF_TO_ORG_RECV = 0;
        ORG_TO_FF_SEND = 0;
        ORG_TO_FF_RECV = 0;
        AMB_TO_ORG_SEND = 0;
        AMB_TO_ORG_RECV = 0;
        ORG_TO_AMB_SEND = 0;
        ORG_TO_AMB_RECV = 0;
        BRIDGE_TO_ORG_SEND = 0;
        BRIDGE_TO_ORG_RECV = 0;
        ORG_TO_BRIDGE_SEND = 0;
        ORG_TO_BRIDGE_RECV = 0;

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
//        if(recvColorStyle == null) {
//            recvColorStyle = workbook.createCellStyle();
//            recvColorStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
//            recvColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        }
//
//        //Row row = sheet.createRow(rowNum++);
//        Row row = currentRow;
//        //row.createCell(0).setCellValue(Time.getFrameCount());
//        row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.id);
//        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(recvColorStyle);
//        row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.from);
//        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(recvColorStyle);
//        row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.to);
//        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(recvColorStyle);
//        if(msg.title == "reserve") {
//            row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.title + " " + msg.to);
//            row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(recvColorStyle);
//        } else {
//            row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.title);
//            row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(recvColorStyle);
//        }
//        row.createCell(row.getPhysicalNumberOfCells());

        if(msg.from.startsWith("FF") && msg.to.startsWith("FF")) {
            FF_TO_FF_RECV++;
            TOTAL_FF_TO_FF_RECV++;
        } else if(msg.from.startsWith("FF") && msg.to.startsWith("Org")) {
            FF_TO_ORG_RECV++;
            TOTAL_FF_TO_ORG_RECV++;
        } else if(msg.from.startsWith("Org") && msg.to.startsWith("FF")) {
            ORG_TO_FF_RECV++;
            TOTAL_ORG_TO_FF_RECV++;
        } else if(msg.from.startsWith("Amb") && msg.to.startsWith("Org")) {
            AMB_TO_ORG_RECV++;
            TOTAL_AMB_TO_ORG_RECV++;
        } else if(msg.from.startsWith("Org") && msg.to.startsWith("Amb")) {
            ORG_TO_AMB_RECV++;
            TOTAL_ORG_TO_AMB_RECV++;
        } else if(msg.from.startsWith("Bri") && msg.to.startsWith("Org")) {
            BRIDGE_TO_ORG_RECV++;
            TOTAL_BRIDGE_TO_ORG_RECV++;
        } else if(msg.from.startsWith("Org") && msg.to.startsWith("Bri")) {
            ORG_TO_BRIDGE_RECV++;
            TOTAL_ORG_TO_BRIDGE_RECV++;
        }

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
//        if(sendColorStyle == null) {
//            sendColorStyle = workbook.createCellStyle();
//            sendColorStyle.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
//            sendColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        }
//
//
//        Row row = currentRow;
//        //row.createCell(0).setCellValue(Time.getFrameCount());
//        row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.id);
//        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(sendColorStyle);
//        row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.from);
//        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(sendColorStyle);
//        row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.to);
//        row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(sendColorStyle);
//        if(msg.title == "reserve") {
//            row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.title + " " + msg.to);
//            row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(sendColorStyle);
//        } else {
//            row.createCell(row.getPhysicalNumberOfCells()).setCellValue(msg.title);
//            row.getCell(row.getPhysicalNumberOfCells() - 1).setCellStyle(sendColorStyle);
//        }
//        row.createCell(row.getPhysicalNumberOfCells());

//        FF_TO_FF_SEND = 0;
//        FF_TO_FF_RECV = 0;
//        FF_TO_ORG_SEND = 0;
//        FF_TO_ORG_RECV = 0;
//        ORG_TO_FF_SEND = 0;
//        ORG_TO_FF_RECV = 0;
//        AMB_TO_ORG_SEND = 0;
//        AMB_TO_ORG_RECV = 0;
//        ORG_TO_AMB_SEND = 0;
//        ORG_TO_AMB_RECV = 0;
//        BRIDGE_TO_ORG_SEND = 0;
//        BRIDGE_TO_ORG_RECV = 0;
//        ORG_TO_BRIDGE_SEND = 0;
//        ORG_TO_BRIDGE_RECV = 0;

        if(msg.from.startsWith("FF") && msg.to.startsWith("FF")) {
            FF_TO_FF_SEND++;
            TOTAL_FF_TO_FF_SEND++;
        } else if(msg.from.startsWith("FF") && msg.to.startsWith("Org")) {
            FF_TO_ORG_SEND++;
            TOTAL_FF_TO_ORG_SEND++;
        } else if(msg.from.startsWith("Org") && msg.to.startsWith("FF")) {
            ORG_TO_FF_SEND++;
            TOTAL_ORG_TO_FF_SEND++;
        } else if(msg.from.startsWith("Amb") && msg.to.startsWith("Org")) {
            AMB_TO_ORG_SEND++;
            TOTAL_AMB_TO_ORG_SEND++;
        } else if(msg.from.startsWith("Org") && msg.to.startsWith("Amb")) {
            ORG_TO_AMB_SEND++;
            TOTAL_ORG_TO_AMB_SEND++;
        } else if(msg.from.startsWith("Bri") && msg.to.startsWith("Org")) {
            BRIDGE_TO_ORG_SEND++;
            TOTAL_BRIDGE_TO_ORG_SEND++;
        } else if(msg.from.startsWith("Org") && msg.to.startsWith("Bri")) {
            ORG_TO_BRIDGE_SEND++;
            TOTAL_ORG_TO_BRIDGE_SEND++;
        }




        SoSObject target = world.findObject(msg.to);

        {
            ArrayList<Delay> removes = new ArrayList<>();
            for (Delay condition : delayConditions) {
                if (condition.endFrame <= Time.getFrameCount()) {
                    removes.add(condition);
                }
            }
            delayConditions.removeAll(removes);
        }

        {
            ArrayList<Loss> removes = new ArrayList<>();
            for (Loss condition : lossConditions) {
                if (condition.endFrame <= Time.getFrameCount()) {
                    removes.add(condition);
                }
            }
            lossConditions.removeAll(removes);
        }

        for(Loss condition: lossConditions) {
            if(condition.frame> Time.getFrameCount() + 1) {
                continue;
            }

            boolean senderIsAll = condition.sender.equalsIgnoreCase("ALL");
            boolean receiverIsAll = condition.receiver.equalsIgnoreCase("ALL");

            boolean senderHasDigit = containDigit(condition.sender);
            boolean receiverHasDigit = containDigit(condition.receiver);

            if(senderIsAll && receiverIsAll) {
                // ALL && ALL
                return;
            } else if(senderIsAll) {
                // ALL && Not ALL
                if(receiverHasDigit) {
                    // ALL && FF1
                    if(msg.to.equals(condition.receiver)) {
                        return;
                    }
                } else {
                    // ALL && FF
                    if(msg.to.startsWith(condition.receiver)) {
                        return;
                    }
                }

            } else if(receiverIsAll) {
                // Not ALL && ALL

                if(senderHasDigit) {
                    // FF1 && ALL
                    if(msg.from.equals(condition.sender)) {
                        return;
                    }
                } else {
                    // FF && ALL
                    if(msg.from.startsWith(condition.sender)) {
                        return;
                    }
                }
            } else {
                // Not ALL && Not ALL

                if(senderHasDigit && receiverHasDigit) {
                    // FF1 && FF2
                    if(msg.from.equals(condition.sender) && msg.to.equals(condition.receiver)) {
                        return;
                    }
                } else if(senderHasDigit) {
                    // FF1 && FF
                    if(msg.from.equals(condition.sender) && msg.to.startsWith(condition.receiver)) {
                        return;
                    }
                } else if(receiverHasDigit) {
                    // FF && FF1
                    if(msg.from.startsWith(condition.sender) && msg.to.startsWith(condition.receiver)) {
                        return;
                    }
                } else {
                    // FF && FF
                    if(msg.from.startsWith(condition.sender) && msg.to.startsWith(condition.receiver)) {
                        return;
                    }
                }
            }
        }

        for(Delay condition: delayConditions) {
            if(condition.frame > Time.getFrameCount() + 1) {
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
//        // Bridgehead <-> Org
//        if(SZ_TO_ORG_DELAY > 0 && msg.from.startsWith("Bridgehead") && msg.to.startsWith("Org")) {
//            delayedMsgs.add(new DelayedMsg(msg, SZ_TO_ORG_DELAY + Time.getFrameCount()));
//            return;
//        }
//        if(ORG_TO_SZ_DELAY > 0 && msg.from.startsWith("Org") && msg.to.startsWith("Bridgehead")) {
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
        delayConditions.add(condition);
    }

    public void add(Loss condition) {
        lossConditions.add(condition);
    }

    @Override
    public void clear() {
        currentRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
        currentRow.createCell(0).setCellValue("Total communication");
        ExcelHelper.getCell(currentRow, 1).setCellValue(TOTAL_FF_TO_FF_SEND / 2);
        ExcelHelper.getCell(currentRow, 2).setCellValue(TOTAL_FF_TO_FF_RECV / 2);

        ExcelHelper.getCell(currentRow, 4).setCellValue(TOTAL_FF_TO_ORG_SEND);
        ExcelHelper.getCell(currentRow, 5).setCellValue(TOTAL_FF_TO_ORG_RECV);

        ExcelHelper.getCell(currentRow, 7).setCellValue(TOTAL_ORG_TO_FF_SEND);
        ExcelHelper.getCell(currentRow, 8).setCellValue(TOTAL_ORG_TO_FF_RECV);

        ExcelHelper.getCell(currentRow, 10).setCellValue(TOTAL_AMB_TO_ORG_SEND);
        ExcelHelper.getCell(currentRow, 11).setCellValue(TOTAL_AMB_TO_ORG_RECV);

        ExcelHelper.getCell(currentRow, 13).setCellValue(TOTAL_ORG_TO_AMB_SEND);
        ExcelHelper.getCell(currentRow, 14).setCellValue(TOTAL_ORG_TO_AMB_RECV);

        ExcelHelper.getCell(currentRow, 16).setCellValue(TOTAL_BRIDGE_TO_ORG_SEND);
        ExcelHelper.getCell(currentRow, 17).setCellValue(TOTAL_BRIDGE_TO_ORG_RECV);

        ExcelHelper.getCell(currentRow, 19).setCellValue(TOTAL_ORG_TO_BRIDGE_SEND);
        ExcelHelper.getCell(currentRow, 20).setCellValue(TOTAL_ORG_TO_BRIDGE_RECV);
        super.clear();
    }
}