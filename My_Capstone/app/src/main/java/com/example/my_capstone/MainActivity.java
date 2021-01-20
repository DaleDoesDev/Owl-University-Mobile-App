package com.example.my_capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBHelper myHelper;

    public static boolean notLoggedIn = true;
    public static ArrayList<Term> allTerms = new ArrayList<>();
    public static ArrayList<Course> allCourses = new ArrayList<>();
    public static ArrayList<Assessment> allAssessments = new ArrayList<>();
    public static ArrayList<CourseStatusReport> allStatusReports = new ArrayList<>();
    public static ArrayList<AssessmentTypeReport> allTypeReports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (notLoggedIn == true)
        setContentView(R.layout.activity_login);
        else setContentView(R.layout.activity_main);

        myHelper = new DBHelper(MainActivity.this); //create db

        myHelper.getWritableDatabase(); //create or open
    }

    @Override
    protected void onPause() {
        super.onPause();
     //  myHelper.close(); //close the db connection
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            allTerms = myHelper.getAllTerms("SELECT * FROM terms_tbl");
            allCourses = myHelper.getAllCourses("SELECT * FROM courses_tbl");
            allAssessments = myHelper.getAllAssessments("SELECT * FROM assessments_tbl");
            allStatusReports = myHelper.getAllStatusReports("SELECT * FROM reports_tbl WHERE title = 'Courses, by status'");
            allTypeReports = myHelper.getAllTypeReports("SELECT * FROM reports_tbl WHERE title = 'Assessments, by type'");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void enterButton(View view) {
        Intent intent= new Intent(MainActivity.this,TermActivity.class);
        startActivity(intent);
    }

    public void loginButton(View view) {
        EditText password, username;
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        if (myHelper.queryForAnyResult("SELECT * FROM auth_tbl " +
                "WHERE username='"+username.getText().toString()+"' AND password='"+password.getText().toString()+"'") == true) {
            notLoggedIn = false;
            setContentView(R.layout.activity_main);
        } else {
            Context context = getBaseContext();
            CharSequence text = "Incorrect login information!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void enterCourseButton(View view) {
        Intent intent= new Intent(MainActivity.this,CourseActivity.class);
        startActivity(intent);
    }



    //this is a test for the app's database: add a new course, then update it's default note data.
    public void testButton(View view) {
        int duration = Toast.LENGTH_LONG;
        long testCourseId;
        Context context = getApplicationContext();

        if (myHelper.addNewCourse((long) 1, "courseName", "Test Course",
                "startDate", "2011-11-11", "endTime", "2012-12-12", "status", "Completed", "mentorName",
                "John Doe", "mentorPhone", "704-705-7066", "mentorEmail", "none@none.com",
                "consoleAlerts", 0, "note", "This is a test course.") != -1) {
            CharSequence text = "Test 1: SUCCESS. 'Test Course' added to database.";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Log.d("DBTest", "Test 1: SUCCESS. 'Test Course' added to database.");
        } else {
            CharSequence text = "Test 1: FAILED. Unable to add the 'Test Course' to the database.";
            Log.d("DBTest", "Test 1: FAILED. Unable to add the 'Test Course' to the database.");
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        testCourseId = myHelper.getSingleCourse("SELECT * FROM courses_tbl WHERE courseName = 'Test Course'").getCourseId();

        if (myHelper.updateCourse(testCourseId, (long) 1, "courseName", "Test Course",
                "startDate", "2011-11-11", "endTime", "2012-12-12", "status", "Completed", "mentorName",
                "John Doe", "mentorPhone", "704-705-7066", "mentorEmail", "none@none.com",
                "consoleAlerts", 0, "note", "The test course was updated.",
                "courseId = ?", new String[]{""+testCourseId}) != -1) {
            CharSequence text = "Test 2: SUCCESS. 'Test Course' note has been updated.";
            Toast toast = Toast.makeText(context, text, duration);
            Log.d("DBTest", "Test 2: SUCCESS. 'Test Course' note has been updated.");
            toast.show();
        } else {
            CharSequence text = "Test 2: FAILED. 'Test Course' note could not be updated.";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Log.d("DBTest", "Test 2: FAILED. 'Test Course' note could not be updated.");
        }
    }


    public void enterReportsButton(View view) {
        Intent intent= new Intent(MainActivity.this,ReportsActivity.class);
        startActivity(intent);
    }

    public void enterSearchButton(View view) {
        Intent intent= new Intent(MainActivity.this,SearchActivity.class);
        startActivity(intent);
    }

    public void enterAssessmentButton(View view) {
        Intent intent= new Intent(MainActivity.this,AssessmentActivity.class);
        startActivity(intent);
    }
}