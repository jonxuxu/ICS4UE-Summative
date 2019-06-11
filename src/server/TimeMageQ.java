package server;
class TimeMageQ extends Status{
  private int x,y,targetX,targetY;
  private static int DURATION = 100;
  private static int ID = -1;
  TimeMageQ(int x, int y, int targetX, int targetY){
    super(DURATION,ID);
    this.x = x;
    this.y = y;
    this.targetX = targetX;
    this.targetY = targetY;
  }
  public int getX(){
    return x;
  }
  public int getY(){
    return y;
  }
  public int getTargetX(){
    return targetX;
  }
  public int getTargetY(){
    return targetY;
  }
}