package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.media.*;
import javafx.scene.control.Button;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Duration;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private MediaView mediaV;
    @FXML
    ImageView ImageV, imageV2;

    @FXML
    Button knapPlay, knapPause, knapStop, knapCreate, knapAdd, knapDelete, knapRemove, knapRename, knapChoose;

    @FXML
    ListView sangeliste, playlistview, playlistsongs;

    @FXML
    TextField searchfield, textfieldInfo, TF_PlaylistName;

    final Timeline timeline = new Timeline();

    private MediaPlayer mp;
    private Media me;
    private String filepath = new File("DemoMediaPlayer-master/src/sample/media/SampleAudio_0.4mb.mp3").getAbsolutePath();
    private String displayInfo;
    public Playlist ActivePlaylist = new Playlist(null, 0);
    public String selectedItem;

    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     * @param location
     * @param resources
     */

    public void initialize(URL location, ResourceBundle resources) {
        textfieldInfo.setStyle("-fx-background-color: Black; -fx-text-inner-color: white");
        Image defaultImage = new Image("DemoMediaPlayer-master/src/sample/Logo/Das Music Playa.png");
        imageV2.setImage(defaultImage);
        //Unicode for play/pause/stop symbols in corresponding buttons
        knapPause.setText("\u23f8");
        knapStop.setText("\u23f9");
        knapPlay.setText("\u23f5");

        // mp.setAutoPlay(true);
        // If autoplay is turned off the method play(), stop(), pause() etc controls how/when medias are played
        //mp.setAutoPlay(false);
        // Create new Media object (the actual media content)

        // create the list of songs
        Song.CreateList();

        ArrayList<String> songName = new ArrayList<>();
        for (Song object : Song.getSongList())
        {
            double duration = Playlist.durationIntToDouble((double)object.getDURATION());

            String navn = "Song: " + object.getSONG_NAME() + " Artist: " + object.getARTIST()+ " Duration: " +duration;
            songName.add(navn);
        }
        ObservableList<String> songs = FXCollections.observableArrayList(songName);

        // set the items of the list view
        sangeliste.setItems(songs);

        // set the selection mode to single, so only one song can be selected at a time
        sangeliste.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Playlist setup
        playlistview.setItems(FXCollections.observableArrayList(Playlist.PlaylistArray()));
        playlistview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        playlistsongs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        System.out.println(Playlist.PlaylistArray());
    }

    /**
     * Method updates the playlist view
     */
    public void updatePlaylistView() {
        playlistview.setItems(FXCollections.observableArrayList(Playlist.PlaylistArray()));
    }
    public void updatePlaylistSongView()
    {
        playlistsongs.setItems(FXCollections.observableArrayList(ActivePlaylist.getListPlaylist()));
    }

    @FXML
    /**
     * Handler for the play/pause/stop button
     */
    public void handlerplay()
    {
        String endSearch = selectedItem.substring(selectedItem.indexOf(" ") + 1, selectedItem.indexOf("Artist") - 1);
        System.out.println(endSearch);
        loadBilleder();



        for (Song songs : Song.getSongList()) {
            if (songs.getSONG_NAME().equals(endSearch)) {
                filepath = songs.getFILE_PATH();
                displayInfo = songs.getARTIST() + " - " + songs.getSONG_NAME() + " - " + songs.getDURATION();
            }
        }

        textfieldInfo.setText(displayInfo);
        System.out.println("Now playing: " + filepath);
        me = new Media(new File(filepath).toURI().toString());
        // Create new MediaPlayer and attach the media to be played
        mp = new MediaPlayer(me);
        //
        mediaV.setMediaPlayer(mp);
        mp.play();
    }

    public void handlerPause() {
        mp.pause();
        timeline.stop();
        timeline.getKeyFrames().clear();
    }

    public void handlerStop() {
        mp.stop();
        timeline.stop();
       timeline.getKeyFrames().clear();
    }

    public void handlerSearch() {
        searchfield.setOnKeyPressed(handlerSearch -> {
            // Handle the key press event here
            KeyCode code = handlerSearch.getCode();
            if (code == KeyCode.ENTER) {

                String search = searchfield.getText();

                Song.searchSong(search);

                ArrayList<String> songName = new ArrayList<>();
                for (Song object : Song.getSongList()) {
                    String navn = "Song: " + object.getSONG_NAME() + " Artist: " + object.getARTIST();
                    songName.add(navn);
                }
                ObservableList<String> songs = FXCollections.observableArrayList(songName);

                // set the items of the list view
                sangeliste.setItems(songs);
                // searchfield.clear();
            }
        });
    }

    public void handleClickView(MouseEvent mouseEvent) {
        selectedItem = (String) sangeliste.getSelectionModel().getSelectedItem();
    }

    public void handlerPL_Create() {
        String PLname = TF_PlaylistName.getText();
        Playlist ActivePlaylist = new Playlist(PLname, Playlist.createPlaylist(PLname)); // Ugly code, Creates the Playlist in SQL and the instance of the Playlist Class
        ActivePlaylist.playlistSongNameFill();
        System.out.println(Playlist.PlaylistArray());
        updatePlaylistView();
    }

    public void handlerPL_Delete() {
        ActivePlaylist.deletePlaylist();
        updatePlaylistView();
    }

    public void handlerPL_Rename() {
        System.out.println();
        String selectedPL = TF_PlaylistName.getText();
        ActivePlaylist.renamePlaylist(selectedPL);
        updatePlaylistView();
    }

    public void handlerPL_Select(MouseEvent event) {
        try // Java throws an error if you click on a non entry in the table, catch to ignore
        {
            String selectedPL = playlistview.getSelectionModel().getSelectedItem().toString();
            ActivePlaylist.setPlaylistName(selectedPL);
            ActivePlaylist.setPlaylistID(Playlist.getPlaylistID(selectedPL));
            ActivePlaylist.playlistSongNameFill();
            updatePlaylistSongView();
        } catch (Exception e) {
            System.out.println();
        }
    }

    public void handlerPLsong_Select()
    {
        System.out.println();
        String selectedPLsong = playlistsongs.getSelectionModel().getSelectedItem().toString();
        selectedPLsong= selectedPLsong.substring(selectedPLsong.indexOf("g")+3,selectedPLsong.indexOf("Artist")-1);
        //Super hacky workaround string requirements in play method
        DB.selectSQL("SELECT fldFilePath FROM tblSong WHERE fldTitel ='"+selectedPLsong+"'");
        selectedItem = selectedPLsong+DB.getData()+"                        Artist";
        DB.getData();
    }

    public void handlerPL_add()
    {
        String endSearch = selectedItem.substring(selectedItem.indexOf(" ") + 1, selectedItem.indexOf("Artist") - 1);
        ActivePlaylist.addSongPlaylist(endSearch);
        updatePlaylistSongView();

    }
    public void handlerPL_remove()
    {
        try {
            ActivePlaylist.deleteSongPlaylist(selectedItem);
        }
        catch (Exception e){}
    }
    public void loadBilleder()
    {
        Random random = new Random();
        ArrayList<String> mylist = Pictures.addPictures();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(5), event -> {
                    final Image image = new Image(mylist.get( random.nextInt(mylist.size())));
                    System.out.println("RONALDO: SUIIIIIIIIIIII");
                    ImageV.setImage(image);
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}

