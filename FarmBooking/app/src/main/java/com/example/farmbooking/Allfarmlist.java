package com.example.farmbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Allfarmlist extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allfarmlist);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_baseline_filter_vintage_24);
        toolbar.setPadding(10,0,0,0);
        setSupportActionBar(toolbar);
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
                Toast.makeText(Allfarmlist.this,"Signout Successful!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void dfarm(View view)
    {
        Intent intent = new Intent(view.getContext(),farmlist2.class);
        intent.putExtra("name","Decorative Farm");
        view.getContext().startActivity(intent);
    }
    public void sfarm(View view)
    {
        Intent intent = new Intent(view.getContext(),farmlist2.class);
        intent.putExtra("name","Simple Farm");
        view.getContext().startActivity(intent);
    }
    public void dhall(View view)
    {
        Intent intent = new Intent(view.getContext(),farmlist2.class);
        intent.putExtra("name","Decorative Hall");
        view.getContext().startActivity(intent);
    }
    public void shall(View view)
    {
        Intent intent = new Intent(view.getContext(),farmlist2.class);
        intent.putExtra("name","Simple Hall");
        view.getContext().startActivity(intent);
    }
}