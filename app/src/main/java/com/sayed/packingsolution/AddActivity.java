package com.sayed.packingsolution;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sayed.packingsolution.adapter.ImageAdapter;
import com.sayed.packingsolution.dataholder.DataHolder;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AddActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnSelect,btnAdd;
    BottomNavigationView bottomNavContainer;
    TextView txtDescription,txtEmail;

    ExecutorService UploadBGTask;

    ArrayList<Uri> uri ;
    ImageAdapter img_adapter;

    public int imageKeyCounter= 0;

    private static final int Read_Permission = 101;
    private static final int PickImage = 1;
    private static final int ImageCountPerRow = 3;
    private ProgressDialog pd;


    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://packing-solution-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference post_node = db.getReference("post");
    private final DatabaseReference settings_node = db.getReference("setting");


    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference img_storage = storage.getReference();




    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        UploadBGTask = Executors.newSingleThreadExecutor();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        uri= new ArrayList<Uri>();

        pd = new ProgressDialog(AddActivity.this);
        pd.setMessage("Uploading Images...");
        pd.setCancelable(false);


        btnSelect = findViewById(R.id.btn_select);
        recyclerView =findViewById(R.id.recview_image);
        btnAdd = findViewById(R.id.btn_add);

        txtDescription = findViewById(R.id.txt_desc);
        txtEmail = findViewById(R.id.txt_email);







        img_adapter = new ImageAdapter(uri);
        recyclerView.setLayoutManager(new GridLayoutManager(AddActivity.this,ImageCountPerRow));
        //recyclerView.setLayoutManager(new LinearLayoutManager(AddActivity.this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(img_adapter);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                if(validInput()){


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.show();
                        }
                    });
                    UploadBGTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            addToDB();
                        }
                    });



                }
                else{
                    Toast.makeText(getApplicationContext(),"Invalid Data",Toast.LENGTH_SHORT).show();
                }




            }
        });




    // Grant permission from user
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
                {
                    ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);

                    return;

                }


                //Opening FileManager

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(Intent.createChooser(intent,"Select Images"),PickImage);

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
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case R.id.menu_setting:
                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;


                }
                return true;
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PickImage && resultCode== Activity.RESULT_OK && null!= data){
            if(data.getClipData()!=null) // multiple image pick
            {
                int ImageCount = data.getClipData().getItemCount();
                for(int i =0;i<ImageCount;i++)
                {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    uri.add(imageUri);
                }
                img_adapter.notifyDataSetChanged();
            }
            else //single image pick
            {
                Uri imageUri = data.getData();
                uri.add(imageUri);
                img_adapter.notifyDataSetChanged();
            }

        }
        else{
            Toast.makeText(this,R.string.no_img_select_toast,Toast.LENGTH_LONG).show();
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentDateTime(){
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatedDate = DateTimeFormatter.ofPattern("dd MMM yyyy - hh:mm:ss");

        String formattedDate = date.format(formatedDate);

        return formattedDate.toUpperCase();
    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getFormattedImageName(String img_path){

        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatedDate = DateTimeFormatter.ofPattern("ddmmyyyy_hh_mm_ss_SSS");
        String formattedDate = date.format(formatedDate);
        return img_path+"_"+formattedDate.toUpperCase();
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addToDB(){

        pd.show();

        String emailData = txtEmail.getText().toString().trim();
        String descData =txtDescription.getText().toString().trim();
        String dateData = getCurrentDateTime();
        String statusData = "a_incomplete";

        DataHolder data = new DataHolder(emailData,descData,dateData,statusData);
       // System.out.println(uri);

        String key = post_node.push().getKey();
        post_node.child(key).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                    for(int i=0;i<uri.size();i++){


                        StorageReference fileRef = img_storage.child("images/"+getFormattedImageName(uri.get(i).getLastPathSegment()));
                        fileRef.putFile(uri.get(i))
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                post_node.child(key).child("images").push().setValue(uri.toString());

                                            }
                                        });

                                    }

                                }).addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(),"Failed to Upload Images!",Toast.LENGTH_SHORT).show();
                                pd.dismiss();

                            }
                        });



                    }

                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Successfully Added!",Toast.LENGTH_SHORT).show();
                startActivity(getIntent());
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Failed to Connect Database!",Toast.LENGTH_SHORT).show();
            }
        });




        //pd.dismiss();



        //startActivity(getIntent());
        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        //finish();


    }


    private boolean validInput(){
        if(txtDescription.getText().toString().length()!=0 && uri.isEmpty()!= true){
            return true;
        }
        else{
            return false;
        }
    }












}