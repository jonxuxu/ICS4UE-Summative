package client.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class soundEffectManager {
  private Map<String, String> urls = new HashMap<String, String>();

  public soundEffectManager (){
    urls.put("cow", "cow-moo.wav");
  }

  public void playSound(String name) { // Add synchronized?
      try {
        // Gets a sound clip resource
        Clip clip = AudioSystem.getClip();
        // Opens an audio input stream
        //System.out.println(System.getProperty("user.dir") + "/res/sound/effects/" + urls.get(name));
        File file = new File(System.getProperty("user.dir") + "/res/sound/effects/" + urls.get(name)).getAbsoluteFile();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
        System.out.println(file.getAbsolutePath());
        // Opens clip and load samples from input stream
        clip.open(inputStream);
        clip.start();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
  }
}
