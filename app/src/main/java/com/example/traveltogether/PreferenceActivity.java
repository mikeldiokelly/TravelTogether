package com.example.traveltogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.traveltogether.Model.Preferences;

import java.util.ArrayList;
import java.util.List;

public class PreferenceActivity extends AppCompatActivity {

    private Spinner spinnerGender, spinnerAge;
    ArrayAdapter<String> adapterGender, adapterAge;
    private final List<String> listGender = Preferences.listGender;
    private final List<String> listAge = Preferences.listAge;
//    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        spinnerGender.setPrompt("Set your gender preferences");
        adapterGender=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listGender);
        spinnerGender.setAdapter(adapterGender);
        SharedPreferences shrd= getSharedPreferences("prefernces", MODE_PRIVATE);
        SharedPreferences.Editor editor= shrd.edit();
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PreferenceActivity.this,"You select："+listGender.get(position),Toast.LENGTH_SHORT).show();

                editor.putString("genderpreference",listGender.get(position) );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//

        spinnerAge = (Spinner) findViewById(R.id.spinnerAge);
        spinnerAge.setPrompt("Set your age preferences");
        adapterAge=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listAge);
        spinnerAge.setAdapter(adapterAge);
        spinnerAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PreferenceActivity.this,"You select："+listAge.get(position),Toast.LENGTH_SHORT).show();
                editor.putString("agepreference",listAge.get(position) );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


}