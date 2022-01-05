package com.example.MyCollection.Acitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodnews.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    Button btnExitSignUp, btnReg;
    TextInputLayout email,password;
    ImageView logo_image;
    TextView logo_text,slogan_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        btnReg = findViewById(R.id.btnReg);
        btnExitSignUp = findViewById(R.id.btnExit);
        email = findViewById(R.id.txtRegEmail);
        password = findViewById(R.id.txtRegPassword);
        logo_image = findViewById(R.id.logo_res);
        logo_text = findViewById(R.id.text_res);
        slogan_name = findViewById(R.id.slogan_res);
        fAuth = FirebaseAuth.getInstance();

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = email.getEditText().getText().toString().trim();
                String strPassword = password.getEditText().getText().toString().trim();

                Log.e("email123", strEmail);
                Log.e("pass123", strPassword);

                if (TextUtils.isEmpty(strEmail)){
                    email.setError("Email is empty");
                    return;
                }
                if (TextUtils.isEmpty(strPassword)){
                    email.setError("Password is empty");
                    return;
                }
                if (strPassword.length()<6){
                    password.setError("Must more than 6 letters");
                    return;
                }
                fAuth.createUserWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this,"User created",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            Log.e("FirebaseAuth", "onComplete" + task.getException().getMessage());
                        }
                        else {
                            Toast.makeText(SignUpActivity.this,"Error" + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            Log.e("FirebaseAuth", "onComplete" + task.getException().getMessage());
                        }
                    }
                });

            }
        });
        btnExitSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View,String>(logo_image, "logo_image");
                pairs[1] = new Pair<View,String>(logo_text, "logo_text");
                pairs[2] = new Pair<View,String>(slogan_name, "slogan_name");
                pairs[3] = new Pair<View,String>(email, "text_username");
                pairs[4] = new Pair<View,String>(password, "text_password");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
    }
}