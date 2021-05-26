package misc;

// simulation.SoSSimulationProgram.java에서 misc.Time 클래스의 콘크리트 클래스 구현
// 그 구현에 update() 메소드가 있고, update 메소드에서 변수들의 값을 업데이트

// 단위: 밀리 세컨드(아마... 까먹... 맞을듯)

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public abstract class Time {

    protected static Time instance;

    // 이전 프레임과 현재 프레임 사이 시간
    protected int deltaTime;

    // 프로그램 실행 후부터 지금까지의 실행 시간
    protected int time;

    // 프레임 수
    protected int frameCount;

    public static int getDeltaTime() {
        return instance.deltaTime;
    }
    public static int getTime() {
        return instance.time;
    }
    public static int getFrameCount() {
        return instance.frameCount;
    }

    // 단위가 밀리 세컨드가 맞으면 수정 안해도 됨
    public static int fromSecond(float second) { return (int)(second * 1000); }

    public static void clear() {
        instance.time = 0;
        instance.frameCount = 0;
    }
}
