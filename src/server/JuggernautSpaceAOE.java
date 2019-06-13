package server;
class JuggernautSpaceAOE extends AOE{
  private static int RADIUS = 50;
  private static int ID = 10;//?
  JuggernautSpaceAOE(int x, int y, int duration){
    super(x,y,duration,RADIUS,ID);
  }
  public void setX(int x){
    this.x = x;
  }
  public void setY(int y){
    this.y = y;
  }
}