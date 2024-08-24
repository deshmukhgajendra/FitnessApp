package com.example.myfitnessapp.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessapp.MainActivity;
import com.example.myfitnessapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    TextView login;
    TextInputEditText usernameEditText,passwordEditText;
    Button loginButton , intentButton;
    FirebaseAuth mAuth;
    TextView nameTextView,emailTextView;
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           Intent intent= new Intent(getApplicationContext(),MainActivity.class);
           startActivity(intent);
           finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText=findViewById(R.id.usernameEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        loginButton=findViewById(R.id.loginButton);
        intentButton=findViewById(R.id.intentButton);


        mAuth=FirebaseAuth.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password;

                email=String.valueOf(usernameEditText.getText());
                password=String.valueOf(passwordEditText.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(login.this,"enter the email:",Toast.LENGTH_LONG);
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(login.this,"enter the password",Toast.LENGTH_LONG);
                    return;
                }


                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    finish();



                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(com.example.myfitnessapp.authentication.login.this,registration.class);
                                    startActivity(i);
                                }
                            }
                        });

            }
        });

        intentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(com.example.myfitnessapp.authentication.login.this,registration.class);
                startActivity(intent);
            }
        });
    }


}