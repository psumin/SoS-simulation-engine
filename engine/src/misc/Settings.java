package misc;

public class Settings {
    private Settings(){}
    //singleton class
    private static Settings settingsInstance = new Settings();

    private int maxPatient = 200;
    private int maxFireFighter = 12;
    private int maxHospital = 4;
    private int maxAmbulance = 4;
    private int maxBridgehead = 4;
    private int maxFrameCount = 800;
    private boolean expertMode = false;

    public static Settings getSettingsInstance(){return settingsInstance;}

    public int getMaxPatient() {
        return maxPatient;
    }

    public int getMaxFireFighter() {
        return maxFireFighter;
    }

    public int getMaxHospital() {
        return maxHospital;
    }

    public int getMaxAmbulance() {
        return maxAmbulance;
    }

    public int getMaxBridgehead() {
        return maxBridgehead;
    }

    public boolean getExpertMode() { return expertMode; }

    public int getMaxFrameCount() { return maxFrameCount; }

    public void setMaxPatient(int maxPatient) {
        this.maxPatient = maxPatient;
    }

    public void setMaxFireFighter(int maxFireFighter) {
        this.maxFireFighter = maxFireFighter;
    }

    public void setMaxHospital(int maxHospital) {
        this.maxHospital = maxHospital;
    }

    public void setMaxAmbulance(int maxAmbulance) {
        this.maxAmbulance = maxAmbulance;
    }

    public void setMaxBridgehead(int maxBridgehead) {
        this.maxBridgehead = maxBridgehead;
    }

    public void setExpertMode(boolean expertMode){ this.expertMode = expertMode; }

    public void setMaxFrameCount(int maxFrameCount){ this.maxFrameCount = maxFrameCount; }
}
