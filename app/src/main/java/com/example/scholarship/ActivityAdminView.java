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
import com.github.mikephil.charting.charts.PieChart;

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
    private SearchView searchView;
    private TextView totalApplicantsText;
    private TextView linkMoreDetails;
    private TextView meritCountText, socialCountText, internationalCountText;
    private PieChart pieChart;
    private LinearLayout detailsContainer;
    private Button btnEdit, btnDelete, btnDownloadPdf;
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

        searchView = findViewById(R.id.search_view);
        totalApplicantsText = findViewById(R.id.total_applicants_text);
        meritCountText = findViewById(R.id.merit_count_text);
        socialCountText = findViewById(R.id.social_count_text);
        internationalCountText = findViewById(R.id.international_count_text);
        pieChart = findViewById(R.id.scholarship_pie_chart);
        linkMoreDetails = findViewById(R.id.link_more_details);
        detailsContainer = findViewById(R.id.details_container);
        btnEdit = findViewById(R.id.btn_edit_applicant);
        btnDelete = findViewById(R.id.btn_delete_applicant);
        btnDownloadPdf = findViewById(R.id.btn_download_pdf);

        totalApplicantsText.setText("Total Applicants: " + applicationList.size());
        // More details chart toggle
        linkMoreDetails.setOnClickListener(v -> {
            if (detailsContainer.getVisibility() == View.GONE) {
                detailsContainer.setVisibility(View.VISIBLE);
                updateScholarshipChart(adapter.getApplicationList());
                linkMoreDetails.setText("Hide details 🔼");
            } else {
                detailsContainer.setVisibility(View.GONE);
                linkMoreDetails.setText("More details 🔽");
            }
        });

        // Edit selected applicant
        btnEdit.setOnClickListener(v -> {
            AplicationModel selected = adapter.getSelectedItem();
            if (selected != null) {
                adapter.showEditDialog(selected);
            } else {
                Toast.makeText(this, "Please select an applicant to edit", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete selected applicant
        btnDelete.setOnClickListener(v -> {
            AplicationModel selected = adapter.getSelectedItem();
            if (selected != null) {
                adapter.deleteSelected();
            } else {
                Toast.makeText(this, "Please select an applicant to delete", Toast.LENGTH_SHORT).show();
            }
        });

        // PDF download
        btnDownloadPdf.setOnClickListener(v -> generatePdf(adapter.getApplicationList()));

        // Search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });

        //logut
        ImageView btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit().clear().apply();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

    }

    private void performSearch(String query) {
        List<AplicationModel> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList = applicationList;
        } else {
            for (AplicationModel app : applicationList) {
                if ((app.getFullname() != null && app.getFullname().toLowerCase().contains(query)) ||
                        (app.getSurname() != null && app.getSurname().toLowerCase().contains(query)) ||
                        (app.getEmail() != null && app.getEmail().toLowerCase().contains(query)) ||
                        (app.getPersonalId() != null && app.getPersonalId().toLowerCase().contains(query)) ||
                        (app.getPhone() != null && app.getPhone().toLowerCase().contains(query)) ||
                        (app.getField() != null && app.getField().toLowerCase().contains(query)) ||
                        (app.getLevel() != null && app.getLevel().toLowerCase().contains(query))) {
                    filteredList.add(app);
                }
            }
        }
        adapter.setFilteredList(filteredList);
        totalApplicantsText.setText("Total Applicants: " + filteredList.size());
        if (detailsContainer.getVisibility() == View.VISIBLE) {
            updateScholarshipChart(filteredList);
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No matching results found", Toast.LENGTH_SHORT).show();
        }
    }




}

}

