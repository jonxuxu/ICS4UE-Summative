package server;
class FlareAOE extends AOE{
  private static int DURATION = 50;
  private static int RADIUS = 100;
  FlareAOE(int x, int y){
    super(x,y,DURATION,RADIUS);
  }
}