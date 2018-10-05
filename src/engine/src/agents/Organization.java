package agents;

import core.Msg;
import core.World;

import java.util.ArrayList;

public class Organization extends CS {

    private final ArrayList<Ambulance> ambulances = new ArrayList<>();
    private final ArrayList<SafeZone> safeZones = new ArrayList<>();

    public Organization(World world, String name) {
        super(world, name);
    }

    private void ambulanceFree(Msg msg) {

    }
    private void patientArrived(Msg msg) {

    }

    @Override
    public void recvMsg(Msg msg) {

    }
}
