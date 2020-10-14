package com.example.fitnesstrackingapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.MalformedInputException;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    public DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Get widgets
        Button updateButton = (Button) findViewById(R.id.updateButton);
        final Spinner liftSpinner = (Spinner) findViewById(R.id.liftNameSpinner);
        final Spinner weightOrRepSpinner = (Spinner) findViewById(R.id.weightRepSpinner);
        final EditText userValue = (EditText) findViewById(R.id.weightRepValueEditText);
        databaseHelper = new DatabaseHelper(this);

        //Populate Spinners
        ArrayAdapter<CharSequence> adapterLiftName = ArrayAdapter.createFromResource(this, R.array.liftType, android.R.layout.simple_spinner_item);
        adapterLiftName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        liftSpinner.setAdapter(adapterLiftName);

        ArrayAdapter<CharSequence> adapterWeightRep = ArrayAdapter.createFromResource(this, R.array.columnName, android.R.layout.simple_spinner_item);
        adapterWeightRep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightOrRepSpinner.setAdapter(adapterWeightRep);

        //Update table with latest values from db
        getCurrentData(this);



        //Insert new values on click
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get selected user input for the lift-column to edit as well as the value.
                String liftName = liftSpinner.getSelectedItem().toString();
                String columnName = weightOrRepSpinner.getSelectedItem().toString();
                String temp = userValue.getText().toString();

                try {
                    Integer value = Integer.valueOf(temp);
                    //Call update function depending on what columnName is. If Weight call setWEight, else setREp
                    databaseHelper.setData(liftName, value, columnName);
                    updateDisplayedData(MainActivity.this, liftName, value, columnName);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Please enter numbers (1-10 digits)", Toast.LENGTH_LONG).show();
                }


            }
        });




        //Notepad button
        Button notepadButton = (Button) findViewById(R.id.notePadButton);

        //Switch activities on click
        notepadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Notepad.class);
                startActivity(intent);
            }
        });





    }

    //Methods to update the table shown to user
    @SuppressLint("SetTextI18n")
    public void updateDisplayedData(Context context, String liftName, Integer value, String columnName) {
        //Item to update is in weight column
        if (columnName.equals("Weight")) {
            //update the corresponding textview
            if (liftName.equals("Back Squat")) {
                TextView backSquat = (TextView) findViewById(R.id.backSquatWeightTextView);
                backSquat.setText(Integer.toString(value));
            } else if (liftName.equals("Deadlift")) {
                TextView deadliftWeight = (TextView) findViewById(R.id.deadliftWeightTextView);
                deadliftWeight.setText(Integer.toString(value));

            } else if (liftName.equals("Bench Press")) {
                TextView benchWeight = (TextView) findViewById(R.id.benchWeightTextView);
                benchWeight.setText(Integer.toString(value));

            } else if (liftName.equals("Overhead Press")) {
                TextView ohpWeight = (TextView) findViewById(R.id.ohpWeightTextView);
                ohpWeight.setText(Integer.toString(value));

            } else if (liftName.equals("Barbell Rows")) {
                TextView rowWeight = (TextView) findViewById(R.id.rowWeightTextView);
                rowWeight.setText(Integer.toString(value));
            } else {
                Toast.makeText(MainActivity.this, "liftName not in table", Toast.LENGTH_LONG).show();
            }
        } else {

            //Column name must be reps. Update corresponding textview.
            if (liftName.equals("Back Squat")) {
                TextView backSquat = (TextView) findViewById(R.id.backSquatRepsTextView);
                backSquat.setText(Integer.toString(value));
            } else if (liftName.equals("Deadlift")) {
                TextView deadliftWeight = (TextView) findViewById(R.id.deadliftRepsTextView);
                deadliftWeight.setText(Integer.toString(value));

            } else if (liftName.equals("Bench Press")) {
                TextView benchWeight = (TextView) findViewById(R.id.benchRepsTextView);
                benchWeight.setText(Integer.toString(value));

            } else if (liftName.equals("Overhead Press")) {
                TextView ohpWeight = (TextView) findViewById(R.id.ohpRepsTextView);
                ohpWeight.setText(Integer.toString(value));

            } else if (liftName.equals("Barbell Rows")) {
                TextView rowWeight = (TextView) findViewById(R.id.rowsRepsTextView);
                rowWeight.setText(Integer.toString(value));

            } else {
                Toast.makeText(MainActivity.this, "liftName not in table", Toast.LENGTH_LONG).show();
            }
        }

    }

    //Populate table with current data from db
    //Get widgets, get corresponding value in db. Set to current most db value.
    public void getCurrentData(Context context) {

        //Squat Widgets
        TextView squatWeight = (TextView) findViewById(R.id.backSquatWeightTextView);
        TextView squatReps = (TextView) findViewById(R.id.backSquatRepsTextView);
        //Deadlift widgets
        TextView deadliftWeight = (TextView) findViewById(R.id.deadliftWeightTextView);
        TextView deadliftReps = (TextView) findViewById(R.id.deadliftRepsTextView);
        //Bench widgets
        TextView benchWeight = (TextView) findViewById(R.id.benchWeightTextView);
        TextView benchReps = (TextView) findViewById(R.id.benchRepsTextView);
        //ohp widgets
        TextView ohpWeight = (TextView) findViewById(R.id.ohpWeightTextView);
        TextView ohpReps = (TextView) findViewById(R.id.ohpRepsTextView);
        //rows widgets
        TextView rowsWeight = (TextView) findViewById(R.id.rowWeightTextView);
        TextView rowsReps = (TextView) findViewById(R.id.rowsRepsTextView);


        //Squat
        Cursor squatWeightDB = databaseHelper.getData("Back Squat", "Weight");
        squatWeightDB.moveToFirst();
        String squatWeightCurrent = squatWeightDB.getString(squatWeightDB.getColumnIndex("Weight"));

        Cursor squatRepDB = databaseHelper.getData("Back Squat", "Reps");
        squatRepDB.moveToFirst();
        String squatRepsCurrent = squatRepDB.getString(squatRepDB.getColumnIndex("Reps"));

        //Deadlift
        Cursor deadliftWeightDB = databaseHelper.getData("Deadlift", "Weight");
        deadliftWeightDB.moveToFirst();
        String deadliftWeightCurrent = deadliftWeightDB.getString(deadliftWeightDB.getColumnIndex("Weight"));

        Cursor deadliftRepDB = databaseHelper.getData("Deadlift", "Reps");
        deadliftRepDB.moveToFirst();
        String deadliftRepsCurrent = deadliftRepDB.getString(deadliftRepDB.getColumnIndex("Reps"));

        //Bench Press
        Cursor benchWeightDB = databaseHelper.getData("Bench Press", "Weight");
        benchWeightDB.moveToFirst();
        String benchWeightsCurrent = benchWeightDB.getString(benchWeightDB.getColumnIndex("Weight"));

        Cursor benchRepsDB = databaseHelper.getData("Bench Press", "Reps");
        benchRepsDB.moveToFirst();
        String benchRepsCurrent = benchRepsDB.getString(benchRepsDB.getColumnIndex("Reps"));

        //Overhead Press
        Cursor ohpWeightDB = databaseHelper.getData("Overhead Press", "Weight");
        ohpWeightDB.moveToFirst();
        String ohpWeightCurrent = ohpWeightDB.getString(ohpWeightDB.getColumnIndex("Weight"));

        Cursor ohpRepsDB = databaseHelper.getData("Overhead Press", "Reps");
        ohpRepsDB.moveToFirst();
        String ohpRepsCurrent = ohpRepsDB.getString(ohpRepsDB.getColumnIndex("Reps"));

        //Barbell Rows
        Cursor rowsWeightDB = databaseHelper.getData("Barbell Rows", "Weight");
        rowsWeightDB.moveToFirst();
        String rowsWeightCurrent = rowsWeightDB.getString(rowsWeightDB.getColumnIndex("Weight"));

        Cursor rowsRepsDB = databaseHelper.getData("Barbell Rows", "Reps");
        rowsRepsDB.moveToFirst();
        String rowsRepsCurrent = rowsRepsDB.getString(rowsRepsDB.getColumnIndex("Reps"));

        //update hard coded table textview values with most current values found in db
        squatWeight.setText(squatWeightCurrent);
        squatReps.setText(squatRepsCurrent);
        deadliftWeight.setText(deadliftWeightCurrent);
        deadliftReps.setText(deadliftRepsCurrent);
        benchWeight.setText(benchWeightsCurrent);
        benchReps.setText(benchRepsCurrent);
        ohpWeight.setText(ohpWeightCurrent);
        ohpReps.setText(ohpRepsCurrent);
        rowsWeight.setText(rowsWeightCurrent);
        rowsReps.setText(rowsRepsCurrent);


    }



}

//TODO:
//-Add Notepad
//-Add exrx api if approved