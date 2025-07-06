package com.example.scholarship;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.security.MessageDigest;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    EditText emailInput, passwordInput;
    TextView forgotPasswordLink, goToSignup;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
        //thirja e animation
        View rootLayout = findViewById(R.id.login_layout); // make sure you have this ID
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        rootLayout.startAnimation(zoomIn);
        //

    //
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        forgotPasswordLink = findViewById(R.id.forgot_password);
        goToSignup = findViewById(R.id.go_to_signup);
        dbHelper = new DatabaseHelper(this);
        ImageView backArrow = findViewById(R.id.back_arrow);
    //
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Invalid email format");
                return;
            }
            if (!isPasswordValid(password)) {
                passwordInput.setError( "Password must be at least 8 characters,\ninclude upper/lowercase, digit and symbol");
                return;
            }

            String hashedPassword = hashPassword(password);
            int userId;
            try {
                userId = dbHelper.getUserIdByCredentials(email, hashedPassword);
            } catch (Exception e) {
                Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (userId != -1)
            {
                // Ruaj user_id dhe login status
                SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("user_id", userId);
                editor.putString("user_email", email);
                editor.putBoolean("isLoggedIn", true);
                editor.apply();
                if (email.equals("florentina@gmail.com"))
                {
                    // Shko në admin panel
                    startActivity(new Intent(this, ActivityAdminView.class));
                }
                else
                {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,DashboardActivity.class));
                    finish();
                }
            }
            else
            {
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

}
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
