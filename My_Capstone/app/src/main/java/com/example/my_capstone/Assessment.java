package com.example.my_capstone;

public class Assessment {

    private long assessmentId, courseAssessmentId;
    private String assessmentName, type, startDate, endDate;
    private int consoleAlerts;

    public Assessment(long assessmentId, long courseAssessmentId, String assessmentName, String type, String startDate, String endDate, int consoleAlerts) {
        this.assessmentId = assessmentId;
        this.courseAssessmentId = courseAssessmentId;
        this.assessmentName = assessmentName;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.consoleAlerts = consoleAlerts;
    }

    public long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public long getCourseAssessmentId() {
        return courseAssessmentId;
    }

    public void setCourseAssessmentId(long courseAssessmentId) {
        this.courseAssessmentId = courseAssessmentId;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getConsoleAlerts() {
        return consoleAlerts;
    }

    public void setConsoleAlerts(int consoleAlerts) {
        this.consoleAlerts = consoleAlerts;
    }
}
