package server;
class Launched extends Status{
  double dx,dy;
  private static int ID = -1;
  Launched(double dx, double dy, int duration){
    super(duration,ID);
    this.dx = dx;
    this.dy = dy;
  }
  public double getDX(){
    return dx;
  }
  public double getDY(){
    return dy;
  }
}