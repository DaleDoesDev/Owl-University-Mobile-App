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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CourseDetailsActivity extends AppCompatActivity {
    long id, courseTermId;
    EditText editTitle, editStart, editEnd, editMentorName, editMentorPhone, editMentorEmail, editNotes;
    String title, start, end, note, startOrEndDateClicked, status, mentorName, mentorPhone, mentorEmail;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();
    int spinnerSelection;
    public static int numAlert = 0;


    public void addAssessment(View view) {
        Intent intent= new Intent(CourseDetailsActivity.this,AssessmentDetailsActivity.class);
        intent.putExtra("courseAssessmentId", id);
        startActivity(intent);
    }

    public void shareButton(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = editNotes.getText().toString();
        String shareHeader = "Course Notes";
        intent.putExtra(Intent.EXTRA_SUBJECT, shareHeader);
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Share Using:"));
    }

    public void notifyStartButton(View view) {
        notify("Course: "+title+" is starting today.", myCalendar);
    }

    public void notifyEndButton(View view) {
        notify("Course: "+title+" ends today.", myCalendar2);
    }

    public void notify(String msg, Calendar c) {
        Intent intentPend = new Intent(CourseDetailsActivity.this,MyReceiver2.class);
        intentPend.putExtra("key",msg);
        PendingIntent sender = PendingIntent.getBroadcast(CourseDetailsActivity.this,++numAlert,intentPend,0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (startOrEndDateClicked == "start") {
            editStart.setText(sdf.format(myCalendar.getTime()));
        }
        else editEnd.setText(sdf.format(myCalendar2.getTime()));
    }


    public void deleteButton(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                        DBHelper myHelper;
                        myHelper = new DBHelper(CourseDetailsActivity.this); //create db
                        myHelper.getWritableDatabase(); //create or open
                        //delete the associated assessments, then delete the selected course.
                        myHelper.runSQL("DELETE FROM assessments_tbl WHERE courseAssessmentId = '"+id+"'");
                        myHelper.runSQL("DELETE FROM courses_tbl WHERE courseId = '"+id+"'");
                        popupMessage(view, "Course deleted", "Success");
                    }
            }
        };
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("Are you sure you want to delete this Course and its associated assessments?").setTitle("Confirmation")
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
                        Intent intent= new Intent(CourseDetailsActivity.this,CourseActivity.class);
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

        id=getIntent().getLongExtra("courseId", -1);
        courseTermId = getIntent().getLongExtra("courseTermId", -1);
        status = "";

        if (id != -1) {
            setContentView(R.layout.activity_cdetails); //which xml file for this activity

            title = getIntent().getStringExtra("courseName");
            start = getIntent().getStringExtra("courseStart");
            end = getIntent().getStringExtra("courseEnd");
            status = getIntent().getStringExtra("courseStatus"); //add 1 for the list offset
            mentorName = getIntent().getStringExtra("mentorName");
            mentorPhone = getIntent().getStringExtra("mentorPhone");
            mentorEmail = getIntent().getStringExtra("mentorEmail");
            note = getIntent().getStringExtra("courseNotes");

            //populate the associated assessments
            DBHelper myHelper;
            myHelper = new DBHelper(CourseDetailsActivity.this); //create db
            myHelper.getWritableDatabase(); //create or open
            MainActivity.allAssessments = myHelper.getAllAssessments("SELECT * FROM assessments_tbl JOIN courses_tbl " +
                    "ON courseAssessmentId = courseId WHERE courseId = '"+id+"'");

            //setup the associated courses
            RecyclerView recyclerView2 = findViewById(R.id.assessments);
            final CourseDetailsAdapter adapter2 = new CourseDetailsAdapter(this);
            recyclerView2.setAdapter(adapter2);
            recyclerView2.setLayoutManager(new LinearLayoutManager(this));
            //update the list
            adapter2.setAssessmentWords(MainActivity.allAssessments);
        } else {
            setContentView(R.layout.activity_newcdetails); //which xml file for this activity
        }


        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{"In Progress", "Completed", "Dropped", "Plan to take"};

        switch (status) {
            case "In Progress": {
                spinnerSelection = 0;
                break;
            }
            case "Completed": {
                spinnerSelection = 1;
                break;
            }
            case "Dropped": {
                spinnerSelection = 2;
                break;
            }
            case "Plan to take": {
                spinnerSelection = 3;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(spinnerSelection);

        editTitle=findViewById(R.id.courseName);
        editTitle.setText(title);
        editStart=findViewById(R.id.courseStart);
        editStart.setText(start);
        editEnd=findViewById(R.id.courseEnd);
        editEnd.setText(end);
        editNotes=findViewById(R.id.courseNotes);
        editNotes.setText(note);

        editMentorName=findViewById(R.id.mentorName);
        editMentorName.setText(mentorName);
        editMentorPhone=findViewById(R.id.mentorPhone);
        editMentorPhone.setText(mentorPhone);
        editMentorEmail=findViewById(R.id.mentorEmail);
        editMentorEmail.setText(mentorEmail);

        editStart.setOnClickListener(v -> {
            startOrEndDateClicked = "start";
            new DatePickerDialog(CourseDetailsActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        editEnd.setOnClickListener(v -> {
            startOrEndDateClicked ="end";
            new DatePickerDialog(CourseDetailsActivity.this, date2, myCalendar2
                    .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                    myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
        });
    } //end onCreate

    public void saveButton(View view) throws ParseException {
        DBHelper myHelper;
        myHelper = new DBHelper(CourseDetailsActivity.this); //create db
        myHelper.getWritableDatabase(); //create or open
        Spinner dropdown = findViewById(R.id.spinner1);

        editTitle=findViewById(R.id.courseName);
        editStart=findViewById(R.id.courseStart);
        editEnd=findViewById(R.id.courseEnd);
        editNotes=findViewById(R.id.courseNotes);
        editMentorName=findViewById(R.id.mentorName);
        editMentorPhone=findViewById(R.id.mentorPhone);
        editMentorEmail=findViewById(R.id.mentorEmail);

        if (editStart.getText().toString().trim().length() == 0 || editEnd.getText().toString().trim().length() == 0 || editTitle.getText().toString().trim().length() == 0 ) {
            if (editMentorName.getText().toString().trim().length() == 0 || editMentorPhone.getText().toString().trim().length() == 0 || editMentorEmail.getText().toString().trim().length() == 0) {
                popupMessage(view, "Error: Please ensure all fields are populated.", "ERROR");
            }
        } else {

            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String str1 = editStart.getText().toString();
            Date date1 = sdf.parse(str1);

            String str2 = editEnd.getText().toString();
            Date date2 = sdf.parse(str2);

            if (date1.compareTo(date2)>0)
            {
                popupMessage(view, "Error: End date must be after start date. ", "ERROR");
            } else {
                if (id != -1) { //update an existing course
                    myHelper.updateCourse(id, courseTermId, "courseName", editTitle.getText().toString(),
                            "startDate", editStart.getText().toString(), "endTime", editEnd.getText().toString(), "status", dropdown.getSelectedItem().toString(), "mentorName",
                            editMentorName.getText().toString(), "mentorPhone", editMentorPhone.getText().toString(), "mentorEmail", editMentorEmail.getText().toString(), "consoleAlerts",
                            0, "note", editNotes.getText().toString(), "courseId = ?", new String[]{""+id});
                }
                else { //adding a new course
                    myHelper.addNewCourse(courseTermId, "courseName", editTitle.getText().toString(),
                            "startDate", editStart.getText().toString(), "endTime", editEnd.getText().toString(), "status", dropdown.getSelectedItem().toString(), "mentorName",
                            editMentorName.getText().toString(), "mentorPhone", editMentorPhone.getText().toString(), "mentorEmail", editMentorEmail.getText().toString(), "consoleAlerts",
                            0, "note", editNotes.getText().toString());
                }

                Intent intent;
                if (id == -1) {
                    intent = new Intent(CourseDetailsActivity.this, TermDetailsActivity.class);
                }
                else {
                    intent = new Intent(CourseDetailsActivity.this, CourseActivity.class);
                }
                final Term current = myHelper.getSingleTerm("SELECT * FROM terms_tbl WHERE termId = '"+courseTermId+"'");
                intent.putExtra("termId", current.getTermId());
                intent.putExtra("termTitle", current.getTitle());
                intent.putExtra("termStart", current.getStartDate());
                intent.putExtra("termEnd", current.getEndDate());
                intent.putExtra("termCurrent", current.getCurrent());

                startActivity(intent);
            }

        }
    }

}