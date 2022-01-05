package com.example.MyCollection.Acitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button btnSignup, btnLogin;
    ImageView logoLogin;
    TextView LogoName,slogan_name;
    TextInputLayout txtEmail,txtPassword;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        btnSignup = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        logoLogin = findViewById(R.id.logoLogin);
        LogoName = findViewById(R.id.LogoName);
        slogan_name = findViewById(R.id.slogan_name);
        txtEmail = findViewById(R.id.txtLoginEmail);
        txtPassword = findViewById(R.id.txtLoginPassword);

        fAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getEditText().getText().toString().trim();
                String password = txtPassword.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    txtEmail.setError("Email is empty");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    txtPassword.setError("Password is empty");
                    return;
                }
                if (password.length() < 6) {
                    txtPassword.setError("Must more than 6 letters");
                    return;
                }
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = fAuth.getCurrentUser();

                            Intent loginIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            loginIntent.putExtra("LoginId", user.getEmail());
                            startActivity(loginIntent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View,String>(logoLogin, "logo_image");
                pairs[1] = new Pair<View,String>(LogoName, "logo_text");
                pairs[2] = new Pair<View,String>(slogan_name, "slogan_name");
                pairs[3] = new Pair<View,String>(txtEmail, "text_username");
                pairs[4] = new Pair<View,String>(txtPassword, "text_password");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
    }
    @Override
    public void onBackPressed() {
        Activity context = LoginActivity.this;
        context.finish();
    }
}