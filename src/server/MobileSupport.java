package server;
/**
 * SafeMarksman.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-19
 */
import java.util.ArrayList;
public class MobileSupport extends Player{
  private int[] spellCooldowns = {100,100,100};
  private int PASSIVE_COOLDOWN = 150;
  private int[] spellTimers = {0,0,0};
  private int passiveTimer = 0;
  private int stacks = 0;
  private static int PASSIVE_RANGE = 500;
  private static int Q_RANGE = 1000;
  private static int Q_SPEED = 25;
  private static int Q_DURATION = Q_RANGE/Q_SPEED;
  private static int Q_STUN_DURATION = 100;
  private static double E_DAMAGE_REDUCTION = 0.3;
  private static int E_STATUS_DURATION = 100;
  private static int SPACE_DURATION = 300;
  private static int SPACE_RANGE = 500;
  
  
  private ArrayList<Player> qBlacklist = new ArrayList<Player>();
  
  MobileSupport(String username, int teamNumber) {
    super(username,teamNumber);
    setMaxHealth(100);
    setHealth(100);
    setAttack(100);
    setMobility(7);
    setRange(300);
    setAutoAttackCooldown(5);
    setFlareCooldown(100);
    setMelee(false);
  }
  
  public boolean castSpell(int spellIndex){
    boolean cast = false;
    if (!getStunned()) {
      if (spellTimers[spellIndex]<=0) {
        if (spellIndex==0) { //Q
          boolean qCast = false;
          for (int i = 0; (i < getAlliesSize() && (!qCast)); i++){
            if (getAlly(i).contains(getMouseX(), getMouseY()) && (Math.sqrt(Math.pow(getAlly(i).getX()-getX(),2) + Math.pow(getAlly(i).getY()-getY(),2)) < Q_RANGE) && (getAlly(i)!=this)){
              spellTimers[spellIndex] = spellCooldowns[spellIndex];
              cast = true;
              stacks++;
              moveTo(getMouseX(), getMouseY(), Q_SPEED);
              for (int j = 0; j < getAlliesSize(); j++){
                for (int k = 0; k < getStatusesSize(); k++){
                  if (getAlly(j).getStatus(k) instanceof MobileSupportEStatus){
                    moveTo(getMouseX(), getMouseY(), Q_SPEED);
                  }
                }
              }
              addShield(new MobileSupportQShield());
              addAOE(new MobileSupportQAOE(getX(), getY(), Q_DURATION));//Stun
              getAlly(i).addShield(new MobileSupportQShield());
              qCast = true;
            }
          }
          for (int i = 0; (i < getAOESize() && (!qCast)); i++){
            if (getAOE(i) instanceof MobileSupportPassiveAOE){
              if (getAOE(i).contains(getMouseX(), getMouseY()) && (Math.sqrt(Math.pow(getAOE(i).getX()-getX(),2) + Math.pow(getAOE(i).getY()-getY(),2)) < Q_RANGE)){
                spellTimers[spellIndex] = spellCooldowns[spellIndex];
                cast = true;
                stacks++;
                moveTo(getAOE(i).getX(), getAOE(i).getY(), Q_SPEED);//Could be AOE x and y
                for (int j = 0; j < getAlliesSize(); j++){
                  for (int k = 0; k < getStatusesSize(); k++){
                    if (getAlly(j).getStatus(k) instanceof MobileSupportEStatus){
                      moveTo(getAOE(i).getX(), getAOE(i).getY(), Q_SPEED);//Could be AOE x and y
                    }
                  }
                }
                addShield(new MobileSupportQShield());
                addAOE(new MobileSupportQAOE(getX(), getY(), Q_DURATION));
                qCast = true;
              }
            }
          }
        }else if (spellIndex==1){//E
          spellTimers[spellIndex] = spellCooldowns[spellIndex];
          cast = true;
          for (int i = 0; i < getAOESize(); i++){
            if (getAOE(i) instanceof MobileSupportPassiveAOE){
              if (getAOE(i).collides(this)){
                ((MobileSupportPassiveAOE)getAOE(i)).activate();
              }
            }
          }
          addAOE(new MobileSupportEAOE(getX(), getY()));
        }else {//Space
          if ((stacks >= 5) && (Math.sqrt(Math.pow(getMouseX()-getX(),2) + Math.pow(getMouseY()-getY(),2)) < SPACE_RANGE)){
            spellTimers[spellIndex] = spellCooldowns[spellIndex];
            cast = true;
            addAOE(new MobileSupportPassiveAOE(getMouseX(), getMouseY()));
            addAOE(new MobileSupportSpaceAOE(getMouseX(), getMouseY()));
            stacks-=5;
          }
        }
        return cast;
      } else {
        return false;
      }
    }else{
      return false;
    }
  }
  public int getSpellPercent(int spellIndex) {
    return ((int)((1.0*(spellCooldowns[spellIndex] - spellTimers[spellIndex]) / spellCooldowns[spellIndex]*100)));
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
    if (passiveTimer > 0){
      passiveTimer--;
    }
    updateBasicTimers();
    //Update Passive
    if (passiveTimer <= 0){
      passiveTimer = PASSIVE_COOLDOWN;
      double angle = Math.random() * 2 * Math.PI;
      double radius = PASSIVE_RANGE * Math.sqrt(Math.random());
      double passiveX = radius * Math.cos(angle) + getX();
      double passiveY = radius * Math.sin(angle) + getY();
      addAOE(new MobileSupportPassiveAOE((int)passiveX, (int)passiveY));
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
        } else if (getAOE(i) instanceof MobileSupportPassiveAOE){
          if (((MobileSupportPassiveAOE)getAOE(i)).isActive()){
            for (int j = 0; j < getAlliesSize(); j++){
              if (getAOE(i).collides(getAlly(j))){
                getAlly(j).addStatus(new ReduceDamage(E_DAMAGE_REDUCTION, E_STATUS_DURATION));
              }
            }
          }
        } else if  (getAOE(i) instanceof MobileSupportQAOE){
          getAOE(i).setX(getX());
          getAOE(i).setY(getY());
          for (int j = 0; j < getEnemiesSize(); j++){
            if (!qBlacklist.isEmpty()){
              for (int k = 0; k < qBlacklist.size(); k++){
                if (getEnemy(j) != qBlacklist.get(k)){
                  if (getAOE(i).collides(getEnemy(j))){
                    getEnemy(j).addStatus(new Stun(Q_STUN_DURATION, 12));
                    qBlacklist.add(getEnemy(j));
                  }
                }
              }
            } else {
              if (getAOE(i).collides(getEnemy(j))){
                getEnemy(j).addStatus(new Stun(Q_STUN_DURATION, 12));
                qBlacklist.add(getEnemy(j));
              }
            }
          }
        } else if (getAOE(i) instanceof MobileSupportEAOE){
          for (int j = 0; j < getAlliesSize(); j++){
            if (getAOE(i).collides(getAlly(j))){
              getAlly(j).addStatus(new MobileSupportEStatus());
            }
          }
        } else if (getAOE(i) instanceof MobileSupportSpaceAOE){
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              getEnemy(j).addStatus(new MobileSupportMSDebuff(SPACE_DURATION));
              getEnemy(j).addStatus(new Illuminated(SPACE_DURATION));
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
