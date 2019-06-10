package server;
class MobileSupportPassiveAOE extends AOE{
  private static int DURATION = 300;
  private static int RADIUS = 100;
  private static int ID = 7;
  private boolean active = false;
  MobileSupportPassiveAOE(int x, int y){
    super(x,y,DURATION,RADIUS,ID);
  }
  public void activate(){
    active = true;
  }
  public boolean isActive(){
    return active;
  }
}
    