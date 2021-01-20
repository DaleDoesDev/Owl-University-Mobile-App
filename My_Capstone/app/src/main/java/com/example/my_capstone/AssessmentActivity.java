package com.example.my_capstone;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AssessmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alist); //which xml file for this activity

        RecyclerView recyclerView = findViewById(R.id.allAssessments);
        final AssessmentAdapter adapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setWords(MainActivity.allAssessments);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_alist); //which xml file for this activity

        RecyclerView recyclerView = findViewById(R.id.allAssessments);
        final AssessmentAdapter adapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DBHelper myHelper;
        myHelper = new DBHelper(AssessmentActivity.this); //create db
        myHelper.getWritableDatabase(); //create or open
        MainActivity.allAssessments = myHelper.getAllAssessments("SELECT * FROM assessments_tbl");

        adapter.setWords(MainActivity.allAssessments);
    }

}