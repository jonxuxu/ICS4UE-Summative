package server;
class SafeMarksmanMSDebuff extends MSBuff{
  private static int STRENGTH = -3;
  private static int DURATION = 100;
  SafeMarksmanMSDebuff(){
    super(STRENGTH, DURATION);
  }
}