package com.example.apptuhorasalud;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InicioActivity extends AppCompatActivity {
    EditText txtCorreo, txtContraseña;
    Button btnIngresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.inicio), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView tvRecuperar = findViewById(R.id.tvRecuperar);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContraseña = findViewById(R.id.txtContraseña);
        btnIngresar = findViewById(R.id.btnRegistrarse);
        // btn Ingresar
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarCampos();
            }
        });

        // btn Registrar
        TextView btnRegistro = findViewById(R.id.btnRegistrar);
        btnRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(InicioActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        tvRecuperar.setOnClickListener(v -> {
            Intent intent = new Intent(InicioActivity.this, RecuperoActivity.class);
            startActivity(intent);
        });
    }
    private void validarCampos() {
        String correo = txtCorreo.getText().toString();
        String contraseña = txtContraseña.getText().toString();
        if (correo.isEmpty()) {
            Toast.makeText(this, "El correo es obligatorio", Toast.LENGTH_SHORT).show();
            //txtCorreo.setError("Correo requerido");
            txtCorreo.requestFocus();
            return;
        }
        // Valida el formato del email
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese un correo válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (contraseña.isEmpty()) {
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
            //txtContraseña.setError("Contraseña requerida");
            txtContraseña.requestFocus();
            return;
        }

        Intent intent = new Intent(InicioActivity.this, MainActivity.class);
        startActivity(intent);
    }

}