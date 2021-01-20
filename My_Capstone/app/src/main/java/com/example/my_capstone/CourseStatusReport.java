package com.example.my_capstone;

import java.util.ArrayList;

public class CourseStatusReport extends Report {

    private String status;
    private String thirdColumnName = "Status";

    public CourseStatusReport(String dateGenerated, String title, String dataNameField, int dataIdField, String status) {
        super(dateGenerated, title, dataNameField, dataIdField);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public ArrayList<String> getTableColumns() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Id");
        columns.add("Name");
        columns.add(thirdColumnName);
        return columns;
    }

}
