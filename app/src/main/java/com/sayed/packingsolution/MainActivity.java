package com.sayed.packingsolution;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_Add,btn_Incomplete,btn_Complete,btn_ViewAll;

    private long incCounter =0;

    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://packing-solution-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference post_node = db.getReference("post");
    private final DatabaseReference settings_node = db.getReference("settings");
    private Query incQuery ;



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Add = findViewById(R.id.btn_add);
        btn_Incomplete = findViewById(R.id.btn_incomplete);
        btn_Complete = findViewById(R.id.btn_completed);
        btn_ViewAll = findViewById(R.id.btn_view_all);



        btn_Add.setOnClickListener(this);
        btn_Incomplete.setOnClickListener(this);
        btn_Complete.setOnClickListener(this);
        btn_ViewAll.setOnClickListener(this);



    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_add:
                startActivity(new Intent(getApplicationContext(),AddActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;

            case R.id.btn_incomplete:
                startActivity(new Intent(getApplicationContext(),IncompleteActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.btn_completed:
                startActivity(new Intent(getApplicationContext(),CompleteWrapper.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.btn_view_all:
                startActivity(new Intent(getApplicationContext(),ViewAllWrapper.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;


        }

    }




}