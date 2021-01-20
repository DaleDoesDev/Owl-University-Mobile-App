package com.example.my_capstone;

public class Course {

    private long courseId, courseTermId;
    private String name, startDate, endDate, status, mentorName, mentorPhone, mentorEmail, note;
    private int consoleAlerts;

    public Course(long courseId, long courseTermId, String name, String startDate, String endDate,
                  String status, String mentorName, String mentorPhone, String mentorEmail, String note, int consoleAlerts) {
        this.courseId = courseId;
        this.courseTermId = courseTermId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;
        this.consoleAlerts = consoleAlerts;
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getCourseTermId() {
        return courseTermId;
    }

    public void setCourseTermId(long courseTermId) {
        this.courseTermId = courseTermId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public String getMentorPhone() {
        return mentorPhone;
    }

    public void setMentorPhone(String mentorPhone) {
        this.mentorPhone = mentorPhone;
    }

    public String getMentorEmail() {
        return mentorEmail;
    }

    public void setMentorEmail(String mentorEmail) {
        this.mentorEmail = mentorEmail;
    }

    public int getConsoleAlerts() {
        return consoleAlerts;
    }

    public void setConsoleAlerts(int consoleAlerts) {
        this.consoleAlerts = consoleAlerts;
    }
}
