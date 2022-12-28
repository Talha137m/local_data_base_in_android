package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.database.databinding.ActivityMain3Binding;

public class MainActivity3 extends AppCompatActivity {
    //Initialize reference of MyHelper Class
    MyHelper myHelperObject;
    //Binding
    ActivityMain3Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
        cancelBtnClick();
        //Create object of myHelper class
        myHelperObject=MyHelper.getHelperClassObject(this);
        //Call btnUpdateClickListener
        updateBtn();
        //tvClickRollNo
        tvClickForRollNo();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getData() {
        Intent intent = getIntent();
        String name = intent.getStringExtra(MainActivity.NAME_KEY);
       int rollNo=intent.getIntExtra(MainActivity.ROLL_NO_KEY,0);
       float fee=intent.getFloatExtra(MainActivity.FEE_KEY,0);

       binding.etName.setText(name);
       binding.tvRollNo.setText(Integer.toString(rollNo));
       binding.etFee.setText(Float.toString(fee));
    }

    private void cancelBtnClick(){
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity3.this,MainActivity.class));
            }
        });
    }
    private void updateBtn(){
        binding.btnSave.setOnClickListener(view -> {
            String name=binding.etName.getText().toString();
            String rollNo=binding.tvRollNo.getText().toString();
            int studentRollNo=Integer.parseInt(rollNo);
            String fee=binding.etFee.getText().toString();
            float studentFee=Float.parseFloat(fee);
           if (myHelperObject.updateStudent(new ModelClass(name,studentRollNo,studentFee))){
               startActivity(new Intent(MainActivity3.this,MainActivity.class));
           }
        });
    }
    private void tvClickForRollNo(){
        binding.tvRollNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity3.this, "RollNo cannot be changed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}