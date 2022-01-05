package com.example.MyCollection.Acitvity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.MyCollection.Models.ImageItem;
import com.example.foodnews.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{
    private static final String Tag = "ViewPager2";
    private ArrayList<ImageItem> imagesList;
    private Context mContext;


    public ImageAdapter(ArrayList<ImageItem> imagesList, Context mContext) {
        this.imagesList = imagesList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.textTitle.setText(imagesList.get(position).getmName());
        holder.textAddress.setText(imagesList.get(position).getmAddress());
        //ImageView
        Glide.with(mContext)
                .load(imagesList.get(position).getmImage())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textTitle, textAddress;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textTitle = itemView.findViewById(R.id.textViewTitle);
            textAddress = itemView.findViewById(R.id.textViewAddress);
        }
    }
}
