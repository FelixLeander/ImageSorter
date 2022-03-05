package com.example.imagesorter;

import android.widget.Button;

import java.io.File;
import java.util.ArrayList;

public class FolderButton {

    public static File SourceFolder;
    public static ArrayList<FolderButton> collection = new ArrayList<>();
    public int id;
    public File directory;
    public String name;
    public Button button;

    public FolderButton(String pName, File pDirectory, Button button) {
        this.id = collection.size();
        this.name = pName;
        this.directory = pDirectory;
        this.button = button;
        collection.add(this);
    }
}
