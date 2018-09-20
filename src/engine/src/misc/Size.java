package misc;

public class Size {
    public int width;
    public int height;

    public Size() {
        set(0, 0);
    }
    public Size(int width, int height) {
        set(width, height);
    }
    public void set(int width, int height) {
        this.width = width;
        this.height = height;
    }
}

