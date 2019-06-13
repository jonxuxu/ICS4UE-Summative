package server;

import java.awt.Rectangle;

/**
 *
 */

public class Artifact {
    private boolean isHeld;
    private int []xy = new int[2];
    private int teamNumber;
    private boolean winner;
    private boolean pickedUp;
    Artifact(int x, int y, int teamNumber){
        this.xy[0]=x;
        this.xy[1]=y;
        this.teamNumber=teamNumber;
    }

    public  int[] getXy(){
        return xy;
    }
    public boolean getPickedUp(){
        return(pickedUp);
    }
    public void setPickedUp(boolean pickedUp){
        this.pickedUp=pickedUp;
    }
    public Rectangle getBoundingBox(){
        Rectangle bounding = new Rectangle(xy[0]-100,xy[1]-100,200,200);
        return(bounding);
    }
    public void setWinner(boolean winner){
        this.winner=winner;
    }
    public boolean getWinner(){
        return winner;
    }
}
