package com.game;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Music {
    private static MediaPlayer mediaPlayer;
    private static String startScreenSong = "src/main/resources/sounds/startScreenSong.mp3";
    private static String gameScreenSong = "src/main/resources/sounds/gameScreenSong.mp3";
    private static String buttonSong =  "src/main/resources/sounds/buttonSound.mp3";
    private static String popSong =  "src/main/resources/sounds/poppingSound.mp3";


    public Music() {
        Media media = new Media(new File(startScreenSong).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
    }

    public static void changeSong(String song) {
        mediaPlayer.dispose();
        Media media = new Media(new File(song).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.4);
        mediaPlayer.play();
    }

    public static Boolean isMute() {
        mediaPlayer.setMute(!mediaPlayer.isMute());
        return mediaPlayer.isMute();
    }

    public static void playButtonSound () {
        Media song = new Media(new File(buttonSong).toURI().toString());
        new MediaPlayer(song).play();
    }

    public static void playPopSound () {
        Media song = new Media(new File(popSong).toURI().toString());
        new MediaPlayer(song).play();
    }

    public static void setVolume(double volume){
        mediaPlayer.setVolume(volume);
    }

    public static String getStartScreenSong(){
        return startScreenSong;
    }

    public static String getGameScreenSong(){
        return gameScreenSong;
    }

}
