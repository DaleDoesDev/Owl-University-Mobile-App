package com.example.my_capstone;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private int dropdownSelection;
    private TextView title, date;
    private static final String[] paths = {"   1", "   2"};
    private static ArrayList<CourseStatusReport> allStatusReports = MainActivity.allStatusReports;
    private static ArrayList<AssessmentTypeReport> allTypeReports = MainActivity.allTypeReports;
    private static ArrayList<String> columns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> starter = new ArrayList<>();
        starter.add("Id");
        starter.add("Name");
        starter.add("-");

        columns = starter;

        setContentView(R.layout.activity_reports); //which xml file for this activity
        //columns = allStatusReports.get(0).getTableColumns(); //start with status report's columns as a default
        title = findViewById(R.id.title);

        TableLayout tableData = (TableLayout)findViewById(R.id.simpleTableLayout);
        tableData.setStretchAllColumns(true);
        tableData.bringToFront();

        prepTable(); //initial prep with the above starter array

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(ReportsActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        date = findViewById(R.id.date);
        TableLayout tableData = (TableLayout)findViewById(R.id.simpleTableLayout);
        DBHelper myHelper;
        myHelper = new DBHelper(ReportsActivity.this); //create db
        myHelper.getWritableDatabase(); //create or open
        allStatusReports = myHelper.getAllStatusReports("SELECT * FROM reports_tbl WHERE title = 'Courses, by status'");
        allTypeReports = myHelper.getAllTypeReports("SELECT * FROM reports_tbl WHERE title = 'Assessments, by type'");

        if (tableData.getChildCount() > 1) //ensure the remove doesn't run early
            tableData.removeViews(1, tableData.getChildCount()-1); //clear the table first

        switch (position) {
            case 0:
                dropdownSelection = 0;
                title.setText("Courses, by status");
                if (myHelper.queryForAnyResult("SELECT * FROM reports_tbl WHERE title = 'Courses, by status'") == true) {
                    date.setText(allStatusReports.get(0).getDateGenerated());

                    columns = allStatusReports.get(0).getTableColumns(); //call overridden obj method
                    prepTable();
                    for(int i = 0; i < allStatusReports.size(); i++) {
                        TableRow tr =  new TableRow(this);
                        TextView c1 = (TextView)getLayoutInflater().inflate(R.layout.row_template_two, null);
                        c1.setText(""+allStatusReports.get(i).getDataIdField());
                        TextView c2 = (TextView)getLayoutInflater().inflate(R.layout.row_template, null);
                        c2.setText(allStatusReports.get(i).getDataNameField());
                        TextView c3 = (TextView)getLayoutInflater().inflate(R.layout.row_template_two, null);
                        c3.setText(String.valueOf(allStatusReports.get(i).getStatus()));
                        tr.addView(c1);
                        tr.addView(c2);
                        tr.addView(c3);
                        tableData.addView(tr);
                    }
                } else {
                    date.setText(null);
                }
                break;
            case 1:
                dropdownSelection = 1;
                title.setText("Assessments, by type");
                if (myHelper.queryForAnyResult("SELECT * FROM reports_tbl WHERE title = 'Assessments, by type'") == true) {
                    date.setText(allTypeReports.get(0).getDateGenerated());

                    columns = allTypeReports.get(0).getTableColumns(); //call overridden obj method
                    prepTable();
                    for(int i = 0; i < allTypeReports.size(); i++) {
                        TableRow tr =  new TableRow(this);
                        TextView c1 = (TextView)getLayoutInflater().inflate(R.layout.row_template_two, null);
                        c1.setText(""+allTypeReports.get(i).getDataIdField());
                        TextView c2 = (TextView)getLayoutInflater().inflate(R.layout.row_template, null);
                        c2.setText(allTypeReports.get(i).getDataNameField());
                        TextView c3 = (TextView)getLayoutInflater().inflate(R.layout.row_template_two, null);
                        c3.setText(String.valueOf(allTypeReports.get(i).getType()));
                        tr.addView(c1);
                        tr.addView(c2);
                        tr.addView(c3);
                        tableData.addView(tr);
                    }
                } else {
                    date.setText(null);

                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    public void generateButton(View view) {
        TableLayout tableData = (TableLayout)findViewById(R.id.simpleTableLayout);
        createTimestamp();
        DBHelper myHelper;
        myHelper = new DBHelper(ReportsActivity.this); //create db
        myHelper.getWritableDatabase(); //create or open

        if (dropdownSelection == 0) {
                allStatusReports = myHelper.createReportOneCourses("SELECT * FROM courses_tbl ORDER BY status");
                if (tableData.getChildCount() > 1) //ensure the remove doesn't run early
                    tableData.removeViews(1, tableData.getChildCount()-1); //clear the table first
                columns = allStatusReports.get(0).getTableColumns(); //call overridden obj method
                prepTable();
                generateStatusReports();
            } else {
                    allTypeReports = myHelper.createReportTwoAssessments("SELECT * FROM assessments_tbl ORDER BY assessmentType");
                    if (tableData.getChildCount() > 1) //ensure the remove doesn't run early
                        tableData.removeViews(1, tableData.getChildCount()-1); //clear the table first
                    columns = allTypeReports.get(0).getTableColumns(); //call other overridden obj method
                    prepTable();
                    generateTypeReports();
            }
    }

    public void prepTable() {
        TableLayout tableData = (TableLayout)findViewById(R.id.simpleTableLayout);
        //set 1st column
        TextView first;
        first = findViewById(R.id.firstColumn);
        first.setText(columns.get(0));
        //set 2nd column
        TextView second;
        second = findViewById(R.id.secondColumn);
        second.setText(columns.get(1));
        //set 3rd column
        TextView third;
        third = findViewById(R.id.thirdColumn);
        third.setText(columns.get(2));
    }

    public void createTimestamp() {
        date = findViewById(R.id.date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        date.setText(sdf.format(new Date()));
    }

    public void generateStatusReports() {
        TableLayout tableData = (TableLayout)findViewById(R.id.simpleTableLayout);
        DBHelper myHelper;
        myHelper = new DBHelper(ReportsActivity.this); //create db
        myHelper.getWritableDatabase(); //create or open
        date = findViewById(R.id.date);
        myHelper.runSQL("DELETE FROM reports_tbl WHERE title= 'Courses, by status'");

        for(int i = 0; i < allStatusReports.size(); i++) {
            myHelper.runSQL("INSERT INTO reports_tbl (title, dateGenerated, dataName, dataId, thirdCol)" +
                    "VALUES ('Courses, by status', '"+date.getText()+"', '"+allStatusReports.get(i).getDataNameField()+"', " +
                    "'"+allStatusReports.get(i).getDataIdField()+"', '"+allStatusReports.get(i).getStatus()+"')");
            TableRow tr =  new TableRow(this);
            TextView c1 = (TextView)getLayoutInflater().inflate(R.layout.row_template_two, null);
            c1.setText(""+allStatusReports.get(i).getDataIdField());
            TextView c2 = (TextView)getLayoutInflater().inflate(R.layout.row_template, null);
            c2.setText(allStatusReports.get(i).getDataNameField());
            TextView c3 = (TextView)getLayoutInflater().inflate(R.layout.row_template_two, null);
            c3.setText(String.valueOf(allStatusReports.get(i).getStatus()));
            tr.addView(c1);
            tr.addView(c2);
            tr.addView(c3);
            tableData.addView(tr);
        }
    }

    public void generateTypeReports() {
        TableLayout tableData = (TableLayout)findViewById(R.id.simpleTableLayout);
        DBHelper myHelper;
        myHelper = new DBHelper(ReportsActivity.this); //create db
        myHelper.getWritableDatabase(); //create or open
        date = findViewById(R.id.date);
        myHelper.runSQL("DELETE FROM reports_tbl WHERE title= 'Assessments, by type'");

        for(int i = 0; i < allTypeReports.size(); i++) {
            myHelper.runSQL("INSERT INTO reports_tbl (title, dateGenerated, dataName, dataId, thirdCol)" +
                    "VALUES ('Assessments, by type', '"+date.getText()+"', '"+allTypeReports.get(i).getDataNameField()+"', " +
                    "'"+allTypeReports.get(i).getDataIdField()+"', '"+allTypeReports.get(i).getType()+"')");
            TableRow tr =  new TableRow(this);
            TextView c1 = (TextView)getLayoutInflater().inflate(R.layout.row_template_two, null);
            c1.setText(""+allTypeReports.get(i).getDataIdField());
            TextView c2 = (TextView)getLayoutInflater().inflate(R.layout.row_template, null);
            c2.setText(allTypeReports.get(i).getDataNameField());
            TextView c3 = (TextView)getLayoutInflater().inflate(R.layout.row_template_two, null);
            c3.setText(String.valueOf(allTypeReports.get(i).getType()));
            tr.addView(c1);
            tr.addView(c2);
            tr.addView(c3);
            tableData.addView(tr);
        }
    }

}