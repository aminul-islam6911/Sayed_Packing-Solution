package com.sayed.packingsolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextView userError, passError;
    private EditText txtUser , txtPass;
    private CheckBox chkRemeber;

    private static boolean IS_REMEMBERED = false;



    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://packing-solution-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference settings_node = db.getReference("setting");
    private final DatabaseReference user_node = db.getReference("user");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtPass = findViewById(R.id.txt_password);
        txtUser = findViewById(R.id.txt_username);









        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {







                String givenUser = txtUser.getText().toString();
                String givenPass = txtPass.getText().toString();



                if(givenPass.length()!=0 && givenUser.length()!=0){
                    loginAction(givenUser,givenPass);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Fill up both field!",Toast.LENGTH_LONG).show();
                }



            }
        });



        userError = findViewById(R.id.username_error);
        passError  = findViewById(R.id.password_error);

        userError.setVisibility(View.INVISIBLE);
        passError.setVisibility(View.INVISIBLE);



    }


    private void loginAction(String u , String p){


        user_node.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {




                if(isUserValid(u,p,dataSnapshot.child("username").getValue().toString(),dataSnapshot.child("password").getValue().toString())){
                    userError.setVisibility(View.INVISIBLE);
                    passError.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
                /*
                else{
                    userError.setVisibility(View.VISIBLE);
                    passError.setVisibility(View.VISIBLE);

                }

                 */



            }
        });


    }




    private boolean isUserValid(String userGiven, String passGiven, String userDb, String passDb){

        if(userDb.equals(userGiven)  &&  passDb.equals(passGiven)){

            return true;
        }
        else{
            if(userDb.equals(userGiven) ){
                userError.setVisibility(View.INVISIBLE);
            }
            if(passDb.equals(passGiven)){
                passError.setVisibility(View.INVISIBLE);
            }

            if(!userDb.equals(userGiven) ){
                userError.setVisibility(View.VISIBLE);
            }
            if(!passDb.equals(passGiven)){
                passError.setVisibility(View.VISIBLE);
            }
            Toast.makeText(this,"Incorrect Credential!",Toast.LENGTH_LONG).show();
            return false;
        }

    }







}