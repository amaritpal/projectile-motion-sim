package utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class MusicManager {
    private final List<String> playlist; // List of audio file paths
    private int currentTrackIndex = 0;
    private MediaPlayer mediaPlayer;

    public MusicManager(List<String> audioFilePaths) {
        this.playlist = new ArrayList<>(audioFilePaths);
        if (!playlist.isEmpty()) {
            loadTrack(currentTrackIndex);
        } else {
            System.err.println("Playlist is empty. Please add audio files.");
        }

        System.out.println("TRACK LIST: ");
        for (String s : playlist) {
            System.out.println(s.substring(6));
        }
    }

    private void loadTrack(int index) {
        if (index >= 0 && index < playlist.size()) {
            String filePath = playlist.get(index);
            try {
                Media media = new Media(getClass().getResource(filePath).toExternalForm());
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                }
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnEndOfMedia(this::playNext); // Automatically play the next track
            } catch (NullPointerException e) {
                System.err.println("Error loading audio file: " + filePath);
            }
        } else {
            System.err.println("Invalid track index: " + index);
        }
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.setCycleCount(1); // Play each track once
            mediaPlayer.play();

            mediaPlayer.setVolume(SettingsManager.getVolume());
        }
        System.out.println("Playing: " + playlist.get(currentTrackIndex).substring(7));
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void playNext() {
        currentTrackIndex = (currentTrackIndex + 1) % playlist.size(); // Loop back to the first track
        loadTrack(currentTrackIndex);
        play();
    }

    public void playPrevious() {
        currentTrackIndex = (currentTrackIndex - 1 + playlist.size()) % playlist.size(); // Go to the previous track
        loadTrack(currentTrackIndex);
        play();
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}
