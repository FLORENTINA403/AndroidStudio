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
    private Context context;
    private List<AplicationModel> list;
    private DatabaseHelper dbHelper;
    private int selectedPosition = RecyclerView.NO_POSITION;

    setContentView(R.layout.activity_admin_view);

    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    TextView totalApplicants = findViewById(R.id.total_applicants);

    ublic AplicationAdapter(Context context, List<AplicationModel> list) {
        this.context = context;
        this.list = list;
        this.dbHelper = new DatabaseHelper(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, field, level, personalId, phone;
        Button pdfPath;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.applicant_name);
            email = itemView.findViewById(R.id.applicant_email);
            field = itemView.findViewById(R.id.applicant_field);
            level = itemView.findViewById(R.id.applicant_level);
            personalId = itemView.findViewById(R.id.applicant_id);
            phone = itemView.findViewById(R.id.applicant_phone);
            pdfPath = itemView.findViewById(R.id.view_pdf_button);
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_application, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AplicationModel model = list.get(position);

            holder.name.setText(model.getFullname() + " " + model.getSurname());
            holder.email.setText(model.getEmail());
            holder.personalId.setText("ID: " + model.getPersonalId());
            holder.phone.setText("Phone: " + model.getPhone());
            holder.level.setText("Level: " + model.getLevel());
            holder.field.setText("Field: " + model.getField());
        }


    }
}