package com.example.scholarship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView nameText, emailText, phoneText;
    Button editProfileButton;
    DatabaseHelper dbHelper;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = findViewById(R.id.name_field);
        emailText = findViewById(R.id.email_field);
        phoneText = findViewById(R.id.phone_field);
        editProfileButton = findViewById(R.id.edit_profile_button);
        dbHelper = new DatabaseHelper(this);

        // Merr user_id nga SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            AlertDialog dialog = new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Authentication Required")
                    .setMessage("You need to login to continue.")
                    .setPositiveButton("Login", (dialog1, which) -> {
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    })
                    .setNegativeButton("Cancel", null)
                    .setIcon(R.drawable.ic_warning)
                    .create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.teal_700));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purple_500));

        }

        loadUserData(userId);

        // Kur klikohet butoni Edit Profile
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
        // Bottom Navigation
        BottomNavHandler.setup(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // rifresko te dhenat kur te kthehet nga edit
        loadUserData(userId);
    }

    private void loadUserData(int userId) {
        Cursor cursor = dbHelper.getUserById(userId);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"));

            nameText.setText(name);
            emailText.setText(email);
            phoneText.setText(phone);

            cursor.close();
        }
        //logout buton
        Button logoutButton = findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(v -> {
            AlertDialog dialog =new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Confirm Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Logout", (dialogInterface, which) -> {
                        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        prefs.edit().clear().apply();

                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
            dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
        });

    }
}

