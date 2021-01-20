package com.example.my_capstone;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TermDetailsActivity extends AppCompatActivity {
    long id;
    EditText editTitle, editStart, editEnd;
    String title, start, end, startOrEndDateClicked;
    final Calendar myCalendar = Calendar.getInstance();

    private void updateLabel() throws ParseException {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (startOrEndDateClicked == "start") {
            editStart.setText(sdf.format(myCalendar.getTime()));
        }
        else editEnd.setText(sdf.format(myCalendar.getTime()));
    }

    public void addCourse(View view) {
        Intent intent= new Intent(TermDetailsActivity.this,CourseDetailsActivity.class);
        intent.putExtra("courseTermId", id);
        startActivity(intent);
    }

    public void saveButton(View view) throws ParseException {
        DBHelper myHelper;
        myHelper = new DBHelper(TermDetailsActivity.this); //create db
        myHelper.getWritableDatabase(); //create or open
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTitle=findViewById(R.id.termTitle);
        editStart=findViewById(R.id.termStart);
        editEnd=findViewById(R.id.termEnd);

        if (editTitle.getText().toString().trim().length() == 0 || editStart.getText().toString().trim().length() == 0 || editEnd.getText().toString().trim().length() == 0) {
            popupMessage(view, "Error: Please ensure all fields are populated.", "ERROR");
        }
        else {

            String str1 = editStart.getText().toString();
            Date date1 = sdf.parse(str1);

            String str2 = editEnd.getText().toString();
            Date date2 = sdf.parse(str2);

            if (date1.compareTo(date2)>0)
            {
                popupMessage(view, "Error: End date must be after start date. ", "ERROR");
            } else {
                if (id != -1) { //update an existing term
                    myHelper.runSQL("UPDATE terms_tbl SET termId = " + id + ", title = '" + editTitle.getText() + "', " +
                            "startDate = '" + editStart.getText() + "', endTime = '" + editEnd.getText() + "', current = 0 WHERE termId = '" + id + "'");
                } else { //adding a new term
                    myHelper.runSQL("INSERT INTO terms_tbl (termId, title, startDate, endTime, current) VALUES (null, '" + editTitle.getText() + "', '" + editStart.getText() + "', '" + editEnd.getText() + "', 0)");
                }

                Intent intent = new Intent(TermDetailsActivity.this, TermActivity.class);
                startActivity(intent);
            }
        }
    }

    public void deleteButton(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    if (MainActivity.allCourses.size() != 0) {
                        popupMessage(view, "Error: Term cannot be deleted before associated courses are removed.", "ERROR");
                    }
                    else {
                        DBHelper myHelper;
                        myHelper = new DBHelper(TermDetailsActivity.this); //create db
                        myHelper.getWritableDatabase(); //create or open
                        myHelper.runSQL("DELETE FROM terms_tbl WHERE termId = '"+id+"'");
                        popupMessage(view, "Term deleted", "Success");
                    }
                }
            }
        };
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("Are you sure you want to delete this Term?").setTitle("Confirmation")
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
                        Intent intent= new Intent(TermDetailsActivity.this,TermActivity.class);
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
        try {
            updateLabel();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id=getIntent().getLongExtra("termId", -1);
        if (id != -1) {
            setContentView(R.layout.activity_tdetails); //which xml file for this activity
            title = getIntent().getStringExtra("termTitle");
            start = getIntent().getStringExtra("termStart");
            end = getIntent().getStringExtra("termEnd");
            //populate the associated courses
            DBHelper myHelper;
            myHelper = new DBHelper(TermDetailsActivity.this); //create db
            myHelper.getWritableDatabase(); //create or open
            MainActivity.allCourses = myHelper.getAllCourses("SELECT * FROM courses_tbl JOIN terms_tbl " +
                    "ON courseTermId = termId WHERE termId = '"+id+"'");

            //setup the associated courses
            RecyclerView recyclerView = findViewById(R.id.associated);
            final TermDetailsAdapter adapter = new TermDetailsAdapter(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            //update the list
            adapter.setCourseWords(MainActivity.allCourses);
        } else {
            setContentView(R.layout.activity_newtdetails); //which xml file for this activity
        }

        editTitle=findViewById(R.id.termTitle);
        editTitle.setText(title);
        editStart=findViewById(R.id.termStart);
        editStart.setText(start);
        editEnd=findViewById(R.id.termEnd);
        editEnd.setText(end);

        editStart.setOnClickListener(v -> {
            startOrEndDateClicked = "start";
            new DatePickerDialog(TermDetailsActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        editEnd.setOnClickListener(v -> {
            startOrEndDateClicked ="end";
            new DatePickerDialog(TermDetailsActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    } //end onCreate

}