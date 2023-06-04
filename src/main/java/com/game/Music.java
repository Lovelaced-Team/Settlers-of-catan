package com.game;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Music {
    private static MediaPlayer mediaPlayer;
    public static String startScreenSong = "src/main/resources/sounds/startScreenSong.mp3";
    public static String gameScreenSong = "src/main/resources/sounds/gameScreenSong.mp3";

    public Music(){
        Media media = new Media(new File(startScreenSong).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public static void changeSong(String song){
        mediaPlayer.dispose();
        Media media = new Media(new File(song).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public static Boolean isMute() {
        mediaPlayer.setMute(!mediaPlayer.isMute());
        return mediaPlayer.isMute();
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
