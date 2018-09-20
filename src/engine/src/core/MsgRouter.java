package core;

import java.util.LinkedList;
import java.util.Queue;

public class MsgRouter {
    static MsgRouter current;

    Queue<Msg> msgQueue = new LinkedList<>();
}
