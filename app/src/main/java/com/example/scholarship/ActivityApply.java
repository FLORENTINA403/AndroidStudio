package com.example.scholarship;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        AutoCompleteTextView levelAutoComplete = findViewById(R.id.level_autocomplete);
        AutoCompleteTextView fieldAutoComplete = findViewById(R.id.field_autocomplete);
        String[] levels = {"Bachelor", "Master", "PhD"};
        String[] fields = {"Computer Science", "Engineering", "Business", "Law", "Medicine", "Art"};

        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, levels);
        levelAutoComplete.setAdapter(levelAdapter);
        levelAutoComplete.setOnClickListener(v -> levelAutoComplete.showDropDown());

        ArrayAdapter<String> fieldAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, fields);
        fieldAutoComplete.setAdapter(fieldAdapter);
        fieldAutoComplete.setOnClickListener(v -> fieldAutoComplete.showDropDown());

        dbHelper = new DatabaseHelper(this);
        EditText nameInput = findViewById(R.id.fullname_input);
        EditText surnameInput = findViewById(R.id.lastname_input);
        EditText emailInput = findViewById(R.id.email_input);
        EditText idInput = findViewById(R.id.id_input);
        EditText phoneInput = findViewById(R.id.tel_input);
        applyButton = findViewById(R.id.apply_button);
        uploadPdfButton = findViewById(R.id.upload_pdf_button);

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

            if (fullname.isEmpty() || surname.isEmpty() || email.isEmpty() || personalId.isEmpty()
                    || phone.isEmpty() || level.isEmpty() || field.isEmpty() || selectedPdfPath.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields and upload a PDF", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean inserted = dbHelper.insertScholarshipApplication(fullname, surname, email, personalId, phone, level, field, selectedPdfPath);
            if (inserted) {
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

                // SHFAQ NJOFTIMIN
                showCustomNotification();
            }
            else{
                Toast.makeText(this, "Application failed", Toast.LENGTH_SHORT).show();
            }
        });
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
