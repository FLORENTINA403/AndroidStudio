package com.example.scholarship;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;
import android.database.Cursor;
import android.content.ContentResolver;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ActivityApply extends AppCompatActivity {

    Button applyButton, uploadPdfButton;
    String selectedPdfPath = ""; // ruan path-in lokal të dokumentit të ngarkuar
    private final String CHANNEL_ID = "apply_channel";
    DatabaseHelper dbHelper;
    EditText nameInput, surnameInput, emailInput, idInput, phoneInput, scholarshipTypeInput;
    ImageView backarrow;
    AutoCompleteTextView levelAutoComplete, fieldAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        // Notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
        dbHelper = new DatabaseHelper(this);

        // Input fields
        nameInput = findViewById(R.id.fullname_input);
        surnameInput = findViewById(R.id.lastname_input);
        emailInput = findViewById(R.id.email_input);
        idInput = findViewById(R.id.id_input);
        phoneInput = findViewById(R.id.tel_input);
        levelAutoComplete = findViewById(R.id.level_autocomplete);
        fieldAutoComplete = findViewById(R.id.field_autocomplete);
        scholarshipTypeInput = findViewById(R.id.scholarship_type_text);
        applyButton = findViewById(R.id.apply_button);
        uploadPdfButton = findViewById(R.id.upload_pdf_button);

        String[] levels = {"Bachelor", "Master", "PhD"};
        String[] fields = {"Computer Science", "Engineering", "Business", "Law", "Medicine", "Art"};

        levelAutoComplete.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, levels));
        levelAutoComplete.setOnClickListener(v -> levelAutoComplete.showDropDown());

        fieldAutoComplete.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, fields));
        fieldAutoComplete.setOnClickListener(v -> fieldAutoComplete.showDropDown());

        // Set scholarship type from intent
        String scholarshipType = getIntent().getStringExtra("scholarship_type");
        if (scholarshipType != null && !scholarshipType.isEmpty()) {
            scholarshipTypeInput.setText(scholarshipType); // Show just the type
        } else {
            scholarshipTypeInput.setText("Not selected");
        }

        // PDF Picker

        ActivityResultLauncher<Intent> pdfPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        selectedPdfPath = savePdfToLocal(uri);
                        if (!selectedPdfPath.isEmpty()) {
                            Toast.makeText(this, "PDF uploaded successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to upload PDF", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        uploadPdfButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            pdfPickerLauncher.launch(intent);
        });

        applyButton.setOnClickListener(v -> {
            String fullname = nameInput.getText().toString().trim();
            String surname = surnameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String personalId = idInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String level = levelAutoComplete.getText().toString().trim();
            String field = fieldAutoComplete.getText().toString().trim();
            String scholarship_type = scholarshipTypeInput.getText().toString().trim();

            if (scholarship_type.equals("Not selected")) scholarship_type = "";

            Log.d("DEBUG", "Scholarship Type: " + scholarship_type);

            //validime te input fields
            if (fullname.isEmpty() || !fullname.matches("^[a-zA-Z\\s]+$")) {
                nameInput.setError("Enter a valid name (letters only)");
                return;
            }

            if (surname.isEmpty() || !surname.matches("^[a-zA-Z\\s]+$")) {
                surnameInput.setError("Enter a valid surname");
                return;
            }

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Enter a valid email");
                return;
            }

            if (personalId.isEmpty() || !personalId.matches("\\d{6,12}")) {
                idInput.setError("Enter a valid personal ID (6-12 digits)");
                return;
            }

            if (phone.isEmpty() || !phone.matches("\\d{8,12}")) {
                phoneInput.setError("Enter a valid phone number (8-12 digits)");
                return;
            }

            if (level.isEmpty()) {
                levelAutoComplete.setError("Please select your level");
                return;
            }

            if (field.isEmpty()) {
                fieldAutoComplete.setError("Please select your field");
                return;
            }

            if (scholarship_type.isEmpty()) {
                scholarshipTypeInput.setError("Scholarship type is required");
                return;
            }

            if (selectedPdfPath.isEmpty()) {
                Toast.makeText(this, "Please upload your PDF document", Toast.LENGTH_SHORT).show();
                return;
            }


            boolean inserted;
            try {
                inserted = dbHelper.insertScholarshipApplication(
                        fullname, surname, email, personalId, phone, level, field, selectedPdfPath, scholarship_type);
            } catch (Exception e) {
                Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
                return;
            }

            if (inserted) {
                //shtojm pjesen e audios
                // Tingulli
                MediaPlayer mediaPlayer = MediaPlayer.create(ActivityApply.this, R.raw.success_sound);
                mediaPlayer.start();
                Toast.makeText(this, "Application submitted successfully", Toast.LENGTH_SHORT).show();

                // Reset fushat
                nameInput.setText("");
                surnameInput.setText("");
                emailInput.setText("");
                idInput.setText("");
                phoneInput.setText("");
                levelAutoComplete.setText("");
                fieldAutoComplete.setText("");
                selectedPdfPath = "";
                scholarshipTypeInput.setText("Not selected");

                // SHFAQ NJOFTIMIN
                showCustomNotification();
            }
            else{
                Toast.makeText(this, "Application failed", Toast.LENGTH_SHORT).show();
            }
        });
        //back button
        ImageView backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(v -> finish());
    }
    private void showCustomNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Create channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Application Success", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Custom layout për njoftimin
        RemoteViews customView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        customView.setTextViewText(R.id.title_text, "🎉 Congratulations!");
        customView.setTextViewText(R.id.message_text, "Your application has been successfully submitted.");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_congratulation)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(customView)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(1001, builder.build());
    }


    private String savePdfToLocal(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(uri, null, null, null, null);
            String fileName = "uploaded_document.pdf";
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) fileName = cursor.getString(nameIndex);
                cursor.close();
            }

            File outputFile = new File(getFilesDir(), fileName);
            InputStream in = resolver.openInputStream(uri);
            OutputStream out = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
            return outputFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
