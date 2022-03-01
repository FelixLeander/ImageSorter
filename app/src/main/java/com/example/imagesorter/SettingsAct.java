package com.example.imagesorter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

public class SettingsAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        disableNotYetImplemented();
        findViewById(R.id.settingsProceed).setOnClickListener(x -> startActivity(new Intent(this, SorterAct.class)));

        ((SwitchCompat) findViewById(R.id.settingsSwitch_debugMode)).setOnCheckedChangeListener((switchView, boolState) -> Helper.S_DEBUG_MODE = boolState);

        Button repoButton = findViewById(R.id.repoButton);
        repoButton.setText(Helper.originalRepo);
        repoButton.setOnClickListener(x -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Helper.originalRepo))));
    }

    private void disableNotYetImplemented() {
        int[] disableList = new int[] { R.id.settingsNotImplementedInfo, R.id.settingsSwitchTwo, R.id.settingsTextTwo};
        for (int id : disableList) {
            findViewById(id).setBackgroundColor(Color.RED);
        }
    }
}