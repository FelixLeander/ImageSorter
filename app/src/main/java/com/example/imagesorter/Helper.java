package com.example.imagesorter;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;


public class Helper {

    public static List<Integer> uniqueIntList = new ArrayList<>(); //List for method getRandomUniqueInt

    public static void BadToast(SorterAct activity, String text) {
        activity.runOnUiThread(() -> {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        });
    }

    public static int getRandomUniqueInt(int maxUpper) {
        boolean exists = false;
        int num = -1;

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



    //Do not change value.
    public static final String originalRepo = "https://github.com/FelixLeander/ImageSorter";
}
