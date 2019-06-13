package server;
class FlareAOE extends AOE{
  private static int DURATION = 400;
  private static int RADIUS = 225;
  private static int ID = 0;
  FlareAOE(int x, int y){
    super(x,y,DURATION,RADIUS, ID);
  }
  public static int getRADIUS(){
    return RADIUS;
  }
}