package com.example.apptuhorasalud.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptuhorasalud.R;
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

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAlarmMedicine = itemView.findViewById(R.id.tvAlarmMedicine);
            tvAlarmDose = itemView.findViewById(R.id.tvAlarmDose);
            tvAlarmTime = itemView.findViewById(R.id.tvAlarmTime);
        }
    }
}
