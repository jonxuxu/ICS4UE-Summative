package client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 * User.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-04-24
 */

public abstract class Player extends User {
   //Constants
   private int ID;
   private int[] xy = {300, 300};
   private int[] centerXy = new int[2];
   private double scaling;
   private int desiredSpell = -1;
   private int[] spellPercent = {100, 100, 100};
   private ArrayList<Status> allStatus = new ArrayList<Status>();
   private int gold = 0;
   private boolean artifact;
   private int level = 0;
   private int maxHealth;
   private int health;
   private int attack;
   private int mobility = 20;
   private int range;
   private boolean damaged;
   private int spriteID;
   private double flashlightAngle;
   private boolean flashlightOn;
   private double ROOT2O2 = 0.70710678118;
   private Polygon flashlightBeam = new Polygon();
   private Polygon test = new Polygon();
   private ArrayList<CustomPolygon> shapes = new ArrayList<CustomPolygon>();
   private int FLASHLIGHT_RADIUS = 200;
   private int xCo;
   private int yCo;
   private int cVal;
   private int []xP={100,200,300,400,100};
   private int []yP={100,300,500,400,400};

   Player(String username) {
      super(username);
      ArrayList<Integer> xPoints = new ArrayList<Integer>();
      ArrayList<Integer> yPoints = new ArrayList<Integer>();
      xPoints.add(300);
      xPoints.add(400);
      xPoints.add(400);
      xPoints.add(300);
      yPoints.add(300);
      yPoints.add(300);
      yPoints.add(400);
      yPoints.add(400);
      shapes.add(new CustomPolygon(4, xPoints, yPoints));
      ArrayList<Integer> x2Points = new ArrayList<Integer>();
      ArrayList<Integer> y2Points = new ArrayList<Integer>();
      x2Points.add(100);
      x2Points.add(200);
      x2Points.add(300);
      x2Points.add(400);
      x2Points.add(100);
      y2Points.add(100);
      y2Points.add(300);
      y2Points.add(500);
      y2Points.add(400);
      y2Points.add(400);
      shapes.add(new CustomPolygon(5, x2Points, y2Points));
      test = new Polygon(xP,yP,5);
   }

   public void setID(int ID) {
      this.ID = ID;
   }

   public void setCenterXy(int[] centerXy) {
      this.centerXy[0] = centerXy[0];
      this.centerXy[1] = centerXy[1];
   }

   public int[] getXy() {
      return (xy);
   }

   public void setXy(int x, int y) {
      this.xy[0] = x;
      this.xy[1] = y;
   }

   public void draw(Graphics2D g2, int[] midXy) {
      int[] tempXy = {(int) (centerXy[0] - xy[0] * scaling), (int) (centerXy[1] - xy[1] * scaling)};
      drawFlashlight(g2, tempXy);
      drawReal(g2, centerXy[0] + (int) (scaling * (xy[0] - midXy[0])) - (int) (60 * scaling) / 2, centerXy[1] + (int) (scaling * (xy[1] - midXy[1])) - (int) (60 * scaling) / 2, (int) (60 * scaling), (int) (60 * scaling), desiredSpell);
      if (desiredSpell != -1) {
         desiredSpell = -1;
      }

   }

   public void drawFlashlight(Graphics2D g2, int[] xyAdjust) {
      //g2.fillRect(xyAdjust[0] + (int) (300 * scaling), xyAdjust[1] + (int) (300 * scaling), (int) (100 * scaling), (int) (100 * scaling));
      if (flashlightOn) {
         flashlightBeam.reset();
         flashlightBeam.addPoint((int) (xy[0] * scaling), (int) (xy[1] * scaling));
         int shapeIndex = -2;
         int intersectionIndex = -2;
         int[] savedPoint = new int[2];
         int[] prevPoint = new int[2];
         int newShapeIndex = -1;
         int newIntersectionIndex = -1;
         int points = 1;
         boolean hit;
         double tempFlashlightAngle = flashlightAngle;
         tempFlashlightAngle -= 0.25;
         for (double k = 0; k < 50; k++) {
            hit = false;
            tempFlashlightAngle += 0.01;
            setPlayerVector(xy, xy[0] + (int) (FLASHLIGHT_RADIUS * Math.cos(tempFlashlightAngle)), xy[1] + (int) (FLASHLIGHT_RADIUS * Math.sin(tempFlashlightAngle)));
            int smallestDist = 100000000;
            for (int i = 0; i < shapes.size(); i++) {
               shapes.get(i).setPlayerScalar(xCo, yCo, cVal);
               shapes.get(i).setPlayerVector(Math.cos(tempFlashlightAngle), Math.sin(tempFlashlightAngle));
               shapes.get(i).setVectorMagnitude(Math.abs(tempFlashlightAngle - flashlightAngle));
               if (shapes.get(i).intersect(xy)) {
                  if ((distance(shapes.get(i).getIntersect(), xy) < smallestDist)) {
                     smallestDist = distance(shapes.get(i).getIntersect(), xy);
                     newShapeIndex = i;
                     newIntersectionIndex = shapes.get(i).getIntersectionIndex();
                     savedPoint[0] = shapes.get(i).getIntersect()[0];
                     savedPoint[1] = shapes.get(i).getIntersect()[1];
                     hit = true;
                  }
               }
            }
            if (!hit) {
               newShapeIndex = -1;
               newIntersectionIndex = -1;
            }
            if ((shapeIndex != newShapeIndex) || (intersectionIndex != newIntersectionIndex) || (k == 0) || (k == 49)) {
               points++;
               shapeIndex = newShapeIndex;
               intersectionIndex = newIntersectionIndex;
               if (!((prevPoint[0] == 0) && (prevPoint[1] == 0))) {
                  flashlightBeam.addPoint((int) (prevPoint[0] * scaling), (int) (prevPoint[1] * scaling));
               }
               if (shapeIndex != -1) {
                  flashlightBeam.addPoint((int) (savedPoint[0] * scaling), (int) (savedPoint[1] * scaling));
               } else {
                  flashlightBeam.addPoint((int) ((xy[0] + FLASHLIGHT_RADIUS / Math.cos(flashlightAngle - tempFlashlightAngle) * Math.cos(tempFlashlightAngle)) * scaling), (int) ((xy[1] + FLASHLIGHT_RADIUS / Math.cos(flashlightAngle - tempFlashlightAngle) * Math.sin(tempFlashlightAngle)) * scaling));
               }
            }
            if (hit) {
               prevPoint[0] = savedPoint[0];
               prevPoint[1] = savedPoint[1];
            } else {
               prevPoint[0] = (int) ((xy[0] + FLASHLIGHT_RADIUS / Math.cos(flashlightAngle - tempFlashlightAngle) * Math.cos(tempFlashlightAngle)));
               prevPoint[1] = (int) ((xy[1] + FLASHLIGHT_RADIUS / Math.cos(flashlightAngle - tempFlashlightAngle) * Math.sin(tempFlashlightAngle)));
            }
         }
         if (points < 3) {
            flashlightBeam.addPoint((int) ((xy[0] + FLASHLIGHT_RADIUS * Math.cos(tempFlashlightAngle + 0.1)) * scaling), (int) ((xy[1] + FLASHLIGHT_RADIUS * Math.sin(tempFlashlightAngle + 0.1)) * scaling));
         }
         g2.setColor(Color.WHITE);
         flashlightBeam.translate(xyAdjust[0], xyAdjust[1]);
         g2.fillPolygon(flashlightBeam);
      }
   }

