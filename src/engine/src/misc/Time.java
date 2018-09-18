// SoSSimulationEngine.java에서 Time 클래스의 콘크리트 클래스 구현
// 그 구현에 update() 메소드가 있고, update 메소드에서 변수들의 값을 업데이트
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
}
