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
    static String userID;

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

        fAuth = FirebaseAuth.getInstance();                                                 // Kết nối với Firebase Authentication - CSDL đăng nhập đăng ký của google

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getEditText().getText().toString().trim();          // Lấy Email từ giao diện đăng nhập
                String password = txtPassword.getEditText().getText().toString().trim();    // Lấy Mật khẩu từ giao diện đăng nhập
                if (TextUtils.isEmpty(email)) {                                             // Check để trống Email
                    txtEmail.setError("Email is empty");
                    return;
                }
                if (TextUtils.isEmpty(password)) {                                          // Check để trống mật khẩu
                    txtPassword.setError("Password is empty");
                    return;
                }
                if (password.length() < 6) {                                                // Check Pass phải trên 6 ký tự
                    txtPassword.setError("Must more than 6 letters");
                    return;
                }

                // Sử dụng Firebase Authentication để đăng ký và đăng nhập
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {               // Xử lý nếu đăng nhập thành công ( task gọi là tác vụ - nếu tác vụ ok thì làm )
                        if (task.isSuccessful()) {                                          // trường hợp OK:
                            FirebaseUser user = fAuth.getCurrentUser();                    // Lấy tài khoản vừa đăng nhập

                            Intent loginIntent = new Intent(LoginActivity.this, HomeActivity.class);    // Tạo Intent chuyển tới trang chủ là Home
                            //loginIntent.putExtra("LoginId", user.getEmail());                                   // Gửi kèm theo Email của người vừa đăng nhập
                            userID = user.getEmail();
                            startActivity(loginIntent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",        // trường hợp đăng nhập thất bại thì báo lỗi
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