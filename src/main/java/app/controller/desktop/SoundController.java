package app.controller.desktop;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;

/**
 *
 * @author repp
 */
public class SoundController {
    
    public final Lock audioLock = new ReentrantLock();
    public HashMap<String, List<Clip>> mediaPlayers = new HashMap();

    public void playSound(String fileName, Boolean isLoop) {
        System.out.println("SoundController: playSound: fileName=" + fileName + ", isLoop=" + isLoop);

        InputStream inputStream = SoundController.class.getResourceAsStream(fileName);
        if (inputStream == null) {
            System.err.println("SoundController: playSound: File not found!");
            return;
        }

        this.audioLock.lock();
        System.out.println("SoundController: playSound: Claimed lock");
        
        try {
            InputStream bufferedIn = new BufferedInputStream(inputStream);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
            
            if (this.mediaPlayers.containsKey(fileName)) {
                List<Clip> list = this.mediaPlayers.get(fileName);
                list.add(clip);
                System.out.println("SoundController: playSound: Added new collection for file");
            } else {
                List<Clip> list = new ArrayList();
                list.add(clip);
                this.mediaPlayers.put(fileName, list);
                System.out.println("SoundController: playSound: Added file to collection");
            }
            if (isLoop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();

                clip.addLineListener((LineEvent event) -> {
                    try {
                        if (event.getType() == LineEvent.Type.STOP) {
                            SoundController.this.audioLock.lock();
                            System.out.println("SoundController: playSound: End of media: Claimed lock");
                            List<Clip> playerList = SoundController.this.mediaPlayers.get(fileName);
                            if (playerList != null) {
                                playerList.remove(clip);
                                if (playerList.isEmpty()) {
                                    SoundController.this.mediaPlayers.remove(fileName);
                                }
                            }
                            clip.close();
                        }
                    } catch (Exception e) {
                        System.err.println("SoundController: playSound: End of media: Error: " + e.getMessage());
                    } finally {
                        SoundController.this.audioLock.unlock();
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("SoundController: playSound: Error: " + e.getMessage());
        } finally {
            this.audioLock.unlock();
        }
    }
    
    public void stopSound(String fileName, Boolean removeAudioPlayer) {
        System.out.println("SoundController: stopSound: fileName=" + fileName + ", removeAudioPlayer=" + removeAudioPlayer);
        
        if (!this.mediaPlayers.containsKey(fileName)) {
            System.out.println("SoundController: stopSound: Collection for file not found");
            return;
        }

        this.audioLock.lock();
        System.out.println("SoundController: stopSound: Claimed lock");
        
        try {
            List<Clip> list = this.mediaPlayers.get(fileName);
            for (Clip mediaPlayer : list) {
                if (mediaPlayer.isRunning()) {
                    mediaPlayer.stop();
                    mediaPlayer.setFramePosition(0); // Please be kind, rewind
                    System.out.println("SoundController: stopSound: Stopped media");
                }
                if (removeAudioPlayer) {
                    HashMap<String, List<Clip>> allMediaPlayers = this.mediaPlayers;
                    list.remove(mediaPlayer);
                    if (list.isEmpty()) {
                        allMediaPlayers.remove(fileName);
                        mediaPlayer.close();
                        System.out.println("SoundController: stopSound: Removed media player");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("SoundController: stopSound: Error: " + e.getMessage());
        } finally {
            this.audioLock.unlock();
        }
    }
    
    public void stopAllSounds() {
        System.out.println("SoundController: stopAllSounds");
        
        this.audioLock.lock();
        System.out.println("SoundController: stopAllSounds: Claimed lock");
        
        try {
            for (String fileName : this.mediaPlayers.keySet()) {
                for (Clip mediaPlayer : this.mediaPlayers.get(fileName)) {
                    if (mediaPlayer.isRunning()) {
                        mediaPlayer.stop();
                        mediaPlayer.close();
                        System.out.println("SoundController: stopSound: Stopped media : " + fileName);
                    }
                }
            }
            this.mediaPlayers.clear();
        } catch (Exception e) {
            System.err.println("SoundController: stopAllSounds: Error: " + e.getMessage());
        } finally {
            this.audioLock.unlock();
        }
    }
    
    public void pauseAllSounds() {
        System.out.println("SoundController: pauseAllSounds");
        
        this.audioLock.lock();
        System.out.println("SoundController: pauseAllSounds: Claimed lock");
        
        try {
            for (String fileName : this.mediaPlayers.keySet()) {
                for (Clip mediaPlayer : this.mediaPlayers.get(fileName)) {
                    if (mediaPlayer.isRunning()) {
                        mediaPlayer.stop(); // Stopping without rewinding pauses
                        System.out.println("SoundController: pauseAllSounds: Paused media : " + fileName);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("SoundController: pauseAllSounds: Error: " + e.getMessage());
        } finally {
            this.audioLock.unlock();
        }
    }

    public void unpauseAllSounds() {
        System.out.println("SoundController: unpauseAllSounds");
        
        this.audioLock.lock();
        System.out.println("SoundController: unpauseAllSounds: Claimed lock");
        
        try {
            for (String fileName : this.mediaPlayers.keySet()) {
                for (Clip mediaPlayer : this.mediaPlayers.get(fileName)) {
                    if (!mediaPlayer.isRunning()) {
                        mediaPlayer.start();
                        System.out.println("SoundController: unpauseAllSounds: Paused media : " + fileName);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("SoundController: unpauseAllSounds: Error: " + e.getMessage());
        } finally {
            this.audioLock.unlock();
        }
    }

}
