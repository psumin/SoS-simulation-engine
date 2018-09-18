package misc;

import interfaces.IDrawable;
import interfaces.IUpdatable;
import misc.Position;

// 시뮬레이션 대상 오브젝트
// 여기서부터 IUpdateable, IDrawable를 구현하기 때문에
// update가 필요없는 오브젝트나, draw가 필요없는 오브젝트들도 빈 함수를 가지게 됨
// 굳이 이걸 깔끔하게 나누려면 나눌 수는 있으나, 그러자면 구조가 너무 복잡해짐
// 일단 간단하게 감
public abstract class SoSObject implements IUpdatable, IDrawable {

    String name = "No Named";

    // 2D라는 전제를 깔고 있음
    // 3D까지 고려하면서 구현하려면 차라리 게임 엔진(ex. 유니티 엔진 등)을 사용하는 것이 낫다 생각함
    Position position = new Position(0, 0);

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    // init()과 clear()를 인터페이스로 빼서 할지 고민.
    // 일단 그냥 감
    public void init() {

    }
    public void clear() {

    }
}
