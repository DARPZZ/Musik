package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
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
    ImageView ImageV;

    @FXML
    Button knapPlay, knapPause, knapStop, knapCreate, knapAdd, knapDelete, knapRemove, knapRename, knapChoose;

    @FXML
    ListView sangeliste, playlistview, playlistsongs;

    @FXML
    TextField searchfield, textfieldInfo;

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


        textfieldInfo.setStyle("-fx-background-color: Black; -fx-text-inner-color: white");
        // mp.setAutoPlay(true);
        // If autoplay is turned off the method play(), stop(), pause() etc controls how/when medias are played
        //mp.setAutoPlay(false);
        // Create new Media object (the actual media content)

        // create the list of songs
        Song.CreateList();

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
    }

    @FXML
    /**
     * Handler for the play/pause/stop button
     */
    public void handlerplay()
    {
        System.out.println("Now playing: " + filepath);
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

                ArrayList<String> songName = new ArrayList<>();
                for (Song object : Song.getSongList())
                {
                    String navn = object.getSONG_NAME();
                    songName.add(navn);
                }
                ObservableList<String> songs = FXCollections.observableArrayList(songName);

                // set the items of the list view
                sangeliste.setItems(songs);
                // searchfield.clear();
            }
        });
    }

    public void handleClickView(MouseEvent mouseEvent)
    {
        String selectedItem = (String) sangeliste.getSelectionModel().getSelectedItem();
        String endSearch = selectedItem.substring(selectedItem.indexOf(" ") + 1, selectedItem.indexOf("Artist") - 1);

        for ( Song songs : Song.getSongList() )
        {
            if (songs.getSONG_NAME().equals(endSearch))
            {
                filepath = songs.getFILE_PATH();
            }
        }
    }
}
