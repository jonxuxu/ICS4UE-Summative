package server;
class ReduceDamage extends Status{
  double damageReduction;//damage = damage * (1 - damageReduction)
  private static int ID = 9;
  ReduceDamage(double damageReduction, int duration){
    super(duration,ID);
    this.damageReduction = damageReduction;
  }
  public double getDamageReduction(){
    return damageReduction;
  }
}