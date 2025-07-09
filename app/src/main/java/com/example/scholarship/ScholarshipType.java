package com.example.scholarship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class ScholarshipType extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_types);

        Button meritButton = findViewById(R.id.btn_apply_merit);
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        //pjesa e butonave
        meritButton.setOnClickListener(v -> {
            if (isLoggedIn) {
                // user is logged in
                Intent intent = new Intent(ScholarshipType.this, ActivityApply.class);
                intent.putExtra("scholarship_type", "Merit-based Scholarship");
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in_fade, R.anim.zoom_out_fade);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(ScholarshipType.this)
                        .setTitle("Authentication Required")
                        .setMessage("You need to login to continue.")
                        .setPositiveButton("Login", (dialog1, which) -> {
                            startActivity(new Intent(ScholarshipType.this, LoginActivity.class));
                        })
                        .setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.ic_warning)
                        .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.teal_700));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purple_500));

            }
        });

        Button socialbuton = findViewById(R.id.btn_apply_social);
        socialbuton.setOnClickListener(v -> {
            if (isLoggedIn) {

                Intent intent = new Intent(ScholarshipType.this, ActivityApply.class);
                intent.putExtra("scholarship_type", "Social-based Scholarship");
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in_fade, R.anim.zoom_out_fade);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(ScholarshipType.this)
                        .setTitle("Authentication Required")
                        .setMessage("You need to login to continue.")
                        .setPositiveButton("Login", (dialog1, which) -> {
                            startActivity(new Intent(ScholarshipType.this, LoginActivity.class));
                        })
                        .setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.ic_warning)
                        .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.teal_700));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purple_500));

            }

        });

        Button InternationButton = findViewById(R.id.btn_apply_international);
        InternationButton.setOnClickListener(v -> {
            if (isLoggedIn) {
                // user is logged in
                Intent intent = new Intent(ScholarshipType.this, ActivityApply.class);
                intent.putExtra("scholarship_type", "International-based Scholarship");
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in_fade, R.anim.zoom_out_fade);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(ScholarshipType.this)
                        .setTitle("Authentication Required")
                        .setMessage("You need to login to continue.")
                        .setPositiveButton("Login", (dialog1, which) -> {
                            startActivity(new Intent(ScholarshipType.this, LoginActivity.class));
                        })
                        .setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.ic_warning)
                        .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.teal_700));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purple_500));

            }
        });

        //back button
        ImageView backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(v -> finish());


    }
}
