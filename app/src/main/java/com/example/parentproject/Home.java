package com.example.parentproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentproject.Common.Common;
import com.example.parentproject.Model.ChildUser;
import com.example.parentproject.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

/*This is Home page which will show when you first
 login and contain the list of all the
 child of parent */

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    CircleImageView imageAvatar;
    StorageReference storageReference;
    FirebaseStorage storage;
    TextView txtRiderName,txtStars;
    FirebaseAuth auth;
    String currentUser;
    FirebaseDatabase db;
    AlertDialog waitingDialog;
    DatabaseReference mReference;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference users;
    RecyclerView mRecyclerView;
    ChildUser Cuser = new ChildUser(  );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        mFirebaseDatabase =FirebaseDatabase.getInstance();
        mReference = mFirebaseDatabase.getReference(  Common.Parent_information_tb1).child( currentUser ).child( Common.Child_user_tb1 );
        users = db.getReference( );
        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showaddChildDialog();
            }
        } );
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );
        View navigationHeaderView = navigationView.getHeaderView( 0 );
        txtRiderName = navigationHeaderView.findViewById( R.id.txtRiderName );
        if (Common.currentUser.getName() != null && !TextUtils.isEmpty( Common.currentUser.getName() ))
        { txtRiderName.setText( String.format( "%s",Common.currentUser.getName() ) );
        } else{
            txtRiderName.setText( " Name " );
        }

        txtStars = navigationHeaderView.findViewById( R.id.txtPhone );
        if (Common.currentUser.getPhone() != null && !TextUtils.isEmpty( Common.currentUser.getPhone() ))
        {
        txtStars.setText( String.format( "%s",Common.currentUser.getPhone() ) );
        }
        else{
            txtStars.setText( "0335054102" );
        }
        imageAvatar = navigationHeaderView.findViewById( R.id.imageAvatar );
        if (Common.currentUser.getAvatarUrl() != null && !TextUtils.isEmpty( Common.currentUser.getAvatarUrl() ))
        {        Picasso.get()
                .load( Common.currentUser.getAvatarUrl() )
                .into( imageAvatar );
        }
        else{
           // Toasty.info(Home.this, "Please Upload your profile picture from UPDATE INFORMATION  ", Toast.LENGTH_SHORT, true).show();

            imageAvatar.setImageResource( R.drawable.ic_user_white );
        }
        mRecyclerView = (RecyclerView)findViewById( R.id.recyclerView );
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        mReference.addValueEventListener( new ValueEventListener() {
            public ArrayList<String> Userlist;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Userlist = new ArrayList<String>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Userlist.add( String.valueOf( dsp.getValue() )); //add result into array list
                }
                Log.e("error",Userlist.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.home, menu );
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signOut)
        {
            SignOut();
        }
        else if (id == R.id.nav_UpdateInformation)
        {
            showUpdateInforamtionDialog();
        }
        else if (id == R.id.nav_addChild)
        {
            showaddChildDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    private void showaddChildDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this  );
        dialog.setTitle( "Adding a new Child " );
        dialog.setMessage("Please fill out the Information to add a new child ");
        waitingDialog = new SpotsDialog( Home.this );
        LayoutInflater inflater= LayoutInflater.from(this);

        View register_layout=inflater.inflate(R.layout.layout_register,null);

        final MaterialEditText edtEmail = register_layout.findViewById( R.id.edt_email );
        final MaterialEditText edtpassword = register_layout.findViewById( R.id.edt_password );
        final MaterialEditText edtName = register_layout.findViewById( R.id.edt_name );
        final MaterialEditText edtPhone = register_layout.findViewById( R.id.edt_phone );



        dialog.setView( register_layout );


        dialog.setPositiveButton( "ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if(TextUtils.isEmpty( edtEmail.getText().toString() ))
                {
                    Toasty.error(Home.this, "Please Enter Your Email address", Toast.LENGTH_LONG, true).show();

                    return; }
                if(TextUtils.isEmpty( edtpassword.getText().toString() ))
                {
                    Toasty.error(Home.this, "Please Enter Your Password", Toast.LENGTH_LONG, true).show();
                    return;
                }
                if(TextUtils.isEmpty( edtName.getText().toString() ))
                {
                    Toasty.error(Home.this, "Please Enter Your Name", Toast.LENGTH_LONG, true).show();
                    return;
                }
                if(TextUtils.isEmpty( edtPhone.getText().toString() ))
                {
                    Toasty.error(Home.this, "Please Enter Your Phone Number", Toast.LENGTH_LONG, true).show();
                    return;
                }
                if((!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()))
                {                       Toasty.error( Home.this, "Please enter a valid Email (youremail@gmail.com)", Toast.LENGTH_LONG, true ).show();

                    return;
                }

                if(edtpassword.getText().toString().length()<6)
                {
                    Toasty.error(Home.this, "Your Password is too Short", Toast.LENGTH_LONG, true).show();
                    return;

                }

                // Register User

                waitingDialog.show();
                auth.createUserWithEmailAndPassword( edtEmail.getText().toString(),edtpassword.getText().toString() )
                        .addOnSuccessListener( new OnSuccessListener <AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // save user to db


                                Cuser.setcEmail( edtEmail.getText().toString());
                                Cuser.setcName(edtName.getText().toString());
                                Cuser.setcPassword( edtpassword.getText().toString() );
                                Cuser.setcPhone( (edtPhone.getText().toString()) );
                                Cuser.setcAvatarUrl("");
                                Cuser.setCkey(FirebaseAuth.getInstance( ).getCurrentUser().getUid());

                                users.child( Common.Parent_information_tb1 )
                                        .child( currentUser )
                                        .child( Common.Child_user_tb1 )
                                        .child( FirebaseAuth.getInstance( ).getCurrentUser().getUid())
                                        .setValue( Cuser )
                                        .addOnSuccessListener( new OnSuccessListener <Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //   loadallContact();
                                                waitingDialog.dismiss();
                                                Toasty.success(Home.this, "Child Added  SuccessFully", Toast.LENGTH_SHORT, true).show();

                                            }
                                        } )
                                        .addOnFailureListener( new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                waitingDialog.dismiss();
                                                Toasty.error(Home.this, "Failed ! "+ e.getMessage(), Toast.LENGTH_LONG, true).show();

                                            }
                                        } );


                            }
                        } )

                        .addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                waitingDialog.dismiss();
                                Toasty.error(Home.this, "Failed ! "+ e.getMessage(), Toast.LENGTH_LONG, true).show();

                            }
                        } );

            }
        } );

        dialog.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        } );

        dialog.show();
    }

    private void showUpdateInforamtionDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this );
        dialog.setTitle( "Update Inforamtion" );
        dialog.setMessage("Please fill all the Inforamtion");

        LayoutInflater inflater= LayoutInflater.from(this);

        View update_info_layout=inflater.inflate(R.layout.layout_update_information,null);



        final MaterialEditText edtName = update_info_layout.findViewById( R.id.edt_Name );
        final MaterialEditText edtPhone = update_info_layout.findViewById( R.id.edt_Phone );
        final ImageView imgAvatar = update_info_layout.findViewById( R.id.imageAvatar );

        dialog.setView( update_info_layout );

        imgAvatar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImageandUpload();
            }
        } );

        dialog.setView( update_info_layout );
        dialog.setPositiveButton( "UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                final AlertDialog waitingDialog = new SpotsDialog( Home.this );
                waitingDialog.show();
                final String name = edtName.getText().toString();
                final String phone = edtPhone.getText().toString();

                Map<String,Object> update = new HashMap<>(  );
                if (!TextUtils.isEmpty(name))
                    update.put( "name",name );
                if (!TextUtils.isEmpty(phone))
                    update.put( "phone",phone );

                //update

                DatabaseReference riderInformation = FirebaseDatabase.getInstance().getReference( Common.Parent_information_tb1);
                riderInformation.child( FirebaseAuth.getInstance().getCurrentUser().getUid() )
                        .updateChildren( update ).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        waitingDialog.dismiss();
                        if (task.isSuccessful()) {
                            if (!TextUtils.isEmpty( name )) {
                                txtRiderName.setText( name );
                            }
                            if (!TextUtils.isEmpty( phone )) {
                                txtStars.setText( phone );
                            }
                            Toasty.success( Home.this, "Inforamtion Updated successfully !", Toast.LENGTH_LONG, true ).show();
                        }
                        else
                            Toasty.error( Home.this ,"Inforamtion wasn't Updated !",Toast.LENGTH_LONG,true).show();
                    }
                } );





            }
        } );

        dialog.setNegativeButton( "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        } );

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)

        {
            final Uri saveUri = data.getData();
            Toast.makeText( this,saveUri.toString(),Toast.LENGTH_LONG).show();
            if (saveUri != null)
            {
                final ProgressDialog progressDialog = new ProgressDialog( this );

                progressDialog.setMessage( "Uploading..." );
                progressDialog.show();

                String imageName = UUID.randomUUID().toString();

                final StorageReference imageFolder = storageReference.child( "images/"+imageName );

                imageFolder.putFile( saveUri )
                        .addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();

                                imageFolder.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Map<String,Object> update = new HashMap <>(  );

                                        update.put( "avatarUrl",uri.toString() );

                                        DatabaseReference riderInformation = FirebaseDatabase.getInstance().getReference(Common.Parent_information_tb1);
                                        riderInformation.child( FirebaseAuth.getInstance().getCurrentUser().getUid() )
                                                .updateChildren( update ).addOnCompleteListener( new OnCompleteListener <Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    imageAvatar.setImageURI( saveUri );
                                                    Toasty.success( Home.this, "Image Was Uploaded", Toast.LENGTH_LONG, true ).show();
                                                }
                                                else
                                                    Toasty.error( Home.this ,"Image wasn't Updated !",Toast.LENGTH_LONG,true).show();
                                            }
                                        } ).addOnFailureListener( new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toasty.error( Home.this ,e.getMessage(),Toast.LENGTH_LONG,true).show();

                                            }
                                        } );
                                        ;                               }
                                } );

                            }
                        } ).addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());

                        progressDialog.setMessage( "Uploaded " +progress +" %" );
                    }
                } );
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseRecyclerAdapter<ChildUser, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter <ChildUser, ViewHolder>(
                        ChildUser.class,
                        R.layout.list_layout,
                        ViewHolder.class,
                        mReference
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, ChildUser model, int position) {
                        viewHolder.setDetails( getApplicationContext(),model.getcName(),model.getcEmail(),model.getcAvatarUrl(),model.getcPhone(),model.getcPassword(),model.getCkey(),currentUser );
                        // waitingDialog.dismiss();
                    }
                };
        mRecyclerView.setAdapter( firebaseRecyclerAdapter );
    }

    private void ChooseImageandUpload() {
        Intent intent = new Intent(  );
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( Intent.createChooser(  intent,"Select Picture"),Common.PICK_IMAGE_REQUEST );
    }

    private void SignOut() {

            // Reset Remeber VALue
            Paper.init( this );
            Paper.book().destroy();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent( Home.this,MainActivity.class );
            startActivity( intent );
            finish();

    }
}
