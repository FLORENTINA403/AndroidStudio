package com.example.scholarship;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.Cursor;
import android.content.ContentResolver;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ActivityApply extends AppCompatActivity {

    Button applyButton, uploadPdfButton;
    String selectedPdfPath = ""; // ruan path-in lokal të dokumentit të ngarkuar

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

            // Trego vetëm që u dërgua me sukses
            Toast.makeText(this, "Application captured successfully (not saved)", Toast.LENGTH_LONG).show();

            // Reset fushat
            nameInput.setText("");
            surnameInput.setText("");
            emailInput.setText("");
            idInput.setText("");
            phoneInput.setText("");
            levelAutoComplete.setText("");
            fieldAutoComplete.setText("");
            selectedPdfPath = "";
        });
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
