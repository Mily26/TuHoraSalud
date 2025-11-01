package com.example.apptuhorasalud.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apptuhorasalud.R;
import com.example.apptuhorasalud.domain.models.Medicine;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<Medicine> medicineList;

    public MedicineAdapter(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.tvMedicineName.setText(medicine.getName());
        holder.tvMedicineQuantity.setText("Cantidad: " + medicine.getQuantity());

        // TODO: Implement update and delete functionality
        holder.btnUpdateMedicine.setOnClickListener(v -> {
            // Update action will be implemented later
        });

        holder.btnDeleteMedicine.setOnClickListener(v -> {
            // Delete action will be implemented later
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public void updateList(List<Medicine> newList) {
        this.medicineList = newList;
        notifyDataSetChanged();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedicineName, tvMedicineQuantity;
        Button btnUpdateMedicine, btnDeleteMedicine;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedicineName = itemView.findViewById(R.id.tvMedicineName);
            tvMedicineQuantity = itemView.findViewById(R.id.tvMedicineQuantity);
            btnUpdateMedicine = itemView.findViewById(R.id.btnUpdateMedicine);
            btnDeleteMedicine = itemView.findViewById(R.id.btnDeleteMedicine);
        }
    }
}
