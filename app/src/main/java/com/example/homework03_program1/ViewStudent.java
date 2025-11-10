package com.example.homework03_program1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewStudent extends AppCompatActivity {

    TextView tv_j_firstName_value, tv_j_lastName_value, tv_j_username_value, tv_j_email_value, tv_j_age_value, tv_j_gpa_value, tv_j_major_value;
    Button btn_j_back, btn_j_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_j_firstName_value = findViewById(R.id.VS_tv_v_firstName_value);
        tv_j_lastName_value = findViewById(R.id.VS_tv_v_lastName_value);
        tv_j_username_value = findViewById(R.id.VS_tv_v_username_value);
        tv_j_email_value = findViewById(R.id.VS_tv_v_email_value);
        tv_j_age_value = findViewById(R.id.VS_tv_v_age_value);
        tv_j_gpa_value = findViewById(R.id.VS_tv_v_gpa_value);
        tv_j_major_value = findViewById(R.id.VS_tv_v_major_value);

        btn_j_back = findViewById(R.id.VS_btn_v_back);
        btn_j_update = findViewById(R.id.VS_btn_v_update);

        initButtonClickListeners();
        fillInStudentInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillInStudentInfo();
    }

    private void initButtonClickListeners() {
        btn_j_back.setOnClickListener(v -> finish());
        btn_j_update.setOnClickListener(v -> startActivity(new Intent(ViewStudent.this, UpdateStudent.class)));
    }

    private void fillInStudentInfo() {
        Student student = SessionData.getActiveStudent();

        String firstName = student.getFirstName();
        String lastName = student.getLastName();
        String username = student.getUsername();
        String email = student.getEmail();
        int age = student.getAge();
        double gpa = student.getGpa();
        Major major = student.getMajor();
        String majorS = SessionData.majorFormat(major);

        tv_j_firstName_value.setText(firstName);
        tv_j_lastName_value.setText(lastName);
        tv_j_username_value.setText(username);
        tv_j_email_value.setText(email);
        tv_j_age_value.setText(Integer.toString(age));
        tv_j_gpa_value.setText(Double.toString(gpa));
        tv_j_major_value.setText(majorS);
    }
}