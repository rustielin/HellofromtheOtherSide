package com.example.rustie.hellofromtheotherside;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by rustie on 7/23/16.
 */
public class SettingsActivity extends Activity{



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        ImageButton nameButton = (ImageButton) findViewById(R.id.name);
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("activity_executed");

                editor.apply();

                Intent intent = new Intent(SettingsActivity.this, HelloNameActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
