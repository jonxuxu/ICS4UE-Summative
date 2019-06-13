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

public class SoundEffectManager {
  private Map<String, String> urls = new HashMap<String, String>();
  private float masterVolume = -6f; // 50% dB
  private float effectVolume = -6f;

  public SoundEffectManager(){
    urls.put("cow", "cow-moo.wav");
    urls.put("error", "error.wav");
  }

  public void playSound(String name) { // Add synchronized?
      try {
        // Gets a sound clip resource
        Clip clip = AudioSystem.getClip();
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

  // Sets volume
  public void setVolume(float[] value ){
    masterVolume = value[0];
  }
}
