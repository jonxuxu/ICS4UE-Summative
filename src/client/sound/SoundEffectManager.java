package client.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * SoundEffectManager.java
 * This is responsible for playing sound effects
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-02
 */

public class SoundEffectManager {
  private Map<String, String> urls = new HashMap<String, String>();
  private static Clip clip;
  private float masterVolume = -6f; // 50% dB
  private float effectVolume = -6f;

  public SoundEffectManager(){
    urls.put("cow", "cow-moo.wav");
    urls.put("error", "error.wav");
    urls.put("arrow", "arrowShoot.wav");
    urls.put("dagger", "daggerSwing.wav");
    urls.put("sword", "dirk.wav");
    urls.put("bow", "fire_bow.wav");
    urls.put("explosion", "fire_spray.wav");
    urls.put("ghost", "fire_wand.wav");
    urls.put("start", "game_start.wav");
    urls.put("hammer", "golden_sword.wav");
    urls.put("towerSpawn", "magic_nova.wav");
  }

  /**
   * This method plays the clip of a sound effect
   * @param name String, the name of the sound effect
   */

  public void playSound(String name) { // Add synchronized?
      try {
        // Gets a sound clip resource
        clip = AudioSystem.getClip();
        clip.addLineListener(new LineListener() {
          public void update(LineEvent myLineEvent) {
            if (myLineEvent.getType() == LineEvent.Type.STOP)
              clip.close();
          }
        });
        // Opens an audio input stream
        File file = new File(System.getProperty("user.dir") + "/res/sound/effects/" + urls.get(name)).getAbsoluteFile();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
        // Opens clip and load samples from input stream
        clip.open(inputStream);

        // Sets volume
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(masterVolume);

        clip.start();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
  }

  /**
   * This method sets the volume of the sound effect manager
   * @param value A float array for different gain values
   */
  public void setVolume(float[] value ){
    masterVolume = value[0];
  }
}
