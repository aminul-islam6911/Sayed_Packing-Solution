package com.sayed.packingsolution.adapter;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sayed.packingsolution.IncompleteActivity;
import com.sayed.packingsolution.MainActivity;
import com.sayed.packingsolution.R;
import com.sayed.packingsolution.dataholder.DataHolder;
import com.sayed.packingsolution.misc.MailSender;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ViewIncAdapter extends FirebaseRecyclerAdapter<DataHolder, ViewIncAdapter.myviewholder> {


    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://packing-solution-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference post_node = db.getReference("post");
    private final DatabaseReference settings_node = db.getReference("setting");



    ExecutorService DoneButtonBGTask;


    public boolean SEARCH_OPERATION = false;


    private boolean MAIL_SENDING;



    public boolean isMAIL_SENDING() {
        return MAIL_SENDING;
    }

    public void setMAIL_SENDING(boolean MAIL_SENDING) {
        this.MAIL_SENDING = MAIL_SENDING;
    }

    public boolean isSEARCH_OPERATION() {
        return SEARCH_OPERATION;
    }

    public void setSEARCH_OPERATION(boolean SEARCH_OPERATION) {
        this.SEARCH_OPERATION = SEARCH_OPERATION;
    }

    public static long postCount = 0;

    public ViewIncAdapter(@NonNull FirebaseRecyclerOptions<DataHolder> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, final int position, @NonNull DataHolder model) {

        DoneButtonBGTask = Executors.newSingleThreadExecutor();






        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


            if(!model.getSTATUS().equals("a_incomplete")){

                holder.postContainer.setVisibility(View.INVISIBLE);
                holder.postContainer.removeAllViews();

                holder.postContainer.setRadius(100);



            }


            else if(model.getSTATUS().equals("a_incomplete")){

                holder.postTitle.setText(model.getDESCRIPTION());

                holder.postDate.setText(model.getDATE());




                //Click on Done Button
                holder.btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //loadMailSettingDB();








                        Map<String,Object> newData = new HashMap<>();
                        newData.put("status","b_complete");

                                    post_node.child(getRef(position).getKey()).updateChildren(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            Toast.makeText(view.getContext(), "Marked As Complete!", Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();

                                        }
                                    });




                                    if(model.getEMAIL().toString().length()!=0){

                                            settings_node.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                @Override
                                                public void onSuccess(DataSnapshot dataSnapshot) {

                                                    String dbVal = (dataSnapshot.child("send_email").getValue().toString());
                                                    if(dbVal.equals("true")){
                                                        setMAIL_SENDING(true);
                                                        MailSender.sendEmail(model.getEMAIL().toString(),view.getContext());

                                                        notifyDataSetChanged();

                                                        //progressDialog.dismiss();


                                                    }
                                                    else if(dbVal.equals("false")){
                                                        setMAIL_SENDING(false);
                                                    }

                                                }
                                            });

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


                                        System.out.println("THIS IS VIEW INC ADAPTER-------"+position);
                                        post_node.child(getRef(position).getKey()).updateChildren(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                Toast.makeText(view.getContext(), "Order has been cancelled!", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();

                                            }
                                        });


                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();

                        //notifyDataSetChanged();
                        //System.err.println("Notified ------------INC ADAPTER-----------------***");

                    }
                });



            }



    }



    //Single Row Design Selection

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_incompletes, parent, false);
        return new myviewholder(view);
    }



    //Initialize Single Row's Component

    class myviewholder extends RecyclerView.ViewHolder {
        CardView postContainer;
        TextView postTitle, postDate;
        Button btnCancel, btnDone;
        LinearLayout posContainerL;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            postTitle = itemView.findViewById(R.id.card_i_post_title);
            postDate = itemView.findViewById(R.id.card_i_post_date);
            btnCancel = itemView.findViewById(R.id.card_i_btn_cancel);
            btnDone = itemView.findViewById(R.id.card_i_btn_done);
            postContainer = itemView.findViewById(R.id.post_container_inc);
            posContainerL = itemView.findViewById(R.id.post_container_inc_L);



        }

    }


    private void loadMailSettingDB(){
        settings_node.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                String dbVal = (dataSnapshot.child("send_email").getValue().toString());
                if(dbVal.equals("true")){
                    setMAIL_SENDING(true);
                }
                else if(dbVal.equals("true")){
                    setMAIL_SENDING(false);
                }

            }
        });

    }





}
