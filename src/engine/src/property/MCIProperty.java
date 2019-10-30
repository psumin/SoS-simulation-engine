package property;

public class MCIProperty extends Property {

  double rescueRate;
  double thresholdValue;
  String prevState;
  String state;
  
  public MCIProperty(String name, String specification, String propertyType, double etc) {
    super(name, specification, propertyType);
    this.rescueRate = etc;
  }

  double prob;
  int t;
  int T;
  int duration;
  
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
  
  public void setDuration(int duration) {
    this.duration = duration;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  public void setThresholdValue(double thresholdV) {
    this.thresholdValue = thresholdV;
  }
  
  public double getValue() {
    return this.rescueRate;
  }
  
  public double getThresholdValue() {return this.thresholdValue;}
  
  public void setState(String state) { this.state = state; }
  
  public void setPrevState(String state) {this.prevState = state; }
  
  public String getState() { return this.state; }
  
  public String getPrevState() {return this.prevState; }
}
