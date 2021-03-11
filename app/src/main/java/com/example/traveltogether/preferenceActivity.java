package com.example.traveltogether;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class preferenceActivity extends AppCompatActivity {


    private Spinner spinnerGender, spinnerAge;
    ArrayAdapter<String> adapterGender, adapterAge;
    private List<String> listGender, listAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        spinnerGender.setPrompt("Set your gender preferences");
        initGenderData();
        adapterGender=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listGender);
        spinnerGender.setAdapter(adapterGender);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(preferenceActivity.this,"You select："+listGender.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//




        spinnerAge = (Spinner) findViewById(R.id.spinnerAge);
        spinnerAge.setPrompt("Set your age preferences");
        initAgeData();
        adapterAge=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listAge);
        spinnerAge.setAdapter(adapterAge);
        spinnerAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(preferenceActivity.this,"You select："+listAge.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }


    private void initGenderData() {
        listGender=new ArrayList<String>();
        listGender.add("Female only");
        listGender.add("Male only");
        listGender.add("No gender preferences");

    }

    private void initAgeData() {
        listAge=new ArrayList<String>();
        listAge.add("no Preferences");
        listAge.add("0-20");
        listAge.add("20-70");
        listAge.add("70+");

    }

}