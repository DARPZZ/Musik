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
    private static DecimalFormat df = new DecimalFormat("0.00");
    private ArrayList ListPlaylist = new ArrayList();
    public Playlist(String name, int ID)
    {
        this.PlaylistName=name;
        this.PlaylistID=ID;
    }
    static public int getPlaylistID(String name)
    {
        DB.selectSQL("SELECT fldListId FROM tblPlaylist WHERE fldListName ='"+name+"'");
        int i =Integer.parseInt(DB.getData());
        return i;
    }
    public ArrayList getListPlaylist()
    {return ListPlaylist;}
    public void setPlaylistID(int i)
    {
        this.PlaylistID = i;
    }
    public void setPlaylistName(String s)
    {
        this.PlaylistName = s;
    }

    /**
     *
     * @param name is the name of the new playlist in the database
     * @return the ID value of the newly created
     */
    public static int createPlaylist(String name)
    {
        //SQL query
        //SELECT MAX(fldListId) from tblPlaylist
        DB.selectSQL("SELECT MAX(fldListId) from tblPlaylist");
        int PlaylistID;
        try {
            PlaylistID = Integer.parseInt(DB.getData())+1;
        }
        catch (Exception e){PlaylistID = 1;}

        DB.getData();
        //SQL query
        //INSERT INTO tblPlaylist (fldListId,fldListName) VALUES ()
        DB.updateSQL("INSERT INTO tblPlaylist (fldListName) VALUES ('"+ name+"')");
        return PlaylistID;
    }

    /**
     * sets the array "ListPlaylisy" to include all the songs in the playlist
     * @return the int, sum of all durations
     */
    public int playlistSongNameFill() //array song names
    {
        // SQL query
        //SELECT fldSongId from tblSonglist WHERE fldListId = 1
        int totalduration = 0;
        this.ListPlaylist.clear();
        int rows = 0;
        DB.selectSQL("SELECT COUNT (fldSongId) from tblSonglist WHERE fldListId="+this.PlaylistID);
        rows = Integer.valueOf(DB.getData());
        DB.getData();
        DB.selectSQL("SELECT fldSongId from tblSonglist WHERE fldListId ="+this.PlaylistID);
        String[] IDLIST = new String[rows];
        for (int i = 0; i < rows; i++)
        {
            IDLIST[i] = DB.getData();
        }
        DB.getData();
        for (String SongID : IDLIST)
        {
            int songInt = Integer.parseInt(SongID);
                DB.selectSQL("SELECT fldTitel, fldArtistName, fldDuration FROM tblSong WHERE fldSongId ="+songInt);
                String titel = DB.getData();
                String artist = DB.getData();
                int durantionInt =Integer.parseInt(DB.getData());
                String duration = durationFormat(durantionInt);
                totalduration +=durantionInt;
                String navn = "Song: " + titel + " Artist: " + artist+ "Duration: "+duration;

            this.ListPlaylist.add(navn);

        }
        return totalduration;
    }

    /**
     *
     * @return Array of all the playlists from the DB
     */
    public static ArrayList<String> PlaylistArray()
    {
        ArrayList<String> listPlaylist = new ArrayList<String>();
        DB.selectSQL("SELECT COUNT (fldListName) FROM tblPlaylist");
        int nameAmount = Integer.parseInt(DB.getData());
        DB.getData();
        DB.selectSQL("SELECT fldListName FROM tblPlaylist");
        for (int i = 0; i < nameAmount; i++)
        {
            listPlaylist.add(DB.getData());
        }
        DB.getData();
        return listPlaylist;
    }

    /**
     * Deletes from playlists from DB
     */
    public void deletePlaylist()
    {
        DB.deleteSQL("DELETE FROM tblSonglist WHERE fldListId ="+this.PlaylistID);
        DB.deleteSQL("DELETE FROM tblPlaylist WHERE fldListId ="+this.PlaylistID);
    }

    /**
     * deletes songs from playlists
     * @param SongName <-- song to be deleted
     */
    public void deleteSongPlaylist(String SongName)
    {
        DB.selectSQL("SELECT fldSongId FROM tblSong WHERE fldTitel ='"+SongName+"'");
        int i = Integer.parseInt(DB.getData());
        DB.getData();
        DB.updateSQL("DELETE TOP (1) FROM tblSonglist WHERE tblSonglist.fldListId =" + this.PlaylistID + " AND tblSonglist.fldSongId ="+i);
    }

    /**
     * adds a song to the playlist
     * @param songName <-- song to be deleted
     */
    public void addSongPlaylist(String songName)
    {
        DB.selectSQL("SELECT fldSongId FROM tblSong WHERE fldTitel ='"+songName+"'");
        int SongID =Integer.parseInt(DB.getData());
        DB.getData();
        //SQL query
        // insert INTO tblSonglist (fldListId, fldSongId) VALUES (x,y)
        DB.updateSQL("insert INTO tblSonglist (fldListId, fldSongId) VALUES ("+this.PlaylistID+","+SongID+")");
    }

    /**
     * Renames an existing playlist
     * @param Newname
     */
    public void renamePlaylist(String Newname)
    {
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
}