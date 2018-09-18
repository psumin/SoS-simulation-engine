package base.singletons;

import base.SoSSimulationEngine;

public class Time {
    private static Time ourInstance = new Time();
    public static Time getInstance() {
        return ourInstance;
    }

    // 이전 프레임과 현재 프레임 사이 시간
    // 단위: 밀리 세컨드
    int deltaTime = 0;

    // 프로그램 실행 후부터 지금까지의 시간
    // 단위: 밀리 세컨드
    int time = 0;

    // 프로그램 실행 후부터 지금까지 프레임 수
    int frameCount = 0;

    private Time() {
    }

    // SoSSimulationEngine 에서만 호출해야함
    public void update(int deltaTime) {
        this.deltaTime = deltaTime;
        this.time += deltaTime;
        this.frameCount++;
    }

    public int getDeltaTime() {
        return deltaTime;
    }
    public int getTime() {
        return time;
    }
    public int getFrameCount() {
        return frameCount;
    }
}
