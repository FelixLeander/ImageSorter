package com.example.imagesorter;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SorterAct extends AppCompatActivity {

    private boolean CUT_OR_COPY = false;
    private String SOURCE_FOLDER_PATH = Environment.getExternalStorageDirectory().toString() + "/Pictures";
    private int CODE_FOLDER_BROWSER_DIALOG = 295001;

    private List<File> FILE_LIST = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorter);

        addButtonFunctionality();
        disableNotYetImplemented();

    }

    private void openFolderBrowserDialog() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(i, "Choose directory"), CODE_FOLDER_BROWSER_DIALOG);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri,DocumentsContract.getTreeDocumentId(uri));
        String path = PathHelper.getPath(this, docUri);
        File scopedFile = new File(path);

        if (scopedFile.exists()) {
            Helper.BadToast(this, "Does exist");
        }
        else {
            Helper.BadToast(this, "Does not exist");
        }

        ((TextView) findViewById(R.id.textView_folder)).setText(scopedFile.getAbsolutePath());
        FILE_LIST = getFilesInPath(SOURCE_FOLDER_PATH);

        StringBuilder combinedText = new StringBuilder();
        for (File file: FILE_LIST) {
            combinedText.append(file.getAbsolutePath()).append("\n");
        }
        ((TextView) findViewById(R.id.textFoundFiles)).setText(combinedText.toString());

        Helper.BadToast(this,"\n" + resultCode + "\n" + data.getData());
    }

    private List<File> getFilesInPath(String path) {
        return Arrays.asList(new File(path).listFiles());
    }

    private void addButtonFunctionality() {

        findViewById(R.id.button_delete).setOnClickListener(x -> {
        });

        //Let user change the folder where the images are located
        findViewById(R.id.button_changeImageFolder).setOnClickListener(x -> openFolderBrowserDialog());

        //Let user switch between cutting and coping images
        Button cutOrCopy = findViewById(R.id.button_cutOrCopy);
        cutOrCopy.setBackgroundColor(Color.GREEN);
        cutOrCopy.setOnClickListener(x -> {
            if (CUT_OR_COPY) {
                cutOrCopy.setBackgroundColor(Color.GREEN);
                CUT_OR_COPY = false;
                ((TextView) findViewById(R.id.button_cutOrCopy)).setText(R.string.mode_copy);
            } else {
                cutOrCopy.setBackgroundColor(Color.RED);
                CUT_OR_COPY = true;
                ((TextView) findViewById(R.id.button_cutOrCopy)).setText(R.string.mode_cut);
            }
        });
    }

    private void disableNotYetImplemented() {
        int[] disableList = new int[] { R.id.button_cutOrCopy, R.id.textView_cutOrCopy};
        for (int id : disableList) {
            findViewById(id).setBackgroundColor(Color.RED);
        }
    }
}