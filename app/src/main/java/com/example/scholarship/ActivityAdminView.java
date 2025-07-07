package com.example.scholarship;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class ActivityAdminView extends AppCompatActivity {
    private RecyclerView applicantsRecyclerView;
    private AplicationAdapter adapter;
    private List<AplicationModel> applicationList;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        databaseHelper = new DatabaseHelper(this);
        applicationList = databaseHelper.getAllApplicationsAsList();

        //set i adapter
        applicantsRecyclerView = findViewById(R.id.applicantsRecyclerView);
        applicantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AplicationAdapter(this, applicationList);
        applicantsRecyclerView.setAdapter(adapter);

    }

}

