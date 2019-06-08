package server;
class JuggernautDamageBuff extends DamageBuff{
  private static int STRENGTH = 50;
  private static int DURATION = 2;
  JuggernautDamageBuff(){
    super(STRENGTH, DURATION);
  }
}