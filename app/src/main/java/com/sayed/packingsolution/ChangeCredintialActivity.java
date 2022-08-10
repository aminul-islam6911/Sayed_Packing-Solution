package com.sayed.packingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChangeCredintialActivity extends AppCompatActivity {

    BottomNavigationView bottomNavContainer;
    Button btnUpdate;
    EditText txtPass,txtUser;

    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://packing-solution-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference settings_node = db.getReference("setting");
    private final DatabaseReference user_node = db.getReference("user");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_credintial);


        btnUpdate = findViewById(R.id.btn_update_cred);
        txtPass = findViewById(R.id.txt_password_cred);
        txtUser = findViewById(R.id.txt_username_cred);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtUser.getText().length()>=4 && txtPass.getText().length()>=4){


                    Map<String,Object> newData = new HashMap<>();
                    newData.put("password",txtPass.getText().toString());
                    newData.put("username",txtUser.getText().toString());


                    user_node.updateChildren(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {


                            //Toast.makeText(view.getContext(), "Credential has been updated!!", Toast.LENGTH_SHORT).show();

                            new AlertDialog.Builder(view.getContext())
                                    .setMessage("Credential updated Successfully")
                                    .setCancelable(false)
                                    .setPositiveButton("HOME", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            finish();
                                        }
                                    }).show();




                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(view.getContext(), "Failed to update Credential!", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else{
                    Toast.makeText(view.getContext(), "Password and Username must be 4 character long!", Toast.LENGTH_SHORT).show();
                }
            }
        });


























        //Bottom Menu
        bottomNavContainer = findViewById(R.id.bottomContainer);
        bottomNavContainer.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                        break;
                    case R.id.menu_setting:
                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        finish();
                        break;


                }
                return true;
            }
        });



    }
}