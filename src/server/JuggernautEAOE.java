package server;
class JuggernautEAOE extends AOE{
  private static int DURATION = 2;
  private static int RADIUS = 200;
  private static int ID = 11;
  JuggernautEAOE(int x, int y){
    super(x,y,DURATION,RADIUS, ID);
  }
}