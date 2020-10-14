package com.example.fitnesstrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Notepad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        String fileName = "fitnessTrackingAppNotesSaveFile";

        //Check and load text if file already exists
        loadText(fileName);

        //Go back to main if cancel pressed
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notepad.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //Save and go back to main activity when Save button clicked
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    EditText notes = (EditText) findViewById(R.id.notesEditText);

                    //write file to default save location on device
                    OutputStreamWriter out = new OutputStreamWriter(openFileOutput("fitnessTrackingAppNotesSaveFile",0));
                    out.write(notes.getText().toString());

                    String temp = notes.getText().toString();
                    out.close();
                    Toast.makeText(Notepad.this,"Notes Saved", Toast.LENGTH_LONG).show();
                    //Go back to main
                    Intent intent = new Intent(Notepad.this, MainActivity.class);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(Notepad.this,"Exception: "+e.toString()+".", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Load saved text if exists
    public void loadText(String fileName){
        //Open file
        File file = getBaseContext().getFileStreamPath(fileName);
        //editText widget
        EditText notes = (EditText) findViewById(R.id.notesEditText);
        //load file if exists
        if(file.exists()){
            //read file and write contents to string
            try{
                InputStream input = openFileInput(fileName);
                if(input!= null) {
                    InputStreamReader temp = new InputStreamReader(input);
                    BufferedReader reader = new BufferedReader(temp);
                    String str;
                    StringBuilder buf = new StringBuilder();
                    //Build file string
                    while ((str = reader.readLine()) != null){
                        buf.append(str + "\n");
                    }
                    input.close();
                    notes.setText(buf.toString());
                }

            }catch (Exception e){
                Toast.makeText(Notepad.this,"Exception: "+e.toString(),Toast.LENGTH_LONG).show();
            }

        }


    }
}
