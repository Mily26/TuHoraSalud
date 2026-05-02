package com.example.apptuhorasalud;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.apptuhorasalud.adapters.AlarmAdapter;
import com.example.apptuhorasalud.application.useCase.Alarm.ToggleAlarm;
import com.example.apptuhorasalud.domain.interfaces.IAlarmRepository;
import com.example.apptuhorasalud.domain.models.Alarm;
import com.example.apptuhorasalud.infrastructure.data.AppDatabase;
import com.example.apptuhorasalud.infrastructure.repository.AlarmRepositoryImpl;
import com.example.apptuhorasalud.utils.AlarmScheduler;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private Button addMedicineButton;
    private Button addAlarmButton;
    private RecyclerView recyclerViewAlarms;
    private TextView tvEmptyAlarms;
    private AlarmAdapter alarmAdapter;
    private List<Alarm> alarmList;
    private int idUsuario;

    private static final String DB_NAME = "usuarios-db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String nombreUsuario = intent.getStringExtra("nombreUsuario");
        idUsuario = intent.getIntExtra("idUsuario", -1);

        addMedicineButton = findViewById(R.id.buttonMedicine);
        addAlarmButton = findViewById(R.id.buttonAlarm);
        recyclerViewAlarms = findViewById(R.id.recyclerViewAlarms);
        tvEmptyAlarms = findViewById(R.id.tvEmptyAlarms);

        alarmList = new ArrayList<>();
        alarmAdapter = new AlarmAdapter(alarmList, this::onAlarmToggle);
        recyclerViewAlarms.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAlarms.setAdapter(alarmAdapter);

        addMedicineButton.setOnClickListener(v -> {
            Intent intentMed = new Intent(this, ListMedicinesActivity.class);
            intentMed.putExtra("idUsuario", idUsuario);
            startActivity(intentMed);
        });

        addAlarmButton.setOnClickListener(v -> {
            Intent intentAlarm = new Intent(this, InsertAlarmActivity.class);
            intentAlarm.putExtra("idUsuario", idUsuario);
            startActivity(intentAlarm);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAlarms();
    }

    private void loadAlarms() {
        if (idUsuario == -1) return;

        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DB_NAME).build();
            IAlarmRepository repo = new AlarmRepositoryImpl(db.alarmDao());

            repo.getAlarmsByUserId(idUsuario).thenAccept(alarms -> runOnUiThread(() -> {
                alarmList.clear();
                alarmList.addAll(alarms);
                alarmAdapter.updateList(alarmList);

                if (alarms.isEmpty()) {
                    tvEmptyAlarms.setVisibility(View.VISIBLE);
                    recyclerViewAlarms.setVisibility(View.GONE);
                } else {
                    tvEmptyAlarms.setVisibility(View.GONE);
                    recyclerViewAlarms.setVisibility(View.VISIBLE);
                }
            })).exceptionally(throwable -> {
                Log.e("DB", "Error al cargar alarmas", throwable);
                return null;
            });
        }).start();
    }

    private void onAlarmToggle(Alarm alarm) {
        boolean newActive = !alarm.isActive();

        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DB_NAME).build();
            IAlarmRepository repo = new AlarmRepositoryImpl(db.alarmDao());
            ToggleAlarm useCase = new ToggleAlarm(repo);

            useCase.execute(alarm.getId(), newActive).thenRun(() -> {
                if (newActive) {
                    alarm.setActive(true);
                    AlarmScheduler.schedule(getApplicationContext(), alarm);
                    Log.d("DB", "Alarma activada: " + alarm.getMedicineName());
                } else {
                    alarm.setActive(false);
                    AlarmScheduler.cancel(getApplicationContext(), alarm.getId());
                    Log.d("DB", "Alarma desactivada: " + alarm.getMedicineName());
                }

                runOnUiThread(() -> {
                    Toast.makeText(this,
                            newActive ? "Alarma activada" : "Alarma desactivada",
                            Toast.LENGTH_SHORT).show();
                    alarmAdapter.notifyDataSetChanged();
                });
            }).exceptionally(throwable -> {
                Log.e("DB", "Error al cambiar estado de alarma", throwable);
                runOnUiThread(() -> Toast.makeText(this,
                        "Error al cambiar el estado de la alarma",
                        Toast.LENGTH_SHORT).show());
                return null;
            });
        }).start();
    }
}
