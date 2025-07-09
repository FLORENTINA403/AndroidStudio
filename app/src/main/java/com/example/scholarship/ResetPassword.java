package com.example.scholarship;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ResetPassword extends AppCompatActivity {

    EditText newPassword, confirmPassword;
    Button resetButton;
    DatabaseHelper dbHelper;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        newPassword = findViewById(R.id.new_password_input);
        confirmPassword = findViewById(R.id.confirm_password_input);
        resetButton = findViewById(R.id.reset_button);
        dbHelper = new DatabaseHelper(this);
        TextView backToLogin = findViewById(R.id.back_to_login);

        // Merr emailin nga intenti
        userEmail = getIntent().getStringExtra("email");

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = newPassword.getText().toString();
                String confirm = confirmPassword.getText().toString();

                if (!isValidPassword(password)) {
                    newPassword.setError("At least 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 symbol");
                    return;
                }

                if (!password.equals(confirm)) {
                    confirmPassword.setError("Passwords do not match");
                    return;
                }

                String hashedPassword = hashPassword(password);
                boolean success = dbHelper.updatePassword(userEmail, hashedPassword);

                if (success) {
                    Toast.makeText(ResetPassword.this, "Password reset successfully!", Toast.LENGTH_LONG).show();
                    finish(); // Mbyll aktivitetin
                } else {
                    Toast.makeText(ResetPassword.this, "Error updating password", Toast.LENGTH_LONG).show();
                }
            }
        });

        //kthehu ne login
        backToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean isValidPassword(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(pattern);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}