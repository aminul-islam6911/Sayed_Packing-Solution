package com.sayed.packingsolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ViewAllWrapper extends AppCompatActivity {


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_wrapper);

        ViewAllFrag viewFragAll = new ViewAllFrag();

        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,viewFragAll).commit();


    }
}