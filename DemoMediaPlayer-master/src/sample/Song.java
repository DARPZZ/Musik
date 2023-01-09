package sample;

import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Song class
 * @author NMP
 */

public class Song
{
    private final String SONG_NAME;
    private final String ARTIST;
    private final String FILE_PATH;
    private final double DURATION;
    private static final ArrayList<Song> SONG_LIST = new ArrayList<>();

    public Song(String songName, String artist, String filePath, double duration)
    {
        this.SONG_NAME = songName;
        this.ARTIST = artist;
        this.FILE_PATH = filePath;
        this.DURATION = duration;
    }

    public static void CreateList()
    {
        DB.selectSQL("Select * from tblSong");

        while (!DB.getData().equals(DB.NOMOREDATA))
        {
            SONG_LIST.add(new sample.Song(DB.getData(), DB.getData(), DB.getData(), Double.parseDouble(DB.getData())));
        }

        if (!SONG_LIST.isEmpty())
        {
            System.out.println(SONG_LIST);
        }
    }

    public static void AddSongToDB(String songName, String artist, String filePath, double duration)
    {
        DB.insertSQL("Insert into tblSong values('" + songName + "', '" + artist + "', '" + filePath + "', '" + duration + "');");
        System.out.printf("%s er oprettet i databasen", songName);
        SONG_LIST.add(new Song(songName, artist, filePath, duration));
    }

    public static ArrayList<Song> CreateList(String tblName, String fldName, String value)
    {
        ArrayList<Song> songList = new ArrayList<>();
        DB.selectSQL("Select * from " + tblName + " where " + fldName + " = '" + value + "';");


        while (!DB.getData().equals(DB.NOMOREDATA))
        {
            songList.add(new sample.Song(DB.getData(), DB.getData(), DB.getData(), Double.parseDouble(DB.getData())));
        }
        return songList;
    }
    public static void searchSong(String searchString)
    {
        SONG_LIST.clear();

        DB.selectSQL("Select * from tblSong where fldArtistName like '%"+searchString +"%' or  fldTitel like '%"+ searchString + "%'");
        while (!DB.getData().equals(DB.NOMOREDATA))
        {
            SONG_LIST.add(new sample.Song(DB.getData(), DB.getData(), DB.getData(), Double.parseDouble(DB.getData())));
        }
    }


    //region getter

    public String getSONG_NAME()
    {
        return SONG_NAME;
    }

    public String getARTIST()
    {
        return ARTIST;
    }

    public String getFILE_PATH()
    {
        return FILE_PATH;
    }

    public double getDURATION()
    {
        return DURATION;
    }

    public static ArrayList<sample.Song> getSongList()
    {
        return SONG_LIST;
    }
    //endregion

}