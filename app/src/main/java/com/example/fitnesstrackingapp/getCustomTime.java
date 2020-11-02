package com.example.fitnesstrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class getCustomTime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_custom_time);
        final Button startButton = (Button) findViewById(R.id.startButtonCustom);

        //Pass back entered time and switch activties
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                //get minutes and seconds
                EditText minuteEditText = (EditText) findViewById(R.id.minuteEditText);
                EditText secondsEditText = (EditText)  findViewById(R.id.secondsEditText);
                MainActivity.customMinutes = Integer.parseInt(minuteEditText.getText().toString());
                MainActivity.customSeconds = Integer.parseInt(secondsEditText.getText().toString());
                Intent intent = new Intent(getCustomTime.this, MainActivity.class);
                setResult(Activity.RESULT_CANCELED,intent);
                finish();
                }catch (Exception e){
                    Toast.makeText(getCustomTime.this, "Please enter a valid numbers for minute and seconds", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
}
