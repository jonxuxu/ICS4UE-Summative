package server;
class SafeMarksmanEAOE extends AOE{
  private static int DURATION = 1;
  private static int RADIUS = 200;
  SafeMarksmanEAOE(int x, int y){
    super(x,y,DURATION,RADIUS);
  }
}