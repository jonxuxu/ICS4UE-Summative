package client.gameUi;

import client.Client;

import java.awt.*;
import java.io.FileWriter;

public class DebugComponent extends GameComponent {
  private final int MAX_X = super.getMAX_X();
  private final int MAX_Y = super.getMAX_Y();
  private final double scaling = super.getSCALING();
  private final Font FONT = super.getFont("main");
  private boolean visible = false;

  // Debug info
  private int fps;
  private int[] mouse;
  private String[] mouseMessage = {"standby", "click"};
  private char keyPress;

  public void draw(Graphics2D g2) {
    if(visible){
      g2.setColor(new Color(0, 0, 0, 128));
      g2.fillRect(0,0,MAX_X, MAX_Y);
      g2.setColor(Color.white);
      g2.setFont(FONT);
      g2.drawString("DEBUG MODE", 0, (int)(20*scaling));
      g2.drawString("Fps: " + fps, 0, (int)(35*scaling));
      g2.drawString("Mouse: " + mouse[0] + "x " + mouse[1] + "y " + mouseMessage[mouse[2]], 0, (int)(50*scaling));
      g2.drawString("Keyboard: " + keyPress, 0, (int)(65*scaling));
    }
  }

  public void toggle(){
    visible = !visible;
  }

  public void update(int fps, int[] mouseState, char keyPress){
    this.fps = fps;
    this.mouse = mouseState;
    this.keyPress = keyPress;
  }
}
