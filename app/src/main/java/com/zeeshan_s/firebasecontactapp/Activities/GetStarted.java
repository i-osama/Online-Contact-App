package com.zeeshan_s.firebasecontactapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.zeeshan_s.firebasecontactapp.R;

public class GetStarted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        AppCompatButton startBtn = findViewById(R.id.getStartedBtn);

        SharedPreferences preferences = getSharedPreferences("getStartedButtonClicked", MODE_PRIVATE);

        boolean isClicked = preferences.getBoolean("userClicked", false);
        if (isClicked){
            startActivity(new Intent(GetStarted.this, LoginActivity.class));
        }

        startBtn.setOnClickListener(view -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("userClicked",true);
            editor.apply();

            Intent intent = new Intent(GetStarted.this, LoginActivity.class);
            startActivity(intent);

        });

    }
}