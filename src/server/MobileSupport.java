package server;
/**
 * SafeMarksman.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-19
 */
import java.util.ArrayList;
public class MobileSupport extends Player{
  private int[] spellCooldowns = {100,100,100};
  private int PASSIVE_COOLDOWN = 100;
  private int[] spellTimers = {0,0,0};
  private int passiveTimer = 0;
  private static int Q_DAMAGE = 50;
  private static int E_RANGE = 500;
  private static int SPACE_DURATION = 100;
  
  private ArrayList<Player> qBlacklist = new ArrayList<Player>();
  
  MobileSupport(String username) {
    super(username);
    setMaxHealth(100);
    setHealth(100);
    setAttack(100);
    setMobility(30);
    setRange(300);
  }
  
  public boolean castSpell(int spellIndex){
    if (!getStunned()) {
      if (spellTimers[spellIndex]<=0) {
        spellTimers[spellIndex] = spellCooldowns[spellIndex];
        if (spellIndex==0) { //Q
          boolean qCast = false;
          for (int i = 0; (i < getAlliesSize() && (!qCast)); i++){
            if (getAlly(i).contains(getMouseX(), getMouseY()) && (Math.sqrt(Math.pow(getAlly(i).getX()-getX(),2) + Math.pow(getAlly(i).getY()-getY(),2)) < Q_RANGE)){
              stacks++;
              launch(getMouseX(), getMouseY(), Q_SPEED, Q_RANGE);
              for (int j = 0; j < getAlliesSize(); j++){
                for (int k = 0; k < getStatusesSize(); k++){
                  if (getAlly(j).getStatus(k) instanceof MobileSupportEStatus){
                    launch(getMouseX(), getMouseY(), Q_SPEED, Q_RANGE);
                  }
                }
              }
              addShield(new MobileSupportQShield());
              addStatus(new MobileSupportQAOE(getX(), getY(), Q_DURATION));//Stun
              getAlly(i).addShield(new MobileSupportQShield());
              qCast = true;
            }
          }
          for (int i = 0; (i < getAOESize() && (!qCast)); i++){
            if (getAOE(i) instanceof MobileSupportPassiveAOE){
              if (getAOE(i).contains(getMouseX(), getMouseY()) && (Math.sqrt(Math.pow(getAOE(i).getX()-getX(),2) + Math.pow(getAOE(i).getY()-getY(),2)) < Q_RANGE)){
                stacks++;
                launch(getMouseX(), getMouseY(), Q_SPEED, Q_RANGE);//Could be AOE x and y
                for (int j = 0; j < getAlliesSize(); j++){
                  for (int k = 0; k < getStatusesSize(); k++){
                    if (getAlly(j).getStatus(k) instanceof MobileSupportEStatus){
                      launch(getMouseX(), getMouseY(), Q_SPEED, Q_RANGE);
                    }
                  }
                }
                addShield(new MobileSupportQShield());
                addStatus(new MobileSupportQAOE(getX(), getY(), Q_DURATION));
                qCast = true;
              }
            }
          }
        }else if (spellIndex==1){//E
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
            addAOE(new MobileSupportPassiveAOE(getMouseX(), getMouseY()));
            addAOE(new MobileSupportSpaceAOE(getMouseX(), getMouseY()));
            stacks-=5;
          }
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
    for (int i = 0; i < 3; i++){
      if (spellTimers[i] > 0){
        spellTimers[i]--;
      }
    }
    if (passiveTimer > 0){
      passiveTimer--;
    }
    
    //Update Passive
    if (passiveTimer == 0){
      passiveTimer = PASSIVE_COOLDOWN;
      double angle = Math.random() * 2 * Math.PI;
      double radius = PASSIVE_RANGE * Math.sqrt(Math.random());
      double passiveX = radius * Math.cos(angle);
      double passiveY = radius * Math.sin(angle);
      addAOE(new MobileSupportPassiveAOE(passiveX, passiveY));
    }
    
    //Update Projectiles
    for (int i = getProjectilesSize()-1; i >= 0; i--){
      getProjectile(i).advance();
      Projectile removed = null;
      if (getProjectile(i).getRemainingDuration() <= 0){
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
                getAlly(j).addStatus(new ReduceDamage(PASSIVE_DAMAGE_REDUCTION, PASSIVE_STATUS_DURATION));
              }
            }
          }
        } else if  (getAOE(i) instanceof MobileSupportQAOE){
          ((MobileSupportQAOE)getAOE(i)).setX(getX());
          ((MobileSupportQAOE)getAOE(i)).setY(getY());
          for (int j = 0; j < getEnemiesSize(); j++){
            for (int k = 0; k < qBlacklist.size(); k++){
              if (getEnemy(j) != qBlacklist.get(k)){
                if (getAOE(i).collides(getEnemy(j))){
                  getEnemy(j).addStatus(new Stun(Q_STUN_DURATION));
                  qBlacklist.add(getEnemy(j));
                }
              }
            }
          }
        } else if (getAOE(i) instanceof MobileSupportE){
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
