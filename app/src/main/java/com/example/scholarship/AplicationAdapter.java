package com.example.newproj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class AplicationAdapter extends RecyclerView.Adapter<AplicationAdapter.ViewHolder> {
    setContentView(R.layout.activity_admin_view);
    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    TextView totalApplicants = findViewById(R.id.total_applicants);

}