package com.example.scholarship;
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

    public AplicationAdapter(Context context, List<AplicationModel> list) {
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
            //select item per me editu aplicantin
            itemView.setOnClickListener(v -> {
                int previousPosition = selectedPosition;
                selectedPosition = getAdapterPosition();
                if (previousPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousPosition); // unselect previous
                }
                notifyItemChanged(selectedPosition); // select new
            });
        }
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
            //tek pjesa e selektimit te aplikantit
            if (position == selectedPosition) {
                holder.itemView.setBackgroundColor(Color.parseColor("#D0E8FF")); // light blue highlight
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT); // default
            }

            String pdfFileName = new File(model.getPdfPath().trim()).getName();
            holder.pdfPath.setText("PDF: " + pdfFileName);

            holder.pdfPath.setOnClickListener(v -> {
                String pdfPath = model.getPdfPath();

                if (pdfPath == null || pdfPath.isEmpty()) {
                    Toast.makeText(context, "No PDF path found", Toast.LENGTH_SHORT).show();
                    return;
                }

                File file = new File(pdfPath);
                if (!file.exists()) {
                    Toast.makeText(context, "File does not exist", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri uri = FileProvider.getUriForFile(context,
                        context.getPackageName() + ".provider", file);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "No app found to open PDF", Toast.LENGTH_SHORT).show();
                }
            });


        }





    }
}