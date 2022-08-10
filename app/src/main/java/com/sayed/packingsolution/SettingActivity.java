package com.sayed.packingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {


    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://packing-solution-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference settings_node = db.getReference("setting");
    private final DatabaseReference user_node = db.getReference("user");
    private final DatabaseReference completion_mail_node = settings_node.child("completion_mail");

    BottomNavigationView bottomNavContainer;
    Button btnChangeP,btnConfig;
    Switch swSendMail;


    private static boolean IS_EMAIL_SENDING_ON = true;

    private static String SUPER_PASSWORD;



    public static String getSuperPassword() {
        return SUPER_PASSWORD;
    }

    public static void setSuperPassword(String superPassword) {
        SUPER_PASSWORD = superPassword;
    }

    public static boolean isIsEmailSendingOn() {
        return IS_EMAIL_SENDING_ON;
    }

    public static void setIsEmailSendingOn(boolean isEmailSendingOn) {
        IS_EMAIL_SENDING_ON = isEmailSendingOn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



        swSendMail = findViewById(R.id.setting_switch_sendMail);
        btnChangeP = findViewById(R.id.setting_btn_change_p);
        btnConfig = findViewById(R.id.setting_btn_config_p);




        getSuperPassFromDB();

        getEmailSettingFromDB();




        btnChangeP.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View viewMain) {

                                    final DialogPlus dialogPlus = DialogPlus.newDialog(viewMain.getContext())
                                            .setContentHolder(new ViewHolder(R.layout.dialog_content_super))
                                            .setExpanded(true,800).setGravity(Gravity.CENTER).setCancelable(true)
                                            .create();


                                    View dialogView = dialogPlus.getHolderView();
                                    EditText  txtSuperPass = dialogView.findViewById(R.id.dlg_txt_super_password);
                                    Button btnEnterSuper = dialogView.findViewById(R.id.dlg_btn_enter_super);

                                    dialogPlus.show();

                                    btnEnterSuper.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    if(txtSuperPass.getText().length()!=0){
                                                           String inputVal = txtSuperPass.getText().toString();
                                                            if(inputVal.equals(getSuperPassword())){


                                                                dialogPlus.dismiss();
                                                                System.out.println("SuperPassWord Matched");

                                                                startActivity(new Intent(getApplicationContext(),ChangeCredintialActivity.class));
                                                                finish();

                                                            }
                                                            else{
                                                                Toast.makeText(view.getContext(), "Super Password Does not Matched!", Toast.LENGTH_SHORT).show();
                                                            }

                                                    }

                                                }
                                            });







                                }
                            });



        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    final DialogPlus dialogPlus = DialogPlus.newDialog(view.getContext())
                            .setContentHolder(new ViewHolder(R.layout.dialog_content_config))
                            .setExpanded(true,1300).setGravity(Gravity.TOP).setCancelable(true)
                            .create();

                View dialogView = dialogPlus.getHolderView();
                EditText  txtFrom = dialogView.findViewById(R.id.dlg_txt_config_from);
                EditText  txtPass = dialogView.findViewById(R.id.dlg_txt_config_from_pass);
                EditText  txtSubj = dialogView.findViewById(R.id.dlg_txt_config_subj);
                EditText  txtBody = dialogView.findViewById(R.id.dlg_txt_config_body);
                EditText  txtPort = dialogView.findViewById(R.id.dlg_txt_config_port);
                EditText  txtHost = dialogView.findViewById(R.id.dlg_txt_config_host);


                Button btnUpdateConfig = dialogView.findViewById(R.id.dlg_btn_config_update);

                completion_mail_node.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        txtFrom.setText(dataSnapshot.child("from_mail").getValue().toString());

                        txtFrom.setText(dataSnapshot.child("from_mail").getValue().toString());
                        txtPass.setText(dataSnapshot.child("from_mail_pass").getValue().toString());
                        txtBody.setText(dataSnapshot.child("msg_body").getValue().toString());
                        txtSubj.setText(dataSnapshot.child("msg_subj").getValue().toString());
                        txtHost.setText(dataSnapshot.child("mail_host").getValue().toString());
                        txtPort.setText(dataSnapshot.child("mail_port").getValue().toString());

                        dialogPlus.show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialogPlus.dismiss();
                    }
                });


                btnUpdateConfig.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(txtFrom.getText().length()!=0 && txtPass.getText().length()!=0 && txtPort.getText().length()!=0 && txtHost.getText().length()!=0 && txtSubj.getText().length()!=0 && txtBody.getText().length()!=0){


                                    Map<String,Object> newData = new HashMap<>();
                                    newData.put("from_mail",txtFrom.getText().toString().trim());
                                    newData.put("from_mail_pass",txtPass.getText().toString().trim());
                                    newData.put("mail_host",txtHost.getText().toString().trim());
                                    newData.put("mail_port",txtPort.getText().toString().trim());
                                    newData.put("msg_body",txtBody.getText().toString().trim());
                                    newData.put("msg_subj",txtSubj.getText().toString().trim());



                                    completion_mail_node.updateChildren(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {


                                            //Toast.makeText(view.getContext(), "Credential has been updated!!", Toast.LENGTH_SHORT).show();

                                            new AlertDialog.Builder(view.getContext())
                                                    .setMessage("Mail System Config Updated Successfully")
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

                                            Toast.makeText(view.getContext(), "Failed to update mail config!", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                        }
                        else{
                            Toast.makeText(view.getContext(), "One or more field are empty!", Toast.LENGTH_SHORT).show();
                        }


                    }
                });





            }
        });











        swSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swSendMail.isChecked()){

                    Map<String,Object> newData = new HashMap<>();
                    newData.put("send_email","true");

                    settings_node.updateChildren(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(view.getContext(), "Sending Email Turned On!", Toast.LENGTH_SHORT).show();

                        }
                    });


                }
                else if(!swSendMail.isChecked()){
                    Map<String,Object> newData = new HashMap<>();
                    newData.put("send_email","false");

                    settings_node.updateChildren(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(view.getContext(), "Sending Email Turned Off!", Toast.LENGTH_SHORT).show();

                        }
                    });

                }


            }
        });


        settings_node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getEmailSettingFromDB();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        user_node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getSuperPassFromDB();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void getEmailSettingFromDB() {

        settings_node.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                String val = dataSnapshot.child("send_email").getValue().toString();
                if(val.equals("false")){
                    setIsEmailSendingOn(false);
                    swSendMail.setChecked(false);
                }
                else if(val.equals("true")){
                    setIsEmailSendingOn(true);

                    swSendMail.setChecked(true);
                }




            }
        });

    }



    private void getSuperPassFromDB(){

        user_node.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                String val = dataSnapshot.child("superpassword").getValue().toString();
                setSuperPassword(val);

            }
        });


    }



}