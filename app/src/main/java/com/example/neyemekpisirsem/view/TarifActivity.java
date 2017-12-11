package com.example.neyemekpisirsem.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.neyemekpisirsem.R;

public class TarifActivity extends AppCompatActivity {

    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarif);
    }

    public void onceki(View view) {
        Intent i = new Intent(getApplicationContext(),FoodActivity.class);
        startActivity(i);
    }
}
