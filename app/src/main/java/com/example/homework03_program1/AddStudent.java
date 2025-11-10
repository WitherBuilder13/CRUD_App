package com.example.homework03_program1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AddStudent extends AppCompatActivity {

    EditText et_j_firstName, et_j_lastName, et_j_username, et_j_email, et_j_age, et_j_gpa;
    Spinner sp_j_major;
    Button btn_j_addMajor, btn_j_back, btn_j_add;
    TextView tv_j_error_usernameExists, tv_j_error_emptyFields;

    DatabaseHelper dbHelper;
    Major major;
    ArrayAdapter<String> adapter_sp_j_major;
    ArrayList<String> listOfMajors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_j_firstName = findViewById(R.id.AS_et_v_firstName);
        et_j_lastName = findViewById(R.id.AS_et_v_lastName);
        et_j_username = findViewById(R.id.AS_et_v_username);
        et_j_email = findViewById(R.id.AS_et_v_email);
        et_j_age = findViewById(R.id.AS_et_v_age);
        et_j_gpa = findViewById(R.id.AS_et_v_gpa);

        sp_j_major = findViewById(R.id.AS_sp_v_major);

        btn_j_addMajor = findViewById(R.id.AS_btn_v_addMajor);
        btn_j_back = findViewById(R.id.AS_btn_v_back);
        btn_j_add = findViewById(R.id.AS_btn_v_add);

        tv_j_error_usernameExists = findViewById(R.id.AS_tv_v_error_usernameExists);
        tv_j_error_emptyFields = findViewById(R.id.AS_tv_v_error_emptyFields);

        dbHelper = new DatabaseHelper(this);

        tv_j_error_usernameExists.setVisibility(View.INVISIBLE);
        tv_j_error_emptyFields.setVisibility(View.INVISIBLE);

        listOfMajors = new ArrayList<>();

        adapter_sp_j_major = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfMajors);
        sp_j_major.setAdapter(adapter_sp_j_major);
        updateMajorsList();

        initButtonClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMajorsList();
    }

    private void initButtonClickListeners() {
        btn_j_addMajor.setOnClickListener(v -> startActivity(new Intent(AddStudent.this, AddMajor.class)));
        btn_j_back.setOnClickListener(v -> {
            clearAllEditTexts();
            finish();
        });
        btn_j_add.setOnClickListener(v -> addStudent());
    }

    private void updateMajorsList() {
        listOfMajors.clear();
        listOfMajors.addAll(getMajorsList());
        listOfMajors.remove(0);
        adapter_sp_j_major.notifyDataSetChanged();
    }

    private void addStudent() {
        String firstName = et_j_firstName.getText().toString();
        String lastName = et_j_lastName.getText().toString();
        String username = et_j_username.getText().toString();
        String email = et_j_email.getText().toString();

        String ageS = et_j_age.getText().toString();
        String gpaS = et_j_gpa.getText().toString();

        major = dbHelper.findMajor(sp_j_major.getSelectedItemPosition() + 1);

        int age;
        double gpa;

        if (!ageS.isEmpty())
            age = Integer.parseInt(ageS);
        else
            age = -1;
        if (!gpaS.isEmpty())
            gpa = Double.parseDouble(gpaS);
        else
            gpa = -1;

        tv_j_error_usernameExists.setVisibility(View.INVISIBLE);

        if (dbHelper.usernameExists(username))
            tv_j_error_usernameExists.setVisibility(View.VISIBLE);

        if (!firstName.isEmpty() && !lastName.isEmpty() && !username.isEmpty() && !email.isEmpty() && age != -1 && gpa != -1) {
            if (!dbHelper.usernameExists(username)) {
                Student student = new Student(firstName, lastName, username, email, age, gpa, major);
                dbHelper.addStudentToDB(student);

                clearAllEditTexts();
                finish();
            }
        } else
            tv_j_error_emptyFields.setVisibility(View.VISIBLE);
    }

    private ArrayList<String> getMajorsList() {
        ArrayList<Major> allMajors = dbHelper.getAllMajors();

        ArrayList<String> majorsList = new ArrayList<>();
        for (Major major : allMajors) {
            majorsList.add(SessionData.majorFormat(major));
        }

        return majorsList;
    }

    private void clearAllEditTexts() {
        et_j_firstName.setText("");
        et_j_lastName.setText("");
        et_j_username.setText("");
        et_j_email.setText("");
        et_j_age.setText("");
        et_j_gpa.setText("");
    }
}