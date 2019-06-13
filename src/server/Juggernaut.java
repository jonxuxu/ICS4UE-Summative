package server;
/**
 * Juggernaut.java
 * This is the class handling the Juggernaut extension of Player
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-19
 */
import java.util.ArrayList;
public class Juggernaut extends Player{
  private int[] spellCooldowns = {100,100,100};
  private int[] spellTimers = {0,0,0};
  private int[] passiveTimers = new int[getEnemiesSize()];
  private static int PASSIVE_RANGE = 700;
  private static int Q_BASE_DAMAGE = 50;
  private static int Q_SPEED = 10;
  private static int Q_RANGE = 300;
  private static int Q_DURATION = Q_RANGE/Q_SPEED;
  private static int E_BASE_DAMAGE = 50;
  private static int SPACE_DURATION = 300;
  
  /**
   * Another constructor for class Juggernaut
   * @param username the username of the player on the server
   * @param teamNumber the team which the player is on
   */
  Juggernaut(String username, int teamNumber) {
    super(username,teamNumber);
    setMaxHealth(300);
    setHealth(300);
    setAttack(30);
    setMobility(5);
    setRange(75);//REE Change to -1 when add support for melee attacks
    setAutoAttackCooldown(20);
    setFlareCooldown(100);
    setMelee(true);
  }
  
  /**
   * Handles the casting of the 3 different spells
   * @param spellIndex the spell which to cast, where 0 is q, 1 is e, and 2 is space
   * @return a boolean for whether the spell is cast or not
   */
  @Override
  public boolean castSpell(int spellIndex){
    if (!getStunned()) {
      if (spellTimers[spellIndex]<=0) {
        spellTimers[spellIndex] = spellCooldowns[spellIndex];
        if (spellIndex==0) { //Q
          launch(getMouseX(), getMouseY(), Q_SPEED, Q_RANGE);
          addAOE(new JuggernautQAOE(getX(), getY(), Q_DURATION));
        }else if (spellIndex==1){//E
          addAOE(new JuggernautEAOE(getX(), getY()));
        }else {//Space
          addStatus(new Unstoppable(SPACE_DURATION));
          addAOE(new JuggernautSpaceAOE(getX(), getY(), SPACE_DURATION));
        }
        return true;
      } else {
        return false;
      }
    }else{
      return false;
    }
  }
  
  /**
   * Gets the cooldown remaining, as a percent, of the chosen spell
   * @param spellIndex the spell which to check, where 0 is q, 1 is e, and 2 is space
   * @return the percent away from the spell being off cooldown
   */
  @Override
  public int getSpellPercent(int spellIndex) {
    return ((int)((1.0*(spellCooldowns[spellIndex] - spellTimers[spellIndex]) / spellCooldowns[spellIndex]*100)));
    /*
    if (spellTick - lastSpellTicks[spellIndex] > spellCooldowns[spellIndex]) {
      return (100);
    } else {
      return ((int) ((100.0 * (spellTick - lastSpellTicks[spellIndex]) / spellCooldowns[spellIndex])));
    }*/
  }
  
  /**
   * Updates the character and all the autoattacks, aoes, statuses, and shields each frame
   */
  @Override
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
    //Passive
    for (int i = 0; i <getEnemiesSize(); i++){
      if (getEnemy(i).getIlluminated()){
        if (Math.sqrt(Math.pow(getEnemy(i).getX()-getX(),2) + Math.pow(getEnemy(i).getY()-getY(),2)) < PASSIVE_RANGE){
          addStatus(new JuggernautMSBuff());
          addStatus(new JuggernautDamageBuff());
        }
      }
    }
    
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
      } else {
        if (getAOE(i) instanceof FlareAOE){
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              getEnemy(j).addStatus(new Illuminated(500));
            }
          }
        } else if (getAOE(i) instanceof AutoAOE){
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              getEnemy(j).damage(getAttack());
            }
          }
        } else if  (getAOE(i) instanceof JuggernautQAOE){
          ((JuggernautQAOE)getAOE(i)).setX(getX());
          ((JuggernautQAOE)getAOE(i)).setY(getY());
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              getEnemy(j).damage(Q_BASE_DAMAGE);
              getEnemy(j).addStatus(new JuggernautQStun());
              getAOE(i).removeNextTurn();
              for (int k = getStatusesSize() - 1; k >= 0; k--){
                if (getStatus(k) instanceof Launched){
                  removeStatus(k);
                }
              }
            }
          }
        } else if (getAOE(i) instanceof JuggernautEAOE){
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              getEnemy(j).damage(E_BASE_DAMAGE);
              getEnemy(j).addStatus(new JuggernautEStun());
            }
          }
        } else if (getAOE(i) instanceof JuggernautSpaceAOE){
          ((JuggernautSpaceAOE)getAOE(i)).setX(getX());
          ((JuggernautSpaceAOE)getAOE(i)).setY(getY());
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              getEnemy(j).addStatus(new Illuminated(2));
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
    }
  }
}
