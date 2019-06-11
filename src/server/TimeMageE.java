package server;
class TimeMageE extends Status{
  private int x,y;
  private static int DURATION = 100;
  private static int ID = -1;
  TimeMageE(int x, int y){
    super(DURATION,ID);
    this.x = x;
    this.y = y;
  }
  public int getX(){
    return x;
  }
  public int getY(){
    return y;
  }
}