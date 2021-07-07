package com.example.farmbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.farmbooking.farmlist1.showProgressBar;

public class farmlist2 extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageAdapter mAdapter;
    private ListView mListView;
    public static ProgressDialog progressDialog;


    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmlist2);
        progressDialog=new ProgressDialog(farmlist2.this);
        final String type = getIntent().getExtras().get("name").toString();
        mListView = findViewById(R.id.farmlist);
        mUploads = new ArrayList<>();

        showProgressBar();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("rental_list");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_baseline_filter_vintage_24);
        toolbar.setPadding(10,0,0,0);
        setSupportActionBar(toolbar);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final String typefech;
                    Map<?, ?> value = (Map<?, ?>) postSnapshot.getValue();
                    typefech = (String) value.get("propertyType");
                    if(type.equals(typefech))
                    {
                        Upload upload = postSnapshot.getValue(Upload.class);
                        mUploads.add(upload);
                    }
                }

                mAdapter = new ImageAdapter(farmlist2.this, mUploads);

                mListView.setAdapter(mAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(farmlist2.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Upload upload = mUploads.get(i);

                showDialogBox(upload.getName(), upload.getImageUrl(), upload.getAddress(), upload.getmSize(), upload.getPropertyType(), upload.getEmail(), upload.getPhone(), upload.getmPrice(), upload.getmlandmark(), upload.getmpincode());


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
                Toast.makeText(farmlist2.this,"Signout Successful!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void showDialogBox(final String name, final String url, final String address, final String size,
                               final String type, final String email, final String phone, final String price, final String landmark, final  String pincode) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(farmlist2.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_advert, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Advert From " + name);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        final TextView textViewPrice = dialogView.findViewById(R.id.text_view_h0);
        final TextView textViewType = dialogView.findViewById(R.id.text_view_h1);
        final TextView textViewFullAddress = dialogView.findViewById(R.id.text_view_h2);
        final TextView textviewSize = dialogView.findViewById(R.id.text_view_h3);
        final TextView textViewlandmark = dialogView.findViewById(R.id.text_view_h4);
        final TextView textviewpincode = dialogView.findViewById(R.id.text_view_h5);
        final TextView textViewName = dialogView.findViewById(R.id.text_view_h6);
        final TextView textViewPhone = dialogView.findViewById(R.id.text_view_h7);
        final TextView textViewEmail = dialogView.findViewById(R.id.text_view_h8);


        final ImageView imageView = dialogView.findViewById(R.id.dialog_ImageView);

        Picasso.with(this).load(url).placeholder(R.drawable.rent_dummy).fit().centerCrop().into(imageView);
        textViewName.setText("Name: " + name);
        textViewPhone.setText("Phone: " + phone);
        textViewEmail.setText("Email: " + email);

        textViewFullAddress.setText("Address: " + address);
        textViewPrice.setText("Price: "  + price + "$");
        textViewType.setText("Type: " + type);
        textviewSize.setText("Size: " + size + " sq. ft");
        textViewlandmark.setText("Landmark: " + landmark);
        textviewpincode.setText("Pincode: " + pincode);

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