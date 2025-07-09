package com.example.scholarship;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

    EditText emailInput;
    Button nextButton;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        emailInput = findViewById(R.id.forgot_email_input);
        nextButton = findViewById(R.id.reset_password_button);
        dbHelper = new DatabaseHelper(this);
        TextView backArrow = findViewById(R.id.back_to_login);
        backArrow.setOnClickListener(v -> finish());

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();


                if (email.isEmpty()) {
                    emailInput.setError("Please enter your email");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError("Invalid email format");
                    return;
                }

                try {
                    boolean exists = dbHelper.checkIfEmailExists(email);

                    if (exists) {
                        Intent intent = new Intent(ForgotPassword.this, ResetPassword.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ForgotPassword.this, "Email not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ForgotPassword.this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

    }
}
