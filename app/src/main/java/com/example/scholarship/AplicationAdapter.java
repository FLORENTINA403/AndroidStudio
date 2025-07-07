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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
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

    @Override
    public int getItemCount() {
        return 0;
    }
    public void setFilteredList(List<AplicationModel> filteredList) {
        this.list = filteredList;
        selectedPosition = RecyclerView.NO_POSITION; // clear selection
        notifyDataSetChanged();
    }


    public List<AplicationModel> getApplicationList() {
        return list;
    }

    public AplicationModel getSelectedItem() {
        if (selectedPosition >= 0 && selectedPosition < list.size()) {
            return list.get(selectedPosition);
        }
        return null;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
    public void showEditDialog(AplicationModel model) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_applicant, null);
        EditText editName = dialogView.findViewById(R.id.edit_name);
        EditText editEmail = dialogView.findViewById(R.id.edit_email);
        EditText editPersonalId = dialogView.findViewById(R.id.edit_personalId);
        EditText editPhone = dialogView.findViewById(R.id.edit_phone);
        EditText editLevel = dialogView.findViewById(R.id.edit_level);
        EditText editField = dialogView.findViewById(R.id.edit_field);

        editName.setText(model.getFullname());
        editEmail.setText(model.getEmail());
        editPersonalId.setText(model.getPersonalId());
        editPhone.setText(model.getPhone());
        editLevel.setText(model.getLevel());
        editField.setText(model.getField());

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Edit Applicant")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();

        Button saveBtn = dialogView.findViewById(R.id.save_button);
        saveBtn.setOnClickListener(btn -> {
            String newName = editName.getText().toString().trim();
            String newEmail = editEmail.getText().toString().trim();
            String newPersonalId = editPersonalId.getText().toString().trim();
            String newPhone = editPhone.getText().toString().trim();
            String newLevel = editLevel.getText().toString().trim();
            String newField = editField.getText().toString().trim();

            boolean updated = dbHelper.updateApplicant(
                    model.getId(), newName, newEmail, newPersonalId, newPhone, newLevel, newField);

            if (updated) {
                Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show();
                model.setFullname(newName);
                model.setEmail(newEmail);
                model.setPersonalId(newPersonalId);
                model.setPhone(newPhone);
                model.setLevel(newLevel);
                model.setField(newField);
                notifyItemChanged(selectedPosition);
            } else {
                Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        });
    }

    public void deleteSelected() {
        if (selectedPosition >= 0 && selectedPosition < list.size()) {
            AplicationModel model = list.get(selectedPosition);
            boolean deleted = dbHelper.deleteApplicant(model.getId());
            if (deleted) {
                list.remove(selectedPosition);
                notifyItemRemoved(selectedPosition);
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                selectedPosition = RecyclerView.NO_POSITION;
            } else {
                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "No applicant selected", Toast.LENGTH_SHORT).show();
        }
    }




}
