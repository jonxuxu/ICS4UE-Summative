package server;
class SafeMarksmanEAOE extends AOE{
  private static int DURATION = 1;
  private static int RADIUS = 200;
  private static int ID = 1;
  SafeMarksmanEAOE(int x, int y){
    super(x,y,DURATION,RADIUS, ID);
  }
}