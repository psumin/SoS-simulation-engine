package misc;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

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

