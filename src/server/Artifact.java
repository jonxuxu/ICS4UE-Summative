package server;

import java.awt.Rectangle;

/**
 * Artifact.java
 *
 * The class containing all the data for an artifact
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-06-11
 * @version 2.0
 */

public class Artifact {
    private boolean isHeld;
    private int []xy = new int[2];
    private int teamNumber;
    private boolean winner;
    private boolean pickedUp;

    /**
     * Basic constructor that initializes basic values
     *
     * @param x the x-value of the Artifact
     * @param y the y-value of the Artifact
     * @param teamNumber which team the artifact belongs to
     */
    Artifact(int x, int y, int teamNumber){
        this.xy[0]=x;
        this.xy[1]=y;
        this.teamNumber=teamNumber;
    }

    /**
     * Basic getter for the position of the artifact
     *
     * @return the position as a length-2 integer array
     */
    public  int[] getXy(){
        return xy;
    }

    /**
     * Basic getter for the state of the artifact
     *
     * @return whether the artifact is picked up by a player or not, as a boolean
     */

    public boolean getPickedUp(){
        return(pickedUp);
    }

    /**
     * Basic setter for the state of the artifact
     *
     * @param pickedUp whether the artifact's state should be set to picked up or not, as a boolean
     */
    public void setPickedUp(boolean pickedUp){
        this.pickedUp=pickedUp;
    }

    /**
     * Getter for the bounding box of the artifact, calculated within the method
     *
     * @return the bounding box of the artifact, as a rectangle
     */
    public Rectangle getBoundingBox(){
        Rectangle bounding = new Rectangle(xy[0]-100,xy[1]-100,200,200);
        return(bounding);
    }

    /**
     * Setter for the winner of the game based on the artifact's position
     *
     * @param winner, whether the artifact's owners have won
     */
    public void setWinner(boolean winner){
        this.winner=winner;
    }

    /**
     * Getter for the winner of the game based on the artifact's position
     *
     * @return winner, whether the artifact's owners have won
     */
    public boolean getWinner(){
        return winner;
    }
}
