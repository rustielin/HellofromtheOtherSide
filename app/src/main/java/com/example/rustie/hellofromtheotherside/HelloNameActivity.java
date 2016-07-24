package com.example.rustie.hellofromtheotherside;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelloNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_name);

        Button name_button = (Button)findViewById(R.id.name_button);

        name_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(HelloNameActivity.this, MapsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.rightin, R.anim.leftout);
            }
        });

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
    }
}
