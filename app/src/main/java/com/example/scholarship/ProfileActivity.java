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
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadUserData(userId);

        // Kur klikohet butoni Edit Profile
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

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
    }

}
