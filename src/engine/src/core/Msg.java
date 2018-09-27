package core;

public class Msg {

    public String from;
    public String to;
    public String title;
    public Object data;

    public Msg() {

    }

    public Msg(String from, String to, String title, Object data) {
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
