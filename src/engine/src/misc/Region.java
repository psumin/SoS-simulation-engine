package misc;

import javafx.geometry.Pos;

public class Region {
    // 왼쪽 위
    public Position from;

    // 오른쪽 아래
    public Position to;

    public Region(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    public void move(Position offset) {
        from.move(offset);
        to.move(offset);
    }
}
