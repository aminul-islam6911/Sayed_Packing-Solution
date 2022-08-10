package com.sayed.packingsolution.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sayed.packingsolution.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Viewholder> {

    private ArrayList<Uri> uriArrayList;


    public ImageAdapter(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public ImageAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_image,parent,false);


        return new Viewholder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.Viewholder holder, int position) {

        holder.imageView.setImageURI(uriArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView imageView;
        Button btn_imgRemove;
        private ImageAdapter imgAdapter;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            btn_imgRemove = itemView.findViewById(R.id.btn_img_remove);

            btn_imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgAdapter.uriArrayList.remove(getAdapterPosition());
                    imgAdapter.notifyItemRemoved(getAdapterPosition());
                }
            });

        }

        public Viewholder linkAdapter(ImageAdapter adapter){
            this.imgAdapter = adapter;
            return this;
        }


    }
}
