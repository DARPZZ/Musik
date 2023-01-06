package sample;

import com.sun.org.apache.xalan.internal.xsltc.dom.SortingIterator;

import java.awt.*;

public class SearchSong{

    public static void søgIgennemSange(String seach)
    {

        for (Song element: Song.getSongList()) {
            if (seach.equals(element.getARTIST())|| seach.equals(element.getSONG_NAME()))
            {
                System.out.println("ååååå");
            }else
                System.out.println("iiiiiiiiiii");

        }
    }

}
