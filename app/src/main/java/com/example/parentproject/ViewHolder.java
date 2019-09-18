package com.example.parentproject;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentproject.Common.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;
    public ImageView mDeleteImage;
    DatabaseReference users;
    FirebaseDatabase db;
    FirebaseAuth auth;


    public ViewHolder(@NonNull View itemView) {
        super( itemView );
        mView = itemView;
        db = FirebaseDatabase.getInstance();
        users = db.getReference( );
        auth = FirebaseAuth.getInstance();
    }                                                             // no of parametr to show result
    public void setDetails(final Context ctx , String name , final String email, String Avatarurl, String phone , final String password , final String key, String CurrentUser ){
        TextView Name = mView.findViewById( R.id.Name );
        TextView Phone = mView.findViewById( R.id.Phone);
        CircleImageView imageView = mView.findViewById( R.id.imageView );
        TextView Password = mView.findViewById( R.id.password );
        TextView Email = mView.findViewById( R.id.email );
        mDeleteImage =mView.findViewById( R.id.image_delete );
        final String Key = key;
        final String Cuser = CurrentUser;
        if (name != null && !TextUtils.isEmpty(name))
        {
            Name.setText( name);
        }else
        {
            Name.setText( "Name" );
        }
        if (phone != null && !TextUtils.isEmpty(phone))
        {
            Phone.setText( phone );
        }else
        {
            Phone.setText( "03350541352");
        }
        if (password != null && !TextUtils.isEmpty(password))
        {
            Password.setText( password );
        }else
        {
            Password.setText("Unknown" );
        }
        if (email != null && !TextUtils.isEmpty(email))
        {
            Email.setText( email );
        }else
        {
            Email.setText( "enterYour@email.com" );
        }
        if (Avatarurl != null && !TextUtils.isEmpty( Avatarurl))
        {
            Picasso.get()
                .load( Avatarurl )
                .into( imageView );
        }
        else{
           // Toasty.info(ctx.getApplicationContext(), "Please Upload Profile Picture of your child "+ name.toUpperCase() +" From Child application ", Toast.LENGTH_SHORT, true).show();
            imageView.setImageResource( R.drawable.ic_user );
        }






        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Test","Name clicked : "+getAdapterPosition());
                Log.e("Test","Name clicked : "+Key.toString());

                Intent i = new Intent( ctx.getApplicationContext(),DataShownActivty.class );
                i.setFlags( FLAG_ACTIVITY_NEW_TASK );
                i.putExtra( "key",Key );
                ctx.startActivity(i);

            }
        });

        mDeleteImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               users.child( Common.Parent_information_tb1 )
                        .child( Cuser )
                        .child( Common.Child_user_tb1 )
                        .child( key).removeValue( new DatabaseReference.CompletionListener() {
                   @Override
                   public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                       users.child( Common.Child_information_tb1 )
                               .child(key ).removeValue( new DatabaseReference.CompletionListener() {
                           @Override
                           public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                               auth.signInWithEmailAndPassword( email,password ).addOnSuccessListener( new OnSuccessListener <AuthResult>() {
                                   @Override
                                   public void onSuccess(AuthResult authResult) {
                                       FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                       firebaseUser.delete();
                                       Toasty.success( ctx.getApplicationContext(),"Child Remove successfully",Toast.LENGTH_SHORT ).show();


                                   }
                               } )
                                       .addOnFailureListener( new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Toasty.error(ctx.getApplicationContext(), "Failed ! "+ e.getMessage(), Toast.LENGTH_LONG, true).show();

                                           }
                                       } );


                           }
                       } );



                   }
               } );




            }
        } );



    }

}
