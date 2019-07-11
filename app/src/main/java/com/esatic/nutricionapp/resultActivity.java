package com.esatic.nutricionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class resultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String result = getIntent().getStringExtra("result");
        TextView textViewResult = findViewById(R.id.resultText);
        textViewResult.setText(result);


    }
}
