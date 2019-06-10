package server;
class SummonerSpaceAOE extends AOE{
  private static int DURATION = 2;
  private static int RADIUS = 300;
  private static int ID = 13;
  SummonerSpaceAOE(int x, int y){
    super(x,y,DURATION,RADIUS, ID);
  }
}