package com.sayed.packingsolution;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sayed.packingsolution.adapter.ImageAdapter;
import com.sayed.packingsolution.adapter.ImageAdapterGet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UpdatePostFrag extends Fragment {



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public int WHO_IS_PARENT = 0 ; // 0-> means view All Wrapper is the parent; 1-> means completeWrapper is the parent

    String EMAIL,DESCRIPTION,CurrentKey;
    int recPosition;




    RecyclerView recyclerView;
    Button btnSelect,btnSave,btnBack;
    BottomNavigationView bottomNavContainer;
    TextView txtDescription,txtEmail;


    ArrayList<Uri> imageUri ;
    ArrayList<String> rcvUri;
    ImageAdapterGet img_adapter_get;

    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://packing-solution-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference post_node = db.getReference("post");
    private final DatabaseReference settings_node = db.getReference("settings");


    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference img_storage = storage.getReference();

    public int imageKeyCounter= 0;

    private static final int Read_Permission = 101;
    private static final int PickImage = 1;
    private static final int ImageCountPerRow = 3;
    private ProgressDialog pd;

    public int getWhoIsParent() {
        return WHO_IS_PARENT;
    }

    public  void setWhoIsParent(int whoIsParent) {
        WHO_IS_PARENT = whoIsParent;
    }

    public UpdatePostFrag() {

    }

    public UpdatePostFrag(int pos,String key) {
        this.recPosition = pos;
        this.CurrentKey = key;

    }





    public static UpdatePostFrag newInstance(String param1, String param2) {
        UpdatePostFrag fragment = new UpdatePostFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_post, container, false);




        imageUri= new ArrayList<Uri>();
        rcvUri = new ArrayList<String>();



        btnSelect = view.findViewById(R.id.btn_select);
        recyclerView =view.findViewById(R.id.recview_image);
        btnSave = view.findViewById(R.id.btn_save);
        btnBack = view.findViewById(R.id.btn_back);

        txtDescription = view.findViewById(R.id.txt_desc);
        txtEmail = view.findViewById(R.id.txt_email);



                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            if(WHO_IS_PARENT==0){
                                ViewAllFrag viewFrag = new ViewAllFrag();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,viewFrag).commit();
                            }
                            else{
                                CompleteFrag completeFrag = new CompleteFrag();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper_c,completeFrag).commit();
                            }

                        }
                    });




                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(txtDescription.getText().length()!=0){
                            Map<String,Object> map = new HashMap<>();
                            map.put("description",txtDescription.getText().toString());
                            map.put("email",txtEmail.getText().toString());

                            System.out.println(CurrentKey);

                            post_node.child(CurrentKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    new AlertDialog.Builder(view.getContext())
                                            .setMessage("Data has been updated Successfully")
                                            .setCancelable(false)
                                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                        /*AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                                        ViewAllFrag viewFrag = new ViewAllFrag();
                                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,viewFrag).commit();

                                                         */

                                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                                    if(WHO_IS_PARENT==0){
                                                        ViewAllFrag viewFrag = new ViewAllFrag();
                                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,viewFrag).commit();
                                                    }
                                                    else{
                                                        CompleteFrag completeFrag = new CompleteFrag();
                                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper_c,completeFrag).commit();
                                                    }


                                                }
                                            }).show();



                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(view.getContext(),"Update Failed !",Toast.LENGTH_SHORT);
                                }
                            });


                        }
                        else{
                            Toast.makeText(view.getContext(),"Incorrect Data !",Toast.LENGTH_SHORT);
                        }




                    }
                });





        //Bottom Menu
        bottomNavContainer = view.findViewById(R.id.bottomContainer);
        bottomNavContainer.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        startActivity(new Intent(view.getContext(),MainActivity.class));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        getActivity().finish();
                        break;

                    case R.id.menu_setting:
                        startActivity(new Intent(view.getContext(),SettingActivity.class));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        getActivity().finish();
                        break;
                }
                return true;
            }
        });





        Bundle rcvdData = getArguments();

        if(rcvdData!= null){
            this.DESCRIPTION = rcvdData.getString("title");
            this.EMAIL = rcvdData.getString("email");
            this.rcvUri.addAll(rcvdData.getStringArrayList("images"));

            txtDescription.setText(this.DESCRIPTION);
            txtEmail.setText(this.EMAIL);



            img_adapter_get = new ImageAdapterGet(view.getContext(),rcvUri);
            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),ImageCountPerRow));
            recyclerView.setAdapter(img_adapter_get);

            img_adapter_get.notifyDataSetChanged();



        }










        return view;
    }
}