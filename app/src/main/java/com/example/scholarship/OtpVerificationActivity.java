package com.example.scholarship;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class OtpVerificationActivity extends AppCompatActivity {
    EditText otpInput;
    Button verifyButton, resendButton;
    TextView otpTimer;
    String generatedOtp, email;
    CountDownTimer countDownTimer;
    final int OTP_DURATION_MS = 2 * 60 * 1000; // 2 minutes
    boolean otpExpired = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        otpInput = findViewById(R.id.otp_input);
        verifyButton = findViewById(R.id.verify_button);
        resendButton = findViewById(R.id.resend_button);
        otpTimer = findViewById(R.id.otp_timer);

        Intent intent = getIntent();
        generatedOtp = intent.getStringExtra("otp");
        email = intent.getStringExtra("email");

        if (generatedOtp == null || email == null) {
            Toast.makeText(this, "Error: Missing OTP or email", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        startOtpCountdown(); // ⏳ Start timer

        verifyButton.setOnClickListener(v -> {
            String enteredOtp = otpInput.getText().toString().trim();
            if (otpExpired) {
                Toast.makeText(this, "OTP expired. Please resend.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (enteredOtp.equals(generatedOtp)) {
                Toast.makeText(this, "OTP Verified. Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }
        });



        private void startOtpCountdown() {
            if (countDownTimer != null) countDownTimer.cancel();
            countDownTimer = new CountDownTimer(OTP_DURATION_MS, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int min = (int) (millisUntilFinished / 1000) / 60;
                    int sec = (int) (millisUntilFinished / 1000) % 60;
                    otpTimer.setText(String.format(Locale.getDefault(), "Code expires in: %02d:%02d", min, sec));
                }








}
