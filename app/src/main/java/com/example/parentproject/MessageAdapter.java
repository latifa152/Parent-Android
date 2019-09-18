package com.example.parentproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends  RecyclerView.ViewHolder {
    View mView;
    public MessageAdapter(@NonNull View itemView) {
        super( itemView );
        mView = itemView;
    }
    public void setDetails(final Context ctx , String name , String message  ){
        TextView Name = mView.findViewById( R.id.Name );
        TextView Message = mView.findViewById( R.id.Message);
        CircleImageView imageView = mView.findViewById( R.id.imageView );

       // imageView.setImageResource( R.drawable.ic_user );
        Name.setText( name);
        Message.setText( message );





    }
}
