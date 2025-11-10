package com.example.homework03_program1;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class FindStudents extends AppCompatActivity {

    EditText et_j_firstName, et_j_lastName, et_j_username, et_j_gpaMin, et_j_gpaMax;
    Spinner sp_j_major;
    ListView lv_j_foundStudents;
    Button btn_j_back;

    DatabaseHelper dbHelper;
    ArrayAdapter<String> adapter_sp_j_major;
    ArrayList<String> listOfMajors;
    StudentSearchAdapter adapter_lv_j_foundStudents;
    ArrayList<Student> listOfStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_students);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_j_firstName = findViewById(R.id.FS_et_v_firstName);
        et_j_lastName = findViewById(R.id.FS_et_v_lastName);
        et_j_username = findViewById(R.id.FS_et_v_username);
        et_j_gpaMin = findViewById(R.id.FS_et_v_gpaMin);
        et_j_gpaMax = findViewById(R.id.FS_et_v_gpaMax);

        sp_j_major = findViewById(R.id.FS_sp_v_major);

        lv_j_foundStudents = findViewById(R.id.FS_lv_v_foundStudents);

        btn_j_back = findViewById(R.id.FS_btn_v_back);

        dbHelper = new DatabaseHelper(this);

        listOfMajors = getMajorsList();
        listOfStudents = new ArrayList<>();

        adapter_sp_j_major = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfMajors);
        sp_j_major.setAdapter(adapter_sp_j_major);

        adapter_lv_j_foundStudents = new StudentSearchAdapter(this, listOfStudents);
        lv_j_foundStudents.setAdapter(adapter_lv_j_foundStudents);

        initButtonClickListener();
        initTextChangeListeners();
        initSpinnerChangeListener();
        initListViewClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        findStudents();
    }

    private void initButtonClickListener() {
        btn_j_back.setOnClickListener(v -> {
            clearEditTexts();
            finish();
        });
    }

    private void initTextChangeListeners() {
        et_j_lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                findStudents();
            }
        });
        et_j_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                findStudents();
            }
        });
        et_j_gpaMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                findStudents();
            }
        });
        et_j_gpaMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                findStudents();
            }
        });
        et_j_firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                findStudents();
            }
        });
    }

    private void initSpinnerChangeListener() {
        sp_j_major.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                findStudents();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void initListViewClickListener() {
        lv_j_foundStudents.setOnItemClickListener((parent, view, position, id) -> {
            SessionData.setActiveStudent(listOfStudents.get(position));
            startActivity(new Intent(FindStudents.this, ViewStudent.class));
        });
        lv_j_foundStudents.setOnItemLongClickListener((parent, view, position, id) -> {
            dbHelper.deleteStudent(listOfStudents.get(position));
            findStudents();
            return false;
        });
    }

    private void findStudents() {
        String firstName = et_j_firstName.getText().toString();
        String lastName = et_j_lastName.getText().toString();
        String username = et_j_username.getText().toString();
        String gpaMinS = et_j_gpaMin.getText().toString();
        String gpaMaxS = et_j_gpaMax.getText().toString();
        int majorId = sp_j_major.getSelectedItemPosition();
        double gpaMin, gpaMax;

        if (gpaMinS.isEmpty())
            gpaMin = 0;
        else
            gpaMin = Double.parseDouble(et_j_gpaMin.getText().toString());
        if (gpaMaxS.isEmpty())
            gpaMax = 100;
        else
            gpaMax = Double.parseDouble(et_j_gpaMax.getText().toString());

        listOfStudents.clear();
        listOfStudents.addAll(dbHelper.findStudents(firstName, lastName, username, majorId, gpaMin, gpaMax));
        adapter_lv_j_foundStudents.notifyDataSetChanged();
    }

    private ArrayList<String> getMajorsList() {
        ArrayList<Major> allMajors = dbHelper.getAllMajors();

        ArrayList<String> majorsList = new ArrayList<>();
        for (Major major : allMajors) {
            majorsList.add(SessionData.majorFormat(major));
        }

        return majorsList;
    }

    private void clearEditTexts() {
        et_j_firstName.setText("");
        et_j_lastName.setText("");
        et_j_username.setText("");
        et_j_gpaMin.setText("");
        et_j_gpaMax.setText("");
    }
}