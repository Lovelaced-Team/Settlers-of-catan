package com.game;

import com.screens.StartScreen;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {
    private static MediaPlayer mediaPlayer;
    private static String startScreenSong = "/sounds/startScreenSong.mp3";
    private static String gameScreenSong = "/sounds/gameScreenSong.mp3";
    private static String buttonSong =  "/sounds/buttonSound.mp3";
    private static String popSong =  "/sounds/poppingSound.mp3";


    public Music() {
        Media media = new Media(StartScreen.class.getResource(startScreenSong).toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
    }

    public static void changeSong(String song) {
        mediaPlayer.dispose();
        Media media = new Media(StartScreen.class.getResource(song).toExternalForm());
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
        Media song = new Media(StartScreen.class.getResource(buttonSong).toExternalForm());
        new MediaPlayer(song).play();
    }

    public static void playPopSound () {
        Media song = new Media(StartScreen.class.getResource(popSong).toExternalForm());
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
