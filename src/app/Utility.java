package app;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Utility {
    
    public static Map<String, AudioInputStream> audioInputStreams = new HashMap<>();
    public static Map<String, Object> audioPlayers = new HashMap<>();
    public static Map<String, InputStream> inputStreams = new HashMap<>();
    private static final Lock lock = new ReentrantLock();
    
    public static void playSound(String fileName, Boolean isLoop) {
        System.out.println("Utility: playSound: fileName= " + fileName);
        
        if (fileName == null) {
            return;
        }
        
        // Quit early if the sound is already playing
        Boolean isPlaying = false;
        Iterator<Map.Entry<String, Object>> iterator = audioPlayers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String currentFileName = entry.getKey();
            if (currentFileName.equals(fileName)) {
                isPlaying = true;
                break;
            }
        }
        
        if (isPlaying) {
            System.out.println("Utility: playSound: Audio is already playing!");
            return;
        }

        try {
            String fileType = getFileExtension(fileName);
            switch (fileType) {
                case "mp3" -> {
                    InputStream inputStream = getInputStream(fileName);
                    if (inputStream == null) {
                        System.err.println("Utility: playSound: File not found in classpath");
                        return;
                    }
                    Player player = getPlayer(fileName, inputStream);
                    new Thread(() -> {
                        try {
                            inputStreams.put(fileName, inputStream);
                            audioPlayers.put(fileName, player);
                            if (isLoop) {
                                ThreadGroup group = Thread.currentThread().getThreadGroup().getParent();
                                Thread[] parentThreads = new Thread[group.activeCount()];
                                group.enumerate(parentThreads);

                                Thread parentThread = null;
                                for(Thread thread: parentThreads){
                                    if(thread.getName().equals("main"))
                                        parentThread = thread;
                                }
                                while (parentThread.isAlive()) {
                                    player.play();
                                }
                            } else {
                                player.play();
                            }
                            stopSound(fileName, true);
                        } catch (JavaLayerException e) {
                            System.err.println("Utility: playSound: Error playing MP3 file: " + e.toString());
                        }
                    }).start();
                }
                case "wav" -> {
                    InputStream inputStream = getInputStream(fileName);
                    if (inputStream == null) {
                        System.err.println("Utility: playSound: File not found in classpath");
                        return;
                    }
                    AudioInputStream audioInputStream = getAudioInputStream(fileName, inputStream);
                    Clip clip = getClip(fileName);
                    clip.open(audioInputStream);
                    new Thread(() -> {
                        audioInputStreams.put(fileName, audioInputStream);
                        inputStreams.put(fileName, inputStream);
                        audioPlayers.put(fileName, clip);
                        clip.start();   // TODO - How to support playing in a loop?
                        clip.addLineListener(event -> {
                            if (event.getType() == LineEvent.Type.STOP) {
                                System.out.println("Utility: playSound: WAV file playback complete");
                                stopSound(fileName, true);
                            }
                        });
                    }).start();
                }
                default -> System.err.println("Utility: playSound: Unsupported file type: " + fileType);
            }
        } catch (IOException | LineUnavailableException  e) {
            Logger.getLogger(SWTApplication.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("Utility: playSound: Error playing file: " + e.toString());
        }
    }
    
    public static InputStream getInputStream(String fileName) {
        InputStream inputStream = inputStreams.get(fileName);
        if (inputStream == null) {
            inputStream = Utility.class.getResourceAsStream(fileName);
        }
        return inputStream;
    }
    
    public static AudioInputStream getAudioInputStream(String fileName, InputStream inputStream) {
        AudioInputStream audioInputStream = audioInputStreams.get(fileName);
        if (audioInputStream == null) {
            try {
                audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(inputStream));
            } catch (UnsupportedAudioFileException | IOException e) {
                System.err.println("Utility: getAudioInputStream: Error: " + e.toString());
            }
        }
        return audioInputStream;
    }
    
    public static Player getPlayer(String fileName, InputStream inputStream) {
        Player player = (Player) audioPlayers.get(fileName);
        if (player == null) {
            try {
                player = new Player(inputStream);
            } catch (JavaLayerException e) {
                System.err.println("Utility: getPlayer: Error: " + e.toString());
            }
        }
        return player;
    }
    
    public static Clip getClip(String fileName) {
        Clip clip = (Clip) audioPlayers.get(fileName);
        if (clip == null) {
            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                System.err.println("Utility: getClip: Error: " + e.toString());
            }
        }
        return clip;
    }
    
    public static void stopSound(String fileName, Boolean removeAudioPlayer) {
        System.out.println("Utility: stopSound: fileName= " + fileName);
        
        lock.lock();
        
        try {
            String fileType = getFileExtension(fileName);
            switch (fileType) {
                case "mp3" -> {
                    Player player = (Player) audioPlayers.get(fileName);
                    if (player != null) {
                        player.close();
                    }
                    InputStream inputStream = inputStreams.get(fileName);
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            System.err.println("Utility: stopSound: Error closing input stream: " + e.toString());
                        }
                    }
                }
                case "wav" -> {
                    Clip clip = (Clip) audioPlayers.get(fileName);
                    if (clip != null) {
                        clip.stop();
                        clip.close();
                    }
                    InputStream inputStream = inputStreams.get(fileName);
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            System.err.println("Utility: stopSound: Error closing input stream: " + e.toString());
                        }
                    }
                    AudioInputStream audioInputStream = audioInputStreams.get(fileName);
                    if (audioInputStream != null) {
                        try {
                            audioInputStream.close();
                        } catch (IOException e) {
                            System.err.println("Utility: stopSound: Error closing audio input stream: " + e.toString());
                        }
                    }
                    audioInputStreams.remove(fileName);
                }
                default -> System.err.println("Utility: stopSound: Unsupported file type: " + fileType);
            }
            if (removeAudioPlayer) {
                audioPlayers.remove(fileName);
            }
            inputStreams.remove(fileName);
        } finally {
            lock.unlock();
        }
    }
    
    public static void stopAllSounds() {
        System.out.println("Utility: stopAllSounds");
        Iterator<Map.Entry<String, Object>> iterator = audioPlayers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String fileName = entry.getKey();
            stopSound(fileName, false);
            lock.lock();
            try {
                iterator.remove();
            } catch(ConcurrentModificationException e) {
                System.err.println("Utility: stopAllSounds: " + e.toString());
            } finally {  
                lock.unlock();
            }
        }
    }
    
    public static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index > 0 && index < fileName.length() - 1) {
            return fileName.substring(index + 1).toLowerCase();
        }
        return "";
    }
    
    public static void playMP3(String soundFileName) {
        if (soundFileName == null) {
            return;
        }
        
        try {
            InputStream audioStream = Utility.class.getResourceAsStream(soundFileName);
            if (audioStream == null) {
                System.err.println("Resource not found: " + soundFileName);
                return;
            }
            Player player = new Player(audioStream);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Object instance(String className) {
        return instance(className, null);
    }
    
    public static Object instance(String className, Object parameter) {
        Object instance = null;
        try {
            Class<?> appClass = Class.forName(className);
            try {
                try {
                    if (parameter == null) {
                        Constructor<?> constructor = appClass.getConstructor();
                        instance = constructor.newInstance();
                    } else {
                        Constructor<?> constructor = appClass.getConstructor(parameter.getClass());
                        instance = constructor.newInstance(parameter);
                    }
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, e);
                    System.err.println("ERROR: " + e.toString());
                }
            } catch (NoSuchMethodException | SecurityException e) {
                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, e);
                System.err.println("ERROR: " + e.toString());
            }
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("ERROR: " + e.toString());
        }
        return instance;
    }
    
    public static Object getValue(Object object, Field field) {
        Object value = null;
        try {
            value = field.get(object);
        } catch (IllegalAccessException e) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("ERROR: " + e.toString());
        }
        return value;
    }
    
    public static String toPascalCase(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        String newString = Character.toUpperCase(string.charAt(0)) + string.substring(1);
        return newString;
    }
    
}
