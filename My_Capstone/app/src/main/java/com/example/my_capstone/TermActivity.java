package com.example.my_capstone;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TermActivity extends AppCompatActivity {


    public void addTerm(View view) {
        Intent intent= new Intent(TermActivity.this,TermDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tlist); //which xml file for this activity

        RecyclerView recyclerView = findViewById(R.id.allTerms);
        final TermAdapter adapter = new TermAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setWords(MainActivity.allTerms);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_tlist); //which xml file for this activity

        RecyclerView recyclerView = findViewById(R.id.allTerms);
        final TermAdapter adapter = new TermAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DBHelper myHelper;
        myHelper = new DBHelper(TermActivity.this); //create db
        myHelper.getWritableDatabase(); //create or open
        MainActivity.allTerms = myHelper.getAllTerms("SELECT * FROM terms_tbl");

        adapter.setWords(MainActivity.allTerms);
    }

}