package com.example.my_capstone;

import java.util.ArrayList;

public class AssessmentTypeReport extends Report {

    private String type;
    private String thirdColumnName = "Type";

    public AssessmentTypeReport(String dateGenerated, String title, String dataNameField, int dataIdField, String type) {
        super(dateGenerated, title, dataNameField, dataIdField);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
