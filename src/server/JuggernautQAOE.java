package server;
class JuggernautQAOE extends AOE{
  private static int RADIUS = 50;
  private static int ID = 10;//?
  JuggernautQAOE(int x, int y, int duration){
    super(x,y,duration,RADIUS,ID);
  }
  public void setX(int x){
    this.x = x;
  }
  public void setY(int y){
    this.y = y;
  }
}