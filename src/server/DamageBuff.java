package server;
class DamageBuff extends Status{
  int strength;
  private static int ID = 0;
  DamageBuff(int strength, int duration){
    super(duration,ID);
    this.strength = strength;
  }
  public int getStrength(){
    return strength;
  }
}