package server;
class SafeMarksmanEShield extends Shield{
  private static int E_SHIELD_STRENGTH = 50;
  private static int E_SHIELD_DURATION = 500;
  SafeMarksmanEShield(){
    super(E_SHIELD_STRENGTH, E_SHIELD_DURATION);
  }
}