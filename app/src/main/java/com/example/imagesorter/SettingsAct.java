package com.example.imagesorter;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.InputType;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.gridlayout.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class SettingsAct extends AppCompatActivity {

    //These numbers are chosen by random.
    private final int CODE_FBD_GET_TARGET_FOLDER = 295001;
    private final int CODE_FBD_GET_SOURCE_FOLDER = 295002;
    private String DIALOG_TEXT = "";

    private String getDIALOG_TEXT() {
        return DIALOG_TEXT;
    }

    private void setDIALOG_TEXT(String set) {
        DIALOG_TEXT = set;

        if (DIALOG_TEXT.isEmpty()) {
            Toast.makeText(this, "Please enter something valid", Toast.LENGTH_SHORT).show();
            return;
        }
        openFolderBrowserDialog(CODE_FBD_GET_TARGET_FOLDER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        final int[] i = {0};
        GridLayout gridLayout = findViewById(R.id.settingsGridLayout);
        findViewById(R.id.settingsButtonSelectSource).setOnClickListener(v -> {
            Button button = new Button(this);
            String text = "Number " + i[0];
            i[0]++;
            button.setText(text);
            button.setBackgroundColor(fetchColor(androidx.appcompat.R.attr.colorPrimary));
            gridLayout.addView(button);
        });

        findViewById(R.id.settingsButtonSelectSource).setOnClickListener(v -> openFolderBrowserDialog(CODE_FBD_GET_SOURCE_FOLDER));
        findViewById(R.id.repoButton).setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/FelixLeander/ImageSorter"))));

        findViewById(R.id.button_addTargetFolder).setOnClickListener(v -> showAlertEnterText());

        findViewById(R.id.button_goToSort).setOnClickListener(v -> {
            if (FolderButton.collection.size() == 0) {
                Toast.makeText(this, "You need at least one target folder", Toast.LENGTH_LONG).show();
                return;
            }
            startActivity(new Intent(this, SorterAct.class));
        });
    }

    private int fetchColor(int rAttrName) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = obtainStyledAttributes(typedValue.data, new int[]{rAttrName});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    private void openFolderBrowserDialog(int requestCode) {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(i, "Choose directory"), requestCode); //Message me for clean replacement
    }

    //Triggered when folderBrowserDialog exits
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Make sure selected path is valid and a directory
        if (data == null) {
            Toast.makeText(this, "Aborted folder choosing", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = data.getData();
        String path = PathHelper.getPath(this, DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri)));
        if (path == null) {
            Toast.makeText(this, "The folder path is not valid", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(path);
        if (!(file.exists() && file.isDirectory())) {
            Toast.makeText(this, "The selected folder is not valid", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case CODE_FBD_GET_TARGET_FOLDER:
                addButton(file);
                break;

            case CODE_FBD_GET_SOURCE_FOLDER:
                setSourceFolder(file);
                break;

            default:
                Toast.makeText(this, "requestCode did not meet any requirements", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setSourceFolder(File file) {
        FolderButton.SourceFolder = file;
        findViewById(R.id.button_goToSort).setEnabled(true);
        ((TextView)  findViewById(R.id.settingsTextViewSelectedSource)).setText(file.getPath());
    }

    //Todo: Replace with dynamic
    //For some reason dynamic add did not work. Suspect "getChildAt(i)" or casting was the fault.
    private void addButton(File file) {

        GridLayout gridLayout = findViewById(R.id.settingsGridLayout);

        //Configure button
        FolderButton folderButton = new FolderButton(getDIALOG_TEXT(), file, new Button(this));
        folderButton.button.setText(folderButton.name);
        folderButton.button.setOnClickListener(v -> onTargetFolderClick(folderButton));

        gridLayout.addView(folderButton.button);
    }

    private void onTargetFolderClick(FolderButton folderButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(folderButton.name);

        //final EditText input = new EditText(this);

        // Specify type of input, like password, text, etc
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Remove", (dialog, which) -> {
            ((GridLayout) folderButton.button.getParent()).removeView(folderButton.button);
            FolderButton.collection.remove(folderButton);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void showAlertEnterText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ENTER NAME FOR FOLDER");

        final EditText editText = new EditText(this);

        // Specify type of editText, like password, text, etc
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        builder.setView(editText);

        // Button setup
        builder.setPositiveButton("OK", (dialog, which) -> setDIALOG_TEXT(editText.getText().toString()));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}