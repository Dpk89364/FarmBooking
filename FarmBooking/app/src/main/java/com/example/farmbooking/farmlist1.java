package com.example.farmbooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class farmlist1 extends AppCompatActivity {

    private Toolbar toolbar;
    ListView listViewProperties;
    List<Upload> propertiesList;
    private DatabaseReference myRef,property_list;
    private String userID;
    private FirebaseAuth mAuth;
    public static ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmlist1);
        progressDialog=new ProgressDialog(farmlist1.this);
        setContentView(R.layout.activity_farmlist1);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_baseline_filter_vintage_24);
        toolbar.setPadding(10,0,0,0);
        setSupportActionBar(toolbar);

        listViewProperties = findViewById(R.id.farmlist);
        propertiesList = new ArrayList<>();

        myRef = FirebaseDatabase.getInstance().getReference().child("users");
        property_list = FirebaseDatabase.getInstance().getReference().child("rental_list");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        userID = user.getUid();

        listViewProperties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Upload upload =  propertiesList.get(i);

                showDialogBox(upload.getmRentalID(),upload.getName(),upload.getEmail(),upload.getPhone(),upload.getPropertyType(),upload.getmPrice(),upload.getAddress(),upload.getmSize(),upload.getmlandmark(),upload.getmpincode());

            }
        });


    }



    private void showDialogBox(final String rentalID,final String name, final String email, final String phone, final String type, final String price, final String address, final String size, final String landmark, final String pincode) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(farmlist1.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dilog_box, null);
        dialogBuilder.setView(dialogView);


        final Button buttonCancel = (Button) dialogView.findViewById(R.id.btnClose);
        final Button buttonReport = (Button) dialogView.findViewById(R.id.btnReport);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);

        dialogBuilder.setTitle("Your Property " + landmark);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDialogBox(rentalID);
                alertDialog.dismiss();
            }
        });

        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Price=price;
                String Size=size;
                String Property_type=type;
                String Phone=phone;
                String Name=name;
                String Address=address;
                String Email=email;
                String Pincode=pincode;
                String Landmark=landmark;



                Intent reportIntent = new Intent(farmlist1.this, Report.class);
                reportIntent.putExtra("PR", Price);
                reportIntent.putExtra("S", Size);
                reportIntent.putExtra("T", Property_type);
                reportIntent.putExtra("A", Address);
                reportIntent.putExtra("N", Name);
                reportIntent.putExtra("P", Phone);
                reportIntent.putExtra("E", Email);
                reportIntent.putExtra("PIN", Pincode);
                reportIntent.putExtra("L", Landmark);



                startActivity(reportIntent);
                alertDialog.dismiss();
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
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
                Toast.makeText(farmlist1.this,"Signout Successful!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void add_farm(View view)
    {
        startActivity(new Intent(getApplicationContext(),addfarm.class));
    }




    private void updateDialogBox(final String rentalID){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(farmlist1.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update, null);
        dialogBuilder.setView(dialogView);


        final Spinner spinner_propertyTypeAd = (Spinner) dialogView.findViewById(R.id.type);
        final EditText edit_text_price =(EditText)dialogView.findViewById(R.id.edit_text_price);
        final EditText edit_text_address=(EditText)dialogView.findViewById(R.id.edit_text_address);
        final EditText edit_text_size=(EditText)dialogView.findViewById(R.id.edit_text_size);
        final EditText edit_text_landmark=(EditText)dialogView.findViewById(R.id.edit_text_landmark);
        final EditText edit_text_pincode=(EditText)dialogView.findViewById(R.id.edit_text_pincode);
        final EditText edit_text_file_name=(EditText)dialogView.findViewById(R.id.edit_text_file_name);
        final EditText edit_text_phone=(EditText)dialogView.findViewById(R.id.edit_text_phone);
        final EditText edit_text_email=(EditText)dialogView.findViewById(R.id.edit_text_email);




        final Button button_update = (Button) dialogView.findViewById(R.id.button_update);
        final Button button_delete = (Button) dialogView.findViewById(R.id.button_delete);
        final Button button_cancel = (Button) dialogView.findViewById(R.id.button_cancel);

        dialogBuilder.setTitle("Updating Property ");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProperties(rentalID);
                alertDialog.dismiss();
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String price = edit_text_price.getText().toString().trim();
                String add=edit_text_address.getText().toString().trim();
                String size=edit_text_size.getText().toString().trim();
                String name=edit_text_file_name.getText().toString().trim();
                String landmark=edit_text_landmark.getText().toString().trim();
                String pincode=edit_text_pincode.getText().toString().trim();
                String ph=edit_text_phone.getText().toString().trim();
                String mail=edit_text_email.getText().toString().trim();
                String pro_types = spinner_propertyTypeAd.getSelectedItem().toString();


                updateProperties(rentalID,price,add,size,name,ph,mail,pro_types,landmark,pincode);

                alertDialog.dismiss();

            }
        });

        DatabaseReference mFirebaseRef = FirebaseDatabase.getInstance().getReference().child("users");

        Query queryLocation = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_address.setText(dataSnapshot.child("address").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Query queryRental = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryRental.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_price.setText(dataSnapshot.child("price").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query queryAddress = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_email.setText(dataSnapshot.child("email").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query queryPostcode = mFirebaseRef.child(userID).child("rental").child(rentalID);



        queryPostcode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_landmark.setText(dataSnapshot.child("landmark").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        queryPostcode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_pincode.setText(dataSnapshot.child("pincode").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query queryTenancyLength = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryTenancyLength.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_file_name.setText(dataSnapshot.child("name").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query queryTenantName = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryTenantName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_phone.setText(dataSnapshot.child("phone").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Query queryManagementName = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryManagementName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_size.setText(dataSnapshot.child("size").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void deleteProperties(String propertyID){
        DatabaseReference drProperties = FirebaseDatabase.getInstance().getReference("users").child(userID).child("rental").child(propertyID);
        DatabaseReference drProperties1 = FirebaseDatabase.getInstance().getReference("rental_list").child(propertyID);
        drProperties.removeValue();
        drProperties1.removeValue();
        Toast.makeText(farmlist1.this, "Deleted Property", Toast.LENGTH_SHORT).show();
    }

    private boolean updateProperties (String rentalID, String price, String add, String size, String name,String ph, String mail, String pro_types, String landmark, String pincode){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("rental").child(rentalID);
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("rental_list").child(rentalID);

        Upload upload = new Upload(name,mail,ph,pro_types,price,add,size,landmark,pincode,mail,rentalID);

        databaseReference.setValue(upload);
        databaseReference1.setValue(upload);

        Toast.makeText(farmlist1.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

        return true;
    }


    @Override
    public void onStart() {
        super.onStart();

        showProgressBar();
        FirebaseDatabase.getInstance().getReference("users").getRef().child(userID).child("rental").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                propertiesList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Upload upload = data.getValue(Upload.class);

                    propertiesList.add(upload);
                }

                progressDialog.dismiss();
                ImageAdapter adapter = new ImageAdapter(farmlist1.this, propertiesList);
                listViewProperties.setAdapter(adapter);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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