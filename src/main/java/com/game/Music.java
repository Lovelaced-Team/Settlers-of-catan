package com.game;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Music {
    private static AudioClip currentSong;

    public static String startScreenSong = "src/main/resources/sounds/startScreenSong.mp3";
    public static String gameScreenSong = "src/main/resources/sounds/gameScreenSong.mp3";

    public Music(){
        this.changeSong(startScreenSong);
    }

    public static void changeSong(String song){
        if (currentSong != null) {currentSong.stop();}

        Media media = new Media(new File(song).toURI().toString());

        currentSong = new AudioClip(media.getSource());
        currentSong.setCycleCount(MediaPlayer.INDEFINITE);
        currentSong.play();
    }

    public static Boolean songPlaying() {

        if(currentSong.isPlaying())
            currentSong.stop();
        else
            currentSong.play();

        return currentSong.isPlaying();
    }
}
