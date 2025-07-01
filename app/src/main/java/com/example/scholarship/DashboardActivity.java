package com.example.scholarship;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //pjesa e butonit apply
        Button appplyButton=findViewById(R.id.btn_apply_scholarship);
        appplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ActivityApply.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in_fade, R.anim.zoom_out_fade); // opsionale, për animacion
            }
        });

        //pjesa e butonit logout
        Button logoutButton = findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(v -> {
            // Kalo te menuja kryesore (MainActivity)
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Mbyll DashboardActivity që të mos kthehet me "Back"
        });




    }}