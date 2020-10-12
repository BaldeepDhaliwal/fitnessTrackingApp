package com.example.fitnesstrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


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

       databaseHelper= new DatabaseHelper(this);
       //databaseHelper.initializeData();

        //Populate Spinners
        ArrayAdapter<CharSequence> adapterLiftName= ArrayAdapter.createFromResource(this,R.array.liftType, android.R.layout.simple_spinner_item);
        adapterLiftName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        liftSpinner.setAdapter(adapterLiftName);

        ArrayAdapter<CharSequence> adapterWeightRep = ArrayAdapter.createFromResource(this,R.array.columnName,android.R.layout.simple_spinner_item);
        adapterWeightRep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightOrRepSpinner.setAdapter(adapterWeightRep);


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
                if(columnName.equals("Weight")){
                    databaseHelper.setWeight(liftName,value);
                }
                //Not weight call setReps
                else{
                    databaseHelper.setReps(liftName,value);
                }
            }
        });




    }
}
