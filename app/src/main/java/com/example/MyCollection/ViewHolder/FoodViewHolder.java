package com.example.MyCollection.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyCollection.Interface.ItemClickListener;
import com.example.foodnews.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView foodName,foodAdress;
    public ImageView foodImage;
    public static ItemClickListener itemClickListenerr;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        foodName = (TextView) itemView.findViewById(R.id.textViewTitle);
        foodImage = (ImageView) itemView.findViewById(R.id.imageView);
        foodAdress = (TextView) itemView.findViewById(R.id.textViewAddress);

        itemView.setOnClickListener(this);

    }

    public static void setOnClickListener(ItemClickListener itemClickListener){
        itemClickListenerr = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListenerr.onClick(v, getAdapterPosition(),false);
    }
}
