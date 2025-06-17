package com.example.scholarship;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    EditText emailInput, passwordInput;
    TextView forgotPasswordLink, goToSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    //
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        forgotPasswordLink = findViewById(R.id.forgot_password);
        goToSignup = findViewById(R.id.go_to_signup);
        ImageView backArrow = findViewById(R.id.back_arrow);
    //
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
    });
        // Shkon në faqen Forgot Password
        forgotPasswordLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
            startActivity(intent);
        });

        // Shkon në faqen e Sign Up
        goToSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
        // Butoni për kthim mbrapa
        backArrow.setOnClickListener(v -> finish());

}}
