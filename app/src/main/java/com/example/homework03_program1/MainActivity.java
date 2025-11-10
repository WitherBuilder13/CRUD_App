package com.example.homework03_program1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btn_j_addStudent, btn_j_findStudents, btn_j_addMajor;
    ListView lv_j_listOfStudents;
    StudentBasicAdapter adapter_lv_j_listOfStudents;
    DatabaseHelper dbHelper;
    ArrayList<Student> listOfStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_j_addStudent = findViewById(R.id.Main_btn_v_addStudent);
        btn_j_findStudents = findViewById(R.id.Main_btn_v_findStudents);
        btn_j_addMajor = findViewById(R.id.Main_btn_v_addMajor);

        lv_j_listOfStudents = findViewById(R.id.Main_lv_v_listOfStudents);

        dbHelper = new DatabaseHelper(this);
        dbHelper.addDummyMajor();

        listOfStudents = new ArrayList<>();

        adapter_lv_j_listOfStudents = new StudentBasicAdapter(this, listOfStudents);
        lv_j_listOfStudents.setAdapter(adapter_lv_j_listOfStudents);
        updateStudentsList();

        initButtonClickListeners();
        initListViewClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStudentsList();
    }

    private void initButtonClickListeners() {
        btn_j_addStudent.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddStudent.class)));
        btn_j_findStudents.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FindStudents.class)));
        btn_j_addMajor.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddMajor.class)));
    }

    private void initListViewClickListener() {
        lv_j_listOfStudents.setOnItemClickListener((parent, view, position, id) -> {
            ArrayList<Student> list = dbHelper.getAllStudents();
            SessionData.setActiveStudent(list.get(position));
            startActivity(new Intent(MainActivity.this, ViewStudent.class));
        });
        lv_j_listOfStudents.setOnItemLongClickListener((parent, view, position, id) -> {
            ArrayList<Student> list = dbHelper.getAllStudents();
            dbHelper.deleteStudent(list.get(position));
            updateStudentsList();
            return false;
        });
    }

    private void updateStudentsList() {
        listOfStudents.clear();
        listOfStudents.addAll(dbHelper.getAllStudents());
        adapter_lv_j_listOfStudents.notifyDataSetChanged();
    }
}