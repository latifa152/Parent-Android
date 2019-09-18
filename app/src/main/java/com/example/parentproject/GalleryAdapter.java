package com.example.parentproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class GalleryAdapter extends RecyclerView.ViewHolder {
    View mView;
    public GalleryAdapter(@NonNull View itemView) {
        super( itemView );
        mView = itemView;
    }
    public void setDetails(final Context ctx , String image  ){
        ImageView imageView = mView.findViewById( R.id.imageView );


        Picasso.get().load( image ).into( imageView );

    }
}
