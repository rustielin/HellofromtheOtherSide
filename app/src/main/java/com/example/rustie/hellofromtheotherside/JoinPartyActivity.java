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
public class JoinPartyActivity extends Activity{


    private static int key;
    private static EditText edit_text;
    private static SharedPreferences shared;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join_party);

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);

        SharedPreferences.Editor ed = pref.edit();
        ed.putBoolean("activity_executed", true);
        ed.apply();


        edit_text = (EditText) findViewById(R.id.editText);
        key = (new Random()).nextInt(1000000);
        Button keyButton = (Button) findViewById(R.id.key_button);

        keyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared = getSharedPreferences("pref2", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.remove("key");

                editor.putInt("key", Integer.parseInt(edit_text.getText().toString()));

                editor.commit();
                Intent i = new Intent();
                i.setClass(JoinPartyActivity.this, MapsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.rightin, R.anim.leftout);
            }
        });


        Button home = (Button) findViewById(R.id.button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinPartyActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(JoinPartyActivity.this, CreateJoinPartyActivity.class);
        startActivity(intent);
        finish();
    }
}
