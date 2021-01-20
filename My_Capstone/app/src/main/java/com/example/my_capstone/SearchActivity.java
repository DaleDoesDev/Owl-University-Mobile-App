package com.example.my_capstone;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private static ArrayList<SearchResult> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search); //which xml file for this activity

        TableLayout tableData = (TableLayout)findViewById(R.id.simpleTableLayout);
        tableData.setStretchAllColumns(true);
        tableData.bringToFront();

        setupSearch();
    }

    public void setupSearch() {
        final EditText edittext = (EditText) findViewById(R.id.searchField);
        edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(edittext.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    public void search(String str) {
        TableLayout tableData = (TableLayout)findViewById(R.id.simpleTableLayout);
        DBHelper myHelper;
        myHelper = new DBHelper(SearchActivity.this); //create db
        myHelper.getWritableDatabase(); //create or open

        searchResults = myHelper.searchMentors("SELECT mentorName, mentorPhone, mentorEmail FROM courses_tbl WHERE mentorName LIKE '%"+str+"%'");
        if (tableData.getChildCount() > 1) //ensure the remove doesn't run early
            tableData.removeViews(1, tableData.getChildCount()-1); //clear the table first
        generateSearchResults();
    }

    public void generateSearchResults() {
        TableLayout tableData = (TableLayout)findViewById(R.id.simpleTableLayout);
        DBHelper myHelper;
        myHelper = new DBHelper(SearchActivity.this); //create db
        myHelper.getWritableDatabase(); //create or open

        for(int i = 0; i < searchResults.size(); i++) {
            TableRow tr =  new TableRow(this);
            TextView c1 = (TextView)getLayoutInflater().inflate(R.layout.row_template_two, null);
            c1.setText(""+searchResults.get(i).getName());
            TextView c2 = (TextView)getLayoutInflater().inflate(R.layout.row_template, null);
            c2.setText(searchResults.get(i).getPhone());
            TextView c3 = (TextView)getLayoutInflater().inflate(R.layout.row_template_two, null);
            c3.setText(String.valueOf(searchResults.get(i).getEmail()));
            tr.addView(c1);
            tr.addView(c2);
            tr.addView(c3);
            tableData.addView(tr);
        }
    }

}