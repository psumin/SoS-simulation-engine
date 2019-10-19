package property;

public class MCIProperty extends Property {

  double rescueRate;
  double thresholdPatient;
  public MCIProperty(String name, String specification, String propertyType, double etc) {
    super(name, specification, propertyType);
    this.rescueRate = etc;
  }

  double prob;
  int t;
  int T;
  
  public void setStateProbabilityValues(double prob, int t, int T) {
    this.prob = prob;
    this.t = t;
    this.T = T;
  }
  
  public double getProb() {
    return this.prob;
  }
  public int getT() {
    return this.t;
  }
  public int getTT() {return this.T;}
  
  public void setThresholdPatient(double thresholdPatient) {
    this.thresholdPatient = thresholdPatient;
  }
  
  public double getValue() {
    return this.rescueRate;
  }
  
  public double getThresholdPatient() {return this.thresholdPatient;}
}
