package com.example.homework03_program1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddMajor extends AppCompatActivity {

    EditText et_j_name, et_j_prefix;
    Button btn_j_back, btn_j_add;
    TextView tv_j_error_nameExists, tv_j_error_prefixExists, tv_j_error_emptyFields;
    DatabaseHelper dbHelper;
    //Class<?> classToReturnTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_major);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_j_name = findViewById(R.id.AM_et_v_name);
        et_j_prefix = findViewById(R.id.AM_et_v_prefix);

        btn_j_back = findViewById(R.id.AM_btn_v_back);
        btn_j_add = findViewById(R.id.AM_btn_v_add);

        tv_j_error_nameExists = findViewById(R.id.AM_tv_v_error_nameExists);
        tv_j_error_prefixExists = findViewById(R.id.AM_tv_v_error_prefixExists);
        tv_j_error_emptyFields = findViewById(R.id.AM_tv_v_error_emptyFields);

        dbHelper = new DatabaseHelper(this);

        tv_j_error_nameExists.setVisibility(View.INVISIBLE);
        tv_j_error_prefixExists.setVisibility(View.INVISIBLE);
        tv_j_error_emptyFields.setVisibility(View.INVISIBLE);

        initButtonClickListeners();
    }

    private void initButtonClickListeners() {
        btn_j_back.setOnClickListener(v -> {
            clearEditTexts();
            finish();
        });
        btn_j_add.setOnClickListener(v -> addMajor());
    }

    private void addMajor() {
        String majorName = et_j_name.getText().toString(), majorPrefix = et_j_prefix.getText().toString();

        boolean nameExists = dbHelper.majorComponentExists("majorName", majorName), prefixExists = dbHelper.majorComponentExists("majorPrefix", majorPrefix);

        tv_j_error_nameExists.setVisibility(View.INVISIBLE);
        tv_j_error_prefixExists.setVisibility(View.INVISIBLE);
        tv_j_error_emptyFields.setVisibility(View.INVISIBLE);

        if (nameExists)
            tv_j_error_nameExists.setVisibility(View.VISIBLE);
        if (prefixExists)
            tv_j_error_prefixExists.setVisibility(View.VISIBLE);

        if (!majorName.isEmpty() && !nameExists && !majorPrefix.isEmpty() && !prefixExists) {
            Major major = new Major();

            major.setName(majorName);
            major.setPrefix(majorPrefix);

            dbHelper.addMajorToDB(major);

            clearEditTexts();
            finish();
        } else
            tv_j_error_emptyFields.setVisibility(View.VISIBLE);
    }

    private void clearEditTexts() {
        et_j_name.setText("");
        et_j_prefix.setText("");
    }
}