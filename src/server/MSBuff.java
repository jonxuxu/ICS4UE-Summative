package server;
class MSBuff extends Status{
  int strength;
  private static int ID = 8;
  MSBuff(int strength, int duration){
    super(duration, ID);
    this.strength = strength;
  }
  public int getStrength(){
    return strength;
  }
}