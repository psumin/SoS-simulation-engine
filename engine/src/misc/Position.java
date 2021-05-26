package misc;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Position {
    public int x;
    public int y;

    public Position() {
        set(0, 0);
    }
    public Position(int x, int y) {
        set(x, y);
    }
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void set(Position other) {
        set(other.x, other.y);
    }

    public Position left() {
        return new Position(x - 1, y);
    }
    public Position right() {
        return new Position(x + 1, y);
    }
    public Position up() {
        return new Position(x, y - 1);
    }
    public Position down() {
        return new Position(x, y + 1);
    }

    public void move(Position offset) {
        x += offset.x;
        y += offset.y;
    }

    @Override
    public String toString() {
        String value = "";
        value += x + ",";
        value += y;
        return value;
    }
}
