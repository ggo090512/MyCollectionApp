package com.example.MyCollection.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyCollection.Common.Container;
import com.example.MyCollection.Interface.ItemClickListener;
import com.example.foodnews.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

    public static TextView txtMenuTitle;
    public static ImageView imageViiew;
    public static ItemClickListener itemClickListenerr;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtMenuTitle = (TextView) itemView.findViewById(R.id.menu_name);
        imageViiew = (ImageView) itemView.findViewById(R.id.ImageViewMenu);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }

    public static void setOnClickListener(ItemClickListener itemClickListener){
        itemClickListenerr = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListenerr.onClick(v, getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("CHOOSE ACTIOM: ");

        menu.add(0,0,getAdapterPosition(), Container.UPDATE);
        menu.add(0,0,getAdapterPosition(), Container.DELETE);
    }

}
