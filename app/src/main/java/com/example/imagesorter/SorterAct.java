package com.example.imagesorter;


import android.content.ContentResolver;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SorterAct extends AppCompatActivity {

    private File CURRENT_FILE;
    private final List<File> MARKED_FOR_DELETION = new ArrayList<>();
    private final List<File> IMAGE_FILES = new ArrayList<>();
    private int CURRENT_IMAGE_INDEX = -1;

    private void setNextImage() {
        if ((CURRENT_IMAGE_INDEX + 1) >= IMAGE_FILES.size()) {
            Toast.makeText(this, "There are no more suitable files in the source dir", Toast.LENGTH_LONG).show();
            return;
        }
        CURRENT_IMAGE_INDEX++;
        CURRENT_FILE = IMAGE_FILES.get(CURRENT_IMAGE_INDEX);
        ((ImageView) findViewById(R.id.sorterImageView)).setImageBitmap(BitmapFactory.decodeFile(CURRENT_FILE.getPath()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorter);


        //Prepare list of files in source folder
        File[] files = FolderButton.SourceFolder.listFiles();
        if (files == null) {
            Toast.makeText(this, "There was an unexpected error.\nPlease inform the developer if you can recreate this.", Toast.LENGTH_LONG).show();
            return;
        } //ToDo: may add sub-folder files
        for (File file : files) {
            String path = file.getAbsolutePath().toLowerCase(Locale.ROOT);
            if (path.contains(".jpg") || path.contains(".png")) {
                IMAGE_FILES.add(file);
            }
        }
        if (IMAGE_FILES.size() == 0) {
            Toast.makeText(this, "There are no .jpg/.png files in the selected source dir", Toast.LENGTH_LONG).show();
        }


        //Add target folders
        for (FolderButton folderButton : FolderButton.collection) {
            Button button = new Button(this);
            button.setText(folderButton.name);
            button.setBackgroundColor(fetchColor(androidx.appcompat.R.attr.colorPrimary));
            button.setOnClickListener(v -> {
                try {
                    if (!moveFile(CURRENT_FILE, folderButton.directory)) {
                        Toast.makeText(this, "The file was copied but original could not be deleted", Toast.LENGTH_SHORT).show();
                    }
                    setNextImage();
                } catch (Exception ex) {
                    Toast.makeText(this, "There was an Exception:\n" + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            ((GridLayout) findViewById(R.id.sorterGridLayout)).addView(button);
        }


        setButtonFunctionality();
        setNextImage();
    }

    private boolean moveFile(File file, File dir) {
        File newFile = new File(dir, file.getName());
        try (FileChannel outputChannel = new FileOutputStream(newFile).getChannel(); FileChannel inputChannel = new FileInputStream(file).getChannel()) {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            return eraseFile(file.getAbsolutePath());
        } catch (Exception ex) {
            Toast.makeText(this, "There was an error moving the file.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean eraseFile(String filePath) {
        if (filePath.startsWith("content://")) {
            this.getContentResolver().delete(Uri.parse(filePath), null, null);
            return true;
        }
        return new File(filePath).delete();
    }

    private void setButtonFunctionality() {
        findViewById(R.id.sorterButtonSkipFile).setOnClickListener(v -> setNextImage());

        findViewById(R.id.sorterButtonMarkDelete).setOnClickListener(v -> {
            MARKED_FOR_DELETION.add(CURRENT_FILE);
            setNextImage();
        });

        findViewById(R.id.sorterButtonConfirmDelete).setOnClickListener(v -> {
            int markedFiles = MARKED_FOR_DELETION.size();
            int filesDeleted = 0;

            Toast.makeText(this, "Starting to delete " + markedFiles + " files.", Toast.LENGTH_SHORT).show();
            for (File file : MARKED_FOR_DELETION) {
                if (file.delete()) {
                    filesDeleted++;
                }
            }
            Toast.makeText(this, "Deleted " + filesDeleted + " out of " + markedFiles + "files.", Toast.LENGTH_SHORT).show();
        });
    }

    private int fetchColor(int rAttrName) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = obtainStyledAttributes(typedValue.data, new int[]{rAttrName});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }
}