package com.example.farmbooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.farmbooking.farmlist1.progressDialog;

public class addfarm extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private String userID;
    private FirebaseAuth mAuth;
    private String imageURL;
    private Uri mImageUri;
    private ProgressBar mProgressBar;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef,myRef;
    public static ProgressDialog progressDialog;

    private StorageTask mUploadTask;

    Button camera,gallery,save,cancel;
    private Toolbar toolbar;
    EditText email,name,phone,size,price,address,pincode,landmark;
    private Spinner spinnerPropertyType;
    ImageView photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfarm);
        progressDialog=new ProgressDialog(addfarm.this);
        photo = findViewById(R.id.farmimage);
        gallery = findViewById(R.id.gallery);
        name = findViewById(R.id.name);
        email = findViewById(R.id.mail);
        phone = findViewById(R.id.Phone);
        size = findViewById(R.id.farmsize);
        price = findViewById(R.id.price);
        address = findViewById(R.id.address);
        landmark = findViewById(R.id.landmark);
        pincode = findViewById(R.id.pincode);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        toolbar = findViewById(R.id.toolbar);
        spinnerPropertyType = findViewById(R.id.spinner_propertyTypeAd);


        mStorageRef = FirebaseStorage.getInstance().getReference().child("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("rental_list");
        myRef = FirebaseDatabase.getInstance().getReference().child("users");


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        userID = user.getUid();

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(addfarm.this, "Upload in progress", Toast.LENGTH_SHORT).show();

                } else {
                    uploadFile();

                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),farmlist1.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(addfarm.this,"Signout Successful!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(photo);

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {

        if (mImageUri != null) {
            StorageReference Imagename = mStorageRef.child("image" + mImageUri.getLastPathSegment());



            Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    imageURL = String.valueOf(uri);
                }
            });

            Imagename.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(addfarm.this, "Upload successful", Toast.LENGTH_LONG).show();
                    String uploadId = mDatabaseRef.push().getKey();
                    Upload upload = new Upload(name.getText().toString().trim(), email.getText().toString().trim(), phone.getText().toString().trim(),spinnerPropertyType.getSelectedItem().toString(), price.getText().toString().trim(), address.getText().toString().trim(), size.getText().toString().trim(), landmark.getText().toString().trim(), pincode.getText().toString().trim(),  taskSnapshot.getStorage().getDownloadUrl().toString(), uploadId);


                    mDatabaseRef.child(uploadId).setValue(upload);

                    myRef.child(userID).child("rental").child(uploadId).setValue(upload);
                    startActivity(new Intent(getApplicationContext(),farmlist1.class));

                }
            });
        }
        else
        {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }



    }



    public static void showProgressBar() {
        progressDialog.show();

        progressDialog.setContentView(R.layout.progress_dialog);

        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }




}