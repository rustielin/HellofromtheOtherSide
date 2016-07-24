package com.example.rustie.hellofromtheotherside;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by rustie on 7/23/16.
 */
public class CreatePartyActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        SharedPreferences shared = getSharedPreferences("pref2", Context.MODE_PRIVATE);


        Long key = (Long) (long) shared.getInt("key", 0);
        TextView keyTextView = (TextView) findViewById(R.id.key_text_view);
        keyTextView.setText("" + key);

        Button home = (Button) findViewById(R.id.button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(CreatePartyActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreatePartyActivity.this, CreateJoinPartyActivity.class);
        startActivity(intent);
        finish();
    }
}
