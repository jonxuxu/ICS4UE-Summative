package server;
class Launched extends Status{
  double dx,dy;
  Launched(double dx, double dy, int duration){
    super(duration);
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