package com.example.my_capstone;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AssessmentDetailsActivity extends AppCompatActivity {
    long id, courseAssessmentId;
    EditText editTitle, editStart, editEnd;
    String title, start, end, type, startOrEndDateClicked;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();
    int spinnerSelection;
    public static int numAlert = 0;

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (startOrEndDateClicked == "start")
            editStart.setText(sdf.format(myCalendar.getTime()));
        else editEnd.setText(sdf.format(myCalendar2.getTime()));
    }

    public void notifyStartButton(View view) {
        notify("Assessment: "+title+" is starting today.", myCalendar);
    }

    public void notifyEndButton(View view) {
        notify("Assessment: "+title+" ends today.", myCalendar2);
    }

    public void notify(String msg, Calendar c) {
        Intent intentPend = new Intent(AssessmentDetailsActivity.this,MyReceiver2.class);
        intentPend.putExtra("key",msg);
        PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetailsActivity.this,++numAlert,intentPend,0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }

    public void saveButton(View view) throws ParseException {
        DBHelper myHelper;
        myHelper = new DBHelper(AssessmentDetailsActivity.this); //create db
        myHelper.getWritableDatabase(); //create or open
        Spinner dropdown = findViewById(R.id.spinner1);
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTitle=findViewById(R.id.assessmentName);
        editStart=findViewById(R.id.assessmentStart);
        editEnd=findViewById(R.id.assessmentEnd);

        if (editTitle.getText().toString().trim().length() == 0 || editStart.getText().toString().trim().length() == 0 || editEnd.getText().toString().trim().length() == 0) {
            popupMessage(view, "Error: Please ensure all fields are populated.", "ERROR");
        } else {

            String str1 = editStart.getText().toString();
            Date date1 = sdf.parse(str1);

            String str2 = editEnd.getText().toString();
            Date date2 = sdf.parse(str2);

            if (date1.compareTo(date2)>0)
            {
                popupMessage(view, "Error: End date must be after start date. ", "ERROR");
            } else {
                if (id != -1) { //update an existing assessment
                    myHelper.runSQL("UPDATE assessments_tbl SET assessmentId = " + id + ", courseAssessmentId =  "+courseAssessmentId+", assessmentName = '" + editTitle.getText() + "', " +
                            "assessmentType = '"+dropdown.getSelectedItem().toString()+"', startDate = '" + editStart.getText() + "', endTime = '" + editEnd.getText() + "', consoleAlert = 0 WHERE assessmentId = '" + id + "'");
                }
                else { //adding a new assessment
                    myHelper.runSQL("INSERT INTO assessments_tbl (assessmentId, courseAssessmentId, assessmentName, assessmentType, startDate, endTime, consoleAlert) VALUES (null, "+courseAssessmentId+", '"+editTitle.getText()+"', '"+dropdown.getSelectedItem().toString()+"', '"+editStart.getText()+"', '"+editEnd.getText()+"', 0)");
                }

                Intent intent;
                if (id != -1) {
                    intent = new Intent(AssessmentDetailsActivity.this, AssessmentActivity.class);
                }
                else {
                    intent= new Intent(AssessmentDetailsActivity.this,CourseDetailsActivity.class);
                    final Course current = myHelper.getSingleCourse("SELECT * FROM courses_tbl WHERE courseId = '"+courseAssessmentId+"'");
                    if (current != null) {
                        intent.putExtra("courseId", current.getCourseId());
                        intent.putExtra("courseTermId", current.getCourseTermId());
                        intent.putExtra("courseName", current.getName());
                        intent.putExtra("courseStart", current.getStartDate());
                        intent.putExtra("courseEnd", current.getEndDate());
                        intent.putExtra("courseStatus", current.getStatus());
                        intent.putExtra("mentorName", current.getMentorName());
                        intent.putExtra("mentorPhone", current.getMentorPhone());
                        intent.putExtra("mentorEmail", current.getMentorEmail());
                        intent.putExtra("courseNotes", current.getNote());
                    }
                }
                startActivity(intent);
            }
        }
    }

    public void deleteButton(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                        DBHelper myHelper;
                        myHelper = new DBHelper(AssessmentDetailsActivity.this); //create db
                        myHelper.getWritableDatabase(); //create or open
                        myHelper.runSQL("DELETE FROM assessments_tbl WHERE assessmentId = '"+id+"'");
                        popupMessage(view, "Assessment deleted", "Success");

                }
            }
        };
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("Are you sure you want to delete this Assessment?").setTitle("Confirmation")
                .setPositiveButton("Yes", dialogClickListener)
                .setNeutralButton("No", dialogClickListener)
                .show();
    }

    public void popupMessage(View view, String str, String title) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    if (title == "Success") {
                        Intent intent= new Intent(AssessmentDetailsActivity.this,AssessmentActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage(str).setTitle(title)
                .setPositiveButton("Okay", dialogClickListener)
                .show();
    }


    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

    DatePickerDialog.OnDateSetListener date2 = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar2.set(Calendar.YEAR, year);
        myCalendar2.set(Calendar.MONTH, monthOfYear);
        myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        id=getIntent().getLongExtra("assessmentId", -1);
        courseAssessmentId = getIntent().getLongExtra("courseAssessmentId", -1);
        type = "";

        if (id != -1) {
            title = getIntent().getStringExtra("assessmentName");
            start = getIntent().getStringExtra("assessmentStart");
            end = getIntent().getStringExtra("assessmentEnd");
            type = getIntent().getStringExtra("assessmentType");
            setContentView(R.layout.activity_adetails);
        } else {
            setContentView(R.layout.activity_newadetails);
        }

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{"Performance", "Objective"};

        switch (type) {
            case "Performance": {
                spinnerSelection = 0;
                break;
            }
            case "Objective": {
                spinnerSelection = 1;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(spinnerSelection);

        editTitle=findViewById(R.id.assessmentName);
        editTitle.setText(title);
        editStart=findViewById(R.id.assessmentStart);
        editStart.setText(start);
        editEnd=findViewById(R.id.assessmentEnd);
        editEnd.setText(end);

        editStart.setOnClickListener(v -> {
            startOrEndDateClicked = "start";
            new DatePickerDialog(AssessmentDetailsActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        editEnd.setOnClickListener(v -> {
            startOrEndDateClicked ="end";
            new DatePickerDialog(AssessmentDetailsActivity.this, date2, myCalendar2
                    .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                    myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
        });
    } //end onCreate

}