package sample;

import java.io.File;
import java.util.ArrayList;

public class Pictures
{
    /**
     * Create an Array that store pictures
     */
     static ArrayList<String> pictures = new ArrayList<>();
    public static ArrayList<String> addPictures()
    {
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/png 1.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/png 2.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/png 3.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/png 4.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/png 5.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/png 6.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/png 7.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/png 8.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/png 9.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/png 10.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/png 11.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/png 12.png");
        pictures.add("DemoMediaPlayer-master/src/sample/Billeder/png 13.png");
      return pictures;
    }

    /**
     * Creates an array with files from a folder
     * @param folderPath The path to a locally stored folder
     * @return An array stored with files
     */
    public static File[] listUserPictures(String folderPath)
    {
        return new File(folderPath).listFiles();
    }
}
