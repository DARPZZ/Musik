package sample;

import com.sun.org.apache.xalan.internal.xsltc.dom.SortingIterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.*;
import javafx.scene.control.Button;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.net.*;
import java.rmi.server.ExportException;
import java.text.BreakIterator;
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
    TextField searchfield, TF_PlaylistName;

    private MediaPlayer mp;
    private Media me;
    private String filepath = new File("DemoMediaPlayer-master/src/sample/media/SampleAudio_0.4mb.mp3").getAbsolutePath();
    public ArrayList<String> ListPlaylist = new ArrayList<>();
    public Playlist ActivePlaylist = new Playlist(null,0);

    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources){
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

        // Playliste View init
        /*
        ObservableList<String> ListPlaylistOut = FXCollections.observableArrayList(ListPlaylist);
        ArrayList<String> songPlaylist = new ArrayList<>();
        ObservableList<String> songPlaylistOut = FXCollections.observableArrayList(songPlaylist);
        playlistview.setItems(ListPlaylistOut);
        playlistsongs.setItems(songPlaylistOut);
         */
        playlistview.setItems(FXCollections.observableArrayList(Playlist.PlaylistArray()));
        playlistview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        playlistsongs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        System.out.println(Playlist.PlaylistArray());


    }

    public String getPlaylistView()
    {
        return playlistview.getSelectionModel().getSelectedItem().toString();
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
        String beginSearch = selectedItem.substring(selectedItem.indexOf(" ") + 1);
        String endSearch = beginSearch.substring(0, selectedItem.indexOf(" "));

        System.out.println(endSearch);

        for ( Song songs : Song.getSongList() )
        {

            if (songs.getSONG_NAME().substring(0, 5).equals(endSearch))
            {
                filepath = songs.getFILE_PATH();
            }
        }
    }
    public void handlerPL_Create()
    {
        String PLname = TF_PlaylistName.getText();
        Playlist ActivePlaylist = new Playlist(PLname,Playlist.createPlaylist(PLname)); // Ugly code, Creates the Playlist in SQL and the instance of the Playlist Class
        ActivePlaylist.playlistSongNameFill();
        System.out.println(Playlist.PlaylistArray());
        playlistview.setItems(FXCollections.observableArrayList(Playlist.PlaylistArray()));


    }
    public void handlerPL_Delete()
    {
        ActivePlaylist.deletePlaylist();
        playlistview.setItems(FXCollections.observableArrayList(Playlist.PlaylistArray()));
    }
    public void handlerPL_Rename()
    {}
    public void handlerPL_Select(MouseEvent event)
    {
        try // Java throws an error if you click on a non entry in the table, catch to ignore
        {
            String selectedPL = playlistview.getSelectionModel().getSelectedItem().toString();
            ActivePlaylist.setPlaylistName(selectedPL);
            ActivePlaylist.setPlaylistID( Playlist.getPlaylistID(selectedPL));
            ActivePlaylist.playlistSongNameFill();
            playlistsongs.setItems(FXCollections.observableArrayList(ActivePlaylist.getListPlaylist()));
        }
        catch (Exception e ) {System.out.println();}

    }
}
