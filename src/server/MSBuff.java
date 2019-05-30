package server;
class MSBuff extends Status{
  int strength;
  MSBuff(int strength, int duration){
    super(duration);
    this.strength = strength;
  }
  public int getStrength(){
    return strength;
  }
}