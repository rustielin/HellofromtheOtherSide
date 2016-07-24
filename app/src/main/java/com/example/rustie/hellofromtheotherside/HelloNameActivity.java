package com.example.rustie.hellofromtheotherside;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HelloNameActivity extends AppCompatActivity {

    private static String full_name;
    private static EditText edit_text;
    private static SharedPreferences shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_name);
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.apply();
        }

        edit_text = (EditText) findViewById(R.id.editText);
        full_name = edit_text.getText().toString();
        Button name_button = (Button) findViewById(R.id.name_button);


        name_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared = getSharedPreferences("pref2", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("full_name", edit_text.getText().toString());
                editor.commit();
                Intent i = new Intent();
                i.setClass(HelloNameActivity.this, MapsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.rightin, R.anim.leftout);
            }
        });
    }
}
