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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.Random;

/**
 * Created by rustie on 7/23/16.
 */
public class CreateJoinPartyActivity extends Activity{


    private static int key;
    private static EditText edit_text;
    private static EditText edit_name;
    private static SharedPreferences shared;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_party);

        final SharedPreferences[] shared = {getSharedPreferences("pref2", Context.MODE_PRIVATE)};


        final String name = shared[0].getString("full_name", "");
//        TextView usernameTextView = (TextView) findViewById(R.id.username);
//        usernameTextView.setText(name);


        Long key = (Long) (long) shared[0].getInt("key", 0);


        edit_text = (EditText) findViewById(R.id.editText);
        edit_name = (EditText) findViewById(R.id.editName);



        edit_text.setText("" + key);
        edit_name.setText(name);

        Button okButton = (Button) findViewById(R.id.ok_button);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared[0] = getSharedPreferences("pref2", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared[0].edit();
                editor.remove("key");

                editor.putInt("key", Integer.parseInt(edit_text.getText().toString()));

                Firebase fire =  new Firebase("https://hellofromtheotherside-5eb21.firebaseio.com/");
                fire.child(name).removeValue();

                editor.putString("full_name", edit_name.getText().toString());

                editor.commit();
                Intent i = new Intent();
                i.setClass(CreateJoinPartyActivity.this, MapsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.rightin, R.anim.leftout);
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
