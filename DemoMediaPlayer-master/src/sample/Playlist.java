package sample;


import com.sun.org.apache.regexp.internal.REUtil;

import java.util.ArrayList;

public class Playlist
{
    private String PlaylistName;
    private int PlaylistID;
    private ArrayList ListPlaylist = new ArrayList();
    public Playlist(String name, int ID)
    {
        this.PlaylistName=name;
        this.PlaylistID=ID;
    }
    public String PlaylistName()
    {return PlaylistName;}
    public int PlaylistID()
    {return PlaylistID;}
    public ArrayList getListPlaylist()
    {return ListPlaylist;}


    public static int createPlaylist(String name)
    {
        //SQL query
        //SELECT MAX(fldPlaylistID) from tblPlaylist
        DB.selectSQL("SELECT MAX(fldPlaylistID) from tblPlaylist");
        int PlaylistID = Integer.parseInt(DB.getData())+1;
        DB.getData();
        //SQL query
        //INSERT INTO tblPlaylist (fldPlaylistID,fldPlaylistName) VALUES ()
        DB.updateSQL("INSERT INTO tblPlaylist (fldPlaylistID,fldPlaylistName) VALUES ("+PlaylistID+",'"+ name+"')");
        return PlaylistID;
    }

    public int getPlaylistID()
    {
        return PlaylistID;
    }
    static public int getPlaylistID(String name)
    {
        DB.selectSQL("SELECT fldPlaylistID FROM tblPlaylist WHERE fldPlaylistName ='"+name+"'");
        int i =Integer.parseInt(DB.getData());
        return i;
    }
    public String getPlaylistName() {return PlaylistName;}

    public void playlistSongNameFill() //array song names
    {
        // SQL query
        //SELECT fldSongID from tblSongList WHERE fldPlaylistID = 1
        this.ListPlaylist.clear();
        int rows = 0;
        DB.selectSQL("SELECT COUNT (fldSongID) from tblSongList WHERE fldPlaylistID="+this.PlaylistID);
        rows = Integer.valueOf(DB.getData());
        DB.getData();
        DB.selectSQL("SELECT fldSongID from tblSongList WHERE fldPlaylistID ="+this.PlaylistID);
        String[] IDLIST = new String[rows];
        for (int i = 0; i < rows; i++)
        {
            IDLIST[i] = DB.getData();
        }
        DB.getData();
        for (String SongID : IDLIST)
        {
            int songInt = Integer.parseInt(SongID);
            DB.selectSQL("SELECT fldTitel from tblSong WHERE fldSongID ="+songInt);
            this.ListPlaylist.add(DB.getData());
            DB.getData();
        }

    }
    public static ArrayList<String> PlaylistArray()
    {
        ArrayList<String> listPlaylist = new ArrayList<String>();
        DB.selectSQL("SELECT COUNT (fldPlaylistName) FROM tblPlaylist");
        int nameAmount = Integer.parseInt(DB.getData());
        DB.getData();
        DB.selectSQL("SELECT fldPlaylistName FROM tblPlaylist");
        for (int i = 0; i < nameAmount; i++)
        {
            listPlaylist.add(DB.getData());
        }
        DB.getData();
        return listPlaylist;
    }

    public void deletePlaylist()
    {
        DB.deleteSQL("DELETE FROM tblSongList WHERE fldPlaylistID ="+PlaylistID);
        DB.deleteSQL("DELETE FROM tblPlaylist WHERE fldPlaylistID ="+PlaylistID);
    }
    public void deleteSongPlaylist(int SongID)
    {
        //SQL query
       // insert INTO tblSongList (fldPlaylistID, fldSongID) VALUES (x,y)
       DB.updateSQL("DELETE FROM tblSongList WHERE flbPlaylistID ="+PlaylistID+" AND fldSongID ="+SongID);
    }
    public void deleteSongPlaylist(String SongName)
    {
        //SQL query
        // insert INTO tblSongList (fldPlaylistID, fldSongID) VALUES (x,y)
        DB.updateSQL("DELETE FROM tblSongList WHERE flbPlaylistID =" + PlaylistID + " AND fldTitel =" + SongName);
    }
    public void addSongPlaylist(int SongID)
    {
        //SQL query
        // insert INTO tblSongList (fldPlaylistID, fldSongID) VALUES (x,y)
        DB.updateSQL("insert INTO tblSongList (fldPlaylistID, fldSongID) VALUES ("+PlaylistID+","+SongID+")");
    }
    public void addSongPlaylist(String songName)
    {
        DB.selectSQL("SELECT fldSongID FROM tblSong WHERE fldTitel ="+songName);
        int SongID =Integer.parseInt(DB.getData());
        DB.getData();
        //SQL query
        // insert INTO tblSongList (fldPlaylistID, fldSongID) VALUES (x,y)
        DB.updateSQL("insert INTO tblSongList (fldPlaylistID, fldSongID) VALUES ("+PlaylistID+","+SongID+")");
    }
    public void renamePlaylist(String Newname)
    {
        DB.updateSQL("UPDATE tblPlaylist SET fldPlaylistName ='"+Newname+"' WHERE fldPlaylistName ='"+this.PlaylistName+"'");
    }
    public void setPlaylistID(int i)
    {
        this.PlaylistID = i;
    }
    public void setPlaylistName(String s)
    {
        this.PlaylistName = s;
    }
}