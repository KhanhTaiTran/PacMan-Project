import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;

public class SoundEffect {
    private Clip clip;

    public void playSound(String soundName) {
        try {
            URL soundFileURL = getClass().getResource("/Sound/" + soundName);
            if (soundFileURL == null) {
                throw new RuntimeException("Sound file not found: " + soundName);
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFileURL);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file format: " + soundName);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error playing sound: " + soundName);
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("Audio line for playing sound is unavailable: " + soundName);
            e.printStackTrace();
        }
    }

    public void stopSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
