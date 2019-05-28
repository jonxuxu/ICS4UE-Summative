package client;

public class FogMap {
  int[][] fog;

  FogMap(int y, int x){
    fog = new int[y][x];
  }

  public void age(){
    for(int i = 0; i < fog.length; i ++){
      for(int j = 0; j < fog[i].length; j++){
        if(fog[i][j] == 2){
          fog[i][j] = 1;
        }
      }
    }
  }

  public void scout(int y, int x){
    updateFog(y, x, 5);
  }
  private void updateFog(int y, int x, int distanceLeft){
    // TODO: Implement obstructions
    fog[y][x] = 2;
    if(distanceLeft > 0){
      for(int i = -1; i <= 1; i++){
        for(int j = -1; j <= 1; j++){
          if(y+ i >= 0 && y + i< fog.length && x + j >= 0 && x + j < fog.length){
            updateFog(y + i, j + x, distanceLeft -1);
          }
        }
      }
    }
  }

  public int[][] getFog(){
    return fog;
  }
}
