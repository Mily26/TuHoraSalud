package com.example.apptuhorasalud.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptuhorasalud.R;
import com.example.apptuhorasalud.UpdateAlarmActivity;
import com.example.apptuhorasalud.domain.models.Alarm;

import java.util.List;
import java.util.Locale;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private List<Alarm> alarmList;

    public AlarmAdapter(List<Alarm> alarmList) {
        this.alarmList = alarmList;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);
        holder.tvAlarmMedicine.setText(alarm.getMedicineName());
        holder.tvAlarmDose.setText("Dosis: " + alarm.getDose());
        holder.tvAlarmTime.setText(String.format(Locale.getDefault(), "%02d:%02d", alarm.getHour(), alarm.getMinute()));

        holder.btnUpdateAlarm.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, UpdateAlarmActivity.class);
            intent.putExtra("alarmId", alarm.getId());
            intent.putExtra("medicineId", alarm.getMedicineId());
            intent.putExtra("medicineName", alarm.getMedicineName());
            intent.putExtra("dose", alarm.getDose());
            intent.putExtra("hour", alarm.getHour());
            intent.putExtra("minute", alarm.getMinute());
            intent.putExtra("idUsuario", alarm.getUserId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public void updateList(List<Alarm> newList) {
        this.alarmList = newList;
        notifyDataSetChanged();
    }

    static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlarmMedicine, tvAlarmDose, tvAlarmTime;
        Button btnUpdateAlarm;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAlarmMedicine = itemView.findViewById(R.id.tvAlarmMedicine);
            tvAlarmDose = itemView.findViewById(R.id.tvAlarmDose);
            tvAlarmTime = itemView.findViewById(R.id.tvAlarmTime);
            btnUpdateAlarm = itemView.findViewById(R.id.btnUpdateAlarm);
        }
    }
}
