package com.sayed.packingsolution.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sayed.packingsolution.R;

import java.util.ArrayList;

public class ImageAdapterGet extends RecyclerView.Adapter<ImageAdapterGet.Viewholder> {


    private ArrayList<String>imgUrl;
    private Context currContext;

    public ImageAdapterGet(Context context,ArrayList<String> passUrl){
        this.currContext = context;
        this.imgUrl = passUrl;
    }



    @NonNull
    @Override
    public ImageAdapterGet.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        View view =  LayoutInflater.from(currContext).inflate(R.layout.single_image_get,parent,false);


        return new Viewholder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapterGet.Viewholder holder, int position) {

        //holder.image_get.setImageURI(uriArrayList.get(position));

        Glide.with(currContext).load(imgUrl.get(position)).into(holder.image_get);

    }

    @Override
    public int getItemCount() {
        return imgUrl.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView image_get;
        private ImageAdapterGet imgAdapter;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            image_get = itemView.findViewById(R.id.image_get);

        }

        public Viewholder linkAdapter(ImageAdapterGet adapter){
            this.imgAdapter = adapter;
            return this;
        }


    }
}
