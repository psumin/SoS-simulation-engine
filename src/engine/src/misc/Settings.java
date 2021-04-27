package misc;

public class Settings {
    private Settings(){}
    //singleton class
    private static Settings settingsInstance = new Settings();

    private int maxPatient = 20;
    private int maxFireFighter = 40;
    private int maxHospital = 4;
    private int maxAmbulance = 40;
    private int maxBridgehead = 4;

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
}
