package com.example.imagesorter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SorterAct extends AppCompatActivity {

    private boolean CUT_OR_COPY = false;
    private String SOURCE_FOLDER_PATH = "";

    public int CODE_FOLDER_BROWSER_DIALOG = 295001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorter);

        addButtonFunctionality();

    }

    private void openFolderBrowserDialog() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(i, "Choose directory"), 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case -1:
                Toast.makeText(this, "-1 in switch SorterAct.class", Toast.LENGTH_SHORT).show();
                break;
            case 777777:
                break;
        }
    }

    private void addButtonFunctionality() {

        //Let user change the folder where the images are located
        findViewById(R.id.button_changeImageFolder).setOnClickListener(x -> {
            SOURCE_FOLDER_PATH = Environment.getExternalStorageState() + "/" + Environment.DIRECTORY_DCIM;
            ((TextView) findViewById(R.id.textView_folder)).setText(SOURCE_FOLDER_PATH);
        });

        //Let user switch between cutting and coping images
        Button cutOrCopy = findViewById(R.id.button_cutOrCopy);
        cutOrCopy.setBackgroundColor(Color.GREEN);
        cutOrCopy.setOnClickListener(x -> {
            if (CUT_OR_COPY) {
                cutOrCopy.setBackgroundColor(Color.GREEN);
                CUT_OR_COPY = false;
                ((TextView) findViewById(R.id.button_cutOrCopy)).setText("MODE: FALSE");
            } else {
                cutOrCopy.setBackgroundColor(Color.RED);
                CUT_OR_COPY = true;
                ((TextView) findViewById(R.id.button_cutOrCopy)).setText("MODE: CUT");
            }
        });
    }
}