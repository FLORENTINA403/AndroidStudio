package com.example.scholarship;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.security.MessageDigest;
import java.util.Random;

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
                    //pjesa kur dergohet otp ne rast te userave te tjere nese eshte online nese eshte ofline kalo ne main
                    if (isConnectedToInternet()) {
                        String otp = generateOTP();
                        sendOtpEmail(email, otp); // You’ll implement this
                        Toast.makeText(this, "OTP sent to your email", Toast.LENGTH_SHORT).show();
                        Intent otpIntent = new Intent(this, OtpVerificationActivity.class);
                        otpIntent.putExtra("email", email);
                        otpIntent.putExtra("otp", otp);
                        startActivity(otpIntent);
                    } else {
                        proceedToMain(email); // Skip 2FA if offline
                    }
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

}
    private boolean isPasswordValid(String password) {
        String passwordPattern =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*/._-])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
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
    ///shiko per internet conection method per 2FA method
    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
    //gjenerimi i nje numri random per 2FA
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    // metoda main ne rast se je offline
    private void proceedToMain(String email) {
        Toast.makeText(this, "Login successful (offline mode)", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    //thirrja e optmethod
    private void sendOtpEmail(String email, String otp) {
        new Thread(() -> {
            EmailSender.sendOtp(email, otp);
        }).start(); // Send on background thread to avoid blocking UI
    }


}
