package com.example.konta.sketch_loyalityapp.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.konta.sketch_loyalityapp.MenuUI.MainActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
        finish();
    }
}
