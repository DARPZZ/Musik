package sample;

import com.sun.media.sound.SoftTuning;
import com.sun.org.apache.xalan.internal.xsltc.dom.SortingIterator;

import javax.swing.text.html.ListView;
import java.awt.*;

public class SearchSong{

    public static void søgIgennemSange(String seach, ListView sangListe)
    {

        for (Song element: Song.getSongList()) {
            if (seach.equals(element.getARTIST())|| seach.equals(element.getSONG_NAME()))
            {


            }else
                System.out.println("ik se her");

        }
    }

}
