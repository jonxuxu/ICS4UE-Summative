package server;
class ReduceDamage extends Status{
  double damageReduction;//damage = damage * (1 - damageReduction)
  ReduceDamage(double damageReduction, int duration){
    super(duration);
    this.damageReduction = damageReduction;
  }
  public double getDamageReduction(){
    return damageReduction;
  }
}