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

public class RegisterActivity extends AppCompatActivity {
    EditText user;
    EditText pass;
    EditText repass;
    Button btn;
    TextView toLogin;
String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
FirebaseAuth mAuth;
FirebaseUser mUser;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        user = findViewById(R.id.User);
        pass=findViewById(R.id.Pass);
        repass=findViewById(R.id.rePass);
        btn=findViewById(R.id.register);
        toLogin=findViewById(R.id.tologin);
        progressDialog=new ProgressDialog(this);
        FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuth();
            }
        });
    }

    private void performAuth() {
        String email = user.getText().toString();
        String password= pass.getText().toString();
        String repassword = repass.getText().toString();
        if(!email.matches(emailPattern)){
            user.setError("Enter Correct Password");
        }else if(password.isEmpty() || password.length()<6){
            user.setError("Enter proper password");
        }else if(!password.equals(repassword)){
            user.setError("Password does'nt match");
        }
        else{
            progressDialog.setTitle("Registration");
            progressDialog.setMessage("Please Wait While Registration....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                        progressDialog.dismiss();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);//making sure it will not come to this activity again
                        Toast.makeText(getApplicationContext(), "Registration is completed succssfully", Toast.LENGTH_SHORT).show();
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