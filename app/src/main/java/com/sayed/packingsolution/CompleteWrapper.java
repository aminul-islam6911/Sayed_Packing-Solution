package com.sayed.packingsolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class CompleteWrapper extends AppCompatActivity {


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_wrapper);

        CompleteFrag compFrag = new CompleteFrag();

        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper_c,compFrag).commit();


    }
}