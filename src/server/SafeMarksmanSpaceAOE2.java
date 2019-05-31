package server;
class SafeMarksmanSpaceAOE2 extends AOE{
  private static int ID = 3;
  SafeMarksmanSpaceAOE2(int x, int y, int duration, int radius){
    super(x,y,duration,radius, ID);
  }
}