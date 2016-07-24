package com.example.rustie.hellofromtheotherside;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by rustie on 7/23/16.
 */
public class CreateJoinPartyActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_party);

        Button createParty = (Button) findViewById(R.id.create_party_button);
        createParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(CreateJoinPartyActivity.this, CreatePartyActivity.class);
                startActivity(intent);
                finish();

            }
        });

        Button joinParty = (Button) findViewById(R.id.join_party_button);
        joinParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(CreateJoinPartyActivity.this, JoinPartyActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateJoinPartyActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
