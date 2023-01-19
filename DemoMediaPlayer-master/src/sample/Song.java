package sample;

import java.util.ArrayList;

/**
 * Song class
 * @author NMP
 */

public class Song
{
    private final int SONG_ID;
    private final String SONG_NAME;
    private final String ARTIST;
    private final String FILE_PATH;
    private final double DURATION;
    private static final ArrayList<Song> SONG_LIST = new ArrayList<>();

    public Song(int songID ,String songName, String artist, String filePath, double duration)
    {
        this.SONG_ID = songID;
        this.SONG_NAME = songName;
        this.ARTIST = artist;
        this.FILE_PATH = filePath;
        this.DURATION = duration;
    }
    public Song(String songName, String artist, String filePath, double duration)
    {
        this.SONG_ID =1;
        this.SONG_NAME = songName;
        this.ARTIST = artist;
        this.FILE_PATH = filePath;
        this.DURATION = duration;
    }

    /**
     * Fills the class variable array with data from the database
     */
    public static void createList()
    {
            DB.selectSQL("Select * from tblSong");
            int id = 0;
            while (id >-1)
            {
                id = Integer.parseInt(DB.getDataHack());
                if (id >-1)
                {
                    SONG_LIST.add(new sample.Song(id, DB.getData(), DB.getData(), DB.getData(), Double.parseDouble(DB.getData())));
                }
            }
    }

    /**
     * reformats the visual list of songs.
     * @param searchString <- The inputted keyword
     */
    public static void searchSong(String searchString)
    {
        SONG_LIST.clear();
        DB.selectSQL("Select * from tblSong where fldArtistName like '%"+searchString +"%' or  fldTitel like '%"+ searchString + "%'");
        while (!DB.getData().equals(DB.NOMOREDATA))
        {
            SONG_LIST.add(new Song(DB.getData(), DB.getData(), DB.getData(), Double.parseDouble(DB.getData())));
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
    public int getSONG_ID()
    {
        return this.SONG_ID;
    }

    public static ArrayList<sample.Song> getSongList()
    {
        return SONG_LIST;
    }
    //endregion

}