package com.example.my_capstone;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clist); //which xml file for this activity

        RecyclerView recyclerView = findViewById(R.id.allCourses);
        final CourseAdapter adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setWords(MainActivity.allCourses);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_clist); //which xml file for this activity

        RecyclerView recyclerView = findViewById(R.id.allCourses);
        final CourseAdapter adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DBHelper myHelper;
        myHelper = new DBHelper(CourseActivity.this); //create db
        myHelper.getWritableDatabase(); //create or open
        MainActivity.allCourses = myHelper.getAllCourses("SELECT * FROM courses_tbl");

        adapter.setWords(MainActivity.allCourses);
    }

}