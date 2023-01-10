package sample;

import java.util.ArrayList;

public class Pictures
{
    Pictures()
    {
        System.out.println("Hej");
    }
     static ArrayList<String> pictures = new ArrayList<>();
    public static ArrayList<String> addPictures()
    {
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/billede 2.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/Billed 1.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/billeder 3.png");


      return pictures;
    }
}
