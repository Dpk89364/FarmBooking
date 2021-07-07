package com.example.farmbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText mmail;
    EditText mpassword;
    ProgressBar progressBar;
    Button login;
    FirebaseAuth fauth;
    TextView forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mmail = findViewById(R.id.mail);
        mpassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fauth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        forgot = findViewById(R.id.forgotpassword);
        if (fauth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),Rentorsell.class));
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mailsch()==false || passch()==false)
                {
                    return;
                }
                String mail = mmail.getText().toString().trim();
                String password = mpassword.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                fauth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this,"LogIn Successful!",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),Rentorsell.class));
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Error!" + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText getmail = new EditText(view.getContext());
                final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("Reset Password?");
                alert.setMessage("Enter your Mail to Recive Resert Link.");
                alert.setView(getmail);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = getmail.getText().toString();
                        fauth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Reset Link Sent to Your Mobile.",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Error! Reset Link Not Sent to Your Mobile." + e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.create().show();
            }
        });

    }
    public  void registeraction(View view)
    {
        startActivity(new Intent(getApplicationContext(),RegisterUser.class));
    }


    public Boolean mailsch()
    {
        String mail_pttern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String ma = mmail.getText().toString();
        Boolean a = Pattern.matches(mail_pttern,ma);
        if (ma.isEmpty())
        {
            mmail.requestFocus();
            mmail.setError("Null Can not Accepted.");
            return false;
        }
        if (a==false)
        {
            mmail.requestFocus();
            mmail.setError("Enter in Proper Format.");
            return false;
        }
        return true;
    }
    //password pattern.
    public Boolean passch()
    {
        String pass_pttern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        String pa = mpassword.getText().toString();
        Boolean a = Pattern.matches(pass_pttern,pa);
        if (pa.isEmpty())
        {
            mpassword.requestFocus();
            mpassword.setError("Null Can not Accepted.");
            return false;
        }
        if (a==false)
        {
            mpassword.requestFocus();
            mpassword.setError("Password Must Include One Capital Character, One small Character, One Spacial Character And Minimum Length Should Be 4");
            return false;
        }
        return true;
    }
}