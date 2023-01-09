package sample;


public class Playlist
{
    private String PlaylistName;
    private int PlaylistID;
    public Playlist(String name, int ID)
    {
        this.PlaylistName=name;
        this.PlaylistID=ID;
    }

    /*region Needs to be move somewhere outside class
    public void createPlaylist(String name)
    {
        //SQL query
        //SELECT MAX(fldPlaylistID) from tblPlaylist
        DB.selectSQL("SELECT MAX(fldPlaylistID) from tblPlaylist");
        int PlaylistID = Integer.parseInt(DB.getData())+1;
        DB.getData();
        //SQL query
        //INSERT INTO tblPlaylist (fldPlaylistID,fldPlaylistName) VALUES ()
        DB.updateSQL("INSERT INTO tblPlaylist (fldPlaylistID,fldPlaylistName) VALUES ("+PlaylistID+","+ name+")");

    }
     */
    public int getPlaylistID_Decrepitated(String PlaylistName) //
    {
        // SQL qeury
        // SELECT fldPlaylistID from tblPlaylist WHERE fldPlaylistName = NAME
        DB.selectSQL("SELECT fldPlaylistID from tblPlaylist WHERE fldPlaylistName ="+PlaylistName);
        int ID = Integer.parseInt(DB.getData());
        DB.getData();
        return ID;

    }

    public int getPlaylistID()
    {
        return PlaylistID;
    }
    public String getPlaylistName() {return PlaylistName;}

    public String[] playlistPathOut() //array file path
    {
        // SQL query
        //SELECT fldSongID from tblSongList WHERE fldPlaylistID = 1
        int rows = 0;
        DB.selectSQL("SELECT COUNT (fldSongID) from tblSongList WHERE fldPlaylistID="+PlaylistID);
        rows = Integer.valueOf(DB.getData());
        DB.getData();
        DB.selectSQL("SELECT fldSongID from tblSongList WHERE fldPlaylistID ="+PlaylistID);
        String[] IDLIST = new String[rows];
        for (int i = 0; i < rows; i++)
        {
            IDLIST[i] = DB.getData();
        }
        DB.getData();
        String[] PathList = new String[IDLIST.length];
        int i =0;
        for (String SongID : IDLIST)
        {
            int songInt = Integer.parseInt(SongID);
            DB.selectSQL("SELECT fldFilePath from tblSong WHERE fldSongID ="+songInt);
            PathList[i] = DB.getData();
            DB.getData();
        }
        return IDLIST;
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
}