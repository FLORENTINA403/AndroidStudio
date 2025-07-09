package com.example.scholarship;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Butoni i login
        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        // Butoni i sign up
        Button signupButton = findViewById(R.id.btn_signup);
        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
        TextView viewAllBtn = findViewById(R.id.view_all_scholarships);
        viewAllBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, com.example.newproj.ScholarshipType.class));
        });

        //explore BUTON
        Button exploreBtn = findViewById(R.id.btn_explore);
        exploreBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScholarshipInfoActivity.class);
            startActivity(intent);
        });

        // Bottom Navigation
        BottomNavHandler.setup(this);
    }
}