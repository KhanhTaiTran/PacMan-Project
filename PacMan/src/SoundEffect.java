import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;

public class SoundEffect {
    private Clip clip;
    private boolean isMuted = false;

    // this method don't repeat sound
    public void playSound(String soundName) {
        if (isMuted) {
            return;
        }
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

    // this method repeat sound
    public void playLoopingSound(String soundName) {
        if (isMuted) {
            return;
        }
        try {
            URL soundFileURL = getClass().getResource("/Sound/" + soundName);
            if (soundFileURL == null) {
                throw new RuntimeException("Sound file not found: " + soundName);
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFileURL);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            setVolume(clip, -20.0f); // set volume to -20.0f
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

    // set volume of the sound
    private void setVolume(Clip clip, float volume) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volume);
        }
    }

    public void stopSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void mute() {
        isMuted = true;
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void unmute() {
        isMuted = false;
        if (clip != null) {
            clip.start();
        }
    }
}
