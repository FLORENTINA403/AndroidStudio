package com.example.scholarship;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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








}
