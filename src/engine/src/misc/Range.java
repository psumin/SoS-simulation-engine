package misc;

public class Range {
    public Position begin;
    public Position end;

    public Range(Position begin, Position end) {
        this.begin = begin;
        this.end = end;
    }
    public Range(int left, int top, int right, int bottom) {
        this.begin = new Position(left, top);
        this.end = new Position(right, bottom);
    }
}
