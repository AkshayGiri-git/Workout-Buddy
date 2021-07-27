package com.example.workoutbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Endrun extends AppCompatActivity {
    TextView textview;
    EditText editText;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endrun);
        textview = findViewById(R.id.textView3);
        editText = findViewById(R.id.edittext);
        Intent intent = getIntent();
        double distance = Math.round(intent.getDoubleExtra("distanceforend", 0.0) * 100.0) / 100.0;
        double avgspeed = Math.round(intent.getDoubleExtra("avgspeedforend", 0.0) * 10.0) / 10.0;

        int time = intent.getIntExtra("amountforend", 0);
        textview.setText("Good job! you coverd "+distance+" miles at "+avgspeed +" miles per hour and took a took a total time of "+time+" seconds");
        submit = findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Endrun.this,MainActivity2.class);
                intent.putExtra("distanceformain",distance);
                intent.putExtra("avgspeedformain",avgspeed);
                intent.putExtra("timeformain",time);
                intent.putExtra("descriptionformain",editText.toString());
                startActivity(intent);
            }
        });
    }
}