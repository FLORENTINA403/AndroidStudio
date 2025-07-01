package com.example.scholarship;

import androidx.appcompat.app.AppCompatActivity;
// EditProfileActivity.java
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {

        EditText fullNameInput, emailInput, phoneInput;
        Button saveButton;
        DatabaseHelper dbHelper;
        int userId;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fullNameInput = findViewById(R.id.edit_name);
        emailInput = findViewById(R.id.edit_email);
        phoneInput = findViewById(R.id.edit_phone);
        saveButton = findViewById(R.id.save_button);
        dbHelper = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        finish();
        return;
        }

        loadUserData(userId);

        saveButton.setOnClickListener(v -> {
        String fullName = fullNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String phoneNumber = phoneInput.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        return;
        }

        boolean updated = dbHelper.updateUserProfile(userId, fullName, email, phoneNumber);

        if (updated) {
        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
        finish();
        } else {
        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
        });
        }

private void loadUserData(int userId) {
        Cursor cursor = dbHelper.getUserById(userId);
        if (cursor != null && cursor.moveToFirst()) {
        fullNameInput.setText(cursor.getString(cursor.getColumnIndexOrThrow("fullname")));
        emailInput.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        phoneInput.setText(cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber")));
        cursor.close();
        }
        }
        }