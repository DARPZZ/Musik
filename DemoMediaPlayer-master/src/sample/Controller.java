package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import javafx.scene.media.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Duration;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable
{

    @FXML
    private MediaView mediaV;
    @FXML
    ImageView ImageV;
    @FXML
    Button knapPlay, knapPause, knapStop, knapCreate, knapAdd, knapDelete, knapRemove, knapRename, knapChoose;
    @FXML
    ListView sangeliste, playlistview, playlistsongs;
    @FXML
    TextField searchfield, textfieldInfo, TF_PlaylistName,textfieldPlDuration;
    @FXML
    Slider sliderVolume;
    @FXML
    ToggleButton knapStart_Pause;


    final Timeline timeline = new Timeline();

    private MediaPlayer mp;
    private Media me;
    private String filepath = new File("DemoMediaPlayer-master/src/sample/media/SampleAudio_0.4mb.mp3").getAbsolutePath();
    private Playlist ActivePlaylist = new Playlist(null, 0);
    private String selectedItem;
    private int identifier; // 1 = songlist, 2 = playlist songlist
    private boolean isPlaying = false;
    private String displayInfo;
    private String userDirectoryPath;
    private double duration;
    private Image userImage;
    private int userImageCount;
    private File[] pictureList;


    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     * @param location
     * @param resources
     */

    public void initialize(URL location, ResourceBundle resources)
    {
        sliderVolume.setShowTickMarks(true);
        sliderVolume.setShowTickLabels(true);
        sliderVolume.setMajorTickUnit(25);
        textfieldInfo.setStyle("-fx-background-color: Black; -fx-text-inner-color: white");
        knapPause.setText("\u23f8");
        knapStop.setText("\u23f9");
        knapPlay.setText("\u23f5");
        // create the list of songs
        Song.createList();
        publishSong();

        // set the selection mode to single, so only one song can be selected at a time
        sangeliste.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Playlist setup
        playlistview.setItems(FXCollections.observableArrayList(Playlist.PlaylistArray()));
        playlistview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        playlistsongs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        System.out.println(Playlist.PlaylistArray());

        sliderVolume.valueProperty().addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable)
            {
                mp.setVolume(sliderVolume.getValue()/ 100);
            }
        });

    }

    /**
     * Method updates the playlist view
     */
    public void updatePlaylistView()
    {
        playlistview.setItems(FXCollections.observableArrayList(Playlist.PlaylistArray()));
    }

    public void updatePlaylistSongView(int i)
    {
        playlistsongs.setItems(FXCollections.observableArrayList(ActivePlaylist.getListPlaylist()));
        textfieldPlDuration.setText(Playlist.durationFormat(i));
    }
    public String stringFormat(String inputString)
    {
        StringBuilder sB = new StringBuilder(25);
        sB.insert(0,inputString);
        for (int i = 0; i < 25-inputString.length(); i++)
        {
        sB.append(" ");
        }
        return sB.toString();
    }

    @FXML
    /**
     * Handler for the play/pause/stop button
     */
    public void handlerplay()
    {
        String endSearch = selectedItem.substring(selectedItem.indexOf(" ") + 1, selectedItem.indexOf("Artist") - 1);
        System.out.println(endSearch);

        if (userDirectoryPath == null)
        {
            loadBilleder();
        }
        else
        {
            runUserImage();
        }

        findFilePath(endSearch);

        System.out.println("Now playing: " + filepath);
        textfieldInfo.setText(displayInfo);

        me = new Media(new File(filepath).toURI().toString());
        // Create new MediaPlayer and attach the media to be played
        mp = new MediaPlayer(me);

        mediaV.setMediaPlayer(mp);

        mp.play();

        knapPlay.setVisible(false);
        knapStart_Pause.setVisible(true);
        isPlaying = true;
        /*
        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run()
            {
                {
                    mp.setStartTime(Duration.ZERO);
                    String endSearch = selectedItem.substring(selectedItem.indexOf(" ") + 1, selectedItem.indexOf("Artist") - 1);
                    int indexCheck = 0;
                    switch (identifier) {
                        case 1: {
                            ArrayList<sample.Song> meme = new ArrayList<>(Song.getSongList());
                            //Song.getSongList() //array af song objekts
                            for (int i = 0; i < meme.size(); i++) {
                                if (endSearch.equals(meme.get(i).getSONG_NAME())) ;
                                indexCheck = i + 1;
                            }
                            meme.get(indexCheck).getSONG_NAME();
                            break;
                        }
                        case 2://ActivePlaylist.getListPlaylist() string array
                        {
                            ArrayList<String> meme = new ArrayList<>(ActivePlaylist.getListPlaylist());
                            for (int i = 0; i < meme.size(); i++) {
                                String Formatted = meme.get(i).substring(meme.get(i).indexOf(" ") + 1, meme.get(i).indexOf("Artist") - 1);
                                if (endSearch.equals(Formatted)) {
                                    indexCheck = i + 1;
                                }
                            }
                            meme.get(indexCheck);
                            break;
                        }
                    }

                }

            }
        });  */
    }



    public void handlerS_P()
    {
        if (knapStart_Pause.isSelected())
        {
            mp.play();
            timeline.play();

        }
        mp.pause();
        timeline.stop();
        timeline.getKeyFrames().clear();
    }
    public void handlerPause()
    {
        mp.pause();
        timeline.stop();
        timeline.getKeyFrames().clear();
    }

    public void handlerStop()
    {
        mp.stop();
        timeline.stop();
        timeline.getKeyFrames().clear();
        knapPlay.setVisible(true);
        knapStart_Pause.setVisible(false);
    }

    public void handlerSearch()
    {
        searchfield.setOnKeyPressed(handlerSearch ->
        {
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
        selectedItem = (String) sangeliste.getSelectionModel().getSelectedItem();
        identifier =1;
        knapPlay.setVisible(true);
        knapStart_Pause.setVisible(false);
    }

    public void handlerPL_Create()
    {
        String PLname = TF_PlaylistName.getText();
        Playlist ActivePlaylist = new Playlist(PLname, Playlist.createPlaylist(PLname)); // Ugly code, Creates the Playlist in SQL and the instance of the Playlist Class
        ActivePlaylist.playlistSongNameFill();
        System.out.println(Playlist.PlaylistArray());
        updatePlaylistView();


    }

    public void handlerPL_Delete()
    {
        ActivePlaylist.deletePlaylist();
        updatePlaylistView();
    }

    public void handlerPL_Rename()
    {
        System.out.println();
        String selectedPL = TF_PlaylistName.getText();
        ActivePlaylist.renamePlaylist(selectedPL);
        updatePlaylistView();

    }

    public void handlerPL_Select(MouseEvent event)
    {
        try // Java throws an error if you click on a non entry in the table, catch to ignore
        {
            String selectedPL = playlistview.getSelectionModel().getSelectedItem().toString();
            ActivePlaylist.setPlaylistName(selectedPL);
            ActivePlaylist.setPlaylistID(Playlist.getPlaylistID(selectedPL));
            int totalDur = ActivePlaylist.playlistSongNameFill();
            updatePlaylistSongView(totalDur);
        } catch (Exception e) {
            System.out.println();
        }

    }

    public void handlerPLsong_Select()
    {
        System.out.println();
        String selectedPLsong = playlistsongs.getSelectionModel().getSelectedItem().toString();
        selectedPLsong = selectedPLsong.substring(selectedPLsong.indexOf("g") + 3, selectedPLsong.indexOf("Artist") - 1);
        //Super hacky workaround string requirements in play method
        selectedItem = " " + selectedPLsong + " Artist";
        identifier =2;
        knapPlay.setVisible(true);
        knapStart_Pause.setVisible(false);
    }

    public void handlerPL_add()
    {
        String endSearch = selectedItem.substring(selectedItem.indexOf(" ") + 1, selectedItem.indexOf("Artist") - 1);
        ActivePlaylist.addSongPlaylist(endSearch);
        int totaldur =ActivePlaylist.playlistSongNameFill();
        updatePlaylistSongView(totaldur);

    }

    public void handlerPL_remove()
    {
        try {
            ActivePlaylist.deleteSongPlaylist(selectedItem);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * Allows user to set a folder with the users own images
     */
    public void handleChoose()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            userDirectoryPath = String.valueOf(chooser.getSelectedFile());
            pictureList = Pictures.listUserPictures(userDirectoryPath);
            System.out.println("User picture folder path: " + userDirectoryPath);
        }
    }

    public void loadBilleder()
    {
        Random random = new Random();
        ArrayList<String> mylist = Pictures.addPictures();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(5), event ->
                {
                    final Image image = new Image(mylist.get(random.nextInt(mylist.size())));
                    System.out.println("RONALDO: SUIIIIIIIIIIII");
                    ImageV.setImage(image);
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void publishSong()
    {
        ArrayList<String> songName = new ArrayList<>();
        for (Song object : Song.getSongList()) {
            String duration = Playlist.durationFormat( object.getDURATION());
            String navn = "Song: " + object.getSONG_NAME() + " Artist: " + object.getARTIST() + "Duration: " + duration;
            songName.add(navn);
        }
        ObservableList<String> songs = FXCollections.observableArrayList(songName);

        // set the items of the list view
        sangeliste.setItems(songs);
    }

    public void findFilePath(String endSearch)
    {
        for (Song songs : Song.getSongList())
        {
            if (songs.getSONG_NAME().equals(endSearch))
            {
                filepath = songs.getFILE_PATH();
                displayInfo = songs.getARTIST() + " - " + songs.getSONG_NAME() + " - " + Playlist.durationFormat(songs.getDURATION()) + " min.";
                duration = songs.getDURATION();
            }
        }
    }

    public void runUserImage()
    {
        if (userImageCount == pictureList.length)
        {
            userImageCount = 0;
        }

        if (pictureList[userImageCount].isFile())
        {
            userImage = new Image((pictureList[userImageCount++]).toURI().toString());
            ImageV.setImage(userImage);
            System.out.println("Displayed user image: " + userImage);
        }
    }
}

