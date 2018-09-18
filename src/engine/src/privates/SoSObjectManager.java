package privates;

public class SoSObjectManager {
    private static SoSObjectManager ourInstance = new SoSObjectManager();

    public static SoSObjectManager getInstance() {
        return ourInstance;
    }

    private SoSObjectManager() {
    }
}
