package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.media.*;
import javafx.scene.control.Button;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private MediaView mediaV;

    @FXML
    Button knapPlay, knapPause, knapStop;

    @FXML
    ListView sangeliste, playlistview, playlistsongs;

    @FXML
    TextField searchfield;

    private MediaPlayer mp;
    private Media me;


    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources){
        // Build the path to the location of the media file
        String path = new File("DemoMediaPlayer-master/src/sample/media/Avicii vs. Conrad Sewell - Taste The Feeling.mp3").getAbsolutePath();
        // Create new Media object (the actual media content)
        me = new Media(new File(path).toURI().toString());
        // Create new MediaPlayer and attach the media to be played
        mp = new MediaPlayer(me);
        //
        mediaV.setMediaPlayer(mp);
        // mp.setAutoPlay(true);
        // If autoplay is turned off the method play(), stop(), pause() etc controls how/when medias are played
        mp.setAutoPlay(false);

        // create the list of songs
        Song.CreateList();

        ArrayList<String> songName = new ArrayList<>();

        for (Song object : Song.getSongList())
        {
            songName.add(object.getSONG_NAME());
        }
        ObservableList<String> songs = FXCollections.observableArrayList(songName);

        // set the items of the list view
        sangeliste.setItems(songs);

        // set the selection mode to single, so only one song can be selected at a time
        sangeliste.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    /**
     * Handler for the play button
     */
    public void handlerplay()
    {
        // Play the mediaPlayer with the attached media
        mp.play();
    }
    public void handlerPause()
    {
        mp.pause();
    }
    public void handlerStop()
    {
        mp.stop();
    }


}
