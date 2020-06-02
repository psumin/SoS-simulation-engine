package core;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class DataStructure {

    public static class AddCS {
        public String command;
        public int frame;
        public int count;

        public AddCS(String command, int frame, int count) {
            this.command = command;
            this.frame = frame;
            this.count = count;
        }
    }

    // Structure o message stimuli
    public static class Message {
        public String command;
        public int startFrame;
        public int finishFrame;
        public String sender;
        public String receiver;
        public int duration;

        public Message(String command, int startFrame, int finishFrame, String sender, String receiver, int duration) {
            this.command = command;
            this.startFrame = startFrame;
            this.finishFrame = finishFrame;
            this.sender = sender;
            this.receiver = receiver;
            this.duration = duration;
        }
    }

    public static class Range {
        public String command;
        public int frame;
        public int left;
        public int top;
        public int right;
        public int bottom;
        public Object value;

        public Range(String command, int frame, int left, int top, int right, int bottom, Object value) {
            this.command = command;
            this. frame = frame;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.value = value;
        }
    }

    public static class ChangeAll {
        public String command;
        public int frame;
        public Object value;

        public ChangeAll(String command, int frame, Object value) {
            this.command = command;
            this.frame = frame;
            this.value = value;
        }
    }

    public static class ChangeOne {
        public String command;
        public int frame;
        public int number;
        public Object value;

        public ChangeOne(String command, int frame, int number, Object value) {
            this.command = command;
            this.frame = frame;
            this.number = number;
            this.value = value;
        }
    }

    public static class RemoveCS {
        public String command;
        public int frame;
        public int number;

        public RemoveCS(String command, int frame, int number) {
            this.command = command;
            this.frame = frame;
            this.number = number;
        }
    }

}
