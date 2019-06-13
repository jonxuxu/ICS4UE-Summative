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
public class Summoner extends Player{
  private int[] spellCooldowns = {50,100,100};
  private int[] spellTimers = {0,0,0};
  private ArrayList<SummonerPet> pets = new ArrayList<SummonerPet>();
  private static int PET_RANGE = 300;
  private static int PET_ATTACK_COOLDOWN = 10;
  private static int PET_ATTACK_SPEED = 10;
  private static int Q_RANGE = 1000;
  private static int E_RANGE = 1000;
  private static int E_COOLDOWN = 10;
  private int eTimer;
  private static int SPACE_SPEED = 25;
  private boolean secondE = false;
  private SummonerPet chosenPet;
  
  Summoner(String username, int teamNumber) {
    super(username,teamNumber);
    setMaxHealth(200);
    setHealth(200);
    setAttack(20);
    setMobility(7);
    setRange(300);
    setAutoAttackCooldown(15);
    setFlareCooldown(100);
    setMelee(false);
  }
  
  public boolean castSpell(int spellIndex){
    boolean cast = false;
    if (!getStunned()) {
      if (spellTimers[spellIndex]<=0) {
        if (spellIndex==0) { //Q
          if (Math.sqrt(Math.pow(getMouseX()-getX(),2) + Math.pow(getMouseY()-getY(),2)) < Q_RANGE){
            spellTimers[spellIndex] = spellCooldowns[spellIndex];
            SummonerPet pet = new SummonerPet(getMouseX(), getMouseY());
            pets.add(pet);
            addAOE(pet);
            cast = true;
          }
        }else if (spellIndex==1){//E
          if (eTimer<=0){
            if (!secondE){
              for (int i = 0; i < pets.size(); i++){
                if (pets.get(i).contains(getMouseX(), getMouseY())){
                  if (Math.sqrt(Math.pow(pets.get(i).getX()-getX(),2) + Math.pow(pets.get(i).getY()-getY(),2)) < E_RANGE){
                    chosenPet = pets.get(i);//Can be remove instead graphically
                    eTimer = E_COOLDOWN;
                    secondE = true;
                    cast = true;
                    System.out.println("First Cast Successful");
                  }
                }
              }
            } else {
              if (Math.sqrt(Math.pow(getMouseX()-getX(),2) + Math.pow(getMouseY()-getY(),2)) < E_RANGE){
                chosenPet.moveTo(getMouseX(), getMouseY());
                spellTimers[spellIndex] = spellCooldowns[spellIndex];
                secondE = false;
                cast = true;
                System.out.println("Second Cast Successful");
              }
            }
          }
        }else {//Space
          if (pets.size()>0){
            for (int i = 0; i < pets.size(); i++){
              addAOE(new SummonerSpaceAOE(pets.get(i).getX(), pets.get(i).getY()));
            }
            spellTimers[spellIndex] = spellCooldowns[spellIndex];
            cast = true;
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
    if (eTimer > 0){
      eTimer--;
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
          for (int j = 0; j < getEnemiesSize(); j++) {
            if (getProjectile(i) != null) {
              if (getProjectile(i).collides(getEnemy(j))) {
                getEnemy(j).damage(getAttack());
                removed = removeProjectile(i);
              }
            }
          }
        }
      }
    }
    
    //Update AOEs
    SummonerSpaceAOE[] closestSpaceAOE = new SummonerSpaceAOE[getEnemiesSize()];
    double[] minDistances = new double[getEnemiesSize()];
    for (int i = 0; i < minDistances.length; i++){
      minDistances[i] = Double.MAX_VALUE;
    }
    for (int i = getAOESize()-1; i >= 0; i--){
      getAOE(i).advance();
      AOE removed = null;
      if (getAOE(i).getRemainingDuration() <= 0){
        removed = removeAOE(i);
        if (removed instanceof SummonerPet){
          pets.remove(removed);
        }
      } else {
        if (getAOE(i) instanceof FlareAOE){
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              getEnemy(j).addStatus(new Illuminated(500));
            }
          }
        } else if (getAOE(i) instanceof SummonerPet){
          double minDistance = Integer.MAX_VALUE;
          Player target = this;
          for (int j = 0; j < getEnemiesSize(); j++){
            double distance = Math.sqrt(Math.pow(getEnemy(j).getX()-getAOE(i).getX(),2) + Math.pow(getEnemy(j).getY()-getAOE(i).getY(),2));
            if (distance < minDistance){
              minDistance = distance;
              target = getEnemy(j);
            }
          }
          if (minDistance < PET_RANGE){
            if (((SummonerPet)getAOE(i)).getAttackTimer() <= 0){
              addProjectile(new AutoProjectile(getAOE(i).getX(), getAOE(i).getY(), target.getX(), target.getY(), PET_ATTACK_SPEED, PET_RANGE));
              ((SummonerPet)getAOE(i)).setAttackTimer(PET_ATTACK_COOLDOWN);
            }
          }
        } else if (getAOE(i) instanceof SummonerSpaceAOE){
          for (int j = 0; j < getEnemiesSize(); j++){
            if (getAOE(i).collides(getEnemy(j))){
              double distance = Math.sqrt(Math.pow(getEnemy(j).getX()-getAOE(i).getX(),2) + Math.pow(getEnemy(j).getY()-getAOE(i).getY(),2));
              if (distance < minDistances[j]){
                minDistances[j] = distance;
                closestSpaceAOE[j] = (SummonerSpaceAOE)getAOE(i);
              }
            }
          }
        }
      }
    }
    for (int i = 0; i < closestSpaceAOE.length; i++){
      if (closestSpaceAOE[i]!=null){
        getEnemy(i).moveTo(closestSpaceAOE[i].getX(), closestSpaceAOE[i].getY(), SPACE_SPEED);
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
