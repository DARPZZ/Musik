package sample;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Pictures
{

     static ArrayList<String> pictures = new ArrayList<>();
    public static ArrayList<String> addPictures()
    {
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/bil 1.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/bil 2.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/bil 3.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/bil 4.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/bil 5.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/bil 6.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/bil 7.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/bil 8.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/bil 9.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/bil 10.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/bil 11.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/bil 12.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/bil 13.png");



      return pictures;
    }

    /**
     * Creates an array with files from a folder
     * @param folderPath The path to a locally stored folder
     * @return An array stored with files
     */
    public static File[] ListUserPictures(String folderPath)
    {
        return new File(folderPath).listFiles();
    }
}
