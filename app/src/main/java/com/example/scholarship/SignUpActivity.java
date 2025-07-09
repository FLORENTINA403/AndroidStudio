package com.example.scholarship;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpActivity extends AppCompatActivity {
    EditText fullNameEditText, emailEditText,phoneNumberEditText;
    Button signUpButton;
    DatabaseHelper dbHelper;
    TextInputEditText passwordInput, confirmPasswordInput;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        fullNameEditText = findViewById(R.id.fullname_input);
        emailEditText = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        phoneNumberEditText=findViewById(R.id.number_input);
        signUpButton = findViewById(R.id.signup_button);
        // Inicilizimi i databazës
        dbHelper = new DatabaseHelper(this);

        signUpButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordInput.getText() != null ? passwordInput.getText().toString() : "";
            String confirmPassword = confirmPasswordInput.getText() != null ? confirmPasswordInput.getText().toString() : "";
            String phoneNumber=phoneNumberEditText.getText().toString();

            // Validimet
            if (fullName.isEmpty() || !fullName.matches("^[a-zA-Z\\s]+$")) {
                fullNameEditText.setError("Enter a valid name (letters only)");
                return;
            }

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.setError("Enter a valid email");
                return;
            }

            if (!isValidPassword(password)) {
                passwordInput.setError("Password must be at least 8 characters, with 1 uppercase, 1 lowercase, 1 digit, and 1 special character");
                return;
            }

            if (!password.equals(confirmPassword)) {
                confirmPasswordInput.setError("Passwords do not match");
                return;
            }
            if (phoneNumber.isEmpty() || !phoneNumber.matches("\\d{8,12}")) {
                phoneNumberEditText.setError("Enter a valid phone number (8-12 digits)");
                return;
            }


            // Hash password-it
            String hashedPassword = hashPassword(password);

            // Ruaje në databazë
            boolean inserted;

            try {
                inserted = dbHelper.insertUser(fullName, email, phoneNumber, hashedPassword);

                if (inserted) {
                    Toast.makeText(this, "Sign Up Successful. Welcome " + fullName + "!", Toast.LENGTH_LONG).show();
                    finish(); // Mbyll aktivitetin
                } else {
                    Toast.makeText(this, "Email already exists!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace(); // për log
            }

            //thirrja e botom nav
            BottomNavHandler.setup(this);


    });
}
        private boolean isValidPassword(String password)
        {
        // Password: min 8 char, 1 upper, 1 lower, 1 digit, 1 symbol
                String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
                 return password.matches(pattern);
         }
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        }
    }

}
