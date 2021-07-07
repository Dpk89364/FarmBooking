package com.example.farmbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity {

    EditText name;
    EditText mmail;
    EditText mpassword,mpassword2;
    EditText phone;
    ProgressBar progressBar;
    Button save;
    FirebaseAuth fauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        name = findViewById(R.id.name);
        mmail = findViewById(R.id.mail);
        mpassword = findViewById(R.id.password);
        mpassword2 = findViewById(R.id.password2);
        phone = findViewById(R.id.Phone);
        progressBar = findViewById(R.id.progressBar2);
        fauth = FirebaseAuth.getInstance();
        save = findViewById(R.id.submit);

        //signup button onclick listener.
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chnull()==false || mailsch()==false || passch()==false || phch()==false)
                {
                    return;
                }

                String mail = mmail.getText().toString().trim();
                String password = mpassword.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                fauth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterUser.this,"User Created.",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),Rentorsell.class));
                        }
                        else
                        {
                            Toast.makeText(RegisterUser.this,"Error!" + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
    public void loginaction(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
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
        String pa2 = mpassword2.getText().toString();
        Boolean a = Pattern.matches(pass_pttern,pa);
        if (pa.isEmpty())
        {
            mpassword.requestFocus();
            mpassword.setError("Null Can not Accepted.");
            return false;
        }
        if(pa2.isEmpty())
        {
            mpassword2.requestFocus();
            mpassword2.setError("Null Can not Accepted.");
            return false;
        }
        if (a==false)
        {
            mpassword.requestFocus();
            mpassword.setError("Password Must Include One Capital Character, One small Character, One Spacial Character And Minimum Length Should Be 4");
            return false;
        }
        if(!pa.equals(pa2))
        {
            mpassword2.requestFocus();
            mpassword2.setError("Password does not match.");
            return false;
        }

        return true;


    }
    //phone pattern.
    public Boolean phch()
    {
        String ph_pttern = "^(?=.*[0-9]).{10}$";
        String ph = phone.getText().toString();
        Boolean b = Pattern.matches(ph_pttern,ph);
        if (ph.isEmpty())
        {
            phone.requestFocus();
            phone.setError("Null Can not Accepted.");
            return false;
        }
        if (b==false)
        {
            phone.requestFocus();
            phone.setError("Enter with Proper format.");
            return false;
        }
        return true;
    }
    //name null check.
    public Boolean chnull()
    {
        String f = name.getText().toString();
        if (f.isEmpty())
        {
            name.requestFocus();
            name.setError("Enter First Name");
            return false;
        }
        else
        {
            return true;
        }

    }
}