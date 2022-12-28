package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.database.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {
    ActivityMain2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //getData from MainActivity and show
        getData();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private void getData(){
        Intent intent=getIntent();
        String name=intent.getStringExtra(MainActivity.NAME_KEY);
        int rollNo=intent.getIntExtra(MainActivity.ROLL_NO_KEY,0);

        float fee=intent.getFloatExtra(MainActivity.FEE_KEY,0);


        binding.tvName.setText(name);
        binding.tvRollNo.setText(Integer.toString(rollNo));
        binding.tvFee.setText(Float.toString(fee));
    }
}