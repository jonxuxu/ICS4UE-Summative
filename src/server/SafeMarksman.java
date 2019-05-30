package server;
/**
 * SafeMarksman.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-19
 */

public class SafeMarksman extends Player{
  private int[] spellCooldowns = {1000,1000,1000};
  private int[] lastSpellTicks = {-10000,-10000,-10000};//So that they can be used immediately
  private int spellTick;

  private static int SPACE_AOE_DURATION = 500;
  private static int SPACE_AOE_RADIUS = 300;
  private static int MS_BUFF_STRENGTH = 3;
  private static int MS_BUFF_DURATION = 100;
  private static int STUN_DURATION = 100;
  
  SafeMarksman(String username) {
    super(username);
    setMaxHealth(300);
    setHealth(300);
    setAttack(100);
    setMobility(10);
    setRange(300);
  }

 public boolean testSpell(int spellIndex){
   if (!getStunned()) {
     if (spellTick - lastSpellTicks[spellIndex] > spellCooldowns[spellIndex]) {
       lastSpellTicks[spellIndex] = spellTick;
       if (spellIndex==0) { //Q
         addProjectile(new SafeMarksmanQProjectile(getX(), getY(), getMouseX(), getMouseY()));
       }else if (spellIndex==1){//E
         addShield(new SafeMarksmanEShield());
       }else {//Space
         addAOE(new SafeMarksmanSpaceAOE1(getX(), getY(), SPACE_AOE_DURATION, SPACE_AOE_RADIUS));
         addAOE(new SafeMarksmanSpaceAOE2(getMouseX(), getMouseY(), SPACE_AOE_DURATION, SPACE_AOE_RADIUS));
       }
       return true;
     } else {
       return false;
     }
   }else{
      return false;
   }
 }
  public int getSpellPercent(int spellIndex) {
    if (spellTick - lastSpellTicks[spellIndex] > spellCooldowns[spellIndex]) {
      return (100);
    } else {
      return ((int) ((100.0 * (spellTick - lastSpellTicks[spellIndex]) / spellCooldowns[spellIndex])));
    }
  }

  public void update(){
    spellTick++;
    //Update Projectiles
    for (int i = getProjectilesSize()-1; i >= 0; i--){
      getProjectile(i).advance();
      Projectile removed = null;//REE NEED TO MAKE AN ARRAY LIST OR ELSE PROBLEMS AND NEED OT HAVE MORE THAN JUST PROJECTILE
      if (getProjectile(i).getRemainingDuration() <= 0){
        removed = removeProjectile(i);
      } else {
        //Insert Collision with Terrain
        if (getProjectile(i) instanceof AutoProjectile){
          for (int j = 0; j < getEnemiesSize(); j++){
            if(getProjectile(i).collides(getEnemy(j))){
              getEnemy(j).damage(getAttack());
              addStatus(new MSBuff(MS_BUFF_STRENGTH, MS_BUFF_DURATION));
              getEnemy(j).addStatus(new MSBuff(-1*MS_BUFF_STRENGTH, MS_BUFF_DURATION));
              removed = removeProjectile(i);
            }
          }
        } else if (getProjectile(i) instanceof SafeMarksmanQProjectile){
          for (int j = 0; j < getEnemiesSize(); j++){
            if(getProjectile(i).collides(getEnemy(j))){
              getEnemy(j).addStatus(new Stun(STUN_DURATION));
              removed = removeProjectile(i);
            }
          }
        }
      }
      if (removed instanceof FlareProjectile){
        addAOE(new FlareAOE(removed.getX(), removed.getY()));
      }
    }
    
    //Update AOEs
    for (int i = getAOESize()-1; i >= 0; i--){
      getAOE(i).advance();
      AOE removed = null;
      if (getAOE(i).getRemainingDuration() <= 0){
        removed = removeAOE(i);
      } else {
        if (getAOE(i) instanceof FlareAOE){
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              getEnemy(j).addStatus(new Illuminated(500));
            }
          }
        } else if  (getAOE(i) instanceof SafeMarksmanEAOE){
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              getEnemy(j).addStatus(new Stun(STUN_DURATION));
            }
          }
        } else if (getAOE(i) instanceof SafeMarksmanSpaceAOE1){
          //Might want to change to illuminate everything illuminatable
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              getEnemy(j).addStatus(new Illuminated(500));
            }
          }
          for (int j = 0; j < getAlliesSize(); j++){
            if (getAOE(i).collides(getAlly(j))){
              getAlly(j).addStatus(new Illuminated(500));//REE
              getAlly(j).addStatus(new SafeMarksmanInTPCircle());
            }
          }
        } else if (getAOE(i) instanceof SafeMarksmanSpaceAOE2){
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              getEnemy(j).addStatus(new Illuminated(500));
            }
          }
          for (int j = 0; j < getAlliesSize(); j++){
            if (getAOE(i).collides(getAlly(j))){
              getAlly(j).addStatus(new Illuminated(500));//REE
            }
          }
        }
      }
      if (removed instanceof SafeMarksmanSpaceAOE2){
        for (int j = 0; j < getAlliesSize(); j++){
          for (int k = 0; k < getAlly(j).getStatusesSize(); k++){
            if (getAlly(j).getStatus(k) instanceof SafeMarksmanInTPCircle){
              getAlly(j).setX(removed.getX());
              getAlly(j).setY(removed.getY());
            }
          }
        }
      }
    }
    
    updateStatuses();
    
    for (int i = getShieldsSize()-1; i >= 0; i--){
      getShield(i).advance();
      Shield removed = null;
      if (getShield(i).getRemainingDuration() <= 0){
        removed = removeShield(i);
      }
      if (removed!=null){
        if (removed instanceof SafeMarksmanEShield){
          addAOE(new SafeMarksmanEAOE(getX(),getY()));
        }
      }
    }
  }
  
  @Override
  public void damage(int damage){
    if (getShieldsSize() == 0){
      setHealth(getHealth() - damage);
    } else {
      if (getShield(0).getStrength() - damage > 0){
        getShield(0).damage(damage);
      } else {
        setHealth(getHealth() - (damage - getShield(0).getStrength()));
        Shield removed = removeShield(0);
        if (removed instanceof SafeMarksmanEShield){
          addAOE(new SafeMarksmanEAOE(getX(),getY()));
        }
      }
    }
  }
}
