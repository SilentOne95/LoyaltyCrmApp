package com.example.konta.sketch_loyalityapp.LoginUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        TextView textView = findViewById(R.id.sign_up_text_view);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        LogInActivity.this.startActivity(intent);
    }
}