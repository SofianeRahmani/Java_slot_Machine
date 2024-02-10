import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Objects;

public class BackgroundMusic {
    private static final BackgroundMusic instance = new BackgroundMusic();
    private Clip clip;
    private int referenceCount = 0;
    private float volume = 1.0f;

    private BackgroundMusic() {}

    public static BackgroundMusic getInstance() {
        return instance;
    }

    public synchronized void addReference() {
        referenceCount++;
        if (referenceCount == 1) {
            play("/music/background.wav");
        }
    }

    public synchronized void removeReference() {
        referenceCount--;
        if (referenceCount == 0) {
            stop();
        }
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        adjustVolume();
    }

    private void play(String musicFilePath) {
        try {
            if (clip == null || !clip.isOpen()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                        Objects.requireNonNull(getClass().getResource(musicFilePath)));
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            adjustVolume();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

            e.printStackTrace();
        }
    }

    private void adjustVolume() {
        if (clip != null && clip.isRunning()) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range * volume) + gainControl.getMinimum();
                gainControl.setValue(gain);
            } catch (IllegalArgumentException e) {
                // Consider logging this exception
                e.printStackTrace();
            }
        }
    }

    private void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
