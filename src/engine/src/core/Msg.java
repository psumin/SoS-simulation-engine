package core;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Msg {

    public static int idCounter = 0;

    public int id;
    public String from;
    public String to;
    public String title;
    public Object data;

    public Msg() {
        id = idCounter++;
    }

    public Msg(String from, String to, String title, Object data) {
        id = idCounter++;
        setFrom(from)
                .setTo(to)
                .setTitle(title)
                .setData(data);
    }

    public Msg setFrom(String from) {
        this.from = from;
        return this;
    }

    public Msg setTo(String to) {
        this.to = to;
        return this;
    }
    public Msg setTitle(String title) {
        this.title = title;
        return this;
    }
    public Msg setData(Object data) {
        this.data = data;
        return this;
    }
}
