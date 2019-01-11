package com.example.konta.sketch_loyalityapp.ui.signUp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.ui.login.LogInActivity;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        TextView textView = findViewById(R.id.log_in_text_view);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        SignUpActivity.this.startActivity(intent);
    }
}