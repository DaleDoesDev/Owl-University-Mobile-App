package com.example.my_capstone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "capstoneApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "Main";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create all tables
        db.execSQL("CREATE TABLE IF NOT EXISTS terms_tbl (termId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, startDate DATE, endTime DATE, current INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS courses_tbl (courseId INTEGER PRIMARY KEY AUTOINCREMENT, courseTermId INTEGER, " +
                "courseName TEXT, startDate DATE, endTime DATE, status TEXT, mentorName TEXT, mentorPhone TEXT, mentorEmail TEXT, note TEXT, consoleAlerts INTEGER," +
                "FOREIGN KEY (courseTermId) REFERENCES terms_tbl(termId))");
        db.execSQL("CREATE TABLE IF NOT EXISTS assessments_tbl (assessmentId INTEGER PRIMARY KEY AUTOINCREMENT, courseAssessmentId INTEGER, " +
                "assessmentName TEXT, assessmentType TEXT, startDate DATE, endTime DATE, consoleAlert INTEGER, " +
                "FOREIGN KEY (courseAssessmentId) REFERENCES courses_tbl(courseId))");
        db.execSQL("CREATE TABLE IF NOT EXISTS auth_tbl (username TEXT,  password TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS reports_tbl (title TEXT,  dateGenerated DATE, dataName TEXT, dataId INTEGER, thirdCol TEXT)");


        //insert sample data: auth
        db.execSQL("INSERT INTO auth_tbl (username, password) VALUES ('admin', 'admin')");

        //insert sample data: terms
        db.execSQL("INSERT INTO terms_tbl (termId, title, startDate, endTime, current) VALUES (null, 'Fall 2020', '2020-08-30', '2020-12-29', 1)");
        db.execSQL("INSERT INTO terms_tbl (termId, title, startDate, endTime, current) VALUES (null, 'Spring 2021', '2021-01-26', '2021-04-29', 0)");
        db.execSQL("INSERT INTO terms_tbl (termId, title, startDate, endTime, current) VALUES (null, 'Summer 2021', '2021-05-12', '2021-07-29', 0)");

        //insert sample data: courses
        db.execSQL("INSERT INTO courses_tbl (courseId, courseTermId, courseName, startDate, endTime, status, mentorName, mentorPhone, mentorEmail, consoleAlerts, note) VALUES (null, 1, 'Mobile Development', '2020-11-26', '2020-11-29', 'In Progress', 'David Davidson', '704-867-5309', 'mentor.dave@owl.edu', 0, 'Example note')");
        db.execSQL("INSERT INTO courses_tbl (courseId, courseTermId, courseName, startDate, endTime, status, mentorName, mentorPhone, mentorEmail, consoleAlerts, note) VALUES (null, 2, 'Software II', '2020-10-10', '2020-10-29', 'Completed', 'Paul Paulson', '704-867-5555', 'mentor.paul@owl.edu', 0, 'Some example note')");

        //insert sample data assessments for courses
        db.execSQL("INSERT INTO assessments_tbl (assessmentId, courseAssessmentId, assessmentName, assessmentType,  startDate, endTime, consoleAlert) " +
                "VALUES (null, 1, 'Final Exam', 'Objective', '2020-08-30', '2020-12-29', 1)");
        db.execSQL("INSERT INTO assessments_tbl (assessmentId, courseAssessmentId, assessmentName, assessmentType,  startDate, endTime, consoleAlert) " +
                "VALUES (null, 1, 'Final Project', 'Performance', '2020-08-30', '2020-12-29', 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //using contentValues
    public long addNewCourse(Long courseTermId, String nameKey, String nameValue, String startKey, String startValue,
                          String endKey, String endValue, String statusKey, String statusValue, String mentorNameKey, String mentorNameValue,
                          String mentorPhoneKey, String mentorPhoneValue, String mentorEmailKey, String mentorEmailValue,
                          String consoleAlertKey, int consoleAlertValue, String noteKey, String noteValue) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put("courseId", (Long) null);

    if (courseTermId == null) {
        values.put("courseTermId", (Long) null);
    } else {
        values.put("courseTermId", courseTermId);
    }
    values.put(nameKey, nameValue);
    values.put(startKey, startValue);
    values.put(endKey, endValue);
    values.put(statusKey, statusValue);
    values.put(mentorNameKey, mentorNameValue);
    values.put(mentorPhoneKey, mentorPhoneValue);
    values.put(mentorEmailKey, mentorEmailValue);
    values.put(consoleAlertKey, consoleAlertValue);
    values.put(noteKey, noteValue);

    return db.insert("courses_tbl", null, values);
    }



    //update course with contentValues
    public int updateCourse(Long courseId, Long courseTermId, String nameKey, String nameValue, String startKey, String startValue,
                            String endKey, String endValue, String statusKey, String statusValue, String mentorNameKey, String mentorNameValue,
                            String mentorPhoneKey, String mentorPhoneValue, String mentorEmailKey, String mentorEmailValue,
                            String consoleAlertKey, int consoleAlertValue, String noteKey, String noteValue, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("courseId", courseId);
        values.put("courseTermId", courseTermId);
        values.put(nameKey, nameValue);
        values.put(startKey, startValue);
        values.put(endKey, endValue);
        values.put(statusKey, statusValue);
        values.put(mentorNameKey, mentorNameValue);
        values.put(mentorPhoneKey, mentorPhoneValue);
        values.put(mentorEmailKey, mentorEmailValue);
        values.put(consoleAlertKey, consoleAlertValue);
        values.put(noteKey, noteValue);

        return db.update("courses_tbl", values, whereClause, whereArgs);
    }




    public ArrayList<Term> getAllTerms(String sqlStatement) {
        ArrayList<Term> allTerms = new ArrayList<>();
        int id, current;
        String title, startDate, endDate;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);
        while(cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("termId"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            startDate = cursor.getString(cursor.getColumnIndex("startDate"));
            endDate = cursor.getString(cursor.getColumnIndex("endTime"));
            current = cursor.getInt(cursor.getColumnIndex("current"));

            //Log.d("Main", id+" "+title+" "+startDate+" "+endDate+" "+current);
            allTerms.add(new Term(id, title, startDate, endDate, current));
        }
        return allTerms;
    }

    public Term getSingleTerm(String sqlStatement) {
        int id, current;
        String title, startDate, endDate;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);

        while(cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("termId"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            startDate = cursor.getString(cursor.getColumnIndex("startDate"));
            endDate = cursor.getString(cursor.getColumnIndex("endTime"));
            current = cursor.getInt(cursor.getColumnIndex("current"));
            return new Term(id, title, startDate, endDate, current);
        }
        return null;
    }

    public Course getSingleCourse(String sqlStatement) {
        int id, courseTermId, consoleAlerts;
        String name, startDate, endDate, status, mentorName, mentorPhone, mentorEmail, note;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);

        while(cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("courseId"));
            courseTermId = cursor.getInt(cursor.getColumnIndex("courseTermId"));
            name = cursor.getString(cursor.getColumnIndex("courseName"));
            startDate = cursor.getString(cursor.getColumnIndex("startDate"));
            endDate = cursor.getString(cursor.getColumnIndex("endTime"));
            status = cursor.getString(cursor.getColumnIndex("status"));
            mentorName = cursor.getString(cursor.getColumnIndex("mentorName"));
            mentorPhone = cursor.getString(cursor.getColumnIndex("mentorPhone"));
            mentorEmail = cursor.getString(cursor.getColumnIndex("mentorEmail"));
            note = cursor.getString(cursor.getColumnIndex("note"));
            consoleAlerts = cursor.getInt(cursor.getColumnIndex("consoleAlerts"));

            return new Course(id, courseTermId, name, startDate, endDate,
                    status, mentorName, mentorPhone, mentorEmail, note, consoleAlerts);
        }
        return null;
    }

    public ArrayList<Assessment> getAllAssessments(String sqlStatement) {
        ArrayList<Assessment> allAssessments = new ArrayList<>();
        int id, courseAssessmentId, consoleAlert;
        String name, type, startDate, endDate;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);
        while(cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("assessmentId"));
            courseAssessmentId = cursor.getInt(cursor.getColumnIndex("courseAssessmentId"));
            name = cursor.getString(cursor.getColumnIndex("assessmentName"));
            type = cursor.getString(cursor.getColumnIndex("assessmentType"));
            startDate = cursor.getString(cursor.getColumnIndex("startDate"));
            endDate = cursor.getString(cursor.getColumnIndex("endTime"));
            consoleAlert = cursor.getInt(cursor.getColumnIndex("consoleAlert"));

            allAssessments.add(new Assessment(id,courseAssessmentId, name, type, startDate, endDate, consoleAlert));
        }
        return allAssessments;
    }

    //this method loads the stored report data from the db for the status reports.
    public ArrayList<CourseStatusReport> getAllStatusReports(String sqlStatement) {
        String name, thirdCol, title, dateGenerated;
        int dataId;
        ArrayList<CourseStatusReport> reports = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);
        while (cursor.moveToNext()) {
            dateGenerated = cursor.getString(cursor.getColumnIndex("dateGenerated"));
            name = cursor.getString(cursor.getColumnIndex("dataName"));
            thirdCol = cursor.getString(cursor.getColumnIndex("thirdCol"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            dataId = cursor.getInt(cursor.getColumnIndex("dataId"));
            reports.add(new CourseStatusReport(dateGenerated, title, name, dataId, thirdCol));
        }

        return reports;
    }

    //this method loads the stored report data from the db for the type reports.
    public ArrayList<AssessmentTypeReport> getAllTypeReports(String sqlStatement) {
        String name, thirdCol, title, dateGenerated;
        int dataId;
        ArrayList<AssessmentTypeReport> reports = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);
        while (cursor.moveToNext()) {
            dateGenerated = cursor.getString(cursor.getColumnIndex("dateGenerated"));
            name = cursor.getString(cursor.getColumnIndex("dataName"));
            thirdCol = cursor.getString(cursor.getColumnIndex("thirdCol"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            dataId = cursor.getInt(cursor.getColumnIndex("dataId"));
            reports.add(new AssessmentTypeReport(dateGenerated, title, name, dataId, thirdCol));
        }

        return reports;
    }

    //this method queries the courses_tbl to build a report ArrayList. It doesn't load report DB data
    public ArrayList<CourseStatusReport> createReportOneCourses(String sqlStatement) {
        ArrayList<CourseStatusReport> courses = new ArrayList<>();
        String name, status;
        int id;
        String title = "Courses, by status";
        String dateGenerated;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);
        while(cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex("courseName"));
            id = cursor.getInt(cursor.getColumnIndex("courseId"));
            status = cursor.getString(cursor.getColumnIndex("status"));
            courses.add(new CourseStatusReport(null, title, name, id, status));

        }
        return courses;
    }

    //this method queries the assessments_tbl to build a report ArrayList. It doesn't load assessment DB data
    public ArrayList<AssessmentTypeReport> createReportTwoAssessments(String sqlStatement) {
        ArrayList<AssessmentTypeReport> assessments = new ArrayList<>();
        String name, type;
        int id;
        String title = "Assessments, by type";
        String dateGenerated;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);
        while(cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex("assessmentName"));
            id = cursor.getInt(cursor.getColumnIndex("assessmentId"));
            type = cursor.getString(cursor.getColumnIndex("assessmentType"));
            assessments.add(new AssessmentTypeReport(null, title, name, id, type));
        }
        return assessments;
    }

    public ArrayList<SearchResult> searchMentors(String sqlStatement) {
        ArrayList<SearchResult> results = new ArrayList<>();
        String name, phone, email;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);

        while(cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex("mentorName"));
            phone = cursor.getString(cursor.getColumnIndex("mentorPhone"));
            email = cursor.getString(cursor.getColumnIndex("mentorEmail"));
            results.add(new SearchResult(name, phone, email));
        }
        return results;
    }

    public ArrayList<Course> getAllCourses(String sqlStatement) {
        ArrayList<Course> allCourses = new ArrayList<>();
        int id, courseTermId, consoleAlerts;
        String name, startDate, endDate, mentorName, mentorPhone, mentorEmail, status, note;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);
        while(cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("courseId"));
            courseTermId = cursor.getInt(cursor.getColumnIndex("courseTermId"));
            name = cursor.getString(cursor.getColumnIndex("courseName"));
            startDate = cursor.getString(cursor.getColumnIndex("startDate"));
            endDate = cursor.getString(cursor.getColumnIndex("endTime"));
            status = cursor.getString(cursor.getColumnIndex("status"));
            mentorName = cursor.getString(cursor.getColumnIndex("mentorName"));
            mentorPhone = cursor.getString(cursor.getColumnIndex("mentorPhone"));
            mentorEmail = cursor.getString(cursor.getColumnIndex("mentorEmail"));
            note = cursor.getString(cursor.getColumnIndex("note"));
            consoleAlerts = cursor.getInt(cursor.getColumnIndex("consoleAlerts"));

            allCourses.add(new Course(id, courseTermId, name, startDate, endDate,
                    status, mentorName, mentorPhone, mentorEmail, note, consoleAlerts));
        }
        return allCourses;
    }

    public void runSQL(String sqlStatement) {
        this.getWritableDatabase().execSQL(sqlStatement);
    }

    public boolean queryForAnyResult(String sqlStatement) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);
        if(cursor.moveToNext())
            return true;
        else return false;
    }

}
