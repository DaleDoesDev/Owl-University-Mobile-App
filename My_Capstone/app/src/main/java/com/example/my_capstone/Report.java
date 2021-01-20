package com.example.my_capstone;

import java.util.ArrayList;

public abstract class Report {

    private String dateGenerated;
    private String title;
    private String dataNameField;
    private int dataIdField;

    public Report(String dateGenerated, String title, String dataNameField, int dataIdField) {
        this.dateGenerated = dateGenerated;
        this.title = title;
        this.dataNameField = dataNameField;
        this.dataIdField = dataIdField;
    }

    public String getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(String dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataNameField() {
        return dataNameField;
    }

    public void setDataNameField(String dataNameField) {
        this.dataNameField = dataNameField;
    }

    public void setDataIdField(int dataIdField) {
        this.dataIdField = dataIdField;
    }

    public int getDataIdField() {
        return dataIdField;
    }

    //instance method to be overridden
    public ArrayList<String> getTableColumns() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Id");
        columns.add("Name");
        return columns;
    }
}
