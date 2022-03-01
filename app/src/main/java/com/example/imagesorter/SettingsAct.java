package com.example.imagesorter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

public class SettingsAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        disableNotYetImplemented();
        findViewById(R.id.settingsProceed).setOnClickListener(x -> startActivity(new Intent(this, SorterAct.class)));
    }

    private void disableNotYetImplemented() {
        int[] disableList = new int[] { R.id.settingsNotImplementedInfo, R.id.settingsSwitchOne, R.id.settingsSwitchTwo, R.id.settingsTextOne, R.id.settingsTextTwo};
        for (int id : disableList) {
            findViewById(id).setBackgroundColor(Color.RED);
        }
    }
}