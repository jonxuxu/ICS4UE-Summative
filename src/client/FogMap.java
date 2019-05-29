package client;

public class FogMap {
  int[][] fog;
  boolean[][] currentlyExploring;

  FogMap(int y, int x){
    fog = new int[y][x];
    currentlyExploring = new boolean[y][x];
  }

  public void age(){
    for(int i = 0; i < fog.length; i ++){
      for(int j = 0; j < fog[i].length; j++){
        if(fog[i][j] == 2 && !currentlyExploring[i][j]){
          fog[i][j] = 1;
        }
        currentlyExploring[i][j] = false;
      }
    }
  }

  public void scout(int y, int x){
    updateFog(y, x, 5);
  }
  private void updateFog(int y, int x, int distanceLeft){
    // TODO: Implement obstructions

    if(distanceLeft > 0){
      for(int i = -2; i <= 2; i++){
        for(int j = -2; j <= 2; j++){
          if(i + j != 4 && i - j != 4){ // Searches in a rounded square shape (3, 5, 5, 5, 3)
            if(y+ i >= 0 && y + i< fog.length && x + j >= 0 && x + j < fog.length){
              fog[y + i][x + j] = 2;
              currentlyExploring[y + i][x + j] = true;
              
              updateFog(y + i, j + x, distanceLeft -1);
            }
          }
        }
      }
    }
  }

  public int[][] getFog(){
    return fog;
  }
}
