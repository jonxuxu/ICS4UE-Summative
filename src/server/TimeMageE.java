package server;
class TimeMageE extends Status{
  private int x,y;
  private static int DURATION = 100;
  TimeMageE(int x, int y){
    super(DURATION);
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