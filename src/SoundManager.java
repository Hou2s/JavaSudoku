import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private Clip clip;

    public SoundManager(String soundFileName) {
        try {
            // Set up an audio input stream piped from the sound file.
            File soundFile = new File(soundFileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            // Get a clip resource.
            clip = AudioSystem.getClip();

            // Open the clip and load samples from the audio input stream.
            clip.open(audioStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // I will maybe later include more soundtracks and more methods , Also I might include sound effects in this class.

}
