package server;
/**
 * SafeMarksman.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-19
 */

public class SafeMarksman extends Player{
  private int[] spellCooldowns = {100,100,100};
  private int[] spellTimers = {0,0,0};

  private static int SPACE_AOE_DURATION = 50;
  private static int SPACE_AOE_RADIUS = 100;
  private static int MS_BUFF_STRENGTH = 5;
  private static int MS_BUFF_DURATION = 100;
  private static int STUN_DURATION = 100;

  SafeMarksman(String username, int teamNumber) {
    super(username,teamNumber);
    setMaxHealth(300);
    setHealth(300);
    setAttack(100);
    setMobility(5);
    setRange(300);
    setAutoAttackCooldown(5);
    setFlareCooldown(100);
    setMelee(false);
  }

  public boolean castSpell(int spellIndex){
    if (!getStunned()) {
      if (spellTimers[spellIndex]<=0) {
        spellTimers[spellIndex] = spellCooldowns[spellIndex];
        if (spellIndex==0) { //Q
          addProjectile(new SafeMarksmanQProjectile(getX(), getY(), getMouseX(), getMouseY()));
        }else if (spellIndex==1){//E
          addShield(new SafeMarksmanEShield());
        }else {//Space
          addAOE(new SafeMarksmanSpaceAOE1(getX(), getY(), SPACE_AOE_DURATION, SPACE_AOE_RADIUS));
          //System.out.println("First AOE made at " + getX() + "," + getY());
          addAOE(new SafeMarksmanSpaceAOE2(getMouseX(), getMouseY(), SPACE_AOE_DURATION, SPACE_AOE_RADIUS));
          //System.out.println("Second AOE made at " + getMouseX() + "," + getMouseY());
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
    return (spellCooldowns[spellIndex] - spellTimers[spellIndex])/spellCooldowns[spellIndex]*100;
    /*
    if (spellTick - lastSpellTicks[spellIndex] > spellCooldowns[spellIndex]) {
      return (100);
    } else {
      return ((int) ((100.0 * (spellTick - lastSpellTicks[spellIndex]) / spellCooldowns[spellIndex])));
    }*/
  }

  public void update(){
    //ARTIFACT ADDED HERE
    if ((checkOnArtifact())) {
      setHasArtifact(true);
    }
    if (getHasArtifact()) {
      if (checkOnBaseArtifact()) {
        getArtifacts(getTeam()).setWinner(true);
      }
    }

    for (int i = 0; i < 3; i++){
      if (spellTimers[i] > 0){
        spellTimers[i]--;
      }
    }
    updateBasicTimers();
    //Update Projectiles
    for (int i = getProjectilesSize()-1; i >= 0; i--){
      getProjectile(i).advance();
      Projectile removed = null;
      if ((getProjectile(i).getRemainingDuration() <= 0) || hitObstacle(getProjectile(i))){
        removed = removeProjectile(i);
        if (removed instanceof FlareProjectile){
          addAOE(new FlareAOE(removed.getX(), removed.getY()));
        }
      } else {
        //Insert Collision with Terrain
        if (getProjectile(i) instanceof AutoProjectile){
          for (int j = 0; j < getEnemiesSize(); j++){
            if(getProjectile(i).collides(getEnemy(j))){
              getEnemy(j).damage(getAttack());
              addStatus(new SafeMarksmanMSBuff());
              getEnemy(j).addStatus(new SafeMarksmanMSDebuff());
              removed = removeProjectile(i);
            }
          }
        } else if (getProjectile(i) instanceof SafeMarksmanQProjectile){
          for (int j = 0; j < getEnemiesSize(); j++){
            if(getProjectile(i).collides(getEnemy(j))){
              getEnemy(j).addStatus(new Stun(STUN_DURATION, 12));
              removed = removeProjectile(i);
            }
          }
        }
      }
    }

    //Update AOEs
    for (int i = getAOESize()-1; i >= 0; i--){
      getAOE(i).advance();
      AOE removed = null;
      if (getAOE(i).getRemainingDuration() <= 0){
        removed = removeAOE(i);
        if (removed instanceof SafeMarksmanSpaceAOE2){
          //System.out.println("AOE Removed");
          for (int j = 0; j < getAlliesSize(); j++){
            for (int k = 0; k < getAlly(j).getStatusesSize(); k++){
              if (getAlly(j).getStatus(k) instanceof SafeMarksmanInTPCircle){
                //System.out.println("TP from " + getAlly(j).getX() + "," + getAlly(j).getY() + " to " + removed.getX() + "," + removed.getY());
                getAlly(j).setX(removed.getX());
                getAlly(j).setY(removed.getY());
              }
            }
          }
          /*
          //NEW STUFF
          for (int k = 0; k < getStatusesSize(); k++){
            if (getStatus(k) instanceof SafeMarksmanInTPCircle){
              //System.out.println("TP from " + getX() + "," + getY() + " to " + removed.getX() + "," + removed.getY());
              setX(removed.getX());
              setY(removed.getY());
            }
          }*/ //Ally of self
        }
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
              getEnemy(j).addStatus(new Stun(STUN_DURATION, 12));
            }
          }
        } else if (getAOE(i) instanceof SafeMarksmanSpaceAOE1){
          //Might want to change to illuminate everything illuminatable
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              getEnemy(j).addStatus(new Illuminated(2));
            }
          }
          for (int j = 0; j < getAlliesSize(); j++){
            if (getAOE(i).collides(getAlly(j))){
              getAlly(j).addStatus(new Illuminated(2));//REE
              getAlly(j).addStatus(new SafeMarksmanInTPCircle());
              //System.out.println("Ally Afflicted");
            }
          }
          /*
          if (getAOE(i).collides(this)){
            addStatus(new Illuminated(2));//REE
            addStatus(new SafeMarksmanInTPCircle());
            //System.out.println("Self Afflicted");
          }*///Self ally
        } else if (getAOE(i) instanceof SafeMarksmanSpaceAOE2){
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              getEnemy(j).addStatus(new Illuminated(2));
            }
          }
          for (int j = 0; j < getAlliesSize(); j++){
            if (getAOE(i).collides(getAlly(j))){
              getAlly(j).addStatus(new Illuminated(2));//REE
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
          //REE ADD PUSH AWAY. ALSO ADD FOR THE OTHER SHIELD BREAK LINE
        }
      }
    }
  }

  @Override
  public void damage(int damage){
    damage = (int)(damage*(1-getDamageReduction()));
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
    if (getHealth() <= 0){
      addStatus(new Dead());
    }
  }
}
