package com.example.imagesorter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class Helper {

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

    public static List<Integer> uniqueIntList = new ArrayList<>();

}
