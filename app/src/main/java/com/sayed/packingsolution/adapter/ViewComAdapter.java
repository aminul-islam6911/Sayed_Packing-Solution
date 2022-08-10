package com.sayed.packingsolution.adapter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sayed.packingsolution.R;
import com.sayed.packingsolution.UpdatePostFrag;
import com.sayed.packingsolution.dataholder.DataHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ViewComAdapter extends FirebaseRecyclerAdapter<DataHolder, ViewComAdapter.myviewholder> {


    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://packing-solution-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference post_node = db.getReference("post");
    private final DatabaseReference settings_node = db.getReference("settings");





    public ViewComAdapter(@NonNull FirebaseRecyclerOptions<DataHolder> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, final int position, @NonNull DataHolder model) {

        if(!model.getSTATUS().equals("b_complete")){

            holder.postContainer.setVisibility(View.INVISIBLE);
        }


        else if(model.getSTATUS().equals("b_complete")){



            holder.postTitle.setText(model.getDESCRIPTION());

            holder.postStatus.setText(model.getSTATUS().toUpperCase(Locale.ROOT).substring(2));

            holder.postDate.setText(model.getDATE());


            //Click on View Button
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
                        updateFragment.setWhoIsParent(1);



                        Bundle dataPass = new Bundle();
                        dataPass.putString("title",model.getDESCRIPTION());
                        dataPass.putString("email",model.getEMAIL());
                        dataPass.putStringArrayList("images",imageUriList);

                        updateFragment.setArguments(dataPass);

                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper_c,updateFragment).commit();


                    }





                }
            });


            // Click on Cancel Button
            holder.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    new AlertDialog.Builder(view.getContext())
                            .setMessage("Do you want to cancel this order?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Map<String,Object> newData = new HashMap<>();
                                    newData.put("status","c_cancelled");


                                    post_node.child(getRef(position).getKey()).updateChildren(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            Toast.makeText(view.getContext(), "Order has been cancelled!", Toast.LENGTH_SHORT).show();

                                        }
                                    });


                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                }
            });


        }




    }



    //Single Row Design Selection

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_complete, parent, false);
        return new myviewholder(view);
    }



    //Initialize Single Row's Component

    class myviewholder extends RecyclerView.ViewHolder {
        TextView postTitle, postDate,postStatus;
        Button btnCancel, btnView,btnPrint;
        CardView postContainer;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            postTitle = itemView.findViewById(R.id.card_c_post_title);
            postStatus = itemView.findViewById(R.id.card_c_post_status);
            postDate = itemView.findViewById(R.id.card_c_post_date);
            btnCancel = itemView.findViewById(R.id.card_c_btn_canecl);
            btnPrint = itemView.findViewById(R.id.card_btn_print);
            btnView = itemView.findViewById(R.id.card_c_btn_view);
            postContainer = itemView.findViewById(R.id.post_container_com);




        }

    }
}
