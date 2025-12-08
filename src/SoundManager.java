import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundManager {
    private static volatile SoundManager instance;
    private Map<String, byte[]> soundData;
    private boolean soundEnabled = true;
    private ExecutorService soundExecutor;
    
    private SoundManager() {
        soundData = new HashMap<>();
        soundExecutor = Executors.newFixedThreadPool(4); // Limit concurrent sound threads
        loadSounds();
    }
    
    public static SoundManager getInstance() {
        if (instance == null) {
            synchronized (SoundManager.class) {
                if (instance == null) {
                    instance = new SoundManager();
                }
            }
        }
        return instance;
    }
    
    private void loadSounds() {
        // Load all sound effects as synthetic sounds
        // We'll generate simple beep sounds programmatically to avoid external dependencies
        try {
            soundData.put("eat_dot", generateBeep(100, 0.05, 800));
            soundData.put("eat_power", generateBeep(200, 0.2, 400));
            soundData.put("eat_ghost", generateBeep(300, 0.3, 600));
            soundData.put("death", generateSweep(500, 200, 0.5));
            soundData.put("level_complete", generateChord(300, 0.5));
        } catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
            soundEnabled = false;
        }
    }
    
    // Generate a simple beep tone
    private byte[] generateBeep(int frequency, double duration, int amplitude) {
        int sampleRate = 8000;
        int numSamples = (int) (sampleRate * duration);
        byte[] buffer = new byte[numSamples * 2]; // 16-bit audio = 2 bytes per sample
        
        for (int i = 0; i < numSamples; i++) {
            double angle = 2.0 * Math.PI * i * frequency / sampleRate;
            short value = (short) (Math.sin(angle) * amplitude);
            
            // Convert to bytes (little-endian)
            buffer[2 * i] = (byte) (value & 0xFF);
            buffer[2 * i + 1] = (byte) ((value >> 8) & 0xFF);
        }
        
        return buffer;
    }
    
    // Generate a frequency sweep (for death sound)
    private byte[] generateSweep(int startFreq, int endFreq, double duration) {
        int sampleRate = 8000;
        int numSamples = (int) (sampleRate * duration);
        byte[] buffer = new byte[numSamples * 2];
        
        for (int i = 0; i < numSamples; i++) {
            double progress = (double) i / numSamples;
            int frequency = (int) (startFreq + (endFreq - startFreq) * progress);
            double angle = 2.0 * Math.PI * i * frequency / sampleRate;
            short value = (short) (Math.sin(angle) * 1000 * (1 - progress)); // Fade out
            
            buffer[2 * i] = (byte) (value & 0xFF);
            buffer[2 * i + 1] = (byte) ((value >> 8) & 0xFF);
        }
        
        return buffer;
    }
    
    // Generate a chord (for level complete)
    private byte[] generateChord(int baseFreq, double duration) {
        int sampleRate = 8000;
        int numSamples = (int) (sampleRate * duration);
        byte[] buffer = new byte[numSamples * 2];
        
        // Major chord: base, major third, perfect fifth
        int[] frequencies = {baseFreq, (int)(baseFreq * 1.25), (int)(baseFreq * 1.5)};
        
        for (int i = 0; i < numSamples; i++) {
            double sample = 0;
            for (int freq : frequencies) {
                double angle = 2.0 * Math.PI * i * freq / sampleRate;
                sample += Math.sin(angle);
            }
            short value = (short) (sample * 300); // Amplitude
            
            buffer[2 * i] = (byte) (value & 0xFF);
            buffer[2 * i + 1] = (byte) ((value >> 8) & 0xFF);
        }
        
        return buffer;
    }
    
    public void playSound(String soundName) {
        if (!soundEnabled) {
            return;
        }
        
        byte[] data = soundData.get(soundName);
        if (data == null) {
            return;
        }
        
        // Play sound using thread pool to avoid resource exhaustion
        soundExecutor.execute(() -> {
            Clip clip = null;
            try {
                // Create audio format
                AudioFormat format = new AudioFormat(
                    8000,  // sample rate
                    16,    // bits per sample
                    1,     // channels (mono)
                    true,  // signed
                    false  // little-endian
                );
                
                // Create audio input stream
                try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                     AudioInputStream audioInputStream = new AudioInputStream(
                         bais, format, data.length / format.getFrameSize())) {
                    
                    // Get and open a clip
                    clip = AudioSystem.getClip();
                    final Clip finalClip = clip;
                    clip.open(audioInputStream);
                    
                    // Add listener to close clip after playing
                    clip.addLineListener(event -> {
                        if (event.getType() == LineEvent.Type.STOP) {
                            finalClip.close();
                        }
                    });
                    
                    // Start playing
                    clip.start();
                    
                    // Wait for clip to finish
                    while (clip.isRunning()) {
                        Thread.sleep(10);
                    }
                }
                
            } catch (LineUnavailableException | IOException | InterruptedException e) {
                System.err.println("Error playing sound: " + e.getMessage());
                if (clip != null && clip.isOpen()) {
                    clip.close();
                }
            }
        });
    }
    
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }
    
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
}
