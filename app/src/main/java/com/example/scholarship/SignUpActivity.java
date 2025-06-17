package com.example.scholarship;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpActivity extends AppCompatActivity {
    EditText fullNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    Button signUpButton;
    DatabaseHelper dbHelper;
    @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

        // Inicilizimi i komponenteve
        fullNameEditText = findViewById(R.id.fullname_input);
        emailEditText = findViewById(R.id.email_input);
        passwordEditText = findViewById(R.id.password_input);
        confirmPasswordEditText = findViewById(R.id.confirm_password_input);
        signUpButton = findViewById(R.id.signup_button);
        ImageView backArrow = findViewById(R.id.back_arrow);

        signUpButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();


            // Validimet
            if (fullName.isEmpty()) {
                fullNameEditText.setError("Name is required");
                return;
            }

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.setError("Enter a valid email");
                return;
            }

            if (!isValidPassword(password)) {
                passwordEditText.setError("Password must be at least 8 characters, with 1 uppercase, 1 lowercase, 1 digit, and 1 special character");
                return;
            }

            if (!password.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Passwords do not match");
                return;
            }
            // Hash password-it
            String hashedPassword = hashPassword(password);

            // Ruaje në databazë
            boolean inserted = dbHelper.insertUser(fullName, email, hashedPassword);

            if (inserted) {
                Toast.makeText(this, "Sign Up Successful. Welcome " + fullName + "!", Toast.LENGTH_LONG).show();
                finish(); // Mbyll aktivitetin
            } else {
                Toast.makeText(this, "Email already exists!", Toast.LENGTH_SHORT).show();
            }

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
            return password; // fallback (e pasigurtë)
        }
    }

}
