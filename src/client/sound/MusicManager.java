package client.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.File;
/**
 * MusicManager.java
 * This is for the music
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-02
 */

public class MusicManager {
  private float masterVolume = -6f; // 50% dB
  private float musicVolume = -6f;
  Clip clip;

  /**
   * Starts the audio
   */
  public void start(){
    try{
      // Gets a sound clip resource
      clip = AudioSystem.getClip();
      clip.addLineListener(new LineListener() {
        public void update(LineEvent myLineEvent) {
          if (myLineEvent.getType() == LineEvent.Type.STOP)
            clip.close();
        }
      });
      // Opens an audio input stream
      File file = new File(System.getProperty("user.dir") + "/res/sound/music/rotmg.wav");
      AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
      // Opens clip and load samples from input stream
      clip.open(inputStream);

      // Sets volume
      FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(masterVolume);

      clip.loop(Clip.LOOP_CONTINUOUSLY);
    } catch (Exception e){
      e.printStackTrace();
    }

  }

  /**
   * Sets the volume
   * @param value: A float array for different gain values
   */
  public void setVolume(float[] value ){
    masterVolume = value[0];
    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(masterVolume);
  }

}
