package client.gameUi;

import java.awt.*;
import java.io.FileWriter;

public class DebugComponent extends GameComponent {
  private final int MAX_X = super.getMAX_X();
  private final int MAX_Y = super.getMAX_Y();
  private final Font FONT = super.getFont("regular");
  private boolean visible = false;

  // Debug info
  private int fps;
  private int[] mouseState;
  private char keyPress;

  public void draw(Graphics2D g2) {
    if(visible){
      g2.setColor(new Color(0, 0, 0, 128));
      g2.fillRect(0,0,MAX_X, MAX_Y);
      g2.setFont(FONT);
      g2.drawString("Debug boi", 0, 0);
    }
  }

  public void toggle(){
    visible = !visible;
  }
}
