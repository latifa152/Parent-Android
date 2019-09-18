package com.example.parentproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.parentproject.Common.Common;
import com.example.parentproject.Model.MessageModel;
import com.example.parentproject.Model.PhoneModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class Message extends AppCompatActivity {
    Toolbar mActionBarToolbar;
    RecyclerView mRecyclerView;
    String key;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mReference;
    AlertDialog waitingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_message );
        waitingDialog = new SpotsDialog( Message.this );
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        mRecyclerView = (RecyclerView)findViewById( R.id.recyclerView );
       mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        Intent intent = getIntent();
        key = intent.getStringExtra( "key" );
        //Toasty.success( this,key,Toast.LENGTH_LONG ).show();
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Child Messages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(),Home.class );
                i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(i);
                finish();
            }
        });
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDatabase.getReference( Common.Child_information_tb1).child(key).child("Message" );
        mReference.addValueEventListener( new ValueEventListener() {
            public ArrayList<String> Userlist;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Userlist = new ArrayList<String>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    // Userlist.add( String.valueOf( dsp.getValue() )); //add result into array list
                }
                Log.e("error",dataSnapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    @Override
    protected void onStart() {
        super.onStart();
        waitingDialog.show();
        FirebaseRecyclerAdapter<MessageModel,MessageAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<MessageModel, MessageAdapter>(
                        MessageModel.class,
                        R.layout.message_layout,
                        MessageAdapter.class,
                        mReference
                ) {



                    @Override
                    protected void populateViewHolder(MessageAdapter viewHolder, MessageModel model, int position) {
                        viewHolder.setDetails( getApplicationContext(),model.getName(),model.getMessage() );
                        waitingDialog.dismiss();
                    }



                };
        mRecyclerView.setAdapter( firebaseRecyclerAdapter );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.onBackPressed();
        Intent i = new Intent( getApplicationContext(),Home.class );
        i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(i);
        finish();
    }
}
