package server;
class DamageBuff extends Status{
  int strength;
  DamageBuff(int strength, int duration){
    super(duration);
    this.strength = strength;
  }
  public int getStrength(){
    return strength;
  }
}