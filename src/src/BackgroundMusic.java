import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Objects;

public class BackgroundMusic {
    private static BackgroundMusic instance;
    private Clip clip;
    private int referenceCount;
    private float volume;

    private BackgroundMusic() {
        referenceCount = 0;
        volume = 1.0f;
    }

    public static BackgroundMusic getInstance() {
        if (instance == null) {
            instance = new BackgroundMusic();
        }
        return instance;
    }

    public void addReference() {
        referenceCount++;
        if (referenceCount == 1) {
            play("/music/wind.wav");
        }
    }

    public void removeReference() {
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
        if (clip != null) {
            adjustVolume();
        }
    }

    private void play(String musicFilePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(musicFilePath)));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            adjustVolume();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException e) {
            System.err.println("The audio format is not supported by the system. Trying a fallback format or handling the error.");
            // Consider a fallback strategy here
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }


    private void adjustVolume() {
        if (clip != null) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range * volume) + gainControl.getMinimum();
                gainControl.setValue(gain);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
