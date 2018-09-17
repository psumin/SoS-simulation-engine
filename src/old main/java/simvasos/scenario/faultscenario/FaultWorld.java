package simvasos.scenario.faultscenario;

import com.sun.xml.internal.ws.wsdl.writer.document.Fault;
import simvasos.scenario.mciresponse.MCIResponseScenario.SoSType;
import simvasos.scenario.mciresponse.MCIResponseWorld;
import simvasos.simulation.component.Message;

import java.util.*;

public class FaultWorld extends MCIResponseWorld {

    enum FaultType { None, DelaySuccessMessage, DelayFailMessage, RemoveMessage }

    private class FaultRange {
        int startTick;
        int endTick;
        FaultType faultType;

        public FaultRange(int startTick, int endTick, FaultType faultType) {
            setStartTick(startTick);
            setEndTick(endTick);
            setFaultType(faultType);
        }

        public FaultRange(int tick, FaultType faultType) {
            setStartTick(tick);
            setEndTick(tick + 1);
            setFaultType(faultType);
        }

        public void setStartTick(int tick) {
            startTick = tick;
        }
        public int getStartTick() {
            return startTick;
        }

        public void setEndTick(int tick) {
            endTick = tick;
        }
        public int getEndTick() {
            return endTick;
        }

        public void setFaultType(FaultType type) {
            faultType = type;
        }
        public FaultType getFaultType() {
            return faultType;
        }

        // [start, end)
        public boolean IsBetween(int tick) {
            return startTick <= tick && tick < endTick;
        }
    }

    private class DelayedMessage {
        Message msg;
        int arrivalTime;

        public DelayedMessage(Message msg, int arrivalTime) {
            this.msg = msg;
            this.arrivalTime = arrivalTime;
        }
    }

    private ArrayList<FaultRange> faultRanges = new ArrayList<>();
    private ArrayList<DelayedMessage> delayedMessages = new ArrayList<DelayedMessage>();

    int delay;

    public FaultWorld(SoSType type, int nPatient) {
        super(type, nPatient);
        setFaultRanges();
    }

    private void setFaultRanges() {
// examples
//        faultRanges.add(new FaultRange(100, 200, FaultType.DelaySuccessMessage));
//        faultRanges.add(new FaultRange(200, 300, FaultType.RemoveMessage));
//        faultRanges.add(new FaultRange(400, FaultType.DelaySuccessMessage));
        //faultRanges.add(new FaultRange(200, 300, FaultType.RemoveMessage));
        //faultRanges.add(new FaultRange(50, 300, FaultType.DelaySuccessMessage));
        faultRanges.add(new FaultRange(0, 200, FaultType.DelayFailMessage));
    }


    @Override
    public void reset() {
        if(delayedMessages != null) {
            delayedMessages.clear();
        }
        if(faultRanges != null) {
            faultRanges.clear();
            setFaultRanges();
        }
        super.reset();
    }

    @Override
    public void progress(int time) {

        assert time == 1 : "현재 이 함수는 time이 1이라는 전제를 바탕으로 작성되었음";

        // 삭제 대기열과 관련한 코드는 성능을 개선시킬 여지가 있다고 생각됨.

        // 삭제 대기열
        ArrayList<DelayedMessage> mustRemoveMsgs = new ArrayList<DelayedMessage>();
        for(DelayedMessage delayedMsg : delayedMessages) {
            if(delayedMsg.arrivalTime == this.time) {
                // 삭제 대기열에 추가
                mustRemoveMsgs.add(delayedMsg);

                super.sendMessage(delayedMsg.msg);
                //System.out.println("tick: " + this.time + " msg arrived");
            }
        }

        // 삭제 대기열에 넣어둔 delayedMsg 들을 delayedMessages에서 제거
        for(DelayedMessage mustRemoveMsg : mustRemoveMsgs) {
            delayedMessages.remove(mustRemoveMsg);
        }

        super.progress(time);
    }

    @Override
    public void sendMessage(Message msg) {

        boolean isFaulted = false;
        ArrayList<FaultRange> mustRemoveRanges = new ArrayList<>();
        for(FaultRange range : faultRanges) {
            if(range.getEndTick() <= this.time) {
                mustRemoveRanges.add(range);
                continue;
            }

            if(range.IsBetween(this.time)) {
                isFaulted = true;
                switch (range.getFaultType()) {
                    case DelayFailMessage:
                    case DelaySuccessMessage:
                        if(delay != 0) {
                            int arrivalTime = this.time + delay;
                            if (range.getFaultType() == FaultType.DelaySuccessMessage
                                    && arrivalTime > range.getEndTick()) {
                                arrivalTime = range.getEndTick();
                            }
                            delayedMessages.add(new DelayedMessage(msg, arrivalTime));
                            //System.out.println("tick: " + this.time + " arrivalTime: " + arrivalTime + " msg type: " + msg.getPurpose());
                        } else {
                            super.sendMessage(msg);
                        }
                        break;
                    case RemoveMessage:
                        break;
                }
                break;
            }
        }

        for(FaultRange range : mustRemoveRanges) {
            faultRanges.remove(range);
        }

        if(isFaulted == false) {
            super.sendMessage(msg);
        }

        // 특정 조건(ex. 확률)을 만족 시키는 경우, delay를 걸어줌
//        boolean isDelay = true;
//        if(isDelay) {
//
//            // 지연 구간
//            int startTick = 100;
//            int endTick = 200;
//
//            // 딜레이 몇 초?
//            int delay = 10;
//
//            if(startTick <= this.time && this.time <= endTick) {
//                delayedMessages.add(new DelayedMessage(msg, delay));
//            }
//        } else {
//            super.sendMessage(msg);
//        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
    public int getDelay() {
        return delay;
    }
}