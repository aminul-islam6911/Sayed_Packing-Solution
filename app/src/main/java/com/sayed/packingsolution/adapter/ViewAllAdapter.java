package com.sayed.packingsolution.adapter;


import static androidx.core.content.ContextCompat.startActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sayed.packingsolution.AddActivity;
import com.sayed.packingsolution.IncompleteActivity;
import com.sayed.packingsolution.R;
import com.sayed.packingsolution.UpdatePostFrag;
import com.sayed.packingsolution.dataholder.DataHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ViewAllAdapter extends FirebaseRecyclerAdapter<DataHolder,ViewAllAdapter.myviewholder> {


    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference img_storage = storage.getReference().child("images");

    int imgCounter =0;
    private ProgressDialog pd;

    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://packing-solution-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference post_node = db.getReference("post");
    private final DatabaseReference settings_node = db.getReference("setting");

    public ViewAllAdapter(@NonNull FirebaseRecyclerOptions<DataHolder> options) {
        super(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, final int position, @NonNull DataHolder model) {


        holder.postTitle.setText(model.getDESCRIPTION());

        holder.postDate.setText(model.getDATE());
        holder.postStatus.setText(model.getSTATUS().toUpperCase(Locale.ROOT).substring(2));
        if(model.getSTATUS().equals("c_cancelled")){
            holder.postStatus.setTextColor(Color.parseColor("#CF0A0A"));

        }


        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(model!=null){


                    ArrayList<String> imageUriList = new ArrayList<String>();
                    //System.out.println(model.getIMAGES());

                    for (String uri:model.getIMAGES().values())
                    {
                        imageUriList.add(uri);
                    }



                    AppCompatActivity activity = (AppCompatActivity) view.getContext();

                    UpdatePostFrag updateFragment = new UpdatePostFrag(position,getRef(position).getKey());
                    updateFragment.setWhoIsParent(0);

                    Bundle dataPass = new Bundle();
                    dataPass.putString("title",model.getDESCRIPTION());
                    dataPass.putString("email",model.getEMAIL());
                    dataPass.putStringArrayList("images",imageUriList);

                    updateFragment.setArguments(dataPass);

                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,updateFragment).commit();


                }


            }
        });


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Do you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                pd = new ProgressDialog(view.getContext());
                                pd.setMessage("Deleting... Please Wait");
                                pd.setCancelable(false);
                                pd.show();

                                //Delete Data
                                System.out.println("THIS IS VIEW ALL ADAPTER-------"+position);
                                post_node.child(getRef(position).getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(view.getContext(),"Data has been removed successfully!!",Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();


                                        //Delete Pic from Cloud
                                        try {

                                            for (String url: model.getIMAGES().values()){

                                                    imgCounter++;
                                                    storage.getReferenceFromUrl(url).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                            Toast.makeText(view.getContext(),"Image("+imgCounter+") Deleted!",Toast.LENGTH_SHORT).show();



                                                        }

                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(view.getContext(),"Failed to Delete Image("+imgCounter+")",Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                                }


                                                pd.dismiss();
                                            }

                                        catch (Exception e){
                                            Toast.makeText(view.getContext(),"Error occured while deleting file",Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(view.getContext(),"Failed to remove Data !",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }


        });





    }



    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_view_all_2, parent, false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder {
        TextView postTitle, postDate,postStatus;
        Button btnPrint, btnDelete,btnView;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            postTitle = itemView.findViewById(R.id.card_post_title);
            postDate = itemView.findViewById(R.id.card_post_date);
            postStatus = itemView.findViewById(R.id.card_post_status);
            btnDelete = itemView.findViewById(R.id.card_btn_delete);
            btnPrint = itemView.findViewById(R.id.card_btn_print);
            btnView = itemView.findViewById(R.id.card_btn_view);



        }

    }
}
