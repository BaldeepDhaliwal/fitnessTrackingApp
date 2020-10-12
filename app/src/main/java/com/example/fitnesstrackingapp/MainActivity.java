package com.example.fitnesstrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


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

        getCurrentData(this);
        //When update clicked update values
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get selected user input for the lift-column to edit as well as the value.
                String liftName = liftSpinner.getSelectedItem().toString();
                String columnName = weightOrRepSpinner.getSelectedItem().toString();
                String temp = userValue.getText().toString();
                Integer value = Integer.valueOf(temp);

                //Call update function depending on what columnName is. If Weight call setWEight, else setREp
                    databaseHelper.setData(liftName,value,columnName);
                    updateDisplayedData(MainActivity.this,liftName,value,columnName);


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
    public void getCurrentData(Context context){

        //Back Squat Widgets
        TextView squatWeight = (TextView) findViewById(R.id.backSquatWeightTextView);
        TextView squatReps = (TextView) findViewById(R.id.backSquatRepsTextView);
        //Get values
        String squatWeightDB = databaseHelper.getData("Back Squat","Weight").getString(0);
        Integer squatRepsDB = databaseHelper.getData("Back Squat", "Reps").getInt(0);


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





    }

}
