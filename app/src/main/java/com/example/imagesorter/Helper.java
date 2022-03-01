package com.example.imagesorter;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Helper {

    public static List<Integer> uniqueIntList = new ArrayList<>(); //List for method getRandomUniqueInt

    public static void DebugToast(SorterAct activity, String text) {
        if (!S_DEBUG_MODE) {
            return;
        }
        activity.runOnUiThread(() -> Toast.makeText(activity, text, Toast.LENGTH_LONG).show());
    }

    public static List<File> getFilesInPath(Context context, String path) {
        File[] files = new File(path).listFiles();
        if (files == null) {
            Toast.makeText(context, "No files found. Possible bad path?", Toast.LENGTH_LONG).show();
            return new ArrayList<>();
        }
        return Arrays.asList(files);
    }

    public static int getRandomUniqueInt(int maxUpper) {
        boolean exists = false;
        int num;

        do {
            num = new Random().nextInt(maxUpper);
            for (int numInList : uniqueIntList) {
                if (numInList == num) {
                    exists = true;
                    break;
                }
                else {
                    exists = false;
                }
            }
        } while (exists);
        return num;
    }

    //-------Actual Settings
    public static boolean S_DEBUG_MODE = false;



    //Do not change value.
    public static final String originalRepo = "https://github.com/FelixLeander/ImageSorter";
}
