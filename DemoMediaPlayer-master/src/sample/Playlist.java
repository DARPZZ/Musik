package sample;

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
        //SELECT MAX(fldListId) from tblPlaylist
        DB.selectSQL("SELECT MAX(fldListId) from tblListName");
        int PlaylistID = Integer.parseInt(DB.getData())+1;
        DB.getData();
        //SQL query
        //INSERT INTO tblPlaylist (fldListId,fldListName) VALUES ()
        DB.updateSQL("INSERT INTO tblPlaylist (fldListId,fldListName) VALUES ("+PlaylistID+",'"+ name+"')");
        return PlaylistID;
    }

    public int getPlaylistID()
    {
        return PlaylistID;
    }
    static public int getPlaylistID(String name)
    {
        DB.selectSQL("SELECT fldListId FROM tblPlaylist WHERE fldListName ='"+name+"'");
        int i =Integer.parseInt(DB.getData());
        return i;
    }
    public String getPlaylistName() {return PlaylistName;}

    public void playlistSongNameFill() //array song names
    {
        // SQL query
        //SELECT fldSongId from tblSonglist WHERE fldListId = 1
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
                DB.selectSQL("SELECT fldTitel FROM tblSong WHERE fldSongId ="+songInt);
                String titel = DB.getData();
                DB.getData();

                DB.selectSQL("SELECT fldArtistName FROM tblSong WHERE fldSongId ="+songInt);
                String artist = DB.getData();
                DB.getData();
                String navn = "Song: " + titel + " Artist: " + artist;

            this.ListPlaylist.add(navn);
        }

    }
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

    public void deletePlaylist()
    {
        DB.deleteSQL("DELETE FROM tblSonglist WHERE fldListId ="+this.PlaylistID);
        DB.deleteSQL("DELETE FROM tblPlaylist WHERE fldListId ="+this.PlaylistID);
    }
    public void deleteSongPlaylist(int SongID)
    {
        //SQL query
        // insert INTO tblSonglist (fldListId, fldSongId) VALUES (x,y)
        DB.updateSQL("DELETE FROM tblSonglist WHERE fldListId ="+PlaylistID+" AND fldSongId ="+SongID);
    }
    public void deleteSongPlaylist(String SongName)
    {
        DB.selectSQL("SELECT fldSongId FROM tblSong WHERE fldTitel ='"+SongName+"'");
        int i = Integer.parseInt(DB.getData());
        DB.getData();
        DB.updateSQL("DELETE TOP (1) FROM tblSonglist WHERE tblSonglist.fldListId =" + this.PlaylistID + " AND tblSonglist.fldSongId ="+i);
    }
    public void addSongPlaylist(int SongID)
    {
        //SQL query
        // insert INTO tblSonglist (fldListId, fldSongId) VALUES (x,y)
        DB.updateSQL("insert INTO tblSonglist (fldListId, fldSongId) VALUES ("+PlaylistID+","+SongID+")");
    }
    public void addSongPlaylist(String songName)
    {
        System.out.println();
        DB.selectSQL("SELECT MAX(fldRelationId) from tblSonglist");

        int listID = Integer.parseInt(DB.getData())+1;
        DB.getData();

        DB.selectSQL("SELECT fldSongId FROM tblSong WHERE fldTitel ='"+songName+"'");
        int SongID =Integer.parseInt(DB.getData());
        DB.getData();
        //SQL query
        // insert INTO tblSonglist (fldListId, fldSongId) VALUES (x,y)
        DB.updateSQL("insert INTO tblSonglist (fldListId, fldSongId, fldRelationId) VALUES ("+this.PlaylistID+","+SongID+","+listID+")");
    }
    public void renamePlaylist(String Newname)
    {
        DB.updateSQL("UPDATE tblPlaylist SET fldListName ='"+Newname+"' WHERE fldListName ='"+this.PlaylistName+"'");
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