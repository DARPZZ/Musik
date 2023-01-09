package sample;

import com.sun.org.apache.xalan.internal.xsltc.dom.SortingIterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
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
    private String filepath = new File("DemoMediaPlayer-master/src/sample/media/SampleAudio_0.4mb.mp3").getAbsolutePath();


    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources){
        // Build the path to the location of the media file

        // mp.setAutoPlay(true);
        // If autoplay is turned off the method play(), stop(), pause() etc controls how/when medias are played
       // mp.setAutoPlay(false);
        // Create new Media object (the actual media content)
        // Play the mediaPlayer with the attached media

        System.out.println(filepath);

        // create the list of songs
        Song.CreateList();
/*
        ArrayList<String> songName = new ArrayList<>();

        for (Song object : Song.getSongList())
        {
            String navn = "Song: "+  object.getSONG_NAME() + " Artist: " + object.getARTIST();
            songName.add(navn);

        }
        ObservableList<String> songs = FXCollections.observableArrayList(songName);

        // set the items of the list view
        sangeliste.setItems(songs);

        // set the selection mode to single, so only one song can be selected at a time
        sangeliste.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

 */
        publishSong();

    }

    @FXML
    /**
     * Handler for the play/pause/stop button
     */
    public void handlerplay()
    {
        me = new Media(new File(filepath).toURI().toString());
        // Create new MediaPlayer and attach the media to be played
        mp = new MediaPlayer(me);
        //
        mediaV.setMediaPlayer(mp);
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
    public void handlerSearch()
    {
        searchfield.setOnKeyPressed(handlerSearch -> {
            // Handle the key press event here
            KeyCode code = handlerSearch.getCode();
            if (code == KeyCode.ENTER) {

                String search = searchfield.getText();

                Song.searchSong(search);
                publishSong();
                // searchfield.clear();
            }


        });

    }



    public void handleClickView(MouseEvent mouseEvent)
    {
        String selectedItem = (String) sangeliste.getSelectionModel().getSelectedItem();

        for (int i = 0; i < Song.getSongList().size(); i++)
        {
            if (Song.getSongList().get(i).getSONG_NAME().equals(selectedItem))
            {
                filepath = Song.getSongList().get(i).getFILE_PATH();
            }
        }
    }
    public void publishSong()
        {
            ArrayList<String> songName = new ArrayList<>();

            for (Song object : Song.getSongList())
            {
                String navn = "Song: "+  object.getSONG_NAME() + " Artist: " + object.getARTIST();
                songName.add(navn);

            }
            ObservableList<String> songs = FXCollections.observableArrayList(songName);

            // set the items of the list view
            sangeliste.setItems(songs);

            // set the selection mode to single, so only one song can be selected at a time
            sangeliste.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            System.out.println("ko");
        }
}
