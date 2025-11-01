package com.example.apptuhorasalud;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {
    private Button addMedicineButton;
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

        // 3. Extract the data using the same keys used in LoginActivity
        // The second parameter is a default value in case the extra is not found.
        String nombreUsuario = intent.getStringExtra("nombreUsuario");
        int idUsuario = intent.getIntExtra("idUsuario", -1);

        addMedicineButton = findViewById(R.id.buttonMedicine);

        Intent intentMed = new Intent(this, ListMedicinesActivity.class);
        intentMed.putExtra("idUsuario", idUsuario);
        addMedicineButton.setOnClickListener(v -> startActivity(intentMed));
    }
}