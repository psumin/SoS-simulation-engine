package agents;

import core.SoSObject;

public class Patient extends SoSObject<Patient> {

    public Patient() {
    }

    @Override
    public Patient init() {
        loadImage("src/engine/resources/patient30x30.png");

        return this;
    }
}
