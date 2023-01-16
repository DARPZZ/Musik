package sample;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @author TC
 */
public class Playlist
{
    private String PlaylistName;
    private int PlaylistID;
    private ArrayList<Integer> SongID = new ArrayList<>();
    public static ArrayList<Playlist> objectPlaylists = new ArrayList<>();
    private static DecimalFormat df = new DecimalFormat("0.00");
    public Playlist(String name, int ID)
    {
        this.PlaylistName=name;
        this.PlaylistID=ID;
    }
    public ArrayList<Integer> getSongID()
    {return this.SongID;}
    public String getSongName(int i)
    {
        String returnString="";
        for (Song s: Song.getSongList())
        {
            if (s.getSONG_ID() == (this.SongID.get(i)))
            {
                 returnString = s.getSONG_NAME();
                 break;
            }
        }
        return returnString;
    }
    public String getPlaylistName()
    {return this.PlaylistName;}
    public void setPlaylistName(String s)
    {this.PlaylistName = s;}
    /**
     *
     * @param name is the name of the new playlist in the database
     * @return the ID value of the newly created
     */
    public static void createPlaylist(String name)
    {
        //SQL query
        //INSERT INTO tblPlaylist (fldListId,fldListName) VALUES ()
        DB.updateSQL("INSERT INTO tblPlaylist (fldListName) VALUES ('"+ name+"')");
        DB.selectSQL("SELECT fldListId FROM tblPlaylist WHERE fldListName ='"+name+"'");
        Playlist newPlaylist = new Playlist(name,Integer.parseInt(DB.getData()));
        DB.getData();
        objectPlaylists.add(newPlaylist);
    }

    /**
     *
     * @return Array of all the playlists from the DB
     */
    public static ArrayList<String> PlaylistArray()
    {
        ArrayList<String> playlistVisual = new ArrayList<>();
        for (Playlist p: objectPlaylists)
        {
            playlistVisual.add(p.getPlaylistName());
        }
        return  playlistVisual;
    }
    /**
     * Deletes from playlists from DB
     */
    public void deletePlaylist()
    {
        DB.deleteSQL("DELETE FROM tblSonglist WHERE fldListId ="+this.PlaylistID);
        DB.deleteSQL("DELETE FROM tblPlaylist WHERE fldListId ="+this.PlaylistID);
        for (int i = 0; i < objectPlaylists.size(); i++)
        {
            if (objectPlaylists.get(i).getPlaylistName().equals(this.PlaylistName))
            {
                objectPlaylists.remove(i);
                break;
            }
        }
    }

    /**
     * deletes songs from playlists
     * @param songName <-- song to be deleted
     */
    public void removeSongPlaylist(String songName)
    {
        int songId =0;
        for (Song s:Song.getSongList())
        {
            if (s.getSONG_NAME().equals(songName))
            {
                songId = s.getSONG_ID();
                break;
            }
        }
        for (int i = 0; i <this.SongID.size() ; i++)
        {
            if (this.SongID.get(i) == songId)
            {
                this.SongID.remove(i);
                break;
            }
        }
        DB.updateSQL("DELETE TOP (1) FROM tblSonglist WHERE tblSonglist.fldListId =" + this.PlaylistID + " AND tblSonglist.fldSongId ="+songId);

    }

    /**
     * adds a song to the playlist
     * @param songName <-- song to be deleted
     */
    public void addSongPlaylist(String songName)
    {
        int songId =0;
        for (Song s:Song.getSongList())
        {
            if (s.getSONG_NAME().equals(songName))
            {
                songId = s.getSONG_ID();
                break;
            }
        }
        //SQL query
        // insert INTO tblSonglist (fldListId, fldSongId) VALUES (x,y)
        DB.updateSQL("insert INTO tblSonglist (fldListId, fldSongId) VALUES ("+this.PlaylistID+","+songId+")");
        SongID.add(songId);
    }
    /**
     * Renames an existing playlist
     * @param Newname
     */
    public void renamePlaylist(String Newname)
    {
        this.setPlaylistName(Newname);
        DB.updateSQL("UPDATE tblPlaylist SET fldListName ='"+Newname+"' WHERE fldListName ='"+this.PlaylistName+"'");
    }

    /**
     *
     * @param inputNR <-- the duration in seconds
     * @return duration formatted m,ss
     */
    public static String durationFormat(double inputNR) //formatere int til en double i formattet tt:mm:ss
    {
         // divide by 60 to get mm.ss and cast away the seconds
        if (inputNR%60 >60)
        {
            int minuttes = (int)inputNR/60;
            int minuteRemain =(int) inputNR%60/60;
            double totalSec = ((inputNR%60/60-minuteRemain)/1.667)/100;
            return df.format(minuttes+minuteRemain+totalSec);
        }
        else
        {
            int totalMinute = (int) inputNR/60;
            double totalSec = (inputNR%60/1.667)/100;
            return df.format(totalMinute+totalSec);
        }
    }

    //Break
    public static void initialize() // Breaks if there are no playlist, could be wrapped in an if statement to avoid
    {
        //SQL Selects the count of playlist as first result, and the distinct id as the rest
        //SELECT COUNT (DISTINCT fldListId) FROM tblPlaylist UNION ALL SELECT DISTINCT fldListId FROM tblPlaylist
        DB.selectSQL("SELECT COUNT (DISTINCT fldListId) FROM tblPlaylist UNION ALL SELECT DISTINCT fldListId FROM tblPlaylist");
        int amountPL = Integer.parseInt(DB.getData());
        int[] uniquePL_ID = new int[amountPL];
        for (int i = 0; i < amountPL; i++)
        {
         uniquePL_ID[i] =Integer.parseInt(DB.getData());
        }
        DB.getData();

        // sql joins tblPLaylist and tblSonglist, without song id
        //SELECT tblPlaylist.fldListId, tblPlaylist.fldListname, tblSonglist.fldSongId  FROM tblPlaylist LEFT JOIN tblSonglist ON tblPlaylist.fldListId = tblSonglist.fldListId
        DB.selectSQL("SELECT tblPlaylist.fldListId, tblPlaylist.fldListname, tblSonglist.fldSongId  FROM tblPlaylist LEFT JOIN tblSonglist ON tblPlaylist.fldListId = tblSonglist.fldListId");
        int id = Integer.parseInt(DB.getData());
        for (int playlistID: uniquePL_ID)
        {
            String name = DB.getData();
            Playlist Playlistnew = new Playlist(name,id);
            Playlistnew.SongID.add(Integer.parseInt(DB.getData()));
            while (id == playlistID)
            {
                id = Integer.parseInt(DB.getDataHack());
                if (id == playlistID)
                {
                    DB.getData();
                    Playlistnew.SongID.add(Integer.parseInt(DB.getData()));
                }
            }
            objectPlaylists.add(Playlistnew);
        }
    }
}