package com.example.homework03_program1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class UpdateStudent extends AppCompatActivity {

    EditText et_j_firstName, et_j_lastName, et_j_username, et_j_email, et_j_age, et_j_gpa;
    TextView tv_j_username_value;
    Spinner sp_j_major;
    Button btn_j_addMajor, btn_j_back, btn_j_update;
    TextView tv_j_error_emptyFields;

    DatabaseHelper dbHelper;
    Major major;
    ArrayAdapter<String> adapter_sp_j_major;
    ArrayList<String> listOfMajors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        et_j_firstName = findViewById(R.id.US_et_v_firstName);
        et_j_lastName = findViewById(R.id.US_et_v_lastName);
        et_j_email = findViewById(R.id.US_et_v_email);
        et_j_age = findViewById(R.id.US_et_v_age);
        et_j_gpa = findViewById(R.id.US_et_v_gpa);

        sp_j_major = findViewById(R.id.US_sp_v_major);

        btn_j_addMajor = findViewById(R.id.US_btn_v_addMajor);
        btn_j_back = findViewById(R.id.US_btn_v_back);
        btn_j_update = findViewById(R.id.US_btn_v_update);

        tv_j_username_value = findViewById(R.id.US_tv_v_username_value);
        tv_j_error_emptyFields = findViewById(R.id.US_tv_v_error_emptyFields);

        dbHelper = new DatabaseHelper(this);

        tv_j_error_emptyFields.setVisibility(View.INVISIBLE);

        listOfMajors = new ArrayList<>();
        adapter_sp_j_major = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfMajors);
        sp_j_major.setAdapter(adapter_sp_j_major);
        updateMajorsList();

        fillInCurrentInfo();
        initButtonClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMajorsList();
    }

    private void initButtonClickListeners() {
        btn_j_addMajor.setOnClickListener(v -> startActivity(new Intent(UpdateStudent.this, AddMajor.class)));
        btn_j_back.setOnClickListener(v -> {
            clearAllEditTexts();
            finish();
        });
        btn_j_update.setOnClickListener(v -> updateStudent());
    }

    private void updateMajorsList() {
        listOfMajors.clear();
        listOfMajors.addAll(getMajorsList());
        listOfMajors.remove(0);
        adapter_sp_j_major.notifyDataSetChanged();
    }

    private void fillInCurrentInfo() {
        Student student = SessionData.getActiveStudent();

        String firstName = student.getFirstName();
        String lastName = student.getLastName();
        String username = student.getUsername();
        String email = student.getEmail();
        int age = student.getAge();
        double gpa = student.getGpa();
        major = student.getMajor();

        et_j_firstName.setText(firstName);
        et_j_lastName.setText(lastName);
        tv_j_username_value.setText(username);
        et_j_email.setText(email);
        et_j_age.setText(Integer.toString(age));
        et_j_gpa.setText(Double.toString(gpa));

        sp_j_major.setSelection(major.getId() - 1);
    }

    private void updateStudent() {
        Student student = SessionData.getActiveStudent();

        String firstName = et_j_firstName.getText().toString();
        String lastName = et_j_lastName.getText().toString();
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

        tv_j_error_emptyFields.setVisibility(View.INVISIBLE);

        if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && age != -1 && gpa != -1) {
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setEmail(email);
            student.setAge(age);
            student.setGpa(gpa);
            student.setMajor(major);

            dbHelper.updateStudentInDB(student);

            clearAllEditTexts();
            finish();
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
        et_j_email.setText("");
        et_j_age.setText("");
        et_j_gpa.setText("");
    }
}