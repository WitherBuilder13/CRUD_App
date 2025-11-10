package com.example.homework03_program1;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CRUD.db";
    private static final String STUDENTS_TABLE_NAME = "Students";
    private static final String MAJORS_TABLE_NAME = "Majors";
    public DatabaseHelper(Context c) {
        // increment version when database fundamentally changes
        super(c, DATABASE_NAME, null, 10);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + STUDENTS_TABLE_NAME + " (firstName varchar(50), lastName varchar(50), username varchar(50) primary key not null, " +
                "email varchar(50), age integer, gpa double, majorId integer, foreign key (majorId) references " + MAJORS_TABLE_NAME + " (majorId));");
        db.execSQL("CREATE TABLE " + MAJORS_TABLE_NAME + " (majorId integer primary key autoincrement not null, majorName varchar(50), majorPrefix varchar(50));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STUDENTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MAJORS_TABLE_NAME);

        // recreate tables
        onCreate(db);
    }

    public void addDummyMajor() {
        if (majorsTableIsEmpty()) {
            // INSERT INTO Majors (majorId,majorName, majorPrefix) VALUES ('0', 'Computer Science', 'CIS');
            String insert = "INSERT INTO " + MAJORS_TABLE_NAME + " (majorId,majorName,majorPrefix) VALUES ('0', 'N/A', 'N/A');";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(insert);
            db.close();
        }

    }

    public void addStudentToDB(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        // INSERT INTO Students (firstName, lastName, username, email, age, gpa, major) VALUES ('Jacob', 'Young', etc);
        String insertStudent = "INSERT INTO " + STUDENTS_TABLE_NAME + " (firstName, lastName, username, email, age, gpa, majorId) VALUES " +
                "('" + student.getFirstName() + "', '" + student.getLastName() + "', '" + student.getUsername() + "', '" + student.getEmail() + "', '" +
                student.getAge() + "', '" + student.getGpa() + "', '" + student.getMajor().getId() + "');";
        db.execSQL(insertStudent);
        db.close();
    }

    public void updateStudentInDB(Student student) {
        // UPDATE Students SET firstName = 'firstName', lastName = 'lastName', email = 'email', age = 'age', gpa = 'gpa', majorId = 'majorId' WHERE username = 'username';
        String update = "UPDATE " + STUDENTS_TABLE_NAME + " SET firstName = '" + student.getFirstName() + "', lastName = '" + student.getLastName() + "', email = '" + student.getEmail() +
                "', age = '" + student.getAge() + "', gpa = '" + student.getGpa() + "', majorId = '" + student.getMajor().getId() + "' WHERE username = '" + student.getUsername() + "';";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(update);
    }

    public void addMajorToDB(Major major) {
        // INSERT INTO Majors (majorName, majorPrefix) VALUES ('Computer Science', 'CIS');
        String insertMajor = "INSERT INTO " + MAJORS_TABLE_NAME + " (majorName, majorPrefix) VALUES ('" + major.getName() + "', '" + major.getPrefix() + "');";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(insertMajor);
        db.close();
    }

    public ArrayList<Student> getAllStudents() {
        // SELECT * FROM Students;
        String selectStatement = "SELECT * FROM " + STUDENTS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectStatement, null);
        ArrayList<Student> students = new ArrayList<>();

        addStudentsToListFromDB(cursor, students);

        return students;
    }

    public ArrayList<Major> getAllMajors() {
        // SELECT * FROM Majors;
        String selectStatement = "SELECT * FROM " + MAJORS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectStatement, null);
        ArrayList<Major> majors = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String prefix = cursor.getString(2);

                Major major = new Major();
                major.setId(id);
                major.setName(name);
                major.setPrefix(prefix);

                majors.add(major);
            } while (cursor.moveToNext());
        } else
            Log.d("Database", "0 rows");

        cursor.close();
        db.close();

        return majors;
    }

    public Major findMajor(int id) {
        if (majorExists(id)) {
            // SELECT * FROM Majors WHERE majorId = 'id';
            String selectStatement = "SELECT * FROM " + MAJORS_TABLE_NAME + " WHERE majorId = '" + id + "';";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectStatement, null);
            Major major = new Major();

            cursor.moveToFirst();
            int majorId = cursor.getInt(0);
            String name = cursor.getString(1);
            String prefix = cursor.getString(2);

            major.setId(majorId);
            major.setName(name);
            major.setPrefix(prefix);

            db.close();

            return major;
        }
        return null;
    }

    private boolean majorsTableIsEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();

        int numRows = (int) DatabaseUtils.queryNumEntries(db, MAJORS_TABLE_NAME);
        db.close();

        return numRows == 0;
    }

    public boolean majorExists(int id) {
        // SELECT COUNT(majorId) FROM Majors WHERE majorId = 'id';
        String selectStatement = "SELECT COUNT(majorId) FROM " + MAJORS_TABLE_NAME + " WHERE majorId = '" + id + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectStatement, null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        db.close();

        return count != 0;
    }

    public boolean majorComponentExists(String columnName, String value) {
        // SELECT COUNT(columnName) FROM Majors WHERE columnName = 'value';
        String query = "SELECT COUNT(" + columnName + ") FROM " + MAJORS_TABLE_NAME + " WHERE " + columnName + " = '" + value + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        db.close();

        return count != 0;
    }

    public boolean usernameExists(String username) {
        // SELECT COUNT(username) FROM Students WHERE username = 'username';
        String select = "SELECT COUNT(username) FROM " + STUDENTS_TABLE_NAME + " WHERE username = '" + username + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        db.close();

        return count != 0;
    }

    public ArrayList<Student> findStudents(String firstName, String lastName, String username, int majorId, double gpaMin, double gpaMax) {
        // SELECT * FROM Students WHERE firstName LIKE '%firstName%' && lastName LIKE '%lastName%';
        String baseSelect = "SELECT * FROM " + STUDENTS_TABLE_NAME + " WHERE firstName LIKE " +
                "'%" + firstName + "%' and lastName LIKE '%" + lastName + "%' and username LIKE '%" + username + "%' and gpa >= " + gpaMin + " and gpa <= " + gpaMax + " and majorId ";
        String fullSelect = baseSelect + (majorId == 0 ? "is not null" : "= " + majorId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(fullSelect, null);
        ArrayList<Student> foundStudents = new ArrayList<>();
        
        addStudentsToListFromDB(cursor, foundStudents);

        return foundStudents;
    }

    private void addStudentsToListFromDB(Cursor cursor, ArrayList<Student> students) {
        if (cursor.moveToFirst()) {
            do {
                String firstName = cursor.getString(0);
                String lastName = cursor.getString(1);
                String username = cursor.getString(2);
                String email = cursor.getString(3);
                int age = cursor.getInt(4);
                double gpa = cursor.getDouble(5);
                int majorId = cursor.getInt(6);

                Major major = findMajor(majorId);

                Student student = new Student(firstName, lastName, username, email, age, gpa, major);
                students.add(student);
            } while (cursor.moveToNext());
        }
    }

    public void deleteStudent(Student student) {
        // DELETE FROM Students WHERE username = 'username';
        String delete = "DELETE FROM " + STUDENTS_TABLE_NAME + " WHERE username = '" + student.getUsername() + "';";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(delete);
    }
}