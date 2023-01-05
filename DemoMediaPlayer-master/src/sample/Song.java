package sample;

import java.util.ArrayList;

public class Song
{
    private final String SONG_NAME;
    private final String ARTIST;
    private final String FILE_PATH;
    private final double DURATION;
    private static ArrayList<sample.Song> songList = new ArrayList<sample.Song>();

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

            songList.add(new sample.Song(DB.getData(), DB.getData(), DB.getData(), Double.parseDouble(DB.getData())));
        }

        if (!songList.isEmpty())
        {
            System.out.println(songList);
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
        return songList;
    }

    //endregion

}