package com.example.homework03_program1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentBasicAdapter extends BaseAdapter {

    Context context;
    ArrayList<Student> studentArrayList;

    public StudentBasicAdapter(Context c, ArrayList<Student> sAL) {
        context = c;
        studentArrayList = sAL;
    }

    @Override
    public int getCount() {
        return studentArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.student_basic_cell, null);
        }

        // get GUI
        TextView tv_j_name = view.findViewById(R.id.SBC_tv_v_name);
        TextView tv_j_username = view.findViewById(R.id.SBC_tv_v_username);

        Student student = studentArrayList.get(position);

        String name = student.getFirstName() + " " + student.getLastName(), username = student.getUsername();

        // set GUI
        tv_j_name.setText("Name: " + name);
        tv_j_username.setText("Username: " + username);

        return view;
    }
}
