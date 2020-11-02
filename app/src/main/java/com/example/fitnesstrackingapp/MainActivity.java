package com.example.fitnesstrackingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    public DatabaseHelper databaseHelper;
    private boolean isAlarmPlaying = false;
    private MediaPlayer mp = new MediaPlayer();
    private boolean isTimerStarted = false;
    private CountDownTimer timer;
    private int timeLeft;
    private boolean isTimerPaused = false;
    public static int customMinutes;
    public static int customSeconds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Get widgets
        Button updateButton = (Button) findViewById(R.id.updateButton);
        Button timerStart = (Button) findViewById(R.id.startButtonCustom);
        final Button resetButton = (Button) findViewById(R.id.resetButton);
        final Button pauseButton = (Button) findViewById(R.id.pauseButton);
        final Spinner liftSpinner = (Spinner) findViewById(R.id.liftNameSpinner);
        final Spinner weightOrRepSpinner = (Spinner) findViewById(R.id.weightRepSpinner);
        final Spinner timerSpinner = (Spinner) findViewById(R.id.timerSpinner);
        final EditText userValue = (EditText) findViewById(R.id.weightRepValueEditText);
        databaseHelper = new DatabaseHelper(this);
        mp = MediaPlayer.create(MainActivity.this, R.raw.alarm);


        //Populate Spinners
        ArrayAdapter<CharSequence> adapterLiftName = ArrayAdapter.createFromResource(this, R.array.liftType, android.R.layout.simple_spinner_item);
        adapterLiftName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        liftSpinner.setAdapter(adapterLiftName);

        ArrayAdapter<CharSequence> adapterWeightRep = ArrayAdapter.createFromResource(this, R.array.columnName, android.R.layout.simple_spinner_item);
        adapterWeightRep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightOrRepSpinner.setAdapter(adapterWeightRep);

        ArrayAdapter<CharSequence> adapterTimer = ArrayAdapter.createFromResource(this, R.array.timerValues, android.R.layout.simple_spinner_item);
        adapterTimer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timerSpinner.setAdapter(adapterTimer);

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
                Intent intent = new Intent(MainActivity.this, Notepad.class);
                startActivity(intent);
            }
        });

        //When start button hit start timer
        timerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = (Spinner) findViewById(R.id.timerSpinner);
                String val = spinner.getSelectedItem().toString();

                //Get custom time if custom selected
                if(val.equals("Custom") && !isTimerPaused && !isTimerStarted){
                    //Switch activties to get time
                    Intent customTime = new Intent(MainActivity.this, getCustomTime.class);
                    startActivityForResult(customTime,1);

                }
                //Paused time/incomplete time
                if (isTimerPaused) {
                    isTimerPaused = false;
                    startTimer(timeLeft);
                }
                //Use predefined time from list
                else {
                    startTimer();
                }
            }
        });

        //Stop alarm if reset pressed
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();

            }
        });

        //Pause timer if pause button pressed
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                isTimerStarted = false;
                isTimerPaused = true;
            }
        });

        //Launch Custom Timer input box

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

    //start timer with predefined durations
    public void startTimer() {
        //only start if not already on
        if (!isTimerStarted) {

            isTimerStarted = true;


            //Get time selected from spinner.
            Spinner timeSpinner = (Spinner) findViewById(R.id.timerSpinner);
            String selectedTime = timeSpinner.getSelectedItem().toString();
            int timeMilliseconds;

            //if(!selectedTime.equals("Custom")){



            //Turn time into milliseconds
            if (selectedTime.equals("30")) {
                int temp = Integer.parseInt("30");
                timeMilliseconds = temp * 1000;
            } else if (selectedTime.equals("1:30")) {
                int temp = Integer.parseInt("90");
                timeMilliseconds = temp * 1000;
            } else if (selectedTime.equals("2:30")) {
                int temp = Integer.parseInt("150");
                timeMilliseconds = temp * 1000;
            } else if (selectedTime.equals("3:00")) {
                int temp = Integer.parseInt("180");
                timeMilliseconds = temp * 1000;
            } else {
                int temp = Integer.parseInt("300");
                timeMilliseconds = temp * 1000;
            }

            //Create timer
            timer = new CountDownTimer(timeMilliseconds, 1000) {
                TextView timer = (TextView) findViewById(R.id.timeLeftTextView);


                @SuppressLint({"DefaultLocale", "SetTextI18n"})
                @Override
                public void onTick(long millisUntilFinished) {
                    //set timer text
                    timer.setText("" + String.format("%d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    //store time left to resume after pressing pause
                    timeLeft = (int) millisUntilFinished;
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onFinish() {
                    timer.setText("0");
                    mp.start();
                    isAlarmPlaying = true;
                    isTimerStarted = false;
                }
            };
            timer.start();

          //  }
        }
    }

    //start timer with custom duration
    public void startTimer(int timeLeftOnTimer) {

        Spinner timeSpinner = (Spinner) findViewById(R.id.timerSpinner);
        String time = timeSpinner.getSelectedItem().toString();


        //only start if not already on
        if (!isTimerStarted) {

            isTimerStarted = true;

            //Create timer
            timer = new CountDownTimer(timeLeftOnTimer, 1000) {
                TextView timer = (TextView) findViewById(R.id.timeLeftTextView);


                @SuppressLint({"DefaultLocale", "SetTextI18n"})
                @Override
                public void onTick(long millisUntilFinished) {
                    //set timer text
                    timer.setText("" + String.format("%d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    //store time left to resume after pressing pause
                    timeLeft = (int) millisUntilFinished;
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onFinish() {
                    timer.setText("0");
                    mp.start();
                    isAlarmPlaying = true;
                    isTimerStarted = false;
                }
            };
            timer.start();

        }
    }

    //reset timer either after time runs out or prematurely while time is remaining
    public void resetTimer() {
        //Timer is done if alarm is playing
        if (isAlarmPlaying) {
            mp.stop();
            mp.reset();
            mp = new MediaPlayer();
            mp = MediaPlayer.create(MainActivity.this, R.raw.alarm);

            //mp.release();
            isAlarmPlaying = false;
        } else {
            //timer running end it
            if (isTimerStarted) {
                isTimerStarted = false;
            }
            timer.cancel();
            //set timer text back to 0.
            TextView timerTimeLeft = (TextView) findViewById(R.id.timeLeftTextView);
            timerTimeLeft.setText("0");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            //if (resultCode == Activity.RESULT_CANCELED) {
                //minute to seconds
                int milliseconds = ((MainActivity.customMinutes*60)+MainActivity.customSeconds)*1000;
                startTimer(milliseconds);
            //}
        }
    }

}

//TODO:
//-Add Notepad - done
//-Add exrx api if approved - No response as of yet.
//-pause button - done
//-reset before hits 0 - done
//improve gui
//add weight tracker + graph
//custom timer input box
//youtube se
