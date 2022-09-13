package com.example.authenticationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView toRegister;
    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;
    EditText mail;
    EditText pass;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toRegister=findViewById(R.id.toRegister);
        mail=findViewById(R.id.email);
        pass=findViewById(R.id.Pass);
        login=findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                Toast.makeText(getApplicationContext(), "To Register Activity", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

    }

    private void performLogin() {
        
            String email = mail.getText().toString();
            String password= pass.getText().toString();
            
            if(!email.matches(emailPattern)){
                mail.setError("Enter Correct Password");
            }else if(password.isEmpty() || password.length()<6){
                mail.setError("Enter proper password");
            }
            else{
                progressDialog.setTitle("Login");
                progressDialog.setMessage("Please Wait While Logging In....");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                FirebaseApp.initializeApp(this);
                mAuth=FirebaseAuth.getInstance();
                mUser=mAuth.getCurrentUser();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            progressDialog.dismiss();
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);//making sure it will not come to this activity again
                            Toast.makeText(getApplicationContext(), "Logged in successfull", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }
