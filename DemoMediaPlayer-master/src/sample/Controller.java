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
import javafx.scene.media.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Duration;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.*;
import java.util.*;

public class Controller implements Initializable
{
    @FXML
    MediaView mediaV;
    @FXML
    ImageView ImageV;
    @FXML
    Button knapPlay, knapStop, knapCreate, knapAdd, knapDelete, knapRemove, knapRename, knapChoose;
    @FXML
    ListView sangeliste, playlistview, playlistsongs;
    @FXML
    TextField searchfield, textfieldInfo, TF_PlaylistName, textfieldPlDuration;
    @FXML
    Slider sliderVolume, sliderPro;
    @FXML
    ToggleButton knapStart_Pause;

    Progress progress = new Progress();
    private final Timeline TIMELINE = new Timeline();
    private MediaPlayer mp;
    private Media me;
    private String filepath = new File("DemoMediaPlayer-master/src/sample/media/SampleAudio_0.4mb.mp3").getAbsolutePath();
    private String selectedItem, selectedPlaylist, selectedSongName;
    private String displayInfo, userDirectoryPath;
    private int identifier, userImageCount;
    private boolean isPlaying = false;
    private File[] pictureList;

    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     */

    public void initialize(URL location, ResourceBundle resources)
    {
        sliderVolume.setShowTickMarks(true);
        sliderVolume.setShowTickLabels(true);
        sliderVolume.setMajorTickUnit(25);
        textfieldInfo.setStyle("-fx-background-color: Black; -fx-text-inner-color: white");
        knapStart_Pause.setText("\u23f5/\u23f8");
        knapStop.setText("\u23f9");
        knapPlay.setText("\u23f5");
        knapStart_Pause.setVisible(false);
        // create the list of songs
        Song.createList();
        publishSong();

        // set the selection mode to single, so only one song can be selected at a time
        sangeliste.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Playlist setup
        Playlist.initialize();
        playlistview.setItems(FXCollections.observableArrayList(Playlist.PlaylistArray()));
        playlistview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        playlistsongs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
/**
 * Allows the user to selecet another volume insted of 100%
 */
        sliderVolume.valueProperty().addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable)
            {
                mp.setVolume(sliderVolume.getValue() / 100);
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

    public void updatePlaylistSongView(ArrayList<Integer> i) //new
    {
        int duration =0;
        ArrayList<String> songOut = new ArrayList<>();
        for (int id :i)
        {
            for (Song song:Song.getSongList())
            {
                if (id == song.getSONG_ID())
                {
                    songOut.add("Song: "+ song.getSONG_NAME() + " Artist: " + song.getARTIST() + " Duration: " + Playlist.durationFormat(song.getDURATION()));
                    duration +=(int)song.getDURATION();
                    break;
                }
            }
        }
        playlistsongs.setItems(FXCollections.observableArrayList(songOut));
        textfieldPlDuration.setText(Playlist.durationFormat(duration));
    }


    private void fieldSubString()
    {
        selectedSongName = selectedItem.substring(selectedItem.indexOf(" ") + 1, selectedItem.indexOf("Artist") - 1);
    }

    @FXML
    public void handlerplay()
    {
        fieldSubString();
        if (userDirectoryPath == null)
        {
            loadBilleder();
        }
        else
        {
            TIMELINE.stop();
            runUserImage();
        }
        findFilePath(selectedSongName);
        textfieldInfo.setText(displayInfo);

        me = new Media(new File(filepath).toURI().toString());
        // Create new MediaPlayer and attach the media to be played
        mp = new MediaPlayer(me);
        mediaV.setMediaPlayer(mp);

        mp.play();

        knapPlay.setVisible(false);
        knapStart_Pause.setVisible(true);
        isPlaying = true;
        progress.beginTimer(mp,sliderPro);
        mp.setOnEndOfMedia(this::playNext);
    }
    private void playNext()
    {
        String newS = null;
        mp.setStartTime(Duration.ZERO);
        fieldSubString();
        int indexCheck = 0;
        switch (identifier)
        {
            case 1: {
                ArrayList<Song> songlist = new ArrayList<>(Song.getSongList());
                //Song.getSongList() //array af song objekts
                for (int i = 0; i < songlist.size(); i++)
                {
                    if (selectedSongName.equals(songlist.get(i).getSONG_NAME()))
                    {
                        indexCheck = i + 1;
                        if (indexCheck >= songlist.size())
                        {
                            indexCheck = 0;
                        }
                        break;
                    }
                }
                newS = "Song: " + songlist.get(indexCheck).getSONG_NAME() + " Artist";

                selectedItem = newS;
                handlerplay();
                break;
            }
            case 2://ActivePlaylist.getListPlaylist() string array
            {
                for (Playlist p: Playlist.objectPlaylists)
                {

                    if (selectedPlaylist.equals(p.getPlaylistName()))
                    {
                        for (int i = 0; i <p.getSongID().size() ; i++)
                        {
                            if (selectedSongName.equals(p.getSongName(i)))
                            {
                                indexCheck =i+1;
                                if (indexCheck >= p.getSongID().size())
                                {
                                    indexCheck=0;
                                }
                            }
                        }
                        newS = "Song: " + p.getSongName(indexCheck) + " Artist";
                    }
                }
                selectedItem = newS;
                handlerplay();
                break;
            }
        }
    }

    public void handlerS_P()
    {
        if (knapStart_Pause.isSelected()) {
            mp.play();
            TIMELINE.play();

        }
        mp.pause();
        TIMELINE.stop();
        TIMELINE.getKeyFrames().clear();
    }


    public void handlerStop()
    {
        mp.stop();
        TIMELINE.stop();
        TIMELINE.getKeyFrames().clear();
        knapPlay.setVisible(true);
        knapStart_Pause.setVisible(false);
        isPlaying = false;


    }

    /**
     * The user can search for what they would like
     */
    public void handlerSearch()
    {
        searchfield.setOnKeyPressed(handlerSearch ->
        {
            // Handle the key press event here
            KeyCode code = handlerSearch.getCode();
            if (code == KeyCode.ENTER)
            {
                String search = searchfield.getText();
                Song.searchSong(search);
                publishSong();
            }
        });
    }

    public void handleClickView()
    {
        selectedItem = (String) sangeliste.getSelectionModel().getSelectedItem();
        identifier = 1;
        knapPlay.setVisible(true);
        knapStart_Pause.setVisible(false);
        if (isPlaying)
        {
            mp.stop();
            isPlaying= true;
        }
    }

    public void handlerPL_Create()
    {
        Playlist.createPlaylist(TF_PlaylistName.getText());
        updatePlaylistView();
    }

    public void handlerPL_Delete()
    {
        for (Playlist p: Playlist.objectPlaylists)
        {
            if (p.getPlaylistName().equals(selectedPlaylist))
            {
                p.deletePlaylist();
                break;
            }
        }
        updatePlaylistView();
        playlistsongs.getItems().clear();
    }

    public void handlerPL_Rename()
    {
        String newPlaylistName = TF_PlaylistName.getText();
        for (Playlist p : Playlist.objectPlaylists)
        {
            if (p.getPlaylistName().equals(selectedPlaylist))
             {
                 p.renamePlaylist(newPlaylistName);
                 break;
             }
        }
        updatePlaylistView();

    }
    public void handlerPL_Select() //new
    {
        String selectedPL = playlistview.getSelectionModel().getSelectedItem().toString();
        for (int i = 0; i <Playlist.objectPlaylists.size() ; i++)
        {
            if (Playlist.objectPlaylists.get(i).getPlaylistName().equals(selectedPL))
            {
                updatePlaylistSongView(Playlist.objectPlaylists.get(i).getSongID());
            }
        }
        selectedPlaylist = selectedPL;
    }

    public void handlerPLsong_Select()
    {
        String selectedPLsong = playlistsongs.getSelectionModel().getSelectedItem().toString();
        selectedPLsong = selectedPLsong.substring(selectedPLsong.indexOf("g") + 3, selectedPLsong.indexOf("Artist") - 1);
        //Super hacky workaround string requirements in play method
        selectedItem = " " + selectedPLsong + " Artist";
        identifier = 2;
        knapPlay.setVisible(true);
        knapStart_Pause.setVisible(false);
        if (isPlaying)
        {mp.stop();}
    }

    public void handlerPL_add()
    {
        for (Playlist p: Playlist.objectPlaylists)
        {
          if (selectedPlaylist.equals(p.getPlaylistName()))
          {
              fieldSubString();
              p.addSongPlaylist(selectedSongName);
              updatePlaylistSongView(p.getSongID());
              break;
          }
        }
    }

    public void handlerPL_remove()
    {
        for (Playlist p: Playlist.objectPlaylists)
        {
            if (selectedPlaylist.equals(p.getPlaylistName()))
            {
                fieldSubString();
                p.removeSongPlaylist(selectedSongName);
                updatePlaylistSongView(p.getSongID());
                break;
            }
        }
    }

    /**
     * Allows user to set a folder with the users own images
     */
    public void handleChoose()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("image files", "png", "jpg", "jpeg", "bmp");
        chooser.setFileFilter(filter);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            userDirectoryPath = String.valueOf(chooser.getSelectedFile());
            pictureList = Pictures.listUserPictures(userDirectoryPath);
        }
    }

    /**
     * Loading pictures random of an Array list, and display random every 5 sec
     */

    public void loadBilleder()
    {
        Random random = new Random();
        ArrayList<String> mylist = Pictures.addPictures();
        TIMELINE.getKeyFrames().add(
                new KeyFrame(Duration.seconds(5), event ->
                {
                    final Image image = new Image(mylist.get(random.nextInt(mylist.size())));
                    ImageV.setImage(image);
                }));
        TIMELINE.setCycleCount(Animation.INDEFINITE);
        TIMELINE.play();
    }

    public void publishSong()
    {
        ArrayList<String> songName = new ArrayList<>();
        for (Song object : Song.getSongList()) {
            String duration = Playlist.durationFormat( object.getDURATION());
            String navn = "Song: " + object.getSONG_NAME() + " Artist: " + object.getARTIST() + " Duration: " + duration;
            songName.add(navn);
        }
        ObservableList<String> songs = FXCollections.observableArrayList(songName);
        // set the items of the list view
        sangeliste.setItems(songs);
    }

    /**
     * Finds the filepath for the selected song
     */
    public void findFilePath(String endSearch)
    {
        for (Song songs : Song.getSongList())
        {
            if (songs.getSONG_NAME().equals(endSearch))
            {
                filepath = songs.getFILE_PATH();
                displayInfo = songs.getARTIST() + " - " + songs.getSONG_NAME() + " - " + Playlist.durationFormat(songs.getDURATION()) + " min.";
            }
        }
    }

    /**
     * Displays user images and changes picture everytime it's run
     */
    public void runUserImage()
    {
        if (userImageCount == pictureList.length)
        {
            // Resets the counter at the end of the array
            userImageCount = 0;
        }
        Image userImage;
        // Sets image in the image viewer if the file is an image else skips to the next
        if (pictureList[userImageCount].toString().endsWith(".png") || pictureList[userImageCount].toString().endsWith(".jpg") || pictureList[userImageCount].toString().endsWith(".bmp"))
        {
            userImage = new Image((pictureList[userImageCount++]).toURI().toString());
            ImageV.setImage(userImage);
        }
        else if (pictureList.length > userImageCount + 1)
        {
            userImage = new Image((pictureList[++userImageCount]).toURI().toString());
            userImageCount++;
            ImageV.setImage(userImage);
        }
    }
}

