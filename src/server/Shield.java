package server;
class Shield{
  private int strength;
  private int duration;
  private int lifetime;
  Shield(int strength, int duration){
    this.strength = strength;
    this.duration = duration;
  }
  public int getStrength(){
    return strength;
  }
  public int getRemainingDuration(){
    return duration-lifetime;
  }
  public void damage(int damage){
    strength -= damage;
  }
  public void advance(){
    lifetime++;
  }
}