   public int distance(int[] firstXy, int[] secondXy) {
      return (((firstXy[0] - secondXy[0]) * (firstXy[0] - secondXy[0])) + ((firstXy[1] - secondXy[1]) * (firstXy[1] - secondXy[1])));
   }

   public void setPlayerVector(int[] initalXy, int finalX, int finalY) { //player is initial, final is mouse
      xCo = initalXy[1] - finalY;
      yCo = finalX - initalXy[0];
      cVal = finalY * initalXy[0] - finalX * initalXy[1];
   }

   public double[] getDisp(int angleOfMovement) {
      double[] displacements = new double[2];
      if (angleOfMovement == 0) {
         displacements[0] = mobility / scaling;
         displacements[1] = 0;
      } else if (Math.abs(angleOfMovement) == 1) {
         displacements[0] = ROOT2O2 * mobility / scaling;
         displacements[1] = ROOT2O2 * mobility / scaling;
      } else if (Math.abs(angleOfMovement) == 2) {
         displacements[0] = 0;
         displacements[1] = mobility / scaling;
      } else if (Math.abs(angleOfMovement) == 3) {
         displacements[0] = -ROOT2O2 * mobility / scaling;
         displacements[1] = ROOT2O2 * mobility / scaling;
      } else {
         displacements[0] = -mobility / scaling;
         displacements[1] = 0;
      }
      if (angleOfMovement < 0) {
         displacements[1] = -displacements[1];
      }
      return (displacements);
   }

   public void setScaling(double scaling) {
      this.scaling = scaling;
   }

   public void setSpell(int spellIndex) {
      if (spellIndex != -1) {
         this.desiredSpell = spellIndex;
      }
   }


   public void setFlashlightOn(boolean flashlightOn) {
      this.flashlightOn = flashlightOn;
   }

   public void setFlashlightAngle(double flashlightAngle) {
      this.flashlightAngle = flashlightAngle;
   }

   public double getFlashlightAngle() {
      return (flashlightAngle);
   }

   public void setSpellPercent(int spellPercent, int spellIndex) {
      this.spellPercent[spellIndex] = spellPercent;
   }

   public double getSpellPercent(int spellIndex) {
      return spellPercent[spellIndex] / 100.0;
   }

   public void setGold(int gold) {
      this.gold = gold;
   }

   public void addStatus(int statusInt) {
      allStatus.clear(); //very inefficient, possibly change?
      allStatus.add(new Status(statusInt));
   }

   public void setArtifact(boolean artifact) {
      this.artifact = artifact;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getGold() {
      return gold;
   }

   public int getLevel() {
      return level;
   }

   public abstract void spellAnimation(Graphics2D g2, int x, int y, int width, int height, int spellIndex);

   //public abstract void spell2(Graphics2D g2, int x, int y, int width, int height);
   //  public abstract void spell3(Graphics2D g2, int x, int y, int width, int height);
   public abstract void move(Graphics2D g2, int x, int y, int width, int height);

   public abstract void drawReal(Graphics2D g2, int x, int y, int width, int height, int spellIndex);

   public abstract void setMovementIndex(int positionIndex, boolean moving);

   public int getAttack() {
      return attack;
   }

   public int getMaxHealth() {
      return maxHealth;
   }

   public int getRange() {
      return range;
   }

   public int getHealth() {
      return health;
   }

   public int getMobility() {
      return mobility;
   }

   public void setAttack(int attack) {
      this.attack = attack;
   }

   public void setHealth(int health) {
      this.health = health;
   }

   public void setMaxHealth(int maxHealth) {
      this.maxHealth = maxHealth;
   }

   public void setMobility(int mobility) {
      this.mobility = mobility;
   }

   public void setRange(int range) {
      this.range = range;
   }

   public void setSpriteID(int spriteID) {
      this.spriteID = spriteID;
   }

   public void setDamaged(boolean damaged) {
      this.damaged = damaged;
   }
}